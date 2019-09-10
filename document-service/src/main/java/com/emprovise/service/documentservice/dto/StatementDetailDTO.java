package com.emprovise.service.documentservice.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class StatementDetailDTO {

    private String documentId;
    private String documentReference;
    private String userId;
    private Boolean read;
    private LocalDateTime dateTime;

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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "StatementDetailDTO{" +
                "documentId='" + documentId + '\'' +
                ", documentReference='" + documentReference + '\'' +
                ", userId='" + userId + '\'' +
                ", read=" + read +
                ", dateTime=" + dateTime +
                '}';
    }
}
