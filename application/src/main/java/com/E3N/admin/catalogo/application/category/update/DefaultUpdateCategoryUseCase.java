package com.E3N.admin.catalogo.application.category.update;

import com.E3N.admin.catalogo.domain.category.CategoryGateway;
import com.E3N.admin.catalogo.domain.category.CategoryID;
import com.E3N.admin.catalogo.domain.exceptions.NotFoundException;
import com.E3N.admin.catalogo.domain.exceptions.NotificationException;
import com.E3N.admin.catalogo.domain.validation.Error;
import com.E3N.admin.catalogo.domain.validation.handler.Notification;

import java.util.Objects;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public UpdateCategoryOutput execute(UpdateCategoryCommand updateCategoryCommand) {
        final var category = this.categoryGateway.findById(CategoryID.from(updateCategoryCommand.id()))
                .orElseThrow(() -> NotFoundException.with(new Error("Category with id %s was not found".formatted(updateCategoryCommand.id()))));
        final var notification = Notification.create();

        category.update(updateCategoryCommand.name(), updateCategoryCommand.description(), updateCategoryCommand.isActive())
                .validate(notification);
        if (notification.hasError()){
            throw new NotificationException("Could not update Aggregate Category", notification);
        }
        final var result = this.categoryGateway.update(category);
        return UpdateCategoryOutput.from(result.getId().getValue());
    }
}
