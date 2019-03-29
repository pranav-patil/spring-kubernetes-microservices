package com.emprovise.service.dto;

import org.apache.commons.codec.binary.Base64;

public class DocumentDTO {

    private String binaryDocument;

    public DocumentDTO() {
    }

    public DocumentDTO(String binaryDocument) {
        this.binaryDocument = binaryDocument;
    }

    public DocumentDTO(byte[] bytes) {
        this.binaryDocument = Base64.encodeBase64String(bytes);
    }

    public String getBinaryDocument() {
        return binaryDocument;
    }

    public byte[] getBinaryFile() {
        return Base64.decodeBase64(binaryDocument);
    }

    public void setBinaryDocument(String binaryDocument) {
        this.binaryDocument = binaryDocument;
    }

    public void setBinaryDocument(byte[] bytes) {
        this.binaryDocument = Base64.encodeBase64String(bytes);
    }
}
