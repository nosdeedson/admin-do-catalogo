package com.E3N.admin.catalogo.application.genre.create;

import com.E3N.admin.catalogo.application.UseCaseTest;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.exceptions.NotificationException;
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
import java.util.function.Function;

public class CreateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateGenreUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway, genreGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var commad = CreateGenreCommand.with(expectedName, expectedIsActive,
                StringUtils.asString(expectedCategories).toList());

        Mockito.when(genreGateway.create(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(commad);
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(genreGateway, Mockito.times(1)).create(Mockito.argThat(
                genre ->
                        Objects.equals(expectedName, genre.getName())
                                && Objects.equals(expectedIsActive, genre.isActive())
                                && Objects.equals(expectedCategories, genre.getCategories())
                                && Objects.nonNull(genre.getCreatedAt())
                                && Objects.nonNull(genre.getUpdatedAt())
                                && Objects.isNull(genre.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(CategoryID.from("123"), CategoryID.from("456"));

        final var commad = CreateGenreCommand.with(expectedName, expectedIsActive,
                StringUtils.asString(expectedCategories).toList());

        Mockito.when(categoryGateway.existByIds(Mockito.any()))
                .thenReturn(expectedCategories);

        Mockito.when(genreGateway.create(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(commad);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .existByIds(expectedCategories);

        Mockito.verify(genreGateway).create(Mockito.argThat(genre ->
                Objects.equals(expectedName, genre.getName())
                        && Objects.equals(expectedIsActive, genre.isActive())
                        && Objects.equals(expectedCategories, genre.getCategories())
                        && Objects.nonNull(genre.getCreatedAt())
                        && Objects.nonNull(genre.getUpdatedAt())
                        && Objects.isNull(genre.getDeletedAt())
                        && Objects.equals(genre.getCategories().size(), expectedCategories.size())
        ));
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId(){
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();
        final var commad = CreateGenreCommand.with(expectedName, expectedIsActive,
                StringUtils.asString(expectedCategories).toList());

        Mockito.when(genreGateway.create(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(commad);
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(genreGateway, Mockito.times(1)).create(Mockito.argThat(
                genre ->
                        Objects.equals(expectedName, genre.getName())
                                && Objects.equals(expectedIsActive, genre.isActive())
                                && Objects.equals(expectedCategories, genre.getCategories())
                                && Objects.nonNull(genre.getCreatedAt())
                                && Objects.nonNull(genre.getUpdatedAt())
                                && Objects.nonNull(genre.getDeletedAt())
        ));
    }

    @Test
    public void givenAInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException(){
        final var expectedName = " ";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var commad = CreateGenreCommand.with(expectedName, expectedIsActive,
                StringUtils.asString(expectedCategories).toList());
        final var exception = Assertions.assertThrows(NotificationException.class, () ->{
            useCase.execute(commad);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
        Mockito.verify(categoryGateway, Mockito.times(0)).existByIds(Mockito.any());
    }

    @Test
    public void givenAInvalidNullName_whenCallsCreateGenre_shouldReturnDomainException(){
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = CreateGenreCommand.with(null, expectedIsActive,
                StringUtils.asString(expectedCategories).toList());

        final var exception = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(command);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Mockito.verify(categoryGateway, Mockito.times(0)).existByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());

    }

    @Test
    public void givenAValidCommand_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException(){
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var filme = CategoryID.from("123");
        final var series = CategoryID.from("456");
        final var documentario = CategoryID.from("789");
        final var expectedErrorMesage = "Some categories could not be found: 123, 789";
        final var expectedErrorCount = 1;
        final var expectedCategories = List.<CategoryID>of(
                filme,
                documentario,
                series
        );

        Mockito.when(categoryGateway.existByIds(Mockito.any()))
                .thenReturn(List.of(series));

        final var command = CreateGenreCommand.with(expectedName, expectedIsActive,
                StringUtils.asString(expectedCategories).toList());

        final var exception = Assertions.assertThrows(NotificationException.class, () ->{
            useCase.execute(command);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMesage, exception.getErrors().get(0).message());
        Mockito.verify(categoryGateway, Mockito.times(1)).existByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());

    }

    @Test
    public void givenAInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException(){
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var filme = CategoryID.from("123");
        final var series = CategoryID.from("456");
        final var documentario = CategoryID.from("789");
        final var expectedErrorMesageOne = "Some categories could not be found: 123, 789";
        final var expectedErrorMesageTwo = "'name' should not be empty";

        final var expectedErrorCount = 2;
        final var expectedCategories = List.<CategoryID>of(
                filme,
                documentario,
                series
        );

        Mockito.when(categoryGateway.existByIds(Mockito.any()))
                .thenReturn(List.of(series));

        final var command = CreateGenreCommand.with(expectedName, expectedIsActive,
                StringUtils.asString(expectedCategories).toList());

        final var exception = Assertions.assertThrows(NotificationException.class, () ->{
            useCase.execute(command);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMesageOne, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMesageTwo, exception.getErrors().get(1).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).existByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());

    }

    /**
     * deprecated
     * List<D> D is the type of the list that will be returned
     * @param actual current type of the list
     * @param mapper  function that
     * @return type of D
     * @param <A> type of the object (atribute) of the list that will receive
     * @param <D> type of the object (attribute) of the list that will be returned
     *
     */
    private <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper){
        return actual.stream().map(mapper).toList();
    }

}
