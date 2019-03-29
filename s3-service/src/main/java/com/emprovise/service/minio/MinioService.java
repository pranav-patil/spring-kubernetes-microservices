package com.emprovise.service.minio;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class MinioService {

    private MinioClient minioClient;

    public MinioService(@Value("${cloud.minio.endpoint}") final String endpoint,
                        @Value("${cloud.minio.accessKey}") final String accessKey,
                        @Value("${cloud.minio.secretKey}") final String secretKey) {
        try {
            this.minioClient = new MinioClient(endpoint, accessKey, secretKey);
        } catch (InvalidPortException | InvalidEndpointException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getObject(String bucketId, String objectId) throws IOException {

        InputStream stream = null;

        try {
            minioClient.statObject(bucketId, objectId);
            stream = minioClient.getObject(bucketId, objectId);
            return IOUtils.toByteArray(stream);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(stream != null) {
                stream.close();
            }
        }
    }
}
