package e3n.com.admin.catalogo.application.video.retrieve.list;

import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.video.VideoGateway;
import e3n.com.admin.catalogo.domain.video.VideoSearchQuery;

import java.util.Objects;

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
