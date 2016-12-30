package com.rca_gps.app.util;

/**
 * 类名：  com.rca_gps.app.util.AppUrl
 * 作者：  yanhao
 * From:
 * 功能 ：
 * 创建日期： 2016-10-27 21:14
 * 修改日期： 2016-10-27 21:14
 */
public class AppUrl {
    //测试地址
//    public static final String BASE_URL = "http://ras-dev.rcaservice.com.cn";
    public static final String BASE_URL = "https://ras-pre.rcaservice.com.cn";
    //上线地址
//    public static final String BASE_URL = "https://ras.rcaservice.com.cn";

    //注册地址
    public static final String REGISTER_URL ="/api/contractGps/appRegist";
    //获取最新模板信息
    public static final String GET_TEMPLATE_CHANGE ="/api/contractGps/getTemplateById";
    //获取历史按键
    public static final String GET_HISTORY_CASE = "/api/contractGps/getAcceptedCases";
}
