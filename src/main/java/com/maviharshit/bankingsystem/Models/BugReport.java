package com.maviharshit.bankingsystem.Models;

public class BugReport {
    private final int id;
    private final String payeeAddress;
    private final String description;
    private final String reportDate;
    private final String status;

    public BugReport(int id, String payeeAddress, String description, String reportDate, String status) {
        this.id = id;
        this.payeeAddress = payeeAddress;
        this.description = description;
        this.reportDate = reportDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getPayeeAddress() {
        return payeeAddress;
    }

    public String getDescription() {
        return description;
    }

    public String getReportDate() {
        return reportDate;
    }

    public String getStatus() {
        return status;
    }
}
