package e3n.com.admin.catalogo.application.category.retrieve.get;

import e3n.com.admin.catalogo.application.UseCaseTest;
import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.exceptions.DomainException;
import e3n.com.admin.catalogo.domain.validation.Error;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GetCategoryByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = category.getId();

        Mockito.when(categoryGateway.findById(expectedId))
                .thenReturn(Optional.of(category.clone()));

        final var categoryBD = useCase.execute(expectedId.getValue());
        Assertions.assertEquals(expectedId, categoryBD.id());
        Assertions.assertEquals(expectedIsActive, categoryBD.active());
        Assertions.assertEquals(expectedDescription, categoryBD.description());
        Assertions.assertNull(categoryBD.deletedAt());

    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldRetrunNotFound(){
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.from("123");

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";

        final var category =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = category.getId();

        Mockito.when(categoryGateway.findById(expectedId))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var exception = Assertions.assertThrows(IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue()));
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(exception.getMessage(), expectedErrorMessage);
    }
}
