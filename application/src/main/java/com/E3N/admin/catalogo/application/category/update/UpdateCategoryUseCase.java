package com.E3N.admin.catalogo.application.category.update;

import com.E3N.admin.catalogo.application.UseCase;
import e3n.com.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {

}
