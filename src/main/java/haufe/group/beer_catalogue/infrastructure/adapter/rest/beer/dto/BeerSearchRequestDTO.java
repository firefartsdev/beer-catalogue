package haufe.group.beer_catalogue.infrastructure.adapter.rest.beer.dto;

import haufe.group.beer_catalogue.domain.beer.vo.BeerSort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record BeerSearchRequestDTO(
        String name,
        String type,
        Double abv,
        String manufacturerName,
        Integer page,
        Integer size,
        String direction,
        String sortBy
) {

    public int resolvedPage() {
        return page != null && page > 0 ? page : 0;
    }

    public int resolvedSize() {
        return size != null && size > 0 ? size : 10;
    }

    public BeerSort resolvedSort() {
        final var field = (sortBy != null && !sortBy.isBlank()) ? sortBy : "name";
        final var dir = "DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC.name() : Sort.Direction.ASC.name();

        return new BeerSort(dir, field);
    }

    public Pageable resolvedPageable() {
        return PageRequest.of(resolvedPage(), resolvedSize());
    }
}
