package com.E3N.admin.catalogo.application.video.retrieve.list;

import com.E3N.admin.catalogo.application.UseCase;
import com.E3N.admin.catalogo.domain.pagination.Pagination;
import com.E3N.admin.catalogo.domain.video.VideoSearchQuery;

public abstract class ListVideoUseCase extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}
