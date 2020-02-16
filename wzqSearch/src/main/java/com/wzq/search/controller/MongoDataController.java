package com.wzq.search.controller;

import com.wzq.entity.AnalyResult;
import com.wzq.search.service.MongoDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("searchInfo")
public class MongoDataController {

  private MongoDataServiceImpl yearBaseImpl;

  @Autowired
  public MongoDataController(MongoDataServiceImpl yearBase) {
    yearBaseImpl = yearBase;
  }

  @RequestMapping("searchYearBase")
  public List<AnalyResult> searchYearBase() {
    List<AnalyResult> result = new ArrayList<>();
    AnalyResult analyResult = new AnalyResult();
    analyResult.setCount(11L);
    analyResult.setInfo("success");
    result.add(analyResult);
    return result;
    //    return yearBaseImpl.listYearBaseInfoBy();
  }

  @RequestMapping(value = "searchUseType", method = RequestMethod.POST)
  public List<AnalyResult> searchUserType() {
    return yearBaseImpl.listYearBaseInfoBy("usetypestatics");
  }

  @RequestMapping(value = "searchEmail", method = RequestMethod.POST)
  public List<AnalyResult> searchEmail() {
    return yearBaseImpl.listYearBaseInfoBy("emailstatics");
  }

  @RequestMapping(value = "searchConsumptionLevel", method = RequestMethod.POST)
  public List<AnalyResult> searchConsumerLevel() {
    return yearBaseImpl.listYearBaseInfoBy("consumptionlevelstatics");
  }

  @RequestMapping(value = "searchChaoNanNv", method = RequestMethod.POST)
  public List<AnalyResult> searchChaoNanNv() {
    return yearBaseImpl.listYearBaseInfoBy("chaoNanNvstatics");
  }

  @RequestMapping(value = "searchCarrier", method = RequestMethod.POST)
  public List<AnalyResult> searchCarrier() {
    return yearBaseImpl.listYearBaseInfoBy("carrierstatics");
  }

  @RequestMapping(value = "searchBrandLike", method = RequestMethod.POST)
  public List<AnalyResult> searchBrandLike() {
    return yearBaseImpl.listYearBaseInfoBy("branklikestatics");
  }
}
