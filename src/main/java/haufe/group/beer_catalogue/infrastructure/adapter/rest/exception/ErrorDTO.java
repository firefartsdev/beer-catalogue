package haufe.group.beer_catalogue.infrastructure.adapter.rest.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDTO {

    private String code;
    private String message;

}
