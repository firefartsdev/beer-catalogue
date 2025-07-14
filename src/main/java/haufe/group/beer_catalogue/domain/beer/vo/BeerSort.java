package haufe.group.beer_catalogue.domain.beer.vo;

import haufe.group.beer_catalogue.domain.BaseSort;
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
