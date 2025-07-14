package haufe.group.beer_catalogue.domain.manufacturer.vo;

import haufe.group.beer_catalogue.domain.BaseSort;
import java.util.List;

public class ManufacturerSort extends BaseSort {

    public ManufacturerSort(String direction, String sortBy) {
        super(direction, sortBy);
    }

    @Override
    protected List<String> getAllowedFields() {
        return List.of("name", "country");
    }
}
