package e3n.com.admin.catalogo.application.category.update;

import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.exceptions.DomainException;
import e3n.com.admin.catalogo.domain.validation.Error;
import e3n.com.admin.catalogo.domain.validation.handler.Notification;
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
                .orElseThrow(() -> DomainException.with(new Error("Category with id %s was not found".formatted(updateCategoryCommand.id()))));
        final var notification = Notification.create();

        category.update(updateCategoryCommand.name(), updateCategoryCommand.description(), updateCategoryCommand.active())
                .validate(notification);
        return notification.hasError() ? API.Left(notification): update(category);
    }

    private Either<Notification, UpdateCategoryOutput> update(Category category){
        return API.Try(() -> this.categoryGateway.update(category))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }
}
