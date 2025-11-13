package Validator;

import Exception.NotAllNecessaryDataEnteredException;

import java.util.ArrayList;

/**
 * Validiert, ob ein Objekt nicht null ist.
 * Wenn das Objekt null ist, wird eine NotAllNecessaryDataEnteredException geworfen.
 *
 * @param <T> Der Typ des zu validierenden Objekts.
 */
public class NotNullValidator<T> extends BaseValidator<T> {

    public NotNullValidator() {
        super();
        errors = new ArrayList<>();
    }

    @Override
    public void validate(T obj) throws NotAllNecessaryDataEnteredException {
        errors.clear();
        if (obj == null) {
            String msg = "Pflichtfeld darf nicht null sein.";
            errors.add(msg);
            throw new NotAllNecessaryDataEnteredException(msg);
        }
    }
}
