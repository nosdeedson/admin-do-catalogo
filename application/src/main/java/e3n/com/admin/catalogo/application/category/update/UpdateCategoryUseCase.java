package e3n.com.admin.catalogo.application.category.update;

import e3n.com.admin.catalogo.application.UseCase;
import e3n.com.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {

}
