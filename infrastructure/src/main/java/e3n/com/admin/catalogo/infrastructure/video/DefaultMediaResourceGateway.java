package e3n.com.admin.catalogo.infrastructure.video;

import com.E3N.admin.catalogo.domain.video.*;
import e3n.com.admin.catalogo.infrastructure.configuration.properties.storage.StorageProperties;
import e3n.com.admin.catalogo.infrastructure.services.StorageService;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class DefaultMediaResourceGateway implements MediaResourceGateway {

    private final String filenamePattern;
    private final String locationPattern;
    private final StorageService storageService;

    public DefaultMediaResourceGateway(final StorageProperties props, StorageService storageService) {
        this.filenamePattern = props.getFilenamePattern();
        this.locationPattern = props.getLocationPattern();
        this.storageService = storageService;
    }

    @Override
    public AudioVideoMedia storeAudioVideo(VideoID id, VideoResource resource) {
        final var filePath = filePath(id, resource.type());
        final var video = resource.resource();
        storageService.store(filePath, video);
        return AudioVideoMedia.with(video.checksum(), video.name(), filePath);
    }

    @Override
    public ImageMedia storeImage(VideoID id, VideoResource resource) {
        final var filePath = filePath(id, resource.type());
        final var image = resource.resource();
        storageService.store(filePath, image);
        return ImageMedia.with(image.checksum(), image.name(), filePath);
    }

    @Override
    public Optional<Resource> getResource(VideoID id, VideoMediaType type) {
        return storageService.get(filePath(id, type));
    }

    @Override
    public void clearReources(VideoID id) {
        final var idsToClean = storageService.list(folder(id));
        storageService.deleteAll(idsToClean);
    }

    private String filename(final VideoMediaType type){
        return filenamePattern.replace("{type}", type.name());
    }

    private String folder(final VideoID id){
        return locationPattern.replace("{videoId}", id.getValue());
    }

    private String filePath(final VideoID id, final VideoMediaType type){
        return folder(id).concat("/").concat(filename(type));
    }

}
