package haufe.group.beer_catalogue.infrastructure.adapter.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.S3Client;

@RestController
@RequestMapping("/s3-test")
@RequiredArgsConstructor
public class S3TestController {

    private final S3ClientFactory s3ClientFactory;

    @Value("${aws.s3.bucket-name}")
    private String bucket;

    @GetMapping("/exists")
    public ResponseEntity<String> testBucket() {
        S3Client s3Client = s3ClientFactory.getS3Client();
        s3Client.headBucket(builder -> builder.bucket(bucket));
        return ResponseEntity.ok("✅ ¡Bucket accesible!");
    }
}