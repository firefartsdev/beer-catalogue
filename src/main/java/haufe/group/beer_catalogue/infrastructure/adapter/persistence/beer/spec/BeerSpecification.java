package haufe.group.beer_catalogue.infrastructure.adapter.persistence.beer.spec;

import haufe.group.beer_catalogue.infrastructure.adapter.persistence.beer.BeerJPAEntity;
import org.springframework.data.jpa.domain.Specification;

public class BeerSpecification {

    public static Specification<BeerJPAEntity> nameContains(String name) {
        return notNullNorEmpty(name,
                (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%")
        );
    }

    public static Specification<BeerJPAEntity> typeEquals(String type) {
        return notNullNorEmpty(type,
                (root, query, cb) -> cb.equal(cb.lower(root.get("type")), type.toLowerCase())
        );
    }

    public static Specification<BeerJPAEntity> abvEquals(Double abv) {
        return notNull(abv,
                (root, query, cb) -> cb.equal(root.get("abv"), abv)
        );
    }

    public static Specification<BeerJPAEntity> manufacturerNameContains(String manufacturerName) {
        return notNullNorEmpty(manufacturerName,
                (root, query, cb) -> cb.like(cb.lower(root.get("manufacturer").get("name")), "%" + manufacturerName.toLowerCase() + "%")
        );
    }

    private static Specification<BeerJPAEntity> notNull(Object value, Specification<BeerJPAEntity> spec) {
        return value != null ? spec : null;
    }
    private static Specification<BeerJPAEntity> notNullNorEmpty(String value, Specification<BeerJPAEntity> spec) {
        return value != null && !value.isBlank() ? spec : null;
    }

}
