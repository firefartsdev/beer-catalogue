package haufe.group.beer_catalogue.domain.images;

import org.springframework.web.multipart.MultipartFile;

public interface ImagesRepository {

    String upload(MultipartFile file);
    String download(String url);
    void delete(String url);
}
