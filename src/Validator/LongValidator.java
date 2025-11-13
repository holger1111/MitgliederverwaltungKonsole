package Validator;

import Exception.LongException;

/**
 * Validator für Long-Werte.
 */
public class LongValidator extends NumericValidator<Long> {

    public LongValidator() {
        super();
    }

    @Override
    public void validate(Long obj) throws LongException {
        errors.clear();
        if (obj == null) {
            String msg = "Long-Wert darf nicht null sein.";
            errors.add(msg);
            throw new LongException(msg);
        }
        // Beispiel für weitere Prüfungen, z.B. Wertebereich könnte geprüft werden:
        if (obj < Long.MIN_VALUE || obj > Long.MAX_VALUE) {
            String msg = "Long-Wert liegt außerhalb des gültigen Bereichs.";
            errors.add(msg);
            throw new LongException(msg);
        }
    }
}
