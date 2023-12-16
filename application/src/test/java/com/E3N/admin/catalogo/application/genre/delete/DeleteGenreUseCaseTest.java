package com.E3N.admin.catalogo.application.genre.delete;

import com.E3N.admin.catalogo.application.UseCaseTest;
import com.E3N.admin.catalogo.application.genre.delelte.DefaultDeleteGenreUseCase;
import e3n.com.admin.catalogo.domain.genre.Genre;
import e3n.com.admin.catalogo.domain.genre.GenreGateway;
import e3n.com.admin.catalogo.domain.genre.GenreId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class DeleteGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of( genreGateway);
    }

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre(){
        final var genre = Genre.newGenre("Action", true);
        final var expectedId = genre.getId();

        Mockito.doNothing().when(genreGateway).deleteById(expectedId);

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Mockito.verify(genreGateway, Mockito.times(1)).deleteById(expectedId);
    }

    @Test
    public void givenAnInvalidGenreId_whenCallsDeleteGenre_shouldBeOk(){
        final var expectedId = GenreId.from("123");
        Mockito.doNothing().when(genreGateway).deleteById(expectedId);
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Mockito.verify(genreGateway, Mockito.times(1)).deleteById(expectedId);
    }

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenreAndGatewayThrowsUnexpectedError_shouldReceiveException(){
        final var genre = Genre.newGenre("Action", true);
        final var expectedId = genre.getId();
        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(genreGateway).deleteById(expectedId);

        final var expection =Assertions.assertThrows(IllegalStateException.class, () ->{
           useCase.execute(expectedId.getValue());
        });
        Assertions.assertNotNull(expection);
        Assertions.assertEquals("Gateway error", expection.getMessage());
        Mockito.verify(genreGateway, Mockito.times(1)).deleteById(expectedId);
    }
}
