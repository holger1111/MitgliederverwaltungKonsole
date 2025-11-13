package Validator;

import Exception.BooleanException;

public class BooleanValidator extends BaseValidator<Boolean> {

    @Override
    public void validate(Boolean value) throws BooleanException {
        errors.clear();
        if (value == null) {
            String msg = "Boolean-Wert darf nicht null sein.";
            errors.add(msg);
            throw new BooleanException(msg);
        }
    }
}
