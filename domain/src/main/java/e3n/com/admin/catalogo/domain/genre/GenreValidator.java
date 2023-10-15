package e3n.com.admin.catalogo.domain.genre;

import e3n.com.admin.catalogo.domain.category.Category;
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
        if (name == null){
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }
        if (name.isBlank()){
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }
        int length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH){
            this.validationHandler().append(new Error("'name must be between 3 and 255 caracteres'"));
        }
    }
}
