package com.rca_gps.app.model;

import java.io.Serializable;

/**
 * Created by wafer on 2016/11/1.
 */

public class Message implements Serializable {
    /**
     * "msgType":"templateChanged",
     * "contractNumber":"4000003000084",
     * "versionNumber":"11.01.09"caseId	string	案件Id	Y
     * caseNumber		案件号	Y
     * fileNo		文件号	Y
     * dateTime		时间的格式字符串（"yyyy-MM-dd HH:mm:ss"）	Y
     * templateId   GPS模板Id
     */
    private String msgType;
    private String contractNumber;
    private String versionNumber;
    private String caseNumber;
    private String fileNo;
    private String dateTime;
    private String templateId;
    private String caseId;

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }
}
