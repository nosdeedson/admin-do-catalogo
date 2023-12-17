package e3n.com.admin.catalogo.infrastructure.video;

import com.E3N.admin.catalogo.domain.video.*;
import com.E3N.admin.catalogo.domain.Identifier;
import com.E3N.admin.catalogo.domain.pagination.Pagination;
import com.E3N.admin.catalogo.domain.utils.CollectionsUtils;
import e3n.com.admin.catalogo.infrastructure.utils.SqlUtils;
import e3n.com.admin.catalogo.infrastructure.video.persistence.VideoJpaEntity;
import e3n.com.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

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
        return this.videoRepository.findById(id.getValue())
                .map(VideoJpaEntity::toAggregate);
    }

    @Override
    public Pagination<VideoPreview> findAll(VideoSearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );
        final var all = this.videoRepository.findAll(
                SqlUtils.like(SqlUtils.upper(query.terms())),
                CollectionsUtils.nullIfEmpty(CollectionsUtils.mapTo(query.castMembers(), Identifier::getValue)),
                CollectionsUtils.nullIfEmpty(CollectionsUtils.mapTo(query.categories(), Identifier::getValue)),
                CollectionsUtils.nullIfEmpty(CollectionsUtils.mapTo(query.genres(), Identifier::getValue)),
                page
        );
        return new Pagination<>(
                all.getNumber(),
                all.getSize(),
                all.getTotalElements(),
                all.toList()
        );
    }

    @Override
    public Video update(Video video) {
        return this.videoRepository.save(VideoJpaEntity.from(video)).toAggregate();
    }
}
