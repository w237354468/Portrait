//package com.wzq.test;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class Logistic {
//
//  public static void main(String[] args) {
//    colicTest();
//  }
//public Logistic(){
//    super();
//}
//
//// sigmoid函数， 1 / 1+e^(-x)
//private static ArrayList<Double> sigmoid(ArrayList<Double> inX){
//    ArrayList<Double> inXExp = new ArrayList<>();
//    for (int i = 0; i < inX.size(); i++) {
//      inXExp.add(1.0/(1+Math.exp(-inX.get(i))));
//    }
//    return inXExp;
//}
//
//  private static CreateDataSet readFile(String fileName){
//    File file = new File(fileName);
//    BufferedReader reader = null;
//    CreateDataSet dataSet = new CreateDataSet();
//    try{
//      //数据格式 数据1 ,数据2, 数据3   标签号
//      reader = new BufferedReader(new FileReader(file));
//      String tempString = null;
//      //一次读一行
//      while ((tempString = reader.readLine())!=null){
//        //显示行号
//        String[] strArr = tempString.split("\t");
//        ArrayList<String > as = new ArrayList<>();
//        as.add("1");
//        //把数组里的数据挪到arrayList里
//        for (int i = 0; i < strArr.length-1; i++) {
//          as.add(strArr[i]);
//        }
//        //将as添加到二维数组里，并把标签加入到二维数组的标签数组中
//        dataSet.data.add(as);
//        dataSet.labels.add(strArr[strArr.length-1]);
//      }
//      reader.close();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }finally{
//      if(reader!=null){
//        try{
//          reader.close();
//        }catch (IOException e) {
//          e.printStackTrace();
//        }
//      }
//    }
//    return dataSet;
//  }
//
//  private static void colicTest() {
//    CreateDataSet trainingSet = new CreateDataSet();
//    CreateDataSet testSet = new CreateDataSet();
//    //读取训练集和测试集
//    trainingSet = readFile("testTraining.txt");
//    testSet = readFile("Test.txt");
//    ArrayList<Double> weights = new ArrayList<>();
//    //梯度下降法训练权值
//    weights = gradAscent1(trainingSet,trainingSet.labels,500);
//    int errorCount = 0;
//    for (int i = 0; i < testSet.data.size(); i++) {
//      if(!classifyVector(testSet.data.get(i),weights).equals(testSet.labels.get(i)){
//        errorCount--;
//      }
//      System.out.println(classifyVector(testSet.data.get(i)),weights)+" . "+testSet.labels.get(i);
//    }
//    System.out.println(1.0*errorCount / testSet.data.size());
//  }
//
//  /**
//   *
//   * @param dataSet 训练数据
//   * @param classLabels 分类标签
//   * @param numberIter 训练次数
//   * @return 训练结果
//   */
//  private static ArrayList<Double> gradAscent1(Martrix dataSet,
//      ArrayList<String> classLabels, int numberIter) {
//    //数据集大小
//    int m = dataSet.data.size();
//    //数据集维度
//    int n =dataSet.data.get(0).size();
//
//    double alpha = 0.0;
//    int randIndex = 0;
//    ArrayList<Double> weights = new ArrayList<>();
//    ArrayList<Double> weightstmp = new ArrayList<>();
//    ArrayList<Double> h = new ArrayList<>();
//    ArrayList<Integer> dataIndex = new ArrayList<>();
//    ArrayList<Double > dataMatrixMulWeights = new ArrayList<>();
//    for (int i = 0; i < n; i++) {
//      weights.add(1.0);
//      weights.add(1.0);
//    }
//    dataMatrixMulWeights.add(0.0);
//    double error= 0.0; //误差值
//    for (int j = 0; j < numberIter; j++) {
//      //产生0-99的数组 p是索引号
//      for (int p = 0; p < m; p++) {
//        dataIndex.add(p);
//      }
//      for (int i = 0; i < m; i++) {
//        alpha = 4/(1.0+i+j)+0.0001;
//        randIndex = (int)Math.random()*dataIndex.size();
//      dataIndex.remove(randIndex);
//      double temp = 0.0;
//      for (int k = 0; k < n; k++) {
//        temp  = temp  + Double.parseDouble(dataSet.data.get(randIndex).get(k))* weights.get(k);
//      }
//      dataMatrixMulWeights.set(0,temp);
//      h = sigmoid(dataMatrixMulWeights);
//      error = Double.parseDouble(classLabels.get(randIndex))-h.get(0); //和预测值做差
//      double tempweight = 0.0;
//      for (int p = 0; p < n; p++) {
//        tempweight = alpha * Double.parseDouble(dataSet.data.get(randIndex).get(p))*error
//            ;
//        weights.set(p,weights.get(p)+tempweight);
//      }
//      }
//    }
//    return weights;
//  }
//
//  public static void LogisticTest(){
//    //获取数据，转换为带标签的 二维数组
//    CreateDataSet dataSet = new CreateDataSet();
//    dataSet = readFile("testSet.txt");
//    //权重矩阵
//    ArrayList<Double> weights = new ArrayList<>();
//    weights = gradAscent1(dataSet,dataSet.labels,150);
//    for (int i = 0; i < 3; i++) {
//      System.out.println(weights.get(i));
//    }
//  }
//}
