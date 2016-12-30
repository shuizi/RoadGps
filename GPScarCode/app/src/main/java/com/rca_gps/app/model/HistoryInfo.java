package com.rca_gps.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wafer on 2016/12/6.
 */

public class HistoryInfo implements Serializable{
    private List<Message> caseInfos = new ArrayList<>();

    public List<Message> getCaseInfos() {
        return caseInfos;
    }

    public void setCaseInfos(List<Message> caseInfos) {
        this.caseInfos = caseInfos;
    }
}
