package com.wzq.util;

import com.wzq.entity.UseTypeInfo;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

public class UseTypeSink implements SinkFunction<UseTypeInfo> {

  @Override
  public void invoke(UseTypeInfo value, Context context) throws Exception {

  }
  
}
