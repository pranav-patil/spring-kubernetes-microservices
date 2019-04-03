package com.emprovise.service.documentservice.client.fallback;

import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FinanceServiceFallbackFactory implements FallbackFactory<StorageServiceClientFallback> {

    @Autowired
    private StorageServiceClientFallback financeServiceClientFallback;
    private static Logger logger = LoggerFactory.getLogger(FinanceServiceFallbackFactory.class);

    @Override
    public StorageServiceClientFallback create(Throwable cause) {

        if(!(cause instanceof RuntimeException && cause.getMessage() == null)) {
            logger.error("StorageServiceClient failed, switching to StorageServiceClientFallback", cause);
        }
        return financeServiceClientFallback;
    }

    @Override
    public String toString() {
        return financeServiceClientFallback.toString();
    }
}
