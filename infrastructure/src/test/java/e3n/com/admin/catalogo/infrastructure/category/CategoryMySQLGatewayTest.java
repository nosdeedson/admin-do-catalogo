package e3n.com.admin.catalogo.infrastructure.category;

import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;
import e3n.com.admin.catalogo.infrastructure.MySQLGatewayTest;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;


@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySqlGateway categoryMySqlGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "The Category most watched";
        final var expectedActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedActive);

        Assertions.assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryMySqlGateway.create(category);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(actualCategory.getId().getValue(), category.getId().getValue());
        Assertions.assertEquals(actualCategory.getCreatedAt(), category.getCreatedAt());
        Assertions.assertEquals(actualCategory.getName(), category.getName());
        Assertions.assertEquals(actualCategory.getDescription(), category.getDescription());
        Assertions.assertEquals(actualCategory.getUpdatedAt(), category.getUpdatedAt());
        Assertions.assertEquals(actualCategory.isActive(), category.isActive());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var entity = this.categoryRepository.findById(actualCategory.getId().getValue()).get();
        Assertions.assertEquals(category.getId().getValue(), entity.getId());
        Assertions.assertEquals(category.getCreatedAt(), entity.getCreatedAt());
        Assertions.assertEquals(expectedName, entity.getName());
        Assertions.assertEquals(expectedDescription, entity.getDescription());
        Assertions.assertEquals(category.getUpdatedAt(), entity.getUpdatedAt());
        Assertions.assertEquals(expectedActive, entity.isActive());
        Assertions.assertNull(entity.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated(){
        final var expectedName = "Filmes";
        final var expectedDescription = "The Category most watched";
        final var expectedActive = true;

        final var category = Category.newCategory("Film", "empty", false);

        Assertions.assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));

        Assertions.assertEquals(1, categoryRepository.count());

        final var invalidCategoryJpa = categoryRepository.findById(category.getId().getValue()).orElseThrow();

        Assertions.assertEquals("Film", invalidCategoryJpa.getName());
        Assertions.assertEquals( "empty", invalidCategoryJpa.getDescription());
        Assertions.assertEquals(category.getUpdatedAt(), invalidCategoryJpa.getUpdatedAt());
        Assertions.assertFalse(actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getDeletedAt());

        final var updatedCategory = category.clone().update(expectedName, expectedDescription, true);

        final var updatedEntity = categoryMySqlGateway.update(updatedCategory);

        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertEquals(updatedEntity.getId().getValue(), category.getId().getValue());
        Assertions.assertEquals(updatedEntity.getName(), updatedCategory.getName());
        Assertions.assertEquals(updatedEntity.getDescription(), updatedCategory.getDescription());
        Assertions.assertEquals(updatedEntity.isActive(), updatedCategory.isActive());
        Assertions.assertNull(updatedEntity.getDeletedAt());
        Assertions.assertTrue(updatedEntity.getUpdatedAt().isAfter(category.getUpdatedAt()));

        final var updatedJpaEntity = categoryRepository.findById(category.getId().getValue()).orElseThrow();

        Assertions.assertEquals(updatedJpaEntity.getId(), category.getId().getValue());
        Assertions.assertEquals(updatedJpaEntity.getName(), expectedName);
        Assertions.assertEquals(updatedJpaEntity.getDescription(), expectedDescription);
        Assertions.assertEquals(updatedJpaEntity.isActive(), expectedActive);
        Assertions.assertNull(updatedJpaEntity.getDeletedAt());
        Assertions.assertTrue(updatedJpaEntity.getUpdatedAt().isAfter(category.getUpdatedAt()));
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "The Category most watched";
        final var expectedActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedActive);

        categoryMySqlGateway.create(category);
        Assertions.assertEquals(0, categoryRepository.count());
        final var categoryJpa = categoryRepository.findById(category.getId().getValue()).orElseThrow();

        Assertions.assertEquals(1, categoryRepository.count());

        categoryMySqlGateway.deleteById(category.getId());
        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenInvalidCategoryId_whenTryToDeleteIt_shoulNotThrowException(){
        Assertions.assertEquals(0, categoryRepository.count());
        categoryMySqlGateway.deleteById(CategoryID.from("unexistent"));
        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenCallsFindById_shouldReturnCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "The Category most watched";
        final var expectedActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedActive);
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));

        final var categoryPreExisted = categoryMySqlGateway.findById(category.getId()).orElse(null);

        Assertions.assertNotNull(categoryPreExisted);
        Assertions.assertNotNull(categoryPreExisted.getId());
        Assertions.assertEquals(categoryPreExisted.getName(), expectedName);
        Assertions.assertEquals(categoryPreExisted.getDescription(), expectedDescription);
        Assertions.assertEquals(categoryPreExisted.isActive(), expectedActive);
        Assertions.assertNull(categoryPreExisted.getDeletedAt());
        Assertions.assertNotNull(categoryPreExisted.getUpdatedAt());
        Assertions.assertNotNull(categoryPreExisted.getCreatedAt());
    }

    @Test
    public void givenValidCategoryIdNotStored_whenCallsFindById_shouldReturnEmpty(){
        final var expectedName = "Filmes";
        final var expectedDescription = "The Category most watched";
        final var expectedActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedActive);
        categoryMySqlGateway.create(category);
        Assertions.assertEquals(1, categoryRepository.count());

        final var categoryNotSaved = Category.newCategory(expectedName, expectedDescription, expectedActive);
        Optional<Category> optionalCategory = categoryMySqlGateway.findById(categoryNotSaved.getId());
        Assertions.assertNotNull(optionalCategory);
        Assertions.assertTrue(optionalCategory.isEmpty());
    }

    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated(){
        final var filme = Category.newCategory("Filmes", "The category most watched", true);
        final var serie = Category.newCategory("Series", "The most fun series", true);
        final var documentary = Category.newCategory("Documentaries", "A vision of the reality", true);

        categoryRepository.saveAllAndFlush(List.of(
                CategoryJpaEntity.from(filme),
                CategoryJpaEntity.from(serie),
                CategoryJpaEntity.from(documentary)
        ));

        final var query = new SearchQuery(1, 1, "", "name", "desc");
        final var categories = categoryMySqlGateway.findAll(query);

        Assertions.assertEquals(filme.getId().getValue(), categories.items().get(0).getId().getValue());
        Assertions.assertEquals(3, categories.total());
        Assertions.assertEquals(1, categories.items().size());
        Assertions.assertEquals("Filmes", categories.items().get(0).getName());
        Assertions.assertEquals("The category most watched", categories.items().get(0).getDescription());
        Assertions.assertTrue(categories.items().get(0).isActive());

    }

}