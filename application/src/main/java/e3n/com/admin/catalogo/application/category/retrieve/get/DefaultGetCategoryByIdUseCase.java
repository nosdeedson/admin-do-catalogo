package e3n.com.admin.catalogo.application.category.retrieve.get;

import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.exceptions.DomainException;
import e3n.com.admin.catalogo.domain.validation.Error;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase{

    private CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public CategoryOutput execute(String id) {
        return this.categoryGateway.findById(CategoryID.from(id))
                .map(CategoryOutput::from)
                .orElseThrow(() -> DomainException.with(new Error("Category with the Id %s was not found".formatted(id))));
    }
}
