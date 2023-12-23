package e3n.com.admin.catalogo.infrastructure.services.local;

import com.E3N.admin.catalogo.domain.video.VideoMediaType;
import e3n.com.admin.catalogo.Fixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InMemoryStorageApiTest {

    private final InMemoryStorageService service = new InMemoryStorageService();

    @BeforeEach
    public void setUp(){
        service.clear();
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldStoreIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedId = "item";

        Assertions.assertEquals(0, service.list(expectedId).size());
        service.store(expectedId, expectedResource);
        Assertions.assertEquals(1, service.list(expectedId).size());
        Assertions.assertEquals(expectedResource, service.get(expectedId).get());
    }

    @Test
    public void givenResource_whenCallsGet_shouldRetrieveIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final var expectedId = "item";

        Assertions.assertEquals(0, service.list(expectedId).size());
        service.store(expectedId, expectedResource);

        final var resource = service.get(expectedId).get();
        Assertions.assertEquals(expectedResource, resource);
        Assertions.assertEquals(1, service.list(expectedId).size());

    }

    @Test
    public void givenInvalidResource_whenCallsGet_shouldRetrieveEmpty() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final var expectedId = "any";

        Assertions.assertEquals(0, service.list(expectedId).size());
        service.store("item", expectedResource);
        Assertions.assertEquals(1, service.list("item").size());

        final var resource = service.get(expectedId);
        Assertions.assertTrue(resource.isEmpty());
    }

    @Test
    public void givenPrefix_whenCallsList_shouldRetrieveAll() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final var expectedResource1 =
                Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var expectedId = "item";
        final var expectedIds = List.of("item", "item1");

        service.store(expectedIds.get(0), expectedResource);
        service.store(expectedIds.get(1), expectedResource1);
        Assertions.assertEquals(2, service.list(expectedId).size());

        final var all = service.list(expectedId);

        Assertions.assertEquals(2, all.size());
        Assertions.assertTrue(expectedIds.containsAll(all));

    }

    @Test
    public void givenResource_whenCallsDeleteAll_shouldEmptyStorage() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final var expectedResource1 =
                Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var expectedIdDelete = "item";
        final var expectedIds = List.of("item", "item1");

        service.store(expectedIds.get(0), expectedResource);
        service.store(expectedIds.get(1), expectedResource1);

        Assertions.assertEquals(2, service.list(expectedIdDelete).size());
        service.deleteAll(expectedIds);
        Assertions.assertEquals(0, service.list(expectedIdDelete).size());

    }

    @Test
    public void givenResource_whenCallsDeleteIds_shouldDeleteMatches() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.TRAILER);

        final var expectedResource1 =
                Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var expectedIdDelete = "video";
        final var expectedIds = List.of("video1", "video2");

        service.store(expectedIds.get(0), expectedResource);
        service.store(expectedIds.get(1), expectedResource1);

        final var expectedResource2 =
                Fixture.Videos.resource(VideoMediaType.BANNER);
        final var expectedResource3 =
                Fixture.Videos.resource(VideoMediaType.THUMBNAIL);

        service.store("image", expectedResource2);
        service.store("image1", expectedResource3);

        int qtdResources = service.list("video").size() + service.list("image").size();

        Assertions.assertEquals(4, qtdResources);
        service.deleteAll(expectedIds);
        Assertions.assertEquals(0, service.list("video").size());

    }
}
