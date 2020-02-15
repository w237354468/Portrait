package com.wzq.service;

import com.wzq.entity.AnalyResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

// 调用其他服务
@FeignClient(value = "searchService")
public interface MongoDataService {

  @RequestMapping(value = "searchInfo/searchYearBase", method = RequestMethod.GET)
  public List<AnalyResult> searchYearBase();

  @RequestMapping(value = "searchInfo/searchUserType", method = RequestMethod.POST)
  public List<AnalyResult> searchUserType();

  @RequestMapping(value = "searchInfo/searchEmail", method = RequestMethod.POST)
  public List<AnalyResult> searchEmail();

  @RequestMapping(value = "searchInfo/searchConsumptionLevel", method = RequestMethod.POST)
  public List<AnalyResult> searchConsumerLevel();

  @RequestMapping(value = "searchInfo/searchChaoNanNv", method = RequestMethod.POST)
  public List<AnalyResult> searchChaoNanNv();

  @RequestMapping(value = "searchInfo/searchCarrier", method = RequestMethod.POST)
  public List<AnalyResult> searchCarrier();

  @RequestMapping(value = "searchInfo/searchBrandLike", method = RequestMethod.POST)
  public List<AnalyResult> searchBrandLike();
}
