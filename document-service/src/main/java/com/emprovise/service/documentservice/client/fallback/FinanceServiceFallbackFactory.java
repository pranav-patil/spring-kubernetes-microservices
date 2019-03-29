package com.emprovise.service.documentservice.client.fallback;

import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FinanceServiceFallbackFactory implements FallbackFactory<S3ServiceClientFallback> {

    @Autowired
    private S3ServiceClientFallback financeServiceClientFallback;
    private static Logger logger = LoggerFactory.getLogger(FinanceServiceFallbackFactory.class);

    @Override
    public S3ServiceClientFallback create(Throwable cause) {

        if(!(cause instanceof RuntimeException && cause.getMessage() == null)) {
            logger.error("S3ServiceClient failed, switching to S3ServiceClientFallback", cause);
        }
        return financeServiceClientFallback;
    }

    @Override
    public String toString() {
        return financeServiceClientFallback.toString();
    }
}
