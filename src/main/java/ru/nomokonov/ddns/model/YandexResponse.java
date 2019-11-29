package ru.nomokonov.ddns.model;

import java.util.List;

public class YandexResponse {
    private String domain;
    private List<SubDomain> records;
    private String success;
    private String error;

    public YandexResponse() {
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<SubDomain> getRecords() {
        return records;
    }

    public void setRecords(List<SubDomain> records) {
        this.records = records;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
