package com.E3N.admin.catalogo.application.video.delete;

import com.E3N.admin.catalogo.application.UseCaseTest;
import com.E3N.admin.catalogo.domain.exceptions.InternalErrorException;
import com.E3N.admin.catalogo.domain.video.MediaResourceGateway;
import com.E3N.admin.catalogo.domain.video.VideoGateway;
import com.E3N.admin.catalogo.domain.video.VideoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class DeleteVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway, mediaResourceGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteVideo_shouldDeleteIt() {
        final var videoId = VideoID.from("123");

        Mockito.doNothing()
                .when(videoGateway).deleteById(Mockito.any());

        Mockito.doNothing()
                        .when(mediaResourceGateway).clearReources(Mockito.any());

        Assertions.assertDoesNotThrow(() -> useCase.execute(videoId.getValue()));
        Mockito.verify(videoGateway, Mockito.times(1)).deleteById(Mockito.eq(videoId));
        Mockito.verify(mediaResourceGateway).clearReources(Mockito.eq(videoId));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteVideo_shouldBeOk() {
        final var videoId = VideoID.from("invalid id");
        Mockito.doNothing()
                .when(videoGateway).deleteById(Mockito.any());
        Mockito.doNothing()
                .when(mediaResourceGateway).clearReources(Mockito.any());

        Assertions.assertDoesNotThrow(() -> useCase.execute(videoId.getValue()));
        Mockito.verify(videoGateway, Mockito.times(1)).deleteById(Mockito.eq(videoId));
        Mockito.verify(mediaResourceGateway, Mockito.times(1)).clearReources(Mockito.eq(videoId));
    }

    @Test
    public void givenAValidId_whenCallsDeleteVideoAndGatewayThrowsException_shouldReceiveException() {
        final var expectedErrorMessage = "Internal server error";
        Mockito.doThrow(InternalErrorException.with(expectedErrorMessage, new RuntimeException()))
                .when(videoGateway).deleteById(Mockito.any());

        final var videoId = VideoID.from("123");

        final var exception = Assertions.assertThrows(InternalErrorException.class,
                ()-> useCase.execute(videoId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
        Mockito.verify(videoGateway, Mockito.times(1)).deleteById(Mockito.eq(videoId));
    }
}
