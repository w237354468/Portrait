package com.wzq.reduce;

import com.wzq.entity.BrandLike;
import com.wzq.util.MonGoUtil;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.bson.Document;

public class BrandLikeSink implements SinkFunction<BrandLike> {

  @Override
  public void invoke(BrandLike value, Context context) throws Exception {

    // 单个品牌的浏览用户数
    String  brand = value.getBrand();
    long count = value.getCount();

    // 找到brand哪一行
    Document doc = MonGoUtil.findoneby("brandlikestatics", "wzqPortrait", brand);
    if (doc == null) {
      doc = new Document();
      doc.put("info", brand);
      doc.put("count", count);
    } else {
      Long countPre = doc.getLong("count");
      Long total = countPre + count;
      doc.put("count", total);
    }
    MonGoUtil.saverupdateMongo("brandlikestatics", "wzqPortrait", doc);
  }
}
