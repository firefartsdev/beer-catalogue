package haufe.group.beer_catalogue.application;

import haufe.group.beer_catalogue.application.exception.InvalidSortCriteriaException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class BaseSort {

    private static final List<String> ALLOWED_DIRECTIONS = List.of("ASC", "DESC");

    private final String direction;
    private final String sortBy;

    protected BaseSort(String direction, String sortBy) {
        final List<String> validationErrors = new ArrayList<>();

        if (!ALLOWED_DIRECTIONS.contains(direction)) {
            validationErrors.add("Invalid sort direction: " + direction);
        }

        if (!getAllowedFields().contains(sortBy)) {
            validationErrors.add("Invalid sort by: " + sortBy);
        }

        if (!validationErrors.isEmpty()) {
            throw new InvalidSortCriteriaException(String.join(" | ", validationErrors));
        }

        this.direction = direction;
        this.sortBy = sortBy;
    }

    protected abstract List<String> getAllowedFields();

}
