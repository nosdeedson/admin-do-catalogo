package e3n.com.admin.catalogo.domain.video;

import e3n.com.admin.catalogo.domain.pagination.Pagination;

import java.util.Optional;

public interface VideoGateway {
    Video create(Video video);
    void deleteById(VideoID id);
    Optional<Video> findById(VideoID id);
    Pagination<Video> findAll(VideoSearchQuery query);
    Video update(Video video);
}
