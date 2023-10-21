package e3n.com.admin.catalogo.application.genre.retrieve.list;

import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record GenreLIstOutput(
        String id,
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant deletedAt
) {

    public static  GenreLIstOutput from(final Genre genre){
        return new GenreLIstOutput(
                genre.getId().getValue(),
                genre.getName(),
                genre.isActive(),
                genre.getCategories().stream().map(CategoryID::getValue).toList(),
                genre.getCreatedAt(),
                genre.getDeletedAt()
        );
    }
}
