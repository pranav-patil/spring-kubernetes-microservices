package com.emprovise.service.api;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableEurekaClient
@RestController
@RequestMapping("/s3")
public class S3Controller {

    @GetMapping("/document/{documentId}")
    public String getDocument(@PathVariable String documentId) {
        return "Microsoft Stock Details.";
    }

    @GetMapping("/info")
    public String info() {
        return "Hello This is mock S3 service.";
    }
}
