package com.E3N.admin.catalogo.application.category.retrieve.list;

import com.E3N.admin.catalogo.application.UseCase;
import com.E3N.admin.catalogo.domain.pagination.Pagination;
import com.E3N.admin.catalogo.domain.pagination.SearchQuery;

public abstract class ListCategoriesUseCase extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}
