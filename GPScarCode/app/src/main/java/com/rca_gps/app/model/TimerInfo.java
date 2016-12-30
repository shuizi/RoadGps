package com.rca_gps.app.model;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wafer on 2016/11/18.
 */

public class TimerInfo implements Serializable{
    private Timer timer;
    private String caseId;
    private TimerTask timerTask;
    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public TimerTask getTimerTask() {
        return timerTask;
    }

    public void setTimerTask(TimerTask timerTask) {
        this.timerTask = timerTask;
    }
}
