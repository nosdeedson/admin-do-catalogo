package e3n.com.admin.catalogo.domain.pagination;

public record SearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {
}
