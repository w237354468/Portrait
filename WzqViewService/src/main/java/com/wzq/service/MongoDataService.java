package com.wzq.service;

import com.wzq.entity.AnalyResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

// 调用其他服务
@FeignClient(value = "searchService")
public interface BaseYearService {

  @RequestMapping(value = "YearBase/searchYearBase", method = RequestMethod.GET)
  public List<AnalyResult> searchYearBase();
}
