package com.E3N.admin.catalogo.domain.video;

import com.E3N.admin.catalogo.domain.pagination.Pagination;

import java.util.Optional;

public interface VideoGateway {
    Video create(Video video);
    void deleteById(VideoID id);
    Optional<Video> findById(VideoID id);
    Pagination<VideoPreview> findAll(VideoSearchQuery query);
    Video update(Video video);
}
