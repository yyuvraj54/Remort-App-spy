package com.app.remortapp;

public class OrderHelper {
    String ontime,offtime,startRec,forceClose,running;
    public OrderHelper(){}

    public OrderHelper(String ontime, String offtime, String startRec, String forceClose, String running) {
        this.ontime = ontime;
        this.offtime = offtime;
        this.startRec = startRec;
        this.forceClose = forceClose;
        this.running= running;
    }

    public String getOntime() {
        return ontime;
    }

    public void setOntime(String ontime) {
        this.ontime = ontime;
    }

    public String getOfftime() {
        return offtime;
    }

    public void setOfftime(String offtime) {
        this.offtime = offtime;
    }

    public String getStartRec() {
        return startRec;
    }

    public void setStartRec(String startRec) {
        this.startRec = startRec;
    }

    public String getForceClose() {
        return forceClose;
    }

    public void setForceClose(String forceClose) {
        this.forceClose = forceClose;
    }

    public String getRunning() {
        return running;
    }

    public void setRunning(String running) {
        this.running = running;
    }

}
