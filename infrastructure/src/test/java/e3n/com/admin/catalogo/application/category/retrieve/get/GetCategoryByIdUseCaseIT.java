package e3n.com.admin.catalogo.application.category.retrieve.get;

import com.E3N.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import e3n.com.admin.catalogo.IntegrationTest;
import com.E3N.admin.catalogo.domain.category.Category;
import com.E3N.admin.catalogo.domain.category.CategoryGateway;
import com.E3N.admin.catalogo.domain.category.CategoryID;
import com.E3N.admin.catalogo.domain.exceptions.NotFoundException;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertEquals(0, repository.count());
        repository.save(CategoryJpaEntity.from(category));
        Assertions.assertEquals(1, repository.count());

        final var fromBd = useCase.execute(category.getId().getValue());
        Assertions.assertEquals(expectedName, fromBd.name());
        Assertions.assertEquals(expectedDescription, fromBd.description());
        Assertions.assertTrue(fromBd.active());
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound(){
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.from("123");
        final var fromBd = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, fromBd.getMessage());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException(){

        final var expectedErrorMessage = "gateway erro";
        final var expectedId = CategoryID.from("123");

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage) )
                .when(gateway).findById(Mockito.eq(expectedId));

        final var execao = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );
        Assertions.assertEquals(expectedErrorMessage, execao.getMessage());
    }

}
