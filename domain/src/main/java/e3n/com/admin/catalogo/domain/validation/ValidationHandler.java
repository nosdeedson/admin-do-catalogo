package e3n.com.admin.catalogo.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error error);
    ValidationHandler append(ValidationHandler handler);
    <T> T validate(Validation<T> aValidation);

    List<Error> getErrors();

    default boolean hasError(){
        final var hasErrors = getErrors() != null && !getErrors().isEmpty();
        return hasErrors;
    }

    default Error firstError(){
        if(getErrors() != null && !getErrors().isEmpty()){
            return getErrors().get(0);
        }else {
            return null;
        }
    }

    interface Validation<T>{
        T validate();
    }
}
