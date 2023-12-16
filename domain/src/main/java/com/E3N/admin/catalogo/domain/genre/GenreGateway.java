package com.E3N.admin.catalogo.domain.genre;

import com.E3N.admin.catalogo.domain.pagination.Pagination;
import com.E3N.admin.catalogo.domain.pagination.SearchQuery;

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
