package e3n.com.admin.catalogo.application.category.update;


import e3n.com.admin.catalogo.IntegrationTest;
import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.exceptions.NotFoundException;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDate;
import java.time.ZoneId;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId(){
        final var category = Category.newCategory("Film", null, true);

        repository.save(CategoryJpaEntity.from(category));
        Assertions.assertEquals(1, repository.count());
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = category.getId();

        final var command = new UpdateCategoryCommand(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);
        final var output = useCase.execute(command).get();

        Assertions.assertEquals(expectedId.getValue(), output.id());

        final var categoriaAtualizada = repository.findById(expectedId.getValue()).get();
        Assertions.assertEquals(expectedName, categoriaAtualizada.getName());
        Assertions.assertEquals(expectedDescription, categoriaAtualizada.getDescription());
        Assertions.assertEquals(expectedIsActive, categoriaAtualizada.isActive());
        Assertions.assertEquals(LocalDate.ofInstant(category.getCreatedAt(), ZoneId.of("UTC")), LocalDate.ofInstant(categoriaAtualizada.getCreatedAt(), ZoneId.of("UTC")));
        Assertions.assertEquals(category.getDeletedAt(), categoriaAtualizada.getDeletedAt());
        Assertions.assertTrue(category.getUpdatedAt().isBefore(categoriaAtualizada.getUpdatedAt()));

    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException(){
        final var category = Category.newCategory("Film", null, true);
        repository.save(CategoryJpaEntity.from(category));
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = category.getId();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command =
                UpdateCategoryCommand.wit(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);
        final var notification = useCase.execute(command).getLeft();
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId(){
        final var category = Category.newCategory("Film", null, true);
        repository.save(CategoryJpaEntity.from(category));

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = category.getId();
        final var command = UpdateCategoryCommand.wit(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);
        final var output = useCase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        final var categoriaAtualizada = repository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedDescription, categoriaAtualizada.getDescription());
        Assertions.assertEquals(expectedName, categoriaAtualizada.getName());
        Assertions.assertFalse(categoriaAtualizada.isActive());
        Assertions.assertEquals(LocalDate.ofInstant(category.getCreatedAt(), ZoneId.of("UTC")), LocalDate.ofInstant(categoriaAtualizada.getCreatedAt(), ZoneId.of("UTC")) );
        Assertions.assertNotNull(categoriaAtualizada.getDeletedAt());
        Assertions.assertTrue(category.getUpdatedAt().isBefore(categoriaAtualizada.getUpdatedAt()));
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException(){
        final var category = Category.newCategory("Film", null, true);
        repository.save(CategoryJpaEntity.from(category));

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = category.getId();
        final var expectedErrorMesage = "gateway error";
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryCommand.wit(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Mockito.doThrow(new IllegalStateException(expectedErrorMesage))
                .when(gateway).update(Mockito.any());

        final var notification = useCase.execute(command).getLeft();
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMesage, notification.firstError().message());

        final var afterModification = repository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(LocalDate.ofInstant(afterModification.getUpdatedAt(), ZoneId.of("UTC")),LocalDate.ofInstant(category.getUpdatedAt(), ZoneId.of("UTC")));
        Assertions.assertEquals(LocalDate.ofInstant(afterModification.getCreatedAt(), ZoneId.of("UTC")),LocalDate.ofInstant(category.getCreatedAt(), ZoneId.of("UTC")));
        Assertions.assertEquals(afterModification.getDeletedAt(), category.getDeletedAt());
        Assertions.assertEquals(afterModification.getName(), category.getName());
        Assertions.assertEquals(afterModification.getDescription(), category.getDescription());
        Assertions.assertEquals(afterModification.isActive(), category.isActive());
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category with id 123 was not found";

        final var command = UpdateCategoryCommand.wit(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);
        final var exception = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(command));
        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
