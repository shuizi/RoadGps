package com.rca_gps.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wafer on 2016/11/1.
 */

public class Entity implements Serializable{
    /**
     *  {"entity":
     *  {"plateNo":"京A888888",
     *  "templates":[{"versionNumber":"11.01.05","templateId":"e3565b40-24a3-4915-bb83-a1221afe027a","contractNumber":"4000003000065","format":"<?xml version=\"1.0\" encoding=\"GBK\"?>\n<request>\n    <requestHead>\n        <request_type> <\/request_type>\n        <uuid> <\/uuid>\n        <sender> <\/sender>\n        <server_version>1.0<\/server_version>\n        <user> <\/user>\n        <password> <\/password>\n        <flowintime>#TIME#<\/flowintime>\n    <\/requestHead>\n    <requestbody>\n        <assistanceInfoVo>\n            <FileNo>110000000001<\/FileNo>\n            <systemReceiveTime >#TIME#<\/systemReceiveTime>\n            <List>\n            \t<Position>\n            \t\t<longitudeLatitudeJ>#X#<\/longitudeLatitudeJ>\n                    <longitudeLatitudeW>#Y#<\/longitudeLatitudeW>\n                    <coordinateX>#X#<\/coordinateX>\n                    <coordinateY>#Y#<\/coordinateY>\n                    <arriveTime>#TIME#<\/arriveTime>\n                <\/Position>\n            <\/List>\n        <\/assistanceInfoVo>\n    <\/requestbody>\n<\/request>","postUrl":"http:\/\/http:\/\/ras-dev.rcaservice.com.cn\/api\/contractGps\/picc_driverInstantGps","postBody":"把format 中的字段：\n#TIME#,#FILENO#,#X#,#Y#,,#CASEID#\n全部替换","timmer":30,"type":"xml"},{"versionNumber":"11.01.10","templateId":"12aed3d0-b95f-4d25-b8a5-d4c6c2e4d8de","contractNumber":"4000003000084","format":"{\n  \"x\": \"#X#\",\n  \"y\": \"#Y#\",\n  \"caseId\": \"#CASEID#\",\n  \"time\": \"#TIME#\"\n}","postUrl":"http:\/\/ras-dev.rcaservice.com.cn\/api\/contractgps\/driverinstantgps","postBody":"把format 中的字段：\n#TIME#,#FILENO#,#X#,#Y#,,#CASEID#\n全部替换","timmer":60,"type":"json"}]},
     *  "pagination":null,"isSuccess":true,"errorCode":"None","errorMessage":"","resCode":0,"resDes":""}
     */
    private String plateNo;
    private List<Template> templates = new ArrayList<>();

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }
}
