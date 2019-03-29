package com.emprovise.service.documentservice.dto;

public class DealerStatementDTO {

    private String documentId;
    private String payerId;
    private Boolean read;
    private String period;

    public DealerStatementDTO() {
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
                ", payerId='" + payerId + '\'' +
                ", read=" + read +
                ", period='" + period + '\'' +
                '}';
    }
}
