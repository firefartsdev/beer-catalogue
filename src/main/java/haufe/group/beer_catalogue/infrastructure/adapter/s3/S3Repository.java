package haufe.group.beer_catalogue.infrastructure.adapter.s3;

import haufe.group.beer_catalogue.domain.images.ImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Repository implements ImagesRepository {

    private final S3ClientFactory s3ClientFactory;
    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    @Value("${aws.s3.region}")
    private String region;

    @Override
    public String upload(MultipartFile file) {
        S3Client s3Client = s3ClientFactory.getS3Client();
        String key = UUID.randomUUID() + "-" + file.getOriginalFilename();
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;
        } catch (IOException e) {
            throw new RuntimeException("[S3Repository.upload]", e);
        }
    }

    @Override
    public String download(String url) {
        String key = URLDecoder.decode(url.substring(url.lastIndexOf("/") + 1), StandardCharsets.UTF_8);
        S3Client s3Client = s3ClientFactory.getS3Client();
        try {
            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(
                    GetObjectRequest.builder().bucket(bucketName).key(key).build()
            );
            final var bytes = response.readAllBytes();
            final var contentType = response.response().contentType();
            final var base64 = Base64.getEncoder().encodeToString(bytes);
            return "data:" + contentType + ";base64," + base64;
        } catch (Exception e) {
            throw new RuntimeException("[S3Repository.download]", e);
        }
    }

    @Override
    public void delete(String url) {
        final var s3Client = s3ClientFactory.getS3Client();
        try {
            String key = URLDecoder.decode(url.substring(url.lastIndexOf("/") + 1), StandardCharsets.UTF_8);
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("[S3Repository.delete]", e);
        }
    }
}
