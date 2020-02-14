package com.wzq.controller;

import com.alibaba.fastjson.JSONObject;
import com.wzq.entity.AnalyResult;
import com.wzq.entity.ViewResultAnaly;
import com.wzq.service.BaseYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("baseYear")
public class BaseYearViewController {

  final BaseYearService baseYearService;

  @Autowired
  public BaseYearViewController(BaseYearService baseYearService) {
    this.baseYearService = baseYearService;
  }

  @RequestMapping("baseYearView")
  public String baseYearView() {
    List<AnalyResult> analyResults = baseYearService.searchYearBase();
    ViewResultAnaly result = new ViewResultAnaly();
    for (AnalyResult analyResult : analyResults) {
      result.getCount().add(analyResult.getCount());
      result.getInfoList().add(analyResult.getInfo());
    }
    return JSONObject.toJSONString(result);
  }
}
