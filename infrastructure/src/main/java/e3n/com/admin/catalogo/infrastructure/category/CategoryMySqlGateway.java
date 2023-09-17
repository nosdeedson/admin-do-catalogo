package e3n.com.admin.catalogo.infrastructure.category;

import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;

import java.util.List;
import java.util.Optional;

public class CategoryMySqlGateway implements CategoryGateway {

    private CategoryRepository categoryRepository;

    public CategoryMySqlGateway(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category category) {
        CategoryJpaEntity entity = CategoryJpaEntity.from(category);
        categoryRepository.save(entity);
        return category;
    }

    @Override
    public void deleteById(CategoryID id) {

    }

    @Override
    public Optional<Category> findById(CategoryID id) {
        return Optional.empty();
    }

    @Override
    public Category update(Category category) {
        return null;
    }

    @Override
    public Pagination<Category> findAll(SearchQuery query) {
        return null;
    }

    @Override
    public List<CategoryID> existByIds(Iterable<CategoryID> ids) {
        return null;
    }
}
