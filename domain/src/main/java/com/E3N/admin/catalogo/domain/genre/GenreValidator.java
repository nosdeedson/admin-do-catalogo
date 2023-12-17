package com.E3N.admin.catalogo.domain.genre;

import com.E3N.admin.catalogo.domain.utils.StringUtils;
import com.E3N.admin.catalogo.domain.validation.ValidationHandler;
import com.E3N.admin.catalogo.domain.validation.Validator;

@SuppressWarnings("all")
public class GenreValidator extends Validator {

    private static final int NAME_MAX_LENGTH = 255;
    private static final int NAME_MIN_LENGTH = 1;
    private Genre genre;

    public GenreValidator(ValidationHandler handler, Genre genre){
        super(handler);
        this.genre = genre;
    }

    @Override
    public void validate() {
        final var name = this.genre.getName();
        final var error = StringUtils.checkStringConstraints(name, "name", NAME_MAX_LENGTH, NAME_MIN_LENGTH);
        if (error != null){
            this.validationHandler().append(error);
        }
    }
}
