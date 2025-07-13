package haufe.group.beer_catalogue.application.exception;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityName, UUID id) {
        super(entityName + " with id " + id + " not found");
    }
}
