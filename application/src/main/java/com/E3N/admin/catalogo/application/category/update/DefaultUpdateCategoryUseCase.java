package com.E3N.admin.catalogo.application.category.update;

import com.E3N.admin.catalogo.domain.category.Category;
import com.E3N.admin.catalogo.domain.category.CategoryGateway;
import com.E3N.admin.catalogo.domain.category.CategoryID;
import com.E3N.admin.catalogo.domain.exceptions.NotFoundException;
import com.E3N.admin.catalogo.domain.validation.Error;
import com.E3N.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand updateCategoryCommand) {
        final var category = this.categoryGateway.findById(CategoryID.from(updateCategoryCommand.id()))
                .orElseThrow(() -> NotFoundException.with(new Error("Category with id %s was not found".formatted(updateCategoryCommand.id()))));
        final var notification = Notification.create();

        category.update(updateCategoryCommand.name(), updateCategoryCommand.description(), updateCategoryCommand.isActive())
                .validate(notification);
        return notification.hasError() ? API.Left(notification): update(category);
    }

    private Either<Notification, UpdateCategoryOutput> update(Category category){
        return API.Try(() -> this.categoryGateway.update(category))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }
}
