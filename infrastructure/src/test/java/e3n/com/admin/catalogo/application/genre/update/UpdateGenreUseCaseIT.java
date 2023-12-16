package e3n.com.admin.catalogo.application.genre.update;


import com.E3N.admin.catalogo.application.genre.update.UpdateGenreCommand;
import com.E3N.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import e3n.com.admin.catalogo.IntegrationTest;
import com.E3N.admin.catalogo.domain.category.Category;
import com.E3N.admin.catalogo.domain.category.CategoryGateway;
import com.E3N.admin.catalogo.domain.category.CategoryID;
import com.E3N.admin.catalogo.domain.exceptions.NotificationException;
import com.E3N.admin.catalogo.domain.genre.Genre;
import com.E3N.admin.catalogo.domain.genre.GenreGateway;
import com.E3N.admin.catalogo.domain.utils.StringUtils;
import e3n.com.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

@IntegrationTest
public class UpdateGenreUseCaseIT {

    @Autowired
    private UpdateGenreUseCase useCase;

    @SpyBean
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var genre = genreGateway.create(Genre.newGenre("acao", false));

        Assertions.assertEquals(1, genreRepository.count());

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedId = genre.getId();

        final var command = UpdateGenreCommand.from(expectedId.getValue(), expectedName, expectedIsActive,
                StringUtils.asString(expectedCategories).toList());

        final var output = useCase.execute(command);
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        final var fromDB = genreGateway.findById(expectedId).get();
        Assertions.assertEquals(expectedName, fromDB.getName());
        Assertions.assertEquals(expectedId.getValue(), fromDB.getId().getValue());
        Assertions.assertEquals(expectedIsActive, fromDB.isActive());
        Assertions.assertNotNull(fromDB.getCreatedAt());
        Assertions.assertTrue(fromDB.getUpdatedAt().isAfter(fromDB.getCreatedAt()));
        Assertions.assertNull(fromDB.getDeletedAt());
        Assertions.assertTrue(
                expectedCategories.size() == fromDB.getCategories().size()
                        && expectedCategories.containsAll(fromDB.getCategories())
        );

    }

    @Test
    public void givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var genre = genreGateway.create(Genre.newGenre("acao", false));
        Assertions.assertEquals(1, genreRepository.count());

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var filmes = categoryGateway.create(Category.newCategory("filmes", null, true));
        final var series = categoryGateway.create(Category.newCategory("series", null, true));

        final var expectedId = genre.getId();
        final var expectedCategories = List.of(filmes.getId(), series.getId());

        final var command = UpdateGenreCommand.from(expectedId.getValue(), expectedName, expectedIsActive,
                StringUtils.asString(expectedCategories).toList());

        final var output = useCase.execute(command);
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        final var fromDB = genreGateway.findById(expectedId).get();
        Assertions.assertEquals(expectedName, fromDB.getName());
        Assertions.assertEquals(expectedId.getValue(), fromDB.getId().getValue());
        Assertions.assertEquals(expectedIsActive, fromDB.isActive());
        Assertions.assertNotNull(fromDB.getCreatedAt());
        Assertions.assertTrue(fromDB.getUpdatedAt().isAfter(fromDB.getCreatedAt()));
        Assertions.assertNull(fromDB.getDeletedAt());
        Assertions.assertTrue(
                expectedCategories.size() == fromDB.getCategories().size()
                        && expectedCategories.containsAll(fromDB.getCategories())
        );
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var genre = genreGateway.create(Genre.newGenre("acao", false));
        Assertions.assertEquals(1, genreRepository.count());

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var filmes = categoryGateway.create(Category.newCategory("filmes", null, true));
        final var series = categoryGateway.create(Category.newCategory("series", null, true));

        final var expectedId = genre.getId();
        final var expectedCategories = List.of(filmes.getId(), series.getId());

        final var command = UpdateGenreCommand.from(expectedId.getValue(), expectedName, expectedIsActive,
                StringUtils.asString(expectedCategories).toList());

        final var output = useCase.execute(command);
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        final var fromDB = genreGateway.findById(expectedId).get();
        Assertions.assertEquals(expectedName, fromDB.getName());
        Assertions.assertEquals(expectedId.getValue(), fromDB.getId().getValue());
        Assertions.assertEquals(expectedIsActive, fromDB.isActive());
        Assertions.assertNotNull(fromDB.getCreatedAt());
        Assertions.assertTrue(fromDB.getUpdatedAt().isAfter(fromDB.getCreatedAt()));
        Assertions.assertNull(fromDB.getDeletedAt());
        Assertions.assertTrue(
                expectedCategories.size() == fromDB.getCategories().size()
                        && expectedCategories.containsAll(fromDB.getCategories())
        );
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
        final var genre = genreGateway.create(Genre.newGenre("acao", false));
        Assertions.assertEquals(1, genreRepository.count());

        final var expectedErroMessage = "'name' should not be empty";
        final var expectedErroCount = 1;

        final var command = UpdateGenreCommand.from(genre.getId().getValue(), " ", true,
                StringUtils.asString(List.<CategoryID>of()).toList());

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        Assertions.assertEquals(expectedErroCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErroMessage, exception.getErrors().get(0).message());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(genre.getId()));

        Mockito.verify(categoryGateway, Mockito.times(0)).existByIds(Mockito.any());

        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {
        final var filmes = categoryGateway.create(Category.newCategory("filmes", null, true));
        final var genre = genreGateway.create(Genre.newGenre("acao", true));
        Assertions.assertEquals(1, genreRepository.count());

        final var series = CategoryID.from("123");
        final var documentarios = CategoryID.from("345");

        final var expectedCategories = List.of(filmes.getId(), series, documentarios);
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErroMessage1 = "Some categories could not be found: 123, 345";
        final var expectedErroMessage2 = "'name' should not be null";
        final var expectedErroCount = 2;
        final var expectedId = genre.getId();

        final var command = UpdateGenreCommand.from(expectedId.getValue(), expectedName, expectedIsActive,
                StringUtils.asString(expectedCategories).toList());

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        Assertions.assertEquals(expectedErroCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErroMessage1, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErroMessage2, exception.getErrors().get(1).message());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        Mockito.verify(categoryGateway, Mockito.times(1)).existByIds(Mockito.eq(expectedCategories));

        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());
    }


}
