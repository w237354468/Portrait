package com.wzq.process.location;

import com.wzq.entity.app.AppEvent;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Row;

public class RowToAppEventMap implements Function<Row,AppEvent> {

  @Override
  public AppEvent call(Row v1) throws Exception {
    return new AppEvent();
  }
}
