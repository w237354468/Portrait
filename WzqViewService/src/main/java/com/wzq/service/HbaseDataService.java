package com.wzq.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "searchService")
public interface HbaseDataService {

  @RequestMapping(value = "searchBaijia", method = RequestMethod.POST)
  public String baijiazhishu(String userId);

  @RequestMapping(value = "brandLike", method = RequestMethod.POST)
  public String branklike(String userId);

  @RequestMapping(value = "carrier", method = RequestMethod.POST)
  public String carrier(String userId);

  @RequestMapping(value = "chaonannv", method = RequestMethod.POST)
  public String chaonannv(String userId);

  @RequestMapping(value = "consumptionlevel", method = RequestMethod.POST)
  public String consumptionlevel(String userId);

  @RequestMapping(value = "emailinfo", method = RequestMethod.POST)
  public String emailinfo(String userId);
  // logistic
  @RequestMapping(value = "sex", method = RequestMethod.POST)
  public String sex(String userId);
  // kmeans
  @RequestMapping(value = "usergroupinfo", method = RequestMethod.POST)
  public String usergroupinfo(String userId);

  @RequestMapping(value = "usetypeinfo", method = RequestMethod.POST)
  public String usetypeinfo(String userId);
  // 年代
  @RequestMapping(value = "ageinfo", method = RequestMethod.POST)
  public String ageinfo(String userId);
}
