package e3n.com.admin.catalogo.domain.genre;

import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.domain.utils.StringUtils;
import e3n.com.admin.catalogo.domain.validation.Error;
import e3n.com.admin.catalogo.domain.validation.ValidationHandler;
import e3n.com.admin.catalogo.domain.validation.Validator;

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
