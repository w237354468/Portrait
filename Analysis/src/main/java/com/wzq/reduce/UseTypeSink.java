package com.wzq.reduce;

import com.wzq.entity.UseTypeInfo;
import com.wzq.util.MonGoUtil;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.bson.Document;

public class UseTypeSink implements SinkFunction<UseTypeInfo> {

  @Override
  public void invoke(UseTypeInfo value, Context context) throws Exception {

    // 单个品牌的浏览用户数
    String  userType = value.getUserType();
    long count = value.getCount();

    // 找到brand哪一行
    Document doc = MonGoUtil.findoneby("usetypeestatics", "wzqPortrait", userType);
    if (doc == null) {
      doc = new Document();
      doc.put("info", userType);
      doc.put("count", count);
    } else {
      Long countPre = doc.getLong("count");
      Long total = countPre + count;
      doc.put("count", total);
    }
    MonGoUtil.saverupdateMongo("usetypestatics", "wzqPortrait", doc);

  }

}
