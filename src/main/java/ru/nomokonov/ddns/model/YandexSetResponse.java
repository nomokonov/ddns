package ru.nomokonov.ddns.model;

public class YandexSetResponse {
    private String domain;
    private Long record_id;
    private SubDomain record;
    private String success;
    private String error;

    public YandexSetResponse() {
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Long getRecord_id() {
        return record_id;
    }

    public void setRecord_id(Long record_id) {
        this.record_id = record_id;
    }

    public SubDomain getRecord() {
        return record;
    }

    public void setRecord(SubDomain record) {
        this.record = record;
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
