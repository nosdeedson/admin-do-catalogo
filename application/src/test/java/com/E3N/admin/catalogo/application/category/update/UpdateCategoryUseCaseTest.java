package com.E3N.admin.catalogo.application.category.update;

import com.E3N.admin.catalogo.application.UseCaseTest;
import com.E3N.admin.catalogo.domain.category.Category;
import com.E3N.admin.catalogo.domain.category.CategoryGateway;
import com.E3N.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UpdateCategoryUseCaseTest extends UseCaseTest {

    @Mock
    private CategoryGateway categoryGateway;

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    // 1. Teste do caminho feliz
    // 2. Teste passando uma propriedade inválida (name)
    // 3. Teste atualizando uma categoria para inativa
    // 4. Teste simulando um erro generico vindo do gateway
    // 5. Teste atualizar categoria passando ID inválido

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId(){

        final var category = Category.newCategory("Filmes", null, true);
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.wit(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(Category.with(category)));

        Mockito.when(categoryGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(categoryGateway, Mockito.times(1)).update(Mockito.argThat(
                updateCategory ->
                        Objects.equals(expectedName, updateCategory.getName())
                        && Objects.equals(expectedDescription, updateCategory.getDescription())
                        && Objects.equals(expectedIsActive, updateCategory.isActive())
                        && Objects.equals(expectedId, updateCategory.getId())
                        && Objects.equals(category.getCreatedAt(), updateCategory.getCreatedAt())
                        && category.getUpdatedAt().isBefore(updateCategory.getUpdatedAt())
                        && Objects.isNull(updateCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException(){
        final var category = Category.newCategory("Filmes", null, true);
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.wit(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(Category.with(category)));

        final var notification = useCase.execute(command).getLeft();
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
        Mockito.verify(categoryGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId(){
        final var category = Category.newCategory("Filmes", null, true);
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedNotActive = false;
        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.wit(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedNotActive
        );

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(Category.with(category)));

        Mockito.when(categoryGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());

        final var output = useCase.execute(command).get();
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());
        Mockito.verify(categoryGateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(categoryGateway, Mockito.times(1)).update(Mockito.argThat(
                updateCategory ->
                        Objects.equals(expectedName, updateCategory.getName())
                                && Objects.equals(expectedDescription, updateCategory.getDescription())
                                && Objects.equals(expectedNotActive, updateCategory.isActive())
                                && Objects.equals(expectedId, updateCategory.getId())
                                && Objects.equals(category.getCreatedAt(), updateCategory.getCreatedAt())
                                && category.getUpdatedAt().isBefore(updateCategory.getUpdatedAt())
                                && Objects.nonNull(updateCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var category = Category.newCategory("Filmes", null, true);
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = category.getId();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var command = UpdateCategoryCommand.wit(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(Category.with(category)));

        Mockito.when(categoryGateway.update(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(command).getLeft();
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .update(Mockito.argThat( updateCategory ->
                        Objects.equals(expectedName, category.getName())
                                && Objects.equals(expectedDescription, updateCategory.getDescription())
                                && Objects.equals(expectedIsActive, updateCategory.isActive())
                                && Objects.nonNull(updateCategory.getId().getValue())
                                && Objects.nonNull(updateCategory.getCreatedAt())
                                && Objects.nonNull(updateCategory.getUpdatedAt())
                                && Objects.isNull(updateCategory.getDeletedAt())
                ));
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = "123";
        final var expectedErrorMessage = "Category with ID 123 was not found";

        final var command = UpdateCategoryCommand.wit(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );
        Mockito.when(categoryGateway.findById(Mockito.eq(CategoryID.from(expectedId))))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var exception = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(command));
        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
        Mockito.verify(categoryGateway, Mockito.times(1)).findById(CategoryID.from(expectedId));
        Mockito.verify(categoryGateway, Mockito.times(0)).update(Mockito.any());

    }

}
