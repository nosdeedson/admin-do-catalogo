package e3n.com.admin.catalogo.infrastructure.category;

import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import e3n.com.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class CategoryMySqlGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;

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
        if (categoryRepository.existsById(id.getValue())){
            categoryRepository.deleteById(id.getValue());
        }
    }

    @Override
    public Optional<Category> findById(CategoryID id) {
        return categoryRepository.findById(id.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(Category category) {
        return categoryRepository.save(CategoryJpaEntity.from(category))
                .toAggregate();
    }

    @Override
    public Pagination<Category> findAll(SearchQuery query) {
       PageRequest pageRequest = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );
        final var specifications = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecificcation)
                .orElse(null);
        final var pageResult = this.categoryRepository.findAll(Specification.where(specifications), pageRequest);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<CategoryID> existByIds(List<CategoryID> ids) {
        List<String> idsBD = this.categoryRepository.existeByIds(ids.stream().map(CategoryID::getValue).toList());
        return idsBD
                .stream()
                .map(CategoryID::from)
                .toList();
    }

    private Specification<CategoryJpaEntity> assembleSpecificcation(String str) {
        final Specification<CategoryJpaEntity> nameLike = SpecificationUtils.like("name", str);
        final Specification<CategoryJpaEntity> descriptionLike = SpecificationUtils.like("description", str);
        return nameLike.or(descriptionLike);
    }

}
