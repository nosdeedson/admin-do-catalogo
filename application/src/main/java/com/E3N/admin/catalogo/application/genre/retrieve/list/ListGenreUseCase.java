package com.E3N.admin.catalogo.application.genre.retrieve.list;

import com.E3N.admin.catalogo.application.UseCase;
import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;

public abstract class ListGenreUseCase extends UseCase<SearchQuery, Pagination<GenreListOutput>> {
}
