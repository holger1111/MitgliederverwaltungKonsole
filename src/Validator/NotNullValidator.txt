package Validator;

import Exception.NotAllNecessaryDataEnteredException;

/**
 * Validiert, ob ein Objekt nicht null ist.
 * Wenn das Objekt null ist, wird eine NotAllNecessaryDataEnteredException geworfen.
 *
 * @param <T> Der Typ des zu validierenden Objekts.
 */
public class NotNullValidator<T> extends BaseValidator<T, NotAllNecessaryDataEnteredException> {

    public NotNullValidator() {
        super();
    }

    @Override
    public void validate(T obj) throws NotAllNecessaryDataEnteredException {
        errors.clear();
        if (obj == null) {
            String msg = "Pflichtfeld darf nicht null sein.";
            addError(msg);
            throw new NotAllNecessaryDataEnteredException(msg);
        }
    }
}
