package e3n.com.admin.catalogo.infrastructure.video;

import com.E3N.admin.catalogo.domain.video.*;
import e3n.com.admin.catalogo.Fixture;
import e3n.com.admin.catalogo.IntegrationTest;
import e3n.com.admin.catalogo.infrastructure.services.StorageService;
import e3n.com.admin.catalogo.infrastructure.services.local.InMemoryStorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@IntegrationTest
public class DefaultMediaResourceGatewayTest {

    @Autowired
    private MediaResourceGateway resourceGateway;

    @Autowired
    private StorageService storageService;
    
    @BeforeEach
    public void setUp(){
        storageService().clear();
    }

    @Test
    public void testInjections(){
        Assertions.assertNotNull(resourceGateway);
        Assertions.assertNotNull(storageService);
    }

    @Test
    public void givenValidResource_whenCallsStorageAudioVideo_shouldStoreIt() {
        final var expectedId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedLocation =
                "videoId-%s/type-%s".formatted(expectedId.getValue(),
                        expectedType.name());

        final var expectedStatus = MediaStatus.PENDING;
        final var expectedEncodedLocation = "";

        final var media = this.resourceGateway.storeAudioVideo(expectedId,
                VideoResource.with(expectedType, expectedResource));

        Assertions.assertNotNull(media.id());
        Assertions.assertEquals(expectedEncodedLocation, media.encodedLocation());
        Assertions.assertEquals(expectedStatus, media.status());
        Assertions.assertEquals(expectedLocation, media.rawLocation());
        Assertions.assertEquals(expectedResource.checksum(), media.checksum());
        Assertions.assertEquals(expectedResource.name(), media.name());

        final var stored = storageService().storage().get(expectedLocation);
        Assertions.assertEquals(expectedResource, stored);
    }

    @Test
    public void givenValidResource_whenCallsStorageImage_shouldStoreIt() {
        final var expectedId = VideoID.unique();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedLocation =
                "videoId-%s/type-%s".formatted(expectedId.getValue(),
                        expectedType.name());

        final var expectedStatus = MediaStatus.PENDING;
        final var expectedEncodedLocation = "";

        final var media = this.resourceGateway.storeImage(expectedId,
                VideoResource.with(expectedType, expectedResource));

        Assertions.assertNotNull(media.id());
        Assertions.assertEquals(expectedLocation, media.location());
        Assertions.assertEquals(expectedResource.checksum(), media.checksum());
        Assertions.assertEquals(expectedResource.name(), media.name());

        final var stored = storageService().storage().get(expectedLocation);
        Assertions.assertEquals(expectedResource, stored);
    }

    @Test
    public void givenValidVideoId_whenCallsGetResource_shouldReturnIt() {
        final var expectedId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);

        storageService().store("videoId-%s/type-%s".formatted(expectedId.getValue(), expectedType.name()),
                expectedResource);
        storageService().store("videoId-%s/type-%s".formatted(expectedId.getValue(), VideoMediaType.TRAILER.name()),
                Fixture.Videos.resource(Fixture.videoMediaType()));
        storageService().store("videoId-%s/type-%s".formatted(expectedId.getValue(), VideoMediaType.BANNER.name()),
                Fixture.Videos.resource(Fixture.videoMediaType()));
        Assertions.assertEquals(3, storageService().storage().size());

        final var result =
                storageService().get("videoId-%s/type-%s".formatted(expectedId.getValue(), expectedType.name())).get();
        Assertions.assertEquals(expectedResource, result);
    }

    @Test
    public void givenInvalidType_whenCallsGetResource_shouldReturnEmpty() {
        final var expectedId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);

        storageService().store("videoId-%s/type-%s".formatted(expectedId.getValue(), expectedType.name()),
                expectedResource);
        storageService().store("videoId-%s/type-%s".formatted(expectedId.getValue(), VideoMediaType.TRAILER.name()),
                Fixture.Videos.resource(Fixture.videoMediaType()));
        storageService().store("videoId-%s/type-%s".formatted(expectedId.getValue(), VideoMediaType.BANNER.name()),
                Fixture.Videos.resource(Fixture.videoMediaType()));
        Assertions.assertEquals(3, storageService().storage().size());

        final var result = storageService().get("invalidId");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void givenValidVideoId_whenCallsClearResources_shouldDeleteAll() {
        final var expectedId1 = VideoID.unique();
        final var expectedId2 = VideoID.unique();
        final var expectedId3 = VideoID.unique();
        final var expectedId4 = VideoID.unique();

        storageService().store("videoId-%s/type-%s".formatted(expectedId1.getValue(), VideoMediaType.VIDEO.name()),
                Fixture.Videos.resource(Fixture.videoMediaType()));

        storageService().store("videoId-%s/type-%s".formatted(expectedId2.getValue(), VideoMediaType.TRAILER.name()),
                Fixture.Videos.resource(Fixture.videoMediaType()));

        storageService().store("videoId-%s/type-%s".formatted(expectedId3.getValue(), VideoMediaType.BANNER.name()),
                Fixture.Videos.resource(Fixture.videoMediaType()));

        storageService().store("videoId-%s/type-%s".formatted(expectedId4.getValue(), VideoMediaType.THUMBNAIL.name()),
                Fixture.Videos.resource(Fixture.videoMediaType()));

        final var toDelete = new ArrayList<String>();
        toDelete.add("videoId-%s/type-%s".formatted(expectedId1.getValue(),
                        VideoMediaType.VIDEO ));

        toDelete.add("videoId-%s/type-%s".formatted(expectedId2.getValue(),
                VideoMediaType.TRAILER ));

        Assertions.assertEquals(4, storageService().storage().size());
        storageService().deleteAll(toDelete);
        Assertions.assertEquals(2, storageService().storage().size());

    }

    private InMemoryStorageService storageService() {
        return (InMemoryStorageService) storageService;
    }

}
