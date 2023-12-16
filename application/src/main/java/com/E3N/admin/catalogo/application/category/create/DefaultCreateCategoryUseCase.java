package com.E3N.admin.catalogo.application.category.create;

import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

import static io.vavr.API.*;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase{
    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Either<Notification, CreateCategoryOutput> execute(CreateCategoryCommand createCategoryCommand) {
        final var notification = Notification.create();
        final var category = Category.newCategory(
                createCategoryCommand.name(),
                createCategoryCommand.description(),
                createCategoryCommand.isActive());
        category.validate(notification);

        // Left from the api io.vavr.control.Either;
        return notification.hasError() ? Left(notification): create(category);
    }

    private Either<Notification, CreateCategoryOutput> create(Category category) {
        // try from the api io.vavr.control.Either;
        return Try(
                () -> this.categoryGateway.create(category))
                .toEither()
                .bimap(Notification::create, CreateCategoryOutput::from);
    }

}
