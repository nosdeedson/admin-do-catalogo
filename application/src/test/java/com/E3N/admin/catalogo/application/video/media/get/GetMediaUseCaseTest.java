package com.E3N.admin.catalogo.application.video.media.get;

import com.E3N.admin.catalogo.application.Fixture;
import com.E3N.admin.catalogo.application.UseCaseTest;
import com.E3N.admin.catalogo.domain.exceptions.NotFoundException;
import com.E3N.admin.catalogo.domain.video.MediaResourceGateway;
import com.E3N.admin.catalogo.domain.video.VideoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class GetMediaUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetMediaUseCase useCase;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(mediaResourceGateway);
    }

    @Test
    public void givenVideoIdAndType_whenIsValidCmd_shouldReturnResource() {
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.videoMediaType();
        final var expectedResource = Fixture.Videos.resource(expectedType);

        Mockito.when(mediaResourceGateway.getResource(expectedId, expectedType))
                .thenReturn(Optional.of(expectedResource));

        final var command = GetMediaCommand.with(expectedId.getValue(), expectedType.name());

        final var result = useCase.execute(command);

        Assertions.assertEquals(expectedResource.name(), result.name());
        Assertions.assertEquals(expectedResource.contentType(), result.contentType());
        Assertions.assertEquals(expectedResource.content(), result.content());
    }

    @Test
    public void givenVideoIdAndType_whenIsNotFound_shouldReturnNotFoundException() {
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.videoMediaType();
        final var expectedErrorMessage = "Media not found";

        Mockito.when(mediaResourceGateway.getResource(expectedId, expectedType))
                .thenReturn(Optional.empty());

        final var command = GetMediaCommand.with(expectedId.getValue(), expectedType.name());
        final var result = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedErrorMessage, result.getMessage());
        Mockito.verify(mediaResourceGateway, Mockito.times(1)).getResource(Mockito.any(), Mockito.any());
    }

    @Test
    public void givenVideoIdAndType_whenTypeDoesntExists_shouldReturnNotFoundException() {
        final var expectedId = VideoID.unique();
        final var expectedType = "failed";
        final var expectedErrorMessage = "type of video doesn't exist";

        final var command = GetMediaCommand.with(expectedId.getValue(), expectedType);
        final var result = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedErrorMessage, result.getMessage());
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).getResource(Mockito.any(), Mockito.any());
    }


}
