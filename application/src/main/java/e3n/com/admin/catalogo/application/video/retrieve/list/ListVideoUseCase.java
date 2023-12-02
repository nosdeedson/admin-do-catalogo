package e3n.com.admin.catalogo.application.video.retrieve.list;

import e3n.com.admin.catalogo.application.UseCase;
import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.video.VideoSearchQuery;

public abstract class ListVideoUseCase extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}
