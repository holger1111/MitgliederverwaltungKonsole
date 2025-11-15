package Validator;

import Exception.ShortException;

/**
 * Validator für Short-Werte, prüft null und gültige Zahl.
 * Funktioniert analog zum DoubleValidator, nur mit ShortException.
 */
public class ShortValidator extends BaseValidator<String, ShortException> {

    public ShortValidator() {
        super();
    }

    @Override
    public void validate(String input) throws ShortException {
        errors.clear();
        if (input == null || input.trim().isEmpty()) {
            String msg = "Eingabe darf nicht leer sein.";
            addError(msg);
            throw new ShortException(msg);
        }
        try {
            Short.parseShort(input.trim());
        } catch (NumberFormatException e) {
            String msg = "Ungültige Zahl: " + input;
            addError(msg);
            throw new ShortException(msg);
        }
    }

    /**
     * Validiert einen String und gibt ein Short-Objekt zurück.
     */
    public Short validateString(String input) throws ShortException {
        errors.clear();
        if (input == null || input.trim().isEmpty()) {
            String msg = "Eingabe darf nicht leer sein.";
            addError(msg);
            throw new ShortException(msg);
        }
        try {
            return Short.parseShort(input.trim());
        } catch (NumberFormatException e) {
            String msg = "Ungültige Zahl: " + input;
            addError(msg);
            throw new ShortException(msg);
        }
    }
}
