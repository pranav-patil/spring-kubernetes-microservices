package com.emprovise.service.minio;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.MinioException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioService {

    @Value("${cloud.minio.endpoint}")
    private String endpoint;

    @Value("${cloud.minio.accessKey}")
    private String accessKey;

    @Value("${cloud.minio.secretKey}")
    private String secretKey;

    private MinioClient minioClient;

    public MinioService() {
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
