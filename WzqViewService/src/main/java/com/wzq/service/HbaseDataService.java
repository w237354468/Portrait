package com.wzq.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "searchService")
public interface HbaseDataService {

  @RequestMapping(value = "hbaseData/searchBaijia", method = RequestMethod.POST)
  public String baijiazhishu(String userId);

  @RequestMapping(value = "hbaseData/brandLike", method = RequestMethod.POST)
  public String branklike(String userId);

  @RequestMapping(value = "hbaseData/carrier", method = RequestMethod.POST)
  public String carrier(String userId);

  @RequestMapping(value = "hbaseData/chaonannv", method = RequestMethod.POST)
  public String chaonannv(String userId);

  @RequestMapping(value = "hbaseData/consumptionlevel", method = RequestMethod.POST)
  public String consumptionlevel(String userId);

  @RequestMapping(value = "hbaseData/emailinfo", method = RequestMethod.POST)
  public String emailinfo(String userId);
  // logistic
  @RequestMapping(value = "hbaseData/sex", method = RequestMethod.POST)
  public String sex(String userId);
  // kmeans
  @RequestMapping(value = "hbaseData/usergroupinfo", method = RequestMethod.POST)
  public String usergroupinfo(String userId);

  @RequestMapping(value = "hbaseData/usetypeinfo", method = RequestMethod.POST)
  public String usetypeinfo(String userId);
  // 年代
  @RequestMapping(value = "hbaseData/ageinfo", method = RequestMethod.POST)
  public String ageinfo(String userId);
}
