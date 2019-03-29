package com.emprovise.service.api;

import com.emprovise.service.dto.DocumentDTO;
import com.emprovise.service.minio.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@EnableEurekaClient
@RestController
@RequestMapping("/storage/s3")
public class S3Controller {

    @Autowired
    private MinioService minioService;

    @GetMapping("/bucket/{bucketId}/object/{objectId}")
    public DocumentDTO getDocument(@PathVariable String bucketId,
                              @PathVariable String objectId) {
        try {
            return new DocumentDTO(minioService.getObject(bucketId, objectId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/info")
    public String info() {
        return "Hello This is mock S3 service.";
    }
}
