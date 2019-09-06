package com.emprovise.service.dataservice.dto;

import java.util.Date;

public class StatementDetail {

    private String documentId;
    private String documentReference;
    private String userId;
    private Boolean read;
    private Date date;

    public StatementDetail() {
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(String documentReference) {
        this.documentReference = documentReference;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
        return "StatementDetail{" +
                "documentId='" + documentId + '\'' +
                ", read=" + read +
                ", date=" + date +
                '}';
    }
}
