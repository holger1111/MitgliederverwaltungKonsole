package Validator;

import Exception.TooShortException;
import Exception.TooLongException;

/**
 * Validator für die Länge eines Strings.
 * Wirft TooShortException, wenn die Eingabe zu kurz ist.
 * Wirft TooLongException, wenn die Eingabe zu lang ist.
 */
public class LengthValidator extends StringValidator {

    private int minLength;
    private int maxLength;

    public LengthValidator(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public void validate(String obj) throws TooShortException, TooLongException {
        errors.clear();
        if (obj == null || obj.trim().isEmpty()) {
            String msg = "Eingabe darf nicht leer sein.";
            addError(msg);
            throw new TooShortException(msg);
        }
        if (obj.length() < minLength) {
            String msg = "Eingabe ist zu kurz. Mindestens " + minLength + " Zeichen erforderlich.";
            addError(msg);
            throw new TooShortException(msg);
        }
        if (obj.length() > maxLength) {
            String msg = "Eingabe ist zu lang. Maximal " + maxLength + " Zeichen erlaubt.";
            addError(msg);
            throw new TooLongException(msg);
        }
    }
}
