package haufe.group.beer_catalogue.application.exception;

import lombok.RequiredArgsConstructor;

public class ApplicationException extends RuntimeException {

    private final String errorCode;

    public ApplicationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
