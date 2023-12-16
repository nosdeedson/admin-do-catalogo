package com.E3N.admin.catalogo.domain.category;

import com.E3N.admin.catalogo.domain.utils.StringUtils;
import com.E3N.admin.catalogo.domain.validation.Error;
import com.E3N.admin.catalogo.domain.validation.ValidationHandler;
import com.E3N.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private static final int NAME_MAX_LENGTH = 255;
    private static final int NAME_MIN_LENGTH = 3;

    private static final int DESCRIPTION_MAX_LENGTH = 4000;
    private final Category category;

    public CategoryValidator(ValidationHandler handler, Category category) {
        super(handler);
        this.category = category;
    }

    @Override
    public void validate() {
        checkNameConstraints();
        int length = this.category.getDescription().length();
        if (length > 400){
            if (length > DESCRIPTION_MAX_LENGTH ){
                this.validationHandler().append(new Error("'description' should not be greater than 4000"));
            }
        }
    }

    private void checkNameConstraints(){
        final var name = this.category.getName();
        final var error = StringUtils.checkStringConstraints(name, "name", NAME_MAX_LENGTH, NAME_MIN_LENGTH);
        if (error != null) {
            this.validationHandler().append(error);
        }
    }
}
