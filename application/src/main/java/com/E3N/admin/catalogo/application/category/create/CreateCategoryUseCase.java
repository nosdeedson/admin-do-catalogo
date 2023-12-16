package com.E3N.admin.catalogo.application.category.create;

import com.E3N.admin.catalogo.application.UseCase;
import com.E3N.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {

}
