package e3n.com.admin.catalogo.application.category.update;

import e3n.com.admin.catalogo.domain.category.Category;

public record UpdateCategoryOutput(
        String id
) {
    public static UpdateCategoryOutput from(final String id){
        return new UpdateCategoryOutput(id);
    }

    public static UpdateCategoryOutput from(final Category category){
        return new UpdateCategoryOutput(category.getId().getValue());
    }
}
