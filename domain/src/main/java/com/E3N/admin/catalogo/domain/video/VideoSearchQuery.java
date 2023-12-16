package com.E3N.admin.catalogo.domain.video;

import com.E3N.admin.catalogo.domain.castmember.CastMemberID;
import com.E3N.admin.catalogo.domain.category.CategoryID;
import com.E3N.admin.catalogo.domain.genre.GenreId;

import java.util.Set;

public record VideoSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction,

        Set<CastMemberID> castMembers,
        Set<CategoryID> categories,
        Set<GenreId> genres
) {
}
