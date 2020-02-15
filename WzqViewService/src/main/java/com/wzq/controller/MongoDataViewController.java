package com.wzq.controller;

import com.alibaba.fastjson.JSONObject;
import com.wzq.entity.AnalyResult;
import com.wzq.entity.ViewResultAnaly;
import com.wzq.form.AnalyForm;
import com.wzq.service.MongoDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("MongoData")
public class MongoDataViewController {

  final MongoDataService mongoDataService;

  @Autowired
  public MongoDataViewController(MongoDataService mongoDataService) {
    this.mongoDataService = mongoDataService;
  }

  // 此处默认不是JSON接收，项目实例中前端访问在URL拼JSON
  // this.$http.post("http://127.0.0.1/mongoData/resultInfoView",{"type":"yearBase"
  // }).then(response)=>{this.option={
  // this.option = {
  //  chart:{type"column},
  //  title:{text:"年代趋势"},
  //  xAxis:{categories:response.body.infolist,crosshair:true},
  //  yAxis:{min:0,title:{text:"数量"}}},
  // series:[{"name":中短篇好",data:response.body.countlist}]
  // }
  // }}
  // components:{XChart}
  @RequestMapping(
      value = "resultInfoView",
      method = RequestMethod.POST,
      produces = "application/json.charset=UTF-8")
  public String resultInfoView(@RequestBody AnalyForm analyForm) {
    String type = analyForm.getType();
    List<AnalyResult> analyResults = new ArrayList<>();
    switch (type) {
      case "yearBase":
        {
          analyResults = mongoDataService.searchYearBase();
          break;
        }
      case "userType":
        {
          analyResults = mongoDataService.searchUserType();
          break;
        }
      case "email":
        {
          analyResults = mongoDataService.searchEmail();
          break;
        }
      case "consumptionLevel":
        {
          analyResults = mongoDataService.searchConsumerLevel();
          break;
        }
      case "ChaoNanNv":
        {
          analyResults = mongoDataService.searchChaoNanNv();
          break;
        }
      case "carrier":
        {
          analyResults = mongoDataService.searchCarrier();
          break;
        }
      case "brandLike":
        {
          analyResults = mongoDataService.searchBrandLike();
          break;
        }
    }
    ViewResultAnaly result = new ViewResultAnaly();
    for (AnalyResult analyResult : analyResults) {
      result.getCount().add(analyResult.getCount());
      result.getInfoList().add(analyResult.getInfo());
    }
    return JSONObject.toJSONString(result);
  }
}
