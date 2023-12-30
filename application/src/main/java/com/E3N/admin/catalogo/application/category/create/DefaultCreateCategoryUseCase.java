package com.E3N.admin.catalogo.application.category.create;

import com.E3N.admin.catalogo.domain.category.Category;
import com.E3N.admin.catalogo.domain.category.CategoryGateway;
import com.E3N.admin.catalogo.domain.exceptions.NotificationException;
import com.E3N.admin.catalogo.domain.validation.handler.Notification;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase{
    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public CreateCategoryOutput execute(CreateCategoryCommand createCategoryCommand) {
        final var notification = Notification.create();
        final var category = Category.newCategory(
                createCategoryCommand.name(),
                createCategoryCommand.description(),
                createCategoryCommand.isActive());
        category.validate(notification);
        if (notification.hasError()){
            throw new NotificationException("Could not create Aggregate Category", notification);
        }
        final var result = this.categoryGateway.create(category);
        return CreateCategoryOutput.from(result.getId().getValue());
    }

}
