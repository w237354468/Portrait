package com.wzq;

import com.wzq.entity.MarkObject;
import com.wzq.entity.ProfileEvent;
import com.wzq.util.HbaseUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import org.apache.hadoop.hbase.client.Put;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.sql.SparkSession;

public class ReadHiveJava {

  public static void main(String[] args) {
    String tableName = "";
    String columnName = "";
    JavaSparkContext sc = new JavaSparkContext("local[6]", "asd", new SparkConf());
    //    SparkSession sparkSession = SparkSession.builder().enableHiveSupport().getOrCreate();
    sc.setLogLevel("ERROR");
    ArrayList<ProfileEvent> profileEvents = new ArrayList<ProfileEvent>();
    profileEvents.add(new ProfileEvent("13264378897", "男", "13123@qq.com", "25", "大学"));
    profileEvents.add(new ProfileEvent("13264378898", "女", "asd@qq.com", "25", "大学"));
    profileEvents.add(new ProfileEvent("13264378899", "男", "xzc@qq.com", "25", "高中"));
    profileEvents.add(new ProfileEvent("13264378900", "女", "qwe@qq.com", "25", "大学"));
    profileEvents.add(new ProfileEvent("13264378901", "男", "1234@qq.com", "25", "研究生"));

    JavaRDD<ProfileEvent> parallelize = sc.parallelize(profileEvents);
    //    Dataset<Row> dataFrame = sparkSession.createDataFrame(parallelize, ProfileEvent.class);
    //    sparkSession.sql("")
    //        .map(
    //            new MapFunction<Row, Tuple2<String, String>>() {
    //              public Tuple2<String, String> call(Row row) throws Exception {
    //                return Tuple2.apply(row.getString(1), row.getString(2));
    //              }
    //            },
    //            Encoders.tuple(Encoders.STRING(), Encoders.STRING()))
    //        .javaRDD()
    JavaRDD<MarkObject> marks =
        parallelize.map(
            new Function<ProfileEvent, MarkObject>() {
              @Override
              public MarkObject call(ProfileEvent profileEvent) throws Exception {

                String phone = profileEvent.getPhone();
                String sex = profileEvent.getSex();
                String age = profileEvent.getAge();
                String email = profileEvent.getEmail();
                String graduation = profileEvent.getGraduation();

                String sexMark = MarkUtil.getSexMark(sex);
                String ageMark = MarkUtil.getYearBaseByAge(age);
                String emailMark = MarkUtil.getEmailType(email);

                // 文字转数字
                return new MarkObject(phone, sexMark, emailMark, ageMark, graduation);
              }
            });
    marks.mapPartitions(
        new FlatMapFunction<Iterator<MarkObject>, MarkObject>() {
          @Override
          public Iterator<MarkObject> call(Iterator<MarkObject> markObjectIterator)
              throws Exception {
            List<Put> marksPuts = new ArrayList<>();

            while (markObjectIterator.hasNext()) {
              // 加入
              MarkObject next = markObjectIterator.next();
              String phone = next.getPhone();
              HashMap<String, String> data = new HashMap<>();
              data.put("sex", next.getSexMark());
              data.put("age", next.getAgeMark());
              data.put("email", next.getEmailMark());
              marksPuts.add(HbaseUtil.getPut(phone, "baseInfo", data));
              // 5000条
              if (marksPuts.size() % 5000 == 0) {
                HbaseUtil.putMoreData(tableName, marksPuts);
                marksPuts.clear();
              }
            }
            HbaseUtil.putMoreData(tableName, marksPuts);
            return markObjectIterator;
          }
        });

    marks.collect().forEach(System.out::println);
    marks
        .groupBy(
            new Function<MarkObject, String>() {
              @Override
              public String call(MarkObject markObject) throws Exception {
                return markObject.getSexMark();
              }
            })
        .mapValues(
            new Function<Iterable<MarkObject>, Long>() {
              @Override
              public Long call(Iterable<MarkObject> markObjects) throws Exception {
                System.out.println(markObjects.spliterator().estimateSize());
                return markObjects.spliterator().estimateSize();
              }
            })
        .collectAsMap()
        .forEach(
            new BiConsumer<String, Long>() {
              @Override
              public void accept(String s, Long aLong) {
                System.out.println(s + ":" + aLong);
              }
            });
  }
  // 1，批量入库用户的全部基础信息，每条中有所有标签
  // 2，本地计算所有标签下的个数，一次性入库

}
