package e3n.com.admin.catalogo.application.category.create;

import e3n.com.admin.catalogo.application.UseCase;
import e3n.com.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {

}
