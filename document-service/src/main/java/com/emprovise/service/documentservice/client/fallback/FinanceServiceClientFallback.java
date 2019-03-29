package com.emprovise.service.documentservice.client.fallback;

import com.emprovise.service.documentservice.client.FinanceServiceClient;
import com.emprovise.service.documentservice.dto.DocumentDTO;
import org.springframework.stereotype.Component;

@Component
public class FinanceServiceClientFallback implements FinanceServiceClient {

    @Override
    public DocumentDTO getObject(String bucketId, String objectId) {
        return new DocumentDTO();
    }

    @Override
    public String info() {
        return "Hystrix Fallback Greeting";
    }
}
