package com.E3N.admin.catalogo.application.video.retrieve.list;

import com.E3N.admin.catalogo.domain.pagination.Pagination;
import com.E3N.admin.catalogo.domain.video.VideoGateway;
import com.E3N.admin.catalogo.domain.video.VideoSearchQuery;

import java.util.Objects;

@SuppressWarnings("all")
public class DefaultListVideosUseCase extends ListVideoUseCase{

    private VideoGateway videoGateway;

    public DefaultListVideosUseCase(VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Pagination<VideoListOutput> execute(VideoSearchQuery query) {
        return videoGateway.findAll(query)
                .map(VideoListOutput::from);
    }
}
