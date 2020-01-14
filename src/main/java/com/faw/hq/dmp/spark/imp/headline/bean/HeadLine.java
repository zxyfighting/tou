package com.faw.hq.dmp.spark.imp.headline.bean;


import org.apache.htrace.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @program: headline
 * @description
 * @author: ZhangXiuYun
 * @create: 2019-12-17 09:21
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeadLine {
    private String reportTime;
    private String k;   //活动编码名
    private String p;   //渠道编码
    private String rt;  //行为类型，1 表示曝光，2 表示点击
    private String ns;  //客户 ip 地址
    private String ni;  //媒体传过来的 iesid
    private String m0;  //OpenUDID
    private String m1;  //android id1
    private String m2;  //IMEI 的 md5
    private String m4;  //AAID
    private String m5;  //IDFA
    private String m6;  //MAC 的 md5

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getK() {
        return k;
    }

    public void setK(String activityId) {
        this.k = activityId;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getNs() {
        return ns;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getNi() {
        return ni;
    }

    public void setNi(String ni) {
        this.ni = ni;
    }

    public String getM0() {
        return m0;
    }

    public void setM0(String m0) {
        this.m0 = m0;
    }

    public String getM1() {
        return m1;
    }

    public void setM1(String m1) {
        this.m1 = m1;
    }

    public String getM2() {
        return m2;
    }

    public void setM2(String m2) {
        this.m2 = m2;
    }

    public String getM4() {
        return m4;
    }

    public void setM4(String m4) {
        this.m4 = m4;
    }

    public String getM5() {
        return m5;
    }

    public void setM5(String m5) {
        this.m5 = m5;
    }

    public String getM6() {
        return m6;
    }

    public void setM6(String m6) {
        this.m6 = m6;
    }

    public String lineToHive(){
        StringBuilder sb = new StringBuilder();
                 sb.append(reportTime==null || reportTime.trim().length()==0 ? "\\N;":reportTime+";").append(k==null ||k.trim().length()==0 ? "\\N;":k+";").append(p==null ||p.trim().length()==0 ? "\\N;":p+";").append(rt==null ||rt.trim().length()==0 ? "\\N;":rt+";")
                .append(ns==null ||ns.trim().length()==0 ? "\\N;":ns+";").append(ni==null ||ni.trim().length()==0 ? "\\N;":ni+";").append(m0==null ||m0.trim().length()==0 ? "\\N;":m0+";")
                .append(m1==null ||m1.trim().length()==0 ? "\\N;":m1+";").append(m2==null ||m2.trim().length()==0 ? "\\N;":m2+";").append(m4==null ||m4.trim().length()==0 ? "\\N;":m4+";")
                .append(m5==null ||m5.trim().length()==0 ? "\\N;":m5+";").append(m6==null ||m6.trim().length()==0 ? "\\N":m6);
        return sb.append(";").append("toutiao").toString();
    }
}
