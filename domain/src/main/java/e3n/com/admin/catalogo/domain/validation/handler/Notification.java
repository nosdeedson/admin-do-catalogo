package e3n.com.admin.catalogo.domain.validation.handler;

import e3n.com.admin.catalogo.domain.exceptions.DomainException;
import e3n.com.admin.catalogo.domain.validation.Error;
import e3n.com.admin.catalogo.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<Error> errors;

    private Notification(final List<Error> errors) {
        this.errors = errors;
    }

    public static Notification create(){
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Throwable t){
        return create(new  Error(t.getMessage()));
    }

    public static Notification create(final Error error) {
        return (Notification) new Notification(new ArrayList<>()).append(error);
    }

    @Override
    public ValidationHandler append(Error error) {
        this.errors.add(error);
        return this;
    }

    @Override
    public Notification append(ValidationHandler handler) {
        this.errors.addAll(handler.getErrors());
        return this;
    }

    @Override
    public <T> T validate(Validation<T> aValidation) {
        try {
            return aValidation.validate();
        } catch (final DomainException e){
            this.errors.addAll(e.getErrors());
        }catch (final Throwable t){
            this.errors.add(new Error(t.getMessage()));
        }
        return null;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }
}
