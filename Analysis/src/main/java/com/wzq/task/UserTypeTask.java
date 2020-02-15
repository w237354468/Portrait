package com.wzq.task;

import com.wzq.entity.UseTypeInfo;
import com.wzq.kafkause.KafkaEvent;
import com.wzq.kafkause.KafkaEventSchema;
import com.wzq.map.UserTypeMap;
import com.wzq.reduce.UseTypeReduce;
import com.wzq.reduce.UseTypeSink;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

public class UserTypeTask {
  public static void main(String[] args) {

    args =
        new String[] {
          "--intput-topic",
          "scanProductLogTopic",
          "--bootstrap.servers",
          "192.168.11.125:9092",
          "--zookeeper.connect",
          "192.168.11.125:2181",
          "--group.id",
          "wzq"
        };

    final ParameterTool parameterTool = ParameterTool.fromArgs(args);

    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    env.getConfig().disableSysoutLogging();
    env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 10000));
    env.enableCheckpointing(5000);
    env.getConfig().setGlobalJobParameters(parameterTool);
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

    DataStream<KafkaEvent> input =
        env.addSource(
                new FlinkKafkaConsumer010<KafkaEvent>(
                    parameterTool.getRequired("input-topic"),
                    new KafkaEventSchema(),
                    parameterTool.getProperties()))
            .assignTimestampsAndWatermarks(
                new AscendingTimestampExtractor<KafkaEvent>() {
                  @Override
                  public long extractAscendingTimestamp(KafkaEvent kafkaEvent) {
                    return kafkaEvent.getTimestamp();
                  }
                });

    SingleOutputStreamOperator<UseTypeInfo> useTypeMap = input.flatMap(new UserTypeMap());

    SingleOutputStreamOperator<UseTypeInfo> reduce =
        useTypeMap
            .keyBy(
                new KeySelector<UseTypeInfo, String>() {
                  @Override
                  public String getKey(UseTypeInfo value) throws Exception {
                    return value.getGroupField();
                  }
                })
            .timeWindow(Time.seconds(2L))
            .reduce(new UseTypeReduce());

    reduce.addSink(new UseTypeSink());

    try {
      env.execute("useType analy");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
