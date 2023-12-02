package e3n.com.admin.catalogo.domain.castmember;

import e3n.com.admin.catalogo.domain.utils.StringUtils;
import e3n.com.admin.catalogo.domain.validation.Error;
import e3n.com.admin.catalogo.domain.validation.ValidationHandler;
import e3n.com.admin.catalogo.domain.validation.Validator;


public class CastMemberValidator extends Validator {

    private static final int NAME_MAX_LENGTH = 255;
    private static final int NAME_MIN_LENGTH = 3;

    private final CastMember castMember;

    public CastMemberValidator(ValidationHandler handler, CastMember castMember) {
        super(handler);
        this.castMember = castMember;
    }

    @Override
    public void validate() {
        // TODO
        //  MAYBE I SHOULD CREATE A PRIVATE METHOD HERE IF THE GENERIC DON'T WORK
        Error error = StringUtils.checkStringConstraints(castMember.getName(), "name", NAME_MAX_LENGTH, NAME_MIN_LENGTH);
        if (error != null){
            this.validationHandler().append(error);
        }
        checkTypeConstraints();
    }

    private void checkTypeConstraints(){
        if (this.castMember.getType() == null){
            this.validationHandler().append(new Error("'type' should not be null"));
        }
    }
}
