package com.rca_gps.app.model;

import java.io.Serializable;

/**
 * Created by wafer on 2016/12/6.
 */

public class HisttoryCaseInfo implements Serializable {
    /**
     *     "pagination": null,
     "isSuccess": true,
     "errorCode": "None",
     "errorMessage": "",
     "resCode": 0,
     "resDes": ""
     */
    private String errorCode;
    private String errorMessage;
    private String resDes;
    private String pagination;
    private boolean isSuccess;
    private int resCode;
    private HistoryInfo entity;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getResDes() {
        return resDes;
    }

    public void setResDes(String resDes) {
        this.resDes = resDes;
    }

    public String getPagination() {
        return pagination;
    }

    public void setPagination(String pagination) {
        this.pagination = pagination;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    public HistoryInfo getEntity() {
        return entity;
    }

    public void setEntity(HistoryInfo entity) {
        this.entity = entity;
    }
}
