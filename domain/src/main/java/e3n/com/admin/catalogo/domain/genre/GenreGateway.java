package e3n.com.admin.catalogo.domain.genre;

import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface GenreGateway {

    Genre create(Genre genre);

    void deleteById( GenreId id);

    Optional<Genre> findById(GenreId id);

    Genre update(Genre genre);

    Pagination<Genre> findAll(SearchQuery query);

    List<GenreId> existsByIds(Iterable<GenreId> ids);
}
