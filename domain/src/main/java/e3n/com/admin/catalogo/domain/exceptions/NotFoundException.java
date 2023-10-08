package e3n.com.admin.catalogo.domain.exceptions;

import e3n.com.admin.catalogo.domain.AggregateRoot;
import e3n.com.admin.catalogo.domain.Identifier;
import e3n.com.admin.catalogo.domain.validation.Error;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException{

    protected NotFoundException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot<?>> anAggregate,
            final Identifier id
            ){
        final var error = "%s with the ID $s was not found".formatted(
                anAggregate.getSimpleName(),
                id.getValue()
        );
        return new NotFoundException(error, Collections.emptyList());
    }

    public static NotFoundException with(Error error){
        return new NotFoundException(error.message(), List.of(error));
    }
}
