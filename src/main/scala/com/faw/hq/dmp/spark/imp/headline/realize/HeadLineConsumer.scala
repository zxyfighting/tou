package com.faw.hq.dmp.spark.imp.headline.realize

import com.faw.hq.dmp.spark.imp.headline.contanst.KafkaParams
import com.faw.hq.dmp.spark.imp.headline.inter.{ImplExposure, ImplJDBC, Implkafka}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming._
import org.apache.spark.{SparkConf, SparkContext}
import com.faw.hq.dmp.spark.imp.headline.util._
import org.apache.log4j.Logger

/**
  * @ program: headline
  * @ description
  * @ author: ZhangXiuYun
  * @ create: 2019-12-17 09:18
  **/
object HeadLineConsumer {
  private val logger: Logger = Logger.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    while (true) {
      //配置spark上下文环境对象
      val processInterval = 59
      val conf = new SparkConf().setAppName("lead_center").setMaster("local[*]")
      val sc = new SparkContext(conf)
      //设置采集器每十秒批次拉取数据
      val sparkstream = new StreamingContext(sc, Seconds(processInterval))

      // 初始化 Redis 连接池
      JedisPoolUtils.makePool(RedisConfig("prod.dbaas.private", 16359, 30000, "realtime123", 1000, 100, 50))
      val kafkaStream = KafkaRedisUtils.createDirectStream(sparkstream, KafkaParams.kafkaParams1, KafkaParams.module, KafkaParams.groupid, KafkaParams.topics)

      //将offset交给redis维护
      Implkafka.getOffset(KafkaParams.module,KafkaParams.groupid,kafkaStream)
      //将一条条json数据解
      val resultDestream1: DStream[String] = kafkaStream.map(_.value()).map(json => {
        val info: String = GetMethods.getLead(json)
        info
      })
      resultDestream1.cache()

      //将数据保存到HDFS上
      resultDestream1.foreachRDD(rdd => {
        Implkafka.toHDFS(rdd)
      })

      //曝光量
        val exposureDestream: DStream[(String, Long)] = resultDestream1.filter(rdd => {
          val strings = rdd.split(";")
          !strings(3).equals("\\N") && strings(3).equals("1")
        }
        ).map(rdd => {
          val strings = rdd.split(";")
          (strings(3), 1L)
        })
      val exposureCounts: DStream[(String, Long)] =ImplExposure.exposureClick(exposureDestream)
      //将数据导入mysql中
      val sql = "replace into dmp_behavior_toutiao_exposure (exposure_dt,exposure_amounts,create_time,update_time) values(?,?,?,now())"
     ImplJDBC.puvCounts(sql,exposureCounts)

     //点击量
     val clickDestream: DStream[(String, Long)] = resultDestream1.filter(rdd => {
       val strings = rdd.split(";")
       !strings(3).equals("\\N") && strings(3).equals("2")
     }
     ).map(rdd => {
       val strings = rdd.split(";")
       (strings(3), 1L)
     })
      val clickCounts: DStream[(String, Long)] =ImplExposure.exposureClick(clickDestream)
      //将数据写入mysql中
      val sql1 = "replace into dmp_behavior_toutiao_click (click_dt,click_amounts,create_time,update_time) values(?,?,?,now())"
      ImplJDBC.puvCounts(sql1,clickCounts)

      //开启采集器
      sparkstream.start()
      sparkstream.awaitTerminationOrTimeout(GetMethods.getSecondsNextEarlyMorning())
      sparkstream.stop(false, true)

    }
  }
 }