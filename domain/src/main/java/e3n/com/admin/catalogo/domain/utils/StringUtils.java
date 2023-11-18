package e3n.com.admin.catalogo.domain.utils;

import e3n.com.admin.catalogo.domain.validation.Error;
import e3n.com.admin.catalogo.domain.validation.ValidationHandler;

public final class StringUtils {

    public static Error checkStringConstraints(final String value, final int maxLengnth, final int minLenght) {
        if (value == null) {
            return new Error("'name' should not be null");
        }

        if (value.isBlank()){
            return new Error("'name' should not be empty");
        }
        final int length = value.trim().length();
        if (length > maxLengnth || length <= minLenght){
            return new Error("'name' sought be between 3 and 255 characteres");
        }
        return null;
    }
}
