package com.wzq.controller;

import com.alibaba.fastjson.JSONObject;
import com.wzq.entity.ResultMessage;
import com.wzq.log.AttentionProductLog;
import com.wzq.log.BuyCartProductLog;
import com.wzq.log.CollectProductLog;
import com.wzq.log.ScanProductLog;
import com.wzq.utils.ReadProperties;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("infoLog")
public class appLoginController {

  private final String attentionProductLogTopic = ReadProperties.getKey("attentionProductLog");
  private final String buyCartProductLogTopic = ReadProperties.getKey("buyCartProductLog");
  private final String collectProductLogTopic = ReadProperties.getKey("collectProductLog");
  private final String scanProductLogTopic = ReadProperties.getKey("scanProductLog");

  @Autowired private KafkaTemplate<String, String> kafkaTemplate;

  @RequestMapping(value = "hw", method = RequestMethod.GET)
  public String hw(HttpServletRequest req) {
    String addr = req.getRemoteAddr();
    return "hello " + addr + " \nreq success!";
  }

  // 接收四种日志
  @RequestMapping(value = "receiveLog", method = RequestMethod.GET)
  public String hw(String receiveLog, HttpServletRequest req) {
    if (StringUtils.isBlank(receiveLog)) {
      return null;
    }

    /*
     limit 参数通过控制分割次数从而影响分割结果
     如果传入 n(n>0) 那么字符串最多被分割 n-1 次,分割得到数组长度最大是 n
     如果 n = -1 将会以最大分割次数分割 如果
     n = 0 将会以最大分割次数分割,但是分割结果会舍弃末位的空串
    */
    String[] receiveArrays = receiveLog.split(":", 2);
    String className = receiveArrays[0];
    String data = receiveArrays[1];
    String messageString = "";
    switch (className) {
      case "AttentionProductLog":
        {
          AttentionProductLog attentionProductLog =
              JSONObject.parseObject(data, AttentionProductLog.class);
          messageString = JSONObject.toJSONString(attentionProductLog);
          kafkaTemplate.send(attentionProductLogTopic, messageString + "##1##" + new Date().getTime());
        }
        break;
      case "BuyCartProductLog":
        {
          BuyCartProductLog buyCartProductLog =
              JSONObject.parseObject(data, BuyCartProductLog.class);
          messageString = JSONObject.toJSONString(buyCartProductLog);
          kafkaTemplate.send(buyCartProductLogTopic, messageString + "##1##" + new Date().getTime());
        }
        break;
      case "CollectProductLog":
        {
          CollectProductLog collectProductLog =
              JSONObject.parseObject(data, CollectProductLog.class);
          messageString = JSONObject.toJSONString(collectProductLog);
          kafkaTemplate.send(collectProductLogTopic, messageString + "##1##" + new Date().getTime());
        }
        break;
      case "ScanProductLog":
        {
          ScanProductLog scanProductLog = JSONObject.parseObject(data, ScanProductLog.class);
          messageString = JSONObject.toJSONString(scanProductLog);
          kafkaTemplate.send(scanProductLogTopic, messageString + "##1##" + new Date().getTime());
        }
        break;
    }
    ResultMessage resultMessage = new ResultMessage();
    resultMessage.setMessage(messageString);
    resultMessage.setStatus("success");
    return JSONObject.toJSONString(resultMessage);
  }
}
