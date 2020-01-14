package com.faw.hq.dmp.spark.imp.headline.util

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import com.fasterxml.jackson.databind.ObjectMapper
import com.faw.hq.dmp.spark.imp.headline.bean.HeadLine

/**
  * author:ZhangXiuYun
  * purpose：存放方法
  */
object GetMethods {

  /**
    * json解析方法
    *
    */
  def getLead(json:String): String = {
    val objectMapper = new ObjectMapper
    val appInfo = objectMapper.readValue(json, classOf[HeadLine])
    appInfo.lineToHive()
  }

  def  getSecondsNextEarlyMorning() ={
    var cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, 1)
    // 改成这样就好了
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.MILLISECOND, 0)
    (cal.getTimeInMillis() - System.currentTimeMillis())
  }


  def main(args: Array[String]): Unit = {
    val json = "{\"k\":\" 活动 id\",\"m1\":\"ANDROIDID1\",\"m2\":\"IMEI\",\"m4\":\"AAID\",\"m5\":\"IDFA\",\"m6\":\"MAC1\",\"ni\":\"IESID\",\"ns\":\"IP 地址 \",\"p\":\"spid\",\"rt\":\" 行为类型 \"}"
    val json1 ="{\"k\":\" 002\",\"m1\":\"ANDROIDID1\",\"m2\":\"IMEI\",\"m4\":\"AAID\",\"m5\":\"IDFA\",\"m6\":\"MAC1\",\"ni\":\"IESID\",\"ns\":\"IP 192.168.1.101 \",\"p\":\"spid\",\"reportTime\":\"2019-12-17 15:18:58\",\"rt\":\" 2 \"}"
    val objectMapper = new ObjectMapper
    val headLine: HeadLine = objectMapper.readValue(json1, classOf[HeadLine])
    println(headLine.lineToHive())

  }

}

