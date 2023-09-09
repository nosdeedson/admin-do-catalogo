package e3n.com.admin.catalogo.application.category.create;

import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;
import io.vavr.control.Try;

import static io.vavr.control.Either.*;

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

        return notification.hasError() ? API.Left(notification): create(category);
    }

    private Either<Notification, CreateCategoryOutput> create(Category category) {
        return API.Try(
                () -> this.categoryGateway.create(category))
                .toEither()
                .bimap(Notification::create, CreateCategoryOutput::from);
    }

}
