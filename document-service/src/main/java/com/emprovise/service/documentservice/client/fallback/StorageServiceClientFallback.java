package com.emprovise.service.documentservice.client.fallback;

import com.emprovise.service.documentservice.client.StorageServiceClient;
import com.emprovise.service.documentservice.dto.DocumentDTO;
import org.springframework.stereotype.Component;

@Component
public class StorageServiceClientFallback implements StorageServiceClient {

    @Override
    public DocumentDTO getObject(String bucketId, String objectId) {
        return new DocumentDTO();
    }

    @Override
    public String info() {
        return "Hystrix Fallback Greeting";
    }
}
