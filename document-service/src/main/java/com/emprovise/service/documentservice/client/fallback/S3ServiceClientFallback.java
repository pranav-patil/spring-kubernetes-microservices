package com.emprovise.service.documentservice.client.fallback;

import com.emprovise.service.documentservice.client.S3ServiceClient;
import com.emprovise.service.documentservice.dto.DocumentDTO;
import org.springframework.stereotype.Component;

@Component
public class S3ServiceClientFallback implements S3ServiceClient {

    @Override
    public DocumentDTO getObject(String bucketId, String objectId) {
        return new DocumentDTO();
    }

    @Override
    public String info() {
        return "Hystrix Fallback Greeting";
    }
}
