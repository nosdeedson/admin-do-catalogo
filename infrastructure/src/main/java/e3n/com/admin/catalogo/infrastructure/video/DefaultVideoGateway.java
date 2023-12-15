package e3n.com.admin.catalogo.infrastructure.video;

import e3n.com.admin.catalogo.domain.exceptions.DomainException;
import e3n.com.admin.catalogo.domain.exceptions.NotFoundException;
import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.video.*;
import e3n.com.admin.catalogo.infrastructure.video.persistence.VideoJpaEntity;
import e3n.com.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DefaultVideoGateway implements VideoGateway {

    private final VideoRepository videoRepository;

    public DefaultVideoGateway(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    public Video create(Video video) {
        return this.videoRepository.save(VideoJpaEntity.from(video)).toAggregate();
    }

    @Override
    public void deleteById(VideoID id) {
        if(this.videoRepository.existsById(id.getValue())){
            this.videoRepository.deleteById(id.getValue());
        }
    }

    @Override
    public Optional<Video> findById(VideoID id) {
        final var fromBD =
                this.videoRepository.findById(id.getValue()).orElseThrow(() -> NotFoundException.with(Video.class, id));
        return Optional.ofNullable(fromBD.toAggregate());
    }

    @Override
    public Pagination<VideoPreview> findAll(VideoSearchQuery query) {
        return null;
    }

    @Override
    public Video update(Video video) {
        return null;
    }
}
