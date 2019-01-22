package com.okjiaoyu.jmeter.report;

public class CleanReportListen implements Runnable {

    ReportTimer reportTimer;
    @Override
    public void run() {
        reportTimer = new ReportTimer();
    }
}
