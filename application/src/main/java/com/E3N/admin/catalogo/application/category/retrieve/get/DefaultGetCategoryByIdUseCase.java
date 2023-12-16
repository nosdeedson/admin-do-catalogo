package com.E3N.admin.catalogo.application.category.retrieve.get;

import com.E3N.admin.catalogo.domain.category.CategoryGateway;
import com.E3N.admin.catalogo.domain.category.CategoryID;
import com.E3N.admin.catalogo.domain.exceptions.NotFoundException;
import com.E3N.admin.catalogo.domain.validation.Error;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase{

    private CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public CategoryOutput execute(String id) {
        return this.categoryGateway.findById(CategoryID.from(id))
                .map(CategoryOutput::from)
                .orElseThrow(() -> NotFoundException.with(new Error("Category with ID " + id + " was not found")));
    }
}
