package com.wzq.search.controller;

import com.wzq.search.service.HbaseServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hbaseData")
public class HbaseController {

  @RequestMapping(value = "searchBaijia", method = RequestMethod.POST)
  public String baijiazhishu(String userId) {
    String tableName = "userflaginfo";
    String familyName = "baseinfo";
    String column = "baijiascore";
    String result = "";
    try {
      result = HbaseServiceImpl.getData(tableName, userId, familyName, column);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  @RequestMapping(value = "brandLike", method = RequestMethod.POST)
  public String branklike(String userId) {
    String tableName = "userflaginfo";
    String familyName = "userbehavior";
    String column = "brandlist";
    String result = "";
    try {
      result = HbaseServiceImpl.getData(tableName, userId, familyName, column);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  @RequestMapping(value = "carrier", method = RequestMethod.POST)
  public String carrier(String userId) {
    String tableName = "userflaginfo";
    String familyName = "baseinfo";
    String column = "carrierinfo";
    String result = "";
    try {
      result = HbaseServiceImpl.getData(tableName, userId, familyName, column);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  @RequestMapping(value = "chaonannv", method = RequestMethod.POST)
  public String chaonannv(String userId) {
    String tableName = "userflaginfo";
    String familyName = "userbehavior";
    String column = "chaonannv";
    String result = "";
    try {
      result = HbaseServiceImpl.getData(tableName, userId, familyName, column);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  @RequestMapping(value = "consumptionlevel", method = RequestMethod.POST)
  public String consumptionlevel(String userId) {
    String tableName = "userflaginfo";
    String familyName = "consumerinfo";
    String column = "consumptionlevel";
    String result = "";
    try {
      result = HbaseServiceImpl.getData(tableName, userId, familyName, column);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  @RequestMapping(value = "emailinfo", method = RequestMethod.POST)
  public String emailinfo(String userId) {
    String tableName = "userflaginfo";
    String familyName = "baseinfo";
    String column = "emailinfo";
    String result = "";
    try {
      result = HbaseServiceImpl.getData(tableName, userId, familyName, column);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  // logistic
  @RequestMapping(value = "sex", method = RequestMethod.POST)
  public String sex(String userId) {
    String tableName = "userflaginfo";
    String familyName = "baseinfo";
    String column = "sex";
    String result = "";
    try {
      result = HbaseServiceImpl.getData(tableName, userId, familyName, column);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  // kmeans
  @RequestMapping(value = "usergroupinfo", method = RequestMethod.POST)
  public String usergroupinfo(String userId) {
    String tableName = "userflaginfo";
    String familyName = "usergroupinfo";
    String column = "usergroupinfo";
    String result = "";
    try {
      result = HbaseServiceImpl.getData(tableName, userId, familyName, column);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

    @RequestMapping(value = "usetypeinfo", method = RequestMethod.POST)
    public String usetypeinfo(String userId) {
        String tableName = "usetypeinfo";
        String familyName = "userbehavior";
        String column = "usetypelist";
        String result = "";
        try {
            result = HbaseServiceImpl.getData(tableName, userId, familyName, column);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

  // 年代
  @RequestMapping(value = "ageinfo", method = RequestMethod.POST)
  public String ageinfo(String userId) {
    String tableName = "userflaginfo";
    String familyName = "baseinfo";
    String column = "age";
    String result = "";
    try {
      result = HbaseServiceImpl.getData(tableName, userId, familyName, column);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }
}
