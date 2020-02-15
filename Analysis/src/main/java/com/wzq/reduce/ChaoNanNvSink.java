package com.wzq.reduce;

import com.wzq.entity.ChaoNanNvInfo;
import com.wzq.util.MonGoUtil;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.bson.Document;

public class ChaoNanNvSink implements SinkFunction<ChaoNanNvInfo> {

  @Override
  public void invoke(ChaoNanNvInfo value, Context context) throws Exception {
    String chaoType = value.getChaoType();
    int count = value.getCount();

    // 找到ChaoNanNv哪一行
    Document doc = MonGoUtil.findoneby("ChaoNanNvstatics", "wzqPortrait", chaoType);
    if (doc == null) {
      doc = new Document();
      doc.put("info", chaoType);
      doc.put("count", count);
    } else {
      Long countPre = doc.getLong("count");
      Long total = countPre + count;
      doc.put("count", total);
    }
    MonGoUtil.saverupdateMongo("ChaoNanNvstatics", "wzqPortrait", doc);
  }
}
