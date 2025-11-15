package Validator;

import Exception.LongException;

public class LongValidator extends NumericValidator<Long, LongException> {

    public LongValidator() {
        super();
    }

    @Override
    public void validate(Long obj) throws LongException {
        errors.clear();
        if (obj == null) {
            String msg = "Long-Wert darf nicht null sein.";
            addError(msg);
            throw new LongException(msg);
        }
        // Beispiel für weitere Prüfungen, z.B. Wertebereich (hier eigentlich überflüssig)
        if (obj < Long.MIN_VALUE || obj > Long.MAX_VALUE) {
            String msg = "Long-Wert liegt außerhalb des gültigen Bereichs.";
            addError(msg);
            throw new LongException(msg);
        }
    }
}
