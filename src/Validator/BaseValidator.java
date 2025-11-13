package Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Basisvalidator mit generischer Exception.
 * @param <T> Typ des zu validierenden Wertes.
 * @param <E> Exception-Typ, den der Validator werfen kann.
 */
public abstract class BaseValidator<T, E extends Exception> {

    protected List<String> errors = new ArrayList<>();

    public abstract void validate(T obj) throws E;

    public List<String> getErrors() {
        return errors;
    }

    protected void addError(String message) {
        errors.add(message);
    }
}
