package com.E3N.admin.catalogo.application.category.delete;

import com.E3N.admin.catalogo.domain.category.CategoryGateway;
import com.E3N.admin.catalogo.domain.category.CategoryID;

import java.util.Objects;

@SuppressWarnings("all")
public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase{

    private CategoryGateway categoryGateway;

    public DefaultDeleteCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public void execute(String s) {
        this.categoryGateway.deleteById(CategoryID.from(s));
    }
}
