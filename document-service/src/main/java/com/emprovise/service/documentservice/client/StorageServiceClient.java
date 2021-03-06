package com.emprovise.service.documentservice.client;

import com.emprovise.service.documentservice.client.fallback.FinanceServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.emprovise.service.documentservice.dto.DocumentDTO;

@Component
@FeignClient(value = "storage-service", fallbackFactory = FinanceServiceFallbackFactory.class)
public interface StorageServiceClient {

    @RequestMapping(value = "/storage/bucket/{bucketId}/object/{objectId}",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            })
    DocumentDTO getObject(@PathVariable("bucketId") String bucketId,
                          @PathVariable("objectId") String objectId);

    @RequestMapping("/storage/info")
    String info();
}
