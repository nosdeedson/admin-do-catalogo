package e3n.com.admin.catalogo.domain.video;

import e3n.com.admin.catalogo.domain.castmember.CastMemberID;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.genre.GenreId;

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
