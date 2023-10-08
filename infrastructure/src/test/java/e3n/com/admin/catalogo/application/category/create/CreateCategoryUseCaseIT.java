package e3n.com.admin.catalogo.application.category.create;


import e3n.com.admin.catalogo.IntegrationTest;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;


@IntegrationTest
public class CreateCategoryUseCaseIT {

    @Autowired
    private CreateCategoryUseCase createCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        Assertions.assertEquals(0, categoryRepository.count());

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var outPut = createCategoryUseCase.execute(command).get();

        Assertions.assertNotNull(outPut);
        Assertions.assertNotNull(outPut.id());

        Assertions.assertEquals(1, categoryRepository.count());

        final var actualCategory =
                categoryRepository.findById(outPut.id()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException(){
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErros = 1;
        final var expectedMessage = "'name' should not be null";

        Assertions.assertEquals(0, categoryRepository.count());

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var notification = createCategoryUseCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErros, notification.getErrors().size());
        Assertions.assertEquals(expectedMessage, notification.firstError().message());

        Assertions.assertEquals(0, categoryRepository.count());
        Mockito.verify(categoryGateway, Mockito.times(0)).create(any());
    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId(){
        final String expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        Assertions.assertEquals(0, categoryRepository.count());

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var output = createCategoryUseCase.execute(command).get();
        Assertions.assertNotNull(output);
        Assertions.assertEquals(1, categoryRepository.count());

        final var category = this.categoryRepository.findById(output.id()).get();

        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNotNull(category.getDeletedAt());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException(){
        final String expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        Assertions.assertEquals(0, categoryRepository.count());

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).create(Mockito.any());

        final var notification = createCategoryUseCase.execute(command).getLeft();
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
    }
}
