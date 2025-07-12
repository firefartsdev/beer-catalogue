package haufe.group.beer_catalogue.application.exception;

import java.util.UUID;

public class EntityNotFoundException extends ApplicationException {

    public EntityNotFoundException(String entityName, UUID id) {
        super("NOT_FOUND", entityName + " with id " + id + " not found");
    }
}
