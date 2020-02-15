package com.wzq.controller;

import com.alibaba.fastjson.JSON;
import com.wzq.form.BaseViewform;
import com.wzq.entity.ViewResultAnaly;
import com.wzq.service.HbaseDataService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("hbaseData")
public class HbaseDataViewController {

  final HbaseDataService hbaseDataService;

  public HbaseDataViewController(HbaseDataService hbaseDataService) {
    this.hbaseDataService = hbaseDataService;
  }

  @RequestMapping(
      value = "resultInfoView",
      method = RequestMethod.POST,
      produces = "application/json.charset=UTF-8")
  public String resultInfoView(@RequestBody BaseViewform baseViewform) {
    String type = baseViewform.getType();
    String userId = baseViewform.getUserId();
    String result = "";
    ArrayList<ViewResultAnaly> resultList = new ArrayList<ViewResultAnaly>();

    switch (type) {
      case "-1":
        {
          resultList.add(new ViewResultAnaly("败家指数", hbaseDataService.baijiazhishu(userId)));
          resultList.add(new ViewResultAnaly("品牌喜好", hbaseDataService.branklike(userId)));
          resultList.add(new ViewResultAnaly("运营商喜好", hbaseDataService.carrier(userId)));
          resultList.add(new ViewResultAnaly("潮男女标签", hbaseDataService.chaonannv(userId)));
          resultList.add(new ViewResultAnaly("消费水平", hbaseDataService.consumptionlevel(userId)));
          resultList.add(new ViewResultAnaly("邮箱", hbaseDataService.emailinfo(userId)));
          resultList.add(new ViewResultAnaly("性别", hbaseDataService.sex(userId)));
          resultList.add(new ViewResultAnaly("用户分组", hbaseDataService.usergroupinfo(userId)));
          resultList.add(new ViewResultAnaly("设备", hbaseDataService.usetypeinfo(userId)));
          resultList.add(new ViewResultAnaly("年龄", hbaseDataService.ageinfo(userId)));
          ViewResultAnaly viewResultAnaly = new ViewResultAnaly();
          viewResultAnaly.setList(resultList);
          return JSON.toJSONString(viewResultAnaly);
        }
      case "baijiazhishu":
        {
          resultList.add(new ViewResultAnaly("败家指数", hbaseDataService.baijiazhishu(userId)));
          break;
        }
      case "brandlike":
        {
          resultList.add(new ViewResultAnaly("品牌喜好", hbaseDataService.branklike(userId)));
          break;
        }
      case "carrierinfo":
        {
          resultList.add(new ViewResultAnaly("运营商喜好", hbaseDataService.carrier(userId)));
          break;
        }
      case "chaonannv":
        {
          resultList.add(new ViewResultAnaly("潮男女标签", hbaseDataService.chaonannv(userId)));
          break;
        }
      case "consumptionlevel":
        {
          resultList.add(new ViewResultAnaly("消费水平", hbaseDataService.consumptionlevel(userId)));
          break;
        }
      case "emailinfo":
        {
          resultList.add(new ViewResultAnaly("邮箱", hbaseDataService.emailinfo(userId)));
          break;
        }
      case "sex":
        {
          resultList.add(new ViewResultAnaly("性别", hbaseDataService.sex(userId)));
          break;
        }
      case "usergroupinfo":
        {
          resultList.add(new ViewResultAnaly("用户分组", hbaseDataService.usergroupinfo(userId)));
          break;
        }
      case "usetypeinfo":
        {
          resultList.add(new ViewResultAnaly("设备", hbaseDataService.usetypeinfo(userId)));
          break;
        }
      case "ageinfo":
        {
          resultList.add(new ViewResultAnaly("年龄", hbaseDataService.ageinfo(userId)));
          break;
        }
    }
    return JSON.toJSONString(resultList);
  }
}
