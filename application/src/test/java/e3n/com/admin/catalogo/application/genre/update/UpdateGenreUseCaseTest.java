package e3n.com.admin.catalogo.application.genre.update;

import e3n.com.admin.catalogo.application.UseCaseTest;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.exceptions.NotificationException;
import e3n.com.admin.catalogo.domain.genre.Genre;
import e3n.com.admin.catalogo.domain.genre.GenreGateway;
import e3n.com.admin.catalogo.domain.utils.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class UpdateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway, categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var genre = Genre.newGenre("actio", true);

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedId = genre.getId();
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.from(expectedId.getValue(), expectedName,
                expectedIsActive, StringUtils.asString(expectedCategories).toList());

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Genre.with(genre)));

        Mockito.when(genreGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(command);

        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedId.getValue(), output.id());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(genreGateway, Mockito.times(1)).update(Mockito.argThat(update ->
                Objects.equals(expectedId.getValue(), update.getId().getValue())
                        && Objects.equals(expectedIsActive, update.isActive())
                        && Objects.equals(expectedName, update.getName())
                        && Objects.equals(expectedCategories, update.getCategories())
                        && Objects.equals(genre.getCreatedAt(), update.getCreatedAt())
                        && genre.getUpdatedAt().isBefore(update.getUpdatedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {

        final var genre = Genre.newGenre("actio", true);

        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedId = genre.getId();

        final var command = UpdateGenreCommand.from(expectedId.getValue(), expectedName,
                expectedIsActive, StringUtils.asString(expectedCategories).toList());

        Mockito.when(categoryGateway.existByIds(Mockito.any()))
                .thenReturn(expectedCategories);

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Genre.with(genre)));

        Mockito.when(genreGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());


        final var output = useCase.execute(command);

        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedId.getValue(), output.id());

        Mockito.verify(categoryGateway, Mockito.times(1)).existByIds(Mockito.eq(expectedCategories));

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(genreGateway, Mockito.times(1)).update(Mockito.argThat(update ->
                Objects.equals(expectedId.getValue(), update.getId().getValue())
                        && Objects.equals(expectedIsActive, update.isActive())
                        && Objects.equals(expectedName, update.getName())
                        && Objects.equals(genre.getCreatedAt(), update.getCreatedAt())
                        && genre.getUpdatedAt().isBefore(update.getUpdatedAt())
                        && Objects.equals(expectedCategories, update.getCategories())
                        && Objects.equals(expectedCategories.size(), update.getCategories().size())
        ));
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var genre = Genre.newGenre("actio", true);

        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedId = genre.getId();
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.from(expectedId.getValue(), expectedName,
                expectedIsActive, StringUtils.asString(expectedCategories).toList());

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Genre.with(genre)));

        Mockito.when(genreGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        Assertions.assertTrue(genre.isActive());
        Assertions.assertNull(genre.getDeletedAt());

        final var output = useCase.execute(command);

        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedId.getValue(), output.id());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(genreGateway, Mockito.times(1)).update(Mockito.argThat(update ->
                Objects.equals(expectedId.getValue(), update.getId().getValue())
                        && Objects.equals(expectedIsActive, update.isActive())
                        && Objects.equals(expectedName, update.getName())
                        && Objects.equals(expectedCategories, update.getCategories())
                        && Objects.equals(genre.getCreatedAt(), update.getCreatedAt())
                        && genre.getUpdatedAt().isBefore(update.getUpdatedAt())
                        && Objects.nonNull(update.getDeletedAt())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
        final var genre = Genre.newGenre("action", true);
        final var expectedId = genre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateGenreCommand.from(
                expectedId.getValue(),
                null,
                expectedIsActive,
                StringUtils.asString(expectedCategories).toList()
        );

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Genre.with(genre)));

        final var exception = Assertions.assertThrows(NotificationException.class,
                () -> useCase.execute(command)
        );
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {
        final var genre = Genre.newGenre("action", true);
        final var expectedId = genre.getId();
        final var expectedIsActive = true;

        final var filmes = CategoryID.from("123");
        final var documentario = CategoryID.from("456");
        final var series = CategoryID.from("789");
        final var kids = CategoryID.from("124");
        final var expectedCategories = List.<CategoryID>of(
                filmes, documentario, series, kids
        );
        final var expectedErrorMessage = "Some categories could not be found: 456, 789, 124";
        final var expectedErrorMessage2 = "'name' should not be null";
        final var expectedErrorCount = 2;

        final var command = UpdateGenreCommand.from(
                expectedId.getValue(),
                null,
                expectedIsActive,
                StringUtils.asString(expectedCategories).toList()
        );

        Mockito.when(categoryGateway.existByIds(Mockito.any()))
                .thenReturn(List.of(filmes));

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Genre.with(genre)));

        final var exception = Assertions.assertThrows(NotificationException.class,
                () -> useCase.execute(command)
        );

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessage2, exception.getErrors().get(1).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).existByIds(Mockito.eq(expectedCategories));
        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());

    }

}
