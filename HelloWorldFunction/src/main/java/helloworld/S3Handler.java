package helloworld;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.time.Instant;

public class S3Handler {
    private final S3Client s3Client;
    private final String bucketName;

    public S3Handler(String bucketName) {
        this.s3Client = S3Client.builder()
                            .region(Region.US_EAST_1)
                            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                            .build();
        this.bucketName = bucketName;
    }

    public String uploadImageToS3(InputStream imageStream, String imageName, long contentLength) throws Exception {
        try {
            String imageKey = "images/" + System.currentTimeMillis() + "-" + imageName;
            String S3Link = "https://" + bucketName + ".s3.amazonaws.com/" + imageKey;
            if (imageStream == null) {
                throw new Exception("Image stream is null");
            }

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(imageKey)
                    .contentType("image/svg+xml")
                    .acl("public-read")
                    .expires(Instant.now().plusSeconds(60 * 60 * 24))
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(imageStream, contentLength));
            System.out.println("Image Link: " + S3Link);
            return S3Link;
        } catch (Exception e) {
            System.err.println("Failed to upload image to S3: " + e.getMessage());
            System.err.println(e);
            System.err.println(e.getStackTrace());
            throw e;
        }
    }

    public void close() {
        s3Client.close();
    }
}
