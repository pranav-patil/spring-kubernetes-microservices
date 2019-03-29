package com.emprovise.service.documentservice.dto;

import java.util.Date;

public class StatementDetailDTO {

    private String documentId;
    private String payerId;
    private Boolean read;
    private Date date;

    public StatementDetailDTO() {
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "StatementDetailDTO{" +
                "documentId='" + documentId + '\'' +
                ", read=" + read +
                ", date=" + date +
                '}';
    }
}
