package com.E3N.admin.catalogo.application.category.retrieve.list;

import com.E3N.admin.catalogo.domain.category.CategoryGateway;
import com.E3N.admin.catalogo.domain.pagination.Pagination;
import com.E3N.admin.catalogo.domain.pagination.SearchQuery;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Pagination<CategoryListOutput> execute(SearchQuery searchQuery) {
        return this.categoryGateway.findAll(searchQuery)
                .map(CategoryListOutput::from);
    }
}
