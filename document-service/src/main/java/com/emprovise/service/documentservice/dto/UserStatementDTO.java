package com.emprovise.service.documentservice.dto;

public class UserStatementDTO {

    private String documentId;
    private String userId;
    private Boolean read;
    private String period;

    public UserStatementDTO() {
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "StatementDetailDTO{" +
                "documentId='" + documentId + '\'' +
                ", userId='" + userId + '\'' +
                ", read=" + read +
                ", period='" + period + '\'' +
                '}';
    }
}
