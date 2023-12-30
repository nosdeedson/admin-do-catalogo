package e3n.com.admin.catalogo.infrastructure.configuration.usecases;

import com.E3N.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase;
import com.E3N.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.E3N.admin.catalogo.application.video.create.DefaultCreateVideoUseCase;
import com.E3N.admin.catalogo.application.video.delete.DefaultDeleteVideoUseCase;
import com.E3N.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import com.E3N.admin.catalogo.application.video.media.get.DefaultGetMediaUseCase;
import com.E3N.admin.catalogo.application.video.media.get.GetMediaUseCase;
import com.E3N.admin.catalogo.application.video.media.update.DefaultUpdateMediaUseCase;
import com.E3N.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import com.E3N.admin.catalogo.application.video.media.upload.DefaultUploadMediaUseCase;
import com.E3N.admin.catalogo.application.video.media.upload.UploadMediaUseCase;
import com.E3N.admin.catalogo.application.video.retrieve.get.DefaultGetVideoByIdUseCase;
import com.E3N.admin.catalogo.application.video.retrieve.get.GetVideoByIdUseCase;
import com.E3N.admin.catalogo.application.video.retrieve.list.DefaultListVideosUseCase;
import com.E3N.admin.catalogo.application.video.retrieve.list.ListVideoUseCase;
import com.E3N.admin.catalogo.application.video.update.DefaultUpdateVideoUseCase;
import com.E3N.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.E3N.admin.catalogo.domain.castmember.CastMemberGateway;
import com.E3N.admin.catalogo.domain.category.CategoryGateway;
import com.E3N.admin.catalogo.domain.genre.GenreGateway;
import com.E3N.admin.catalogo.domain.video.MediaResourceGateway;
import com.E3N.admin.catalogo.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfig {

    private final CategoryGateway categoryGateway;
    private final CastMemberGateway castMemberGateway;
    private final GenreGateway genreGateway;
    private final MediaResourceGateway mediaResourceGateway;
    private final VideoGateway videoGateway;


    public VideoUseCaseConfig(
            final CategoryGateway categoryGateway,
            final CastMemberGateway castMemberGateway,
            final GenreGateway genreGateway,
            final MediaResourceGateway mediaResourceGateway,
            final VideoGateway videoGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
        this.videoGateway = Objects.requireNonNull(Objects.requireNonNull(videoGateway));
    }

    @Bean
    public CreateVideoUseCase createVideoUseCase() {
        return new DefaultCreateVideoUseCase(categoryGateway, genreGateway, castMemberGateway, mediaResourceGateway, videoGateway);
    }

    @Bean
    public DeleteVideoUseCase deleteVideoUseCase() {
        return new DefaultDeleteVideoUseCase(videoGateway, mediaResourceGateway);
    }

    @Bean
    public GetVideoByIdUseCase getVideoByIdUseCase() {
        return new DefaultGetVideoByIdUseCase(videoGateway);
    }

    @Bean
    public GetMediaUseCase getMediaUseCase() {
        return new DefaultGetMediaUseCase(mediaResourceGateway);
    }

    @Bean
    public ListVideoUseCase listVideoUseCase() {
        return new DefaultListVideosUseCase(videoGateway);
    }

    @Bean
    public UpdateVideoUseCase updateVideoUseCase() {
        return new DefaultUpdateVideoUseCase(videoGateway, categoryGateway, genreGateway, castMemberGateway, mediaResourceGateway);
    }

    @Bean
    public UploadMediaUseCase uploadMediaUseCase(){
        return new DefaultUploadMediaUseCase(mediaResourceGateway, videoGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new DefaultUpdateMediaUseCase(videoGateway);
    }
}
