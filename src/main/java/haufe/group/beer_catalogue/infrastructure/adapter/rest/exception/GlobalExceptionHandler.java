package haufe.group.beer_catalogue.infrastructure.adapter.rest.exception;

import haufe.group.beer_catalogue.application.exception.EntityNotFoundException;
import haufe.group.beer_catalogue.application.exception.InvalidSortCriteriaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleEntityNotFoundException(final EntityNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND)
                .body(new ErrorDTO("NOT FOUND", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        final var errors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(" | "));
        return ResponseEntity.badRequest()
                .body(new ErrorDTO("VALIDATION_EXCEPTION", errors));
    }

    @ExceptionHandler(InvalidSortCriteriaException.class)
    public ResponseEntity<ErrorDTO> handleInvalidSortCriteriaException(final InvalidSortCriteriaException e) {
        return ResponseEntity.badRequest().body(new ErrorDTO("INVALID_SORT_CRITERIA", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(final Exception e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ErrorDTO("INTERNAL SERVER ERROR", e.getMessage()));
    }
}
