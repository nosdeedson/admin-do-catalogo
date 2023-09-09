package e3n.com.admin.catalogo.application.category.retrieve.list;

import e3n.com.admin.catalogo.application.UseCase;
import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;

public abstract class ListCategoriesUseCase extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}
