package e3n.com.admin.catalogo.domain.utils;

import e3n.com.admin.catalogo.domain.Identifier;
import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.domain.validation.Error;
import e3n.com.admin.catalogo.domain.validation.ValidationHandler;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    
    public static Stream<String> asString(final Collection<? extends Identifier> ids){
        return ids.stream().map(Identifier::getValue);
    }

    /**
     * List<D> D is the type of the list that will be returned
     * @param actual current type of the list
     * @param mapper  function that will be executed
     * @return type of D
     * @param <A> type of the object (atribute) of the list that will receive
     * @param <D> type of the object (attribute) of the list that will be returned
     *
     */
    private <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper){
        return actual.stream().map(mapper).toList();
    }


}
