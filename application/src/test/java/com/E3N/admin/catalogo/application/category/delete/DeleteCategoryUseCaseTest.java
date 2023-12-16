package com.E3N.admin.catalogo.application.category.delete;

import com.E3N.admin.catalogo.application.UseCaseTest;
import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class DeleteCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOK(){
        final var category = Category.newCategory("Filmes", null, true);
        final var expectedId = category.getId();

        Mockito.doNothing()
                .when(categoryGateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(Mockito.any());
    }

    @Test
    public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOK(){
        final var category = Category.newCategory("Filmer", null, true);
        final var expectedId = "123";
        Mockito.doNothing()
                .when(categoryGateway).deleteById(Mockito.eq(CategoryID.from(expectedId)));
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId));
        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(Mockito.any());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException(){
        final var category = Category.newCategory("Filmes", null, true);
        final var expectedId = category.getId();
        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(categoryGateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }

}
