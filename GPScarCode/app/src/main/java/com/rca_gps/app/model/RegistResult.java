package com.rca_gps.app.model;

import java.io.Serializable;

/**
 * Created by wafer on 2016/11/1.
 */

public class RegistResult implements Serializable {
    /**
     *   "entity": {}
     *     "pagination": null,
     "isSuccess": true,
     "errorCode": "None",
     "errorMessage": "",
     "resCode": 0,
     "resDes": ""
     */
    private Entity entity;
    private String pagination;
    private boolean isSuccess;
    private String errorCode;
    private String errorMessage;
    private String resDes;
    private int resCode;

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
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

    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }
}
