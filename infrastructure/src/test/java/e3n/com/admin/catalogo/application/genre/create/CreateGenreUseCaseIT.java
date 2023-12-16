package e3n.com.admin.catalogo.application.genre.create;

import com.E3N.admin.catalogo.application.genre.create.CreateGenreCommand;
import com.E3N.admin.catalogo.application.genre.create.CreateGenreUseCase;
import e3n.com.admin.catalogo.IntegrationTest;
import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.exceptions.NotificationException;
import e3n.com.admin.catalogo.domain.genre.GenreGateway;
import e3n.com.admin.catalogo.domain.utils.StringUtils;
import e3n.com.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

@IntegrationTest
public class CreateGenreUseCaseIT {

    @Autowired
    private CreateGenreUseCase useCase;

    @Autowired
    private GenreRepository genreRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @SpyBean
    private GenreGateway genreGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        final var filmes =
                categoryGateway.create(Category.newCategory("Filmes", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId());

        final var command = CreateGenreCommand.with(expectedName, expectedIsActive, StringUtils.asString(expectedCategories).toList());

        final var output = useCase.execute(command);
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        final var fromDB = genreRepository.findById(output.id()).get();

        Assertions.assertEquals(expectedName, fromDB.getName());
        Assertions.assertEquals(expectedIsActive, fromDB.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == fromDB.getCategories().size()
                && expectedCategories.containsAll(fromDB.getCategoryIds())
        );
        Assertions.assertNotNull( fromDB.getCreatedAt());
        Assertions.assertNotNull( fromDB.getUpdateAt());
        Assertions.assertNull( fromDB.getDeletedAt());
    }

    @Test
    public void givenAValidCommandWithoutCategories_whenCallsCreateGenre_shouldReturnGenreId() {

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var command = CreateGenreCommand.with(expectedName, expectedIsActive, StringUtils.asString(expectedCategories).toList());

        final var output = useCase.execute(command);
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        final var fromDB = genreRepository.findById(output.id()).get();

        Assertions.assertEquals(expectedName, fromDB.getName());
        Assertions.assertEquals(expectedIsActive, fromDB.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == fromDB.getCategories().size()
                        && expectedCategories.containsAll(fromDB.getCategoryIds())
        );
        Assertions.assertNotNull( fromDB.getCreatedAt());
        Assertions.assertNotNull( fromDB.getUpdateAt());
        Assertions.assertNull( fromDB.getDeletedAt());
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId(){
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var command = CreateGenreCommand.with(expectedName, expectedIsActive, StringUtils.asString(expectedCategories).toList());

        final var output = useCase.execute(command);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        final var fromDB = genreRepository.findById(output.id()).get();

        Assertions.assertEquals(expectedName, fromDB.getName());
        Assertions.assertEquals(expectedIsActive, fromDB.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == fromDB.getCategories().size()
                        && expectedCategories.containsAll(fromDB.getCategoryIds())
        );
        Assertions.assertNotNull( fromDB.getCreatedAt());
        Assertions.assertNotNull( fromDB.getUpdateAt());
        Assertions.assertNotNull( fromDB.getDeletedAt());
    }

    @Test
    public void givenAInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException() {
        final var expectedName = " ";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;
        final var command = CreateGenreCommand.with(expectedName, expectedIsActive, StringUtils.asString(expectedCategories).toList());

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());

        Mockito.verify(categoryGateway, Mockito.times(0)).existByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenAInvalidNullName_whenCallsCreateGenre_shouldReturnDomainException() {
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var command = CreateGenreCommand.with(null, true, StringUtils.asString(expectedCategories).toList());

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());

        Mockito.verify(categoryGateway, Mockito.times(0)).existByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenAInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var series = categoryGateway.create(Category.newCategory("Series", null, true));

        final var documentario = CategoryID.from("124");

        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(filmes.getId(), series.getId(), documentario);
        final var expectedErrorMessageOne = "Some categories could not be found: 124";
        final var expectedErrorMessageTwo = "'name' should not be empty";
        final var expectedErrorCount = 2;

        final var command = CreateGenreCommand.with(expectedName, expectedIsActive,
                StringUtils.asString(expectedCategories).toList());

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageOne, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, exception.getErrors().get(1).message());
        Mockito.verify(categoryGateway, Mockito.times(1)).existByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
    }

}
