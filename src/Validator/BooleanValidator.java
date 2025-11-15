package Validator;

import Exception.BooleanException;

/**
 * Validator für Boolean-Werte mit Basisklasse NumericValidator.
 */
public class BooleanValidator extends NumericValidator<Boolean, BooleanException> {

    @Override
    public void validate(Boolean value) throws BooleanException {
        errors.clear();
        if (value == null) {
            String msg = "Boolean-Wert darf nicht null sein.";
            addError(msg);
            throw new BooleanException(msg);
        }
    }
}
