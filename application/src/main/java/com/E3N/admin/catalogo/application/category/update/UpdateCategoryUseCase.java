package com.E3N.admin.catalogo.application.category.update;

import com.E3N.admin.catalogo.application.UseCase;
import com.E3N.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {

}
