package e3n.com.admin.catalogo.infrastructure.category.presenters;

import com.E3N.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.E3N.admin.catalogo.application.category.retrieve.list.CategoryListOutput;
import e3n.com.admin.catalogo.infrastructure.category.models.CategoryListResponse;
import e3n.com.admin.catalogo.infrastructure.category.models.CategoryResponse;

public interface CategoryApiPresenter {

    static CategoryResponse present(final CategoryOutput output){
        return new CategoryResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.active(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    public static CategoryListResponse present(final CategoryListOutput listOutput){
        return new CategoryListResponse(
                listOutput.id().getValue(),
                listOutput.name(),
                listOutput.description(),
                listOutput.active(),
                listOutput.createdAt(),
                listOutput.deletedAt()
        );
    }
}
