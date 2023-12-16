package e3n.com.admin.catalogo.application.category.delete;

import com.E3N.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import e3n.com.admin.catalogo.IntegrationTest;
import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;


    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOK(){
        final var categoria = Category.newCategory("Filmes", "", true);
        final var expectedId = categoria.getId();

        repository.save(CategoryJpaEntity.from(categoria));
        Assertions.assertEquals(1, repository.count());
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(0, repository.count());
    }

    @Test
    public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOK(){
        final var categoryId = CategoryID.from("123");
        Assertions.assertEquals(0, repository.count());
        Assertions.assertDoesNotThrow(() -> useCase.execute(categoryId.getValue()));
        Assertions.assertEquals(0, repository.count());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException(){
        final var category = Category.newCategory("Filmes", "A mais assistida", true);
        final var expetedId = category.getId();
        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(gateway).deleteById(expetedId);
        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expetedId.getValue()));
        Mockito.verify(gateway, Mockito.times(1)).deleteById(expetedId);
    }
}
