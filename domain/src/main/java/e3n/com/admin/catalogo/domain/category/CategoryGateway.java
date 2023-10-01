package e3n.com.admin.catalogo.domain.category;

import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CategoryGateway {

    Category create(Category  category);
    void deleteById(CategoryID id);
    Optional<Category> findById(CategoryID id);
    Category update(Category category);
    Pagination<Category> findAll(SearchQuery query);
    List<CategoryID> existByIds(List<CategoryID> ids);
}
