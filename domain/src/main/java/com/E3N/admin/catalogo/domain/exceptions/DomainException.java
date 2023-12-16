package com.E3N.admin.catalogo.domain.exceptions;

import com.E3N.admin.catalogo.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStacktraceException{

    protected final List<Error> errors;

    protected DomainException(String message, List<Error> errors) {
        super(message);
        this.errors = errors;
    }

    public static DomainException with(final Error error){
        return new DomainException(error.message(), List.of(error));
    }

    public static DomainException with(final List<Error> errors){
        return new DomainException("", errors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
