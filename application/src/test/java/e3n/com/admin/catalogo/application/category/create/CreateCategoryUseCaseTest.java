package e3n.com.admin.catalogo.application.category.create;

import com.sun.source.tree.AssertTree;
import e3n.com.admin.catalogo.application.UseCaseTest;
import e3n.com.admin.catalogo.application.category.update.UpdateCategoryCommand;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Objects;
import java.util.SimpleTimeZone;

public class CreateCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    public void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryid(){
        final var expectedName = "Filmes";
        final var expectedDescripition = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescripition, expectedIsActive);
        Mockito.when(categoryGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, Mockito.times(1)).create(Mockito.argThat(category ->
                    Objects.equals(expectedName, category.getName())
                    && Objects.equals(expectedDescripition, category.getDescription())
                    && Objects.nonNull(category.getId())
                    && Objects.nonNull(category.getCreatedAt())
                    && Objects.nonNull(category.getUpdatedAt())
                    && Objects.isNull(category.getDeletedAt())
                ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var command =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command).get(); // this get is from the Either return of vavr API

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(categoryGateway, Mockito.times(1)).create(Mockito.argThat(category ->
                Objects.equals(expectedName, category.getName())
                        && Objects.equals(expectedDescription, category.getDescription())
                        && Objects.nonNull(category.getId())
                        && Objects.nonNull(category.getCreatedAt())
                        && Objects.nonNull(category.getUpdatedAt())
                        && Objects.nonNull(category.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.create(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat( category ->
                        Objects.equals(expectedName, category.getName())
                        && Objects.equals(expectedDescription, category.getDescription())
                        && Objects.equals(expectedIsActive, category.isActive())
                                && Objects.nonNull(category.getId().getValue())
                                && Objects.nonNull(category.getCreatedAt())
                                && Objects.nonNull(category.getUpdatedAt())
                                && Objects.isNull(category.getDeletedAt())
                        ));
    }
}
