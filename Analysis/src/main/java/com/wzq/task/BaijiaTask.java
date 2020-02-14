package com.wzq.task;

import com.wzq.reduce.BaijiaReduce;
import com.wzq.entity.BaijiaInfo;
import com.wzq.map.BaijiaMap;
import com.wzq.util.DateUtil;
import com.wzq.util.HbaseUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.LocalEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.utils.ParameterTool;

public class BaijiaTask {

  public static void main(String[] args) {
    ParameterTool parameter = ParameterTool.fromArgs(args);
    LocalEnvironment env = ExecutionEnvironment.createLocalEnvironment();
    env.getConfig().setGlobalJobParameters(parameter);

    // 统计相同年代人数
    DataSource<String> text = env.readTextFile("input");
    DataSet<BaijiaInfo> mapResult = text.map(new BaijiaMap());
    DataSet<BaijiaInfo> reduceResult = mapResult.groupBy("groupFieldId").reduce(new BaijiaReduce());
    // 更新或删除总人数
    try {
      List<BaijiaInfo> resultList = reduceResult.collect(); // BaijiaInfo 里包括每个用户的全部信息
      for (BaijiaInfo baijiaInfo : resultList) { // 单个用户
        String userId = baijiaInfo.getUserId(); // 用户ID
        List<BaijiaInfo> ordersInOneUser = baijiaInfo.getList(); // 单个用户的所有订单
        Collections.sort(
            ordersInOneUser,
            new Comparator<BaijiaInfo>() {
              @Override
              public int compare(BaijiaInfo o1, BaijiaInfo o2) {
                String timeo1 = o1.getCreateTime();
                String timeo2 = o2.getCreateTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd hhmmss");
                Date dateNow = new Date();
                Date time1 = dateNow;
                Date time2 = dateNow;
                try {
                  time1 = dateFormat.parse(timeo1);
                  time2 = dateFormat.parse(timeo2);
                } catch (ParseException e) {
                  e.printStackTrace();
                }
                return time1.compareTo(time2);
              }
            }); // 按照顶安东尼创建时间排序，从小到大

        Map<Integer, Integer> frequencyMap = new HashMap<>(); // 全局统计购买间隔
        BaijiaInfo beforeorder = null; // 全局前一个订单
        double maxAmount = 0.0; // 全局最大金额
        double sum = 0.0; // 全局金额总和

        for (BaijiaInfo perOrder : ordersInOneUser) {
          if (beforeorder == null) {
            beforeorder = perOrder;
            continue;
          }
          // 计算购买频率
          String beforeTime = beforeorder.getCreateTime(); // 前一个订单的创建时间
          String endTime = perOrder.getCreateTime(); // 当前订单的创建时间
          int daysBetweenTwoOrder =
              DateUtil.getDaysBetweenByStartAndEnd(beforeTime, endTime, "yyyyMMdd hhmmss");
          int gapDays =
              frequencyMap.get(daysBetweenTwoOrder) == null
                  ? 0
                  : frequencyMap.get(daysBetweenTwoOrder);
          frequencyMap.put(daysBetweenTwoOrder, gapDays + 1); // 每隔几天购买一次出现的次数

          // 计算最大金额
          String totalAmountCurrentOrderString = perOrder.getTotalAmount();
          double totalAmountCurrentOrder = Double.parseDouble(totalAmountCurrentOrderString);
          if (totalAmountCurrentOrder > maxAmount) {
            maxAmount = totalAmountCurrentOrder;
          }
          // 计算平均值
          sum += totalAmountCurrentOrder;
          beforeorder = perOrder;
        }
        double avramount = sum / ordersInOneUser.size();
        int totaldays = 0;
        Set<Entry<Integer, Integer>> set = frequencyMap.entrySet();
        for (Entry<Integer, Integer> entry : set) {

          Integer frequencyDays = entry.getKey();
          Integer count = entry.getValue();
          totaldays += frequencyDays * count;
        }
        int avrDays = totaldays / ordersInOneUser.size(); // 平均天数

        // 败家指数= 支付金额平均值*0.3   ,最大支付金额*0.3 ,下单频率*0.4
        int avramountscore = 0;
        if (avramount > 0 && avramount < 20) {
          avramountscore = 5;
        }

        int maxamountscore = 0;
        if (maxAmount >= 0 && avramount < 20) {
          maxamountscore = 5;
        }

        // 下单率
        int avrdayscore = 0;
        if (avrDays > 0 && avrDays < 5) {
          avrdayscore = 100;
        }
        double totalscore =
            (avramountscore / 100) * 30 + (maxamountscore / 100) * 30 + (avrdayscore / 100) * 30;

        String rowKey = userId;
        String tableName = "userFlagInfo";
        String familyName = "baseInfo";
        String column = "baijiasouce"; // 运营商
        HbaseUtil.putData(tableName, rowKey, familyName, column, "totalscore" + totalscore);
      }
      env.execute("baijia score");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
