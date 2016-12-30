package com.rca_gps.app.model;

import java.io.Serializable;

/**
 * Created by wafer on 2016/11/1.
 */

public class Template implements Serializable {
    /**
     * "versionNumber": "11.01.10",
     * "templateId": "12aed3d0-b95f-4d25-b8a5-d4c6c2e4d8de",
     * "contractNumber": "4000003000084",
     * "format": "{\n  \"x\": \"#X#\",\n  \"y\": \"#Y#\",\n  \"caseId\": \"#CASEID#\",\n  \"time\": \"#TIME#\"\n}",
     * "postUrl": "http://ras-dev.rcaservice.com.cn/api/contractgps/driverinstantgps",
     * "postBody": "把format 中的字段：\n#TIME#,#FILENO#,#X#,#Y#,,#CASEID#\n全部替换",
     * "timmer": 60,
     * "type": "json"
     */
    private String versionNumber;
    private String templateId;
    private String contractNumber;
    private String format;
    private String postUrl;
    private String postBody;
    private String type;
    private int timmer;
    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTimmer() {
        return timmer;
    }

    public void setTimmer(int timmer) {
        this.timmer = timmer;
    }
}
