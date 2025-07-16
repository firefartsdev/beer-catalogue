package haufe.group.beer_catalogue.application.beer;

import haufe.group.beer_catalogue.application.exception.EntityNotFoundException;
import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import haufe.group.beer_catalogue.domain.images.ImagesRepository;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateBeerUseCase {

    private final BeerRepository beerRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final ImagesRepository imagesRepository;

    public Beer createBeer(final Beer beer, final MultipartFile image) {
        final var manufacturer = this.manufacturerRepository.findById(beer.manufacturer().id())
                .orElseThrow(() -> new EntityNotFoundException("Manufacturer", beer.manufacturer().id()));
        String imageUrl = null;
        try {
            imageUrl = this.imagesRepository.upload(image);
        } catch (Exception e) {
            log.error("[CreateBeerUseCase.createBeer] - error uploading image beer -> {}", e.getMessage());
        }

        final var newBeer = new Beer(
                beer.id(),
                beer.name(),
                beer.abv(),
                beer.type(),
                beer.description(),
                manufacturer,
                imageUrl,
                null
        );
        try {
            return this.beerRepository.create(newBeer);
        } catch (Exception e) {
            log.error("[CreateBeerUseCase.createBeer] - error creating beer -> {}", e.getMessage());
            if(imageUrl != null) {
                try {
                    this.imagesRepository.delete(imageUrl);
                } catch (Exception e1) {
                    log.error("[CreateBeerUseCase.createBeer] - error deleting image {} beer -> {}", imageUrl, e.getMessage());
                }
            }
            return null;
        }
    }
}
