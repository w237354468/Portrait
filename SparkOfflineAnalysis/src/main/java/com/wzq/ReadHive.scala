//package com.wzq
//
//import org.apache.spark.SparkConf
//import org.apache.spark.api.java.function.{MapFunction, ReduceFunction}
//import org.apache.spark.sql.{Encoders, Row, SparkSession}
//
//object ReadHive {
//
//  def main(args: Array[String]): Unit = {
//    import org.apache.spark.sql._
//    val conf = new SparkConf()
//    val ss = SparkSession.builder().config(conf).appName("AdsCtrPredictionLR")
//      .master("local[2]").enableHiveSupport().getOrCreate();
//    ss.sql("select * from original.ppppp")
//      .map((a:Row)=>{
//        val phone = a.getString(1)
//        val sex = a.getString(2)
//        val email = a.getString(3)
//        val age = a.getString(4)
//        val graduation = a.getLong(5)
//          sEvent(phone,sex,email,age,graduation)
//      })(Encoders.bean(Class[sEvent]))
//      .rdd
//      .map(a=>(a.phone,a.sex))
//      .groupBy(_._1)
//      .reduceByKey(new Function2[] {})
//      .reduce(new ReduceFunction[String] {
//        override def call(t: String, t1: String): String = ???
//      })
//
//    ss.stop()
//  }
//}
//case class sEvent(phone:String, sex:String, key:String, value:String, dayTime:Long)