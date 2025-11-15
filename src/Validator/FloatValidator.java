package Validator;

import Exception.FloatException;

/**
 * Validator für Float-Werte, prüft null und gültige Zahl.
 * Funktioniert analog zum DoubleValidator, nur mit FloatException.
 */
public class FloatValidator extends BaseValidator<String, FloatException> {

    public FloatValidator() {
        super();
    }

    @Override
    public void validate(String input) throws FloatException {
        errors.clear();
        if (input == null || input.trim().isEmpty()) {
            String msg = "Eingabe darf nicht leer sein.";
            addError(msg);
            throw new FloatException(msg);
        }
        String normalized = input.replace(',', '.');
        try {
            Float.parseFloat(normalized);
        } catch (NumberFormatException e) {
            String msg = "Ungültige Zahl: " + input;
            addError(msg);
            throw new FloatException(msg);
        }
    }

    /**
     * Validiert einen String und gibt ein Float-Objekt zurück.
     * Akzeptiert Komma und Punkt als Dezimaltrenner.
     */
    public Float validateString(String input) throws FloatException {
        errors.clear();
        if (input == null || input.trim().isEmpty()) {
            String msg = "Eingabe darf nicht leer sein.";
            addError(msg);
            throw new FloatException(msg);
        }
        String normalized = input.replace(',', '.');
        try {
            return Float.parseFloat(normalized);
        } catch (NumberFormatException e) {
            String msg = "Ungültige Zahl: " + input;
            addError(msg);
            throw new FloatException(msg);
        }
    }
}
