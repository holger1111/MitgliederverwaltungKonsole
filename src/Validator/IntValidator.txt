package Validator;

import Exception.IntException;

/**
 * Validator für Integer-Werte.
 */
public class IntValidator extends NumericValidator<Integer, IntException> {

    public IntValidator() {
        super();
    }

    @Override
    public void validate(Integer obj) throws IntException {
        errors.clear();
        if (obj == null) {
            String msg = "Integer-Wert darf nicht null sein.";
            addError(msg);
            throw new IntException(msg);
        }
    }
}
