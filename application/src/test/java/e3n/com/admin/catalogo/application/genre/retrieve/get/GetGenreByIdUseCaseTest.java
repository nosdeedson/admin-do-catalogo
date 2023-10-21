package e3n.com.admin.catalogo.application.genre.retrieve.get;

import e3n.com.admin.catalogo.application.UseCaseTest;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.exceptions.NotFoundException;
import e3n.com.admin.catalogo.domain.genre.Genre;
import e3n.com.admin.catalogo.domain.genre.GenreGateway;
import e3n.com.admin.catalogo.domain.genre.GenreId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.Struct;
import java.util.List;
import java.util.Optional;

public class GetGenreByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetGenreByIdUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetGenre_shouldReturnGenre(){
        final var expectedName = "Action";
        final var expectedIsActive = true;

        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var genre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories);
        final var expectedId = genre.getId();

        Mockito.when(genreGateway.findById(genre.getId()))
                .thenReturn(Optional.of(genre));

        final var output = useCase.execute(genre.getId().getValue());

        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedId.getValue(), output.id());
        Assertions.assertEquals(expectedIsActive, output.isActive());
        Assertions.assertNotNull(output.created_at());
        Assertions.assertNull(output.deleted_at());
        Assertions.assertEquals(expectedCategories.stream().map(CategoryID::getValue).toList(), output.categories());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(expectedId);
    }

    @Test
    public void givenAValidId_whenCallsGetGenreAndDontExists_shouldReturnNotFound(){
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreId.from("123");

        Mockito.when(genreGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(NotFoundException.class, () ->{
           useCase.execute(expectedId.getValue());
        });

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());

    }
}
