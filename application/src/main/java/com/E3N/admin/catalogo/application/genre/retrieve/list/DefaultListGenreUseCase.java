package com.E3N.admin.catalogo.application.genre.retrieve.list;

import com.E3N.admin.catalogo.domain.genre.GenreGateway;
import com.E3N.admin.catalogo.domain.pagination.Pagination;
import com.E3N.admin.catalogo.domain.pagination.SearchQuery;

public class DefaultListGenreUseCase extends ListGenreUseCase {

    private final GenreGateway genreGateway;

    public DefaultListGenreUseCase(GenreGateway genreGateway) {
        this.genreGateway = genreGateway;
    }

    @Override
    public Pagination<GenreListOutput> execute(SearchQuery searchQuery) {
        return this.genreGateway.findAll(searchQuery)
                .map(GenreListOutput::from);
    }
}
