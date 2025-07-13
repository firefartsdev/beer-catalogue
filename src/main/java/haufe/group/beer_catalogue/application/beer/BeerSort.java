package haufe.group.beer_catalogue.application.beer;

import haufe.group.beer_catalogue.application.BaseSort;
import java.util.List;

public class BeerSort extends BaseSort {

    public BeerSort(String direction, String sortBy) {
        super(direction, sortBy);
    }

    @Override
    protected List<String> getAllowedFields() {
        return List.of(
                "name",
                "abv",
                "type",
                "description",
                "manufacturer.name",
                "manufacturer.country"
        );
    }
}
