package Validator;

import Exception.ByteException;

/**
 * Validator für Byte-Werte, prüft null und Werte außerhalb der Byte-Grenzen.
 */
public class ByteValidator extends NumericValidator<Byte, ByteException> {

    public ByteValidator() {
        super();
    }

    @Override
    public void validate(Byte obj) throws ByteException {
        errors.clear();
        if (obj == null) {
            String msg = "Byte-Wert darf nicht null sein.";
            addError(msg);
            throw new ByteException(msg);
        }
        if (obj < Byte.MIN_VALUE || obj > Byte.MAX_VALUE) {
            String msg = "Byte-Wert liegt außerhalb des gültigen Bereichs (" + Byte.MIN_VALUE + " bis " + Byte.MAX_VALUE + ").";
            addError(msg);
            throw new ByteException(msg);
        }
    }

    /**
     * Optional: Validiert einen String und konvertiert ihn zu Byte
     */
    public Byte validateString(String input) throws ByteException {
        errors.clear();
        if (input == null || input.trim().isEmpty()) {
            String msg = "Eingabe darf nicht leer sein.";
            addError(msg);
            throw new ByteException(msg);
        }
        try {
            Byte value = Byte.parseByte(input.trim());
            validate(value);
            return value;
        } catch (NumberFormatException e) {
            String msg = "Ungültige Zahl: " + input;
            addError(msg);
            throw new ByteException(msg);
        }
    }
}
