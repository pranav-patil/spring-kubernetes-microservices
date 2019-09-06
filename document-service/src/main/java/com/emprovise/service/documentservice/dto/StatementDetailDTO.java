package com.emprovise.service.documentservice.dto;

import java.util.Date;

public class StatementDetailDTO {

    private String documentId;
    private String documentReference;
    private String userId;
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
        return "StatementDetailDTO{" +
                "documentId='" + documentId + '\'' +
                ", documentReference='" + documentReference + '\'' +
                ", userId='" + userId + '\'' +
                ", read=" + read +
                ", date=" + date +
                '}';
    }
}
