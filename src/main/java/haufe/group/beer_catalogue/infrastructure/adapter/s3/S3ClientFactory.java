package haufe.group.beer_catalogue.infrastructure.adapter.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Component
public class S3ClientFactory {

    private final S3Client s3Client;

    public S3ClientFactory(@Value("${aws.s3.region}") String region) {
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public S3Client getS3Client() {
        return s3Client;
    }

}
