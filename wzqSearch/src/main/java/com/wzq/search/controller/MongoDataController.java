package com.wzq.search.controller;

import com.wzq.entity.AnalyResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("YearBase")
public class MongoDataController{

  //  private YearBaseImpl yearBaseImpl;
  //
  //  @Autowired
  //  public YearBaseController(YearBaseImpl yearBase) {
  //    yearBaseImpl = yearBase;
  //  }

  @RequestMapping("searchYearBase")
  public List<AnalyResult> searchYearBase() {
    ArrayList<AnalyResult> result = new ArrayList<>();
    AnalyResult analyResult = new AnalyResult();
    analyResult.setCount(11L);
    analyResult.setInfo("success");
    result.add(analyResult);
    return result;
    //    return yearBaseImpl.listYearBaseInfoBy();
  }
}
