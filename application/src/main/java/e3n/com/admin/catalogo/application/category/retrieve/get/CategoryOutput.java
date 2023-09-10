package e3n.com.admin.catalogo.application.category.retrieve.get;

import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.domain.category.CategoryID;

import java.time.Instant;

public record CategoryOutput(
        CategoryID id,
        String name,
        String description,
        boolean active,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    public static CategoryOutput from(Category category){
        return new CategoryOutput(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getDeletedAt()
        );
    }
}
