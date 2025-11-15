package Validator;

import Exception.BigIntegerException;
import java.math.BigInteger;

/**
 * Validator für BigInteger-Werte, prüft null und negative Werte.
 */
public class BigIntegerValidator extends NumericValidator<BigInteger, BigIntegerException> {

    public BigIntegerValidator() {
        super();
    }

    @Override
    public void validate(BigInteger value) throws BigIntegerException {
        errors.clear();
        if (value == null) {
            String msg = "BigInteger-Wert darf nicht null sein.";
            addError(msg);
            throw new BigIntegerException(msg);
        }
        if (value.compareTo(BigInteger.ZERO) < 0) {
            String msg = "BigInteger-Wert darf nicht negativ sein.";
            addError(msg);
            throw new BigIntegerException(msg);
        }
    }

    /**
     * Optional: Validiert einen String und konvertiert ihn zu BigInteger
     */
    public BigInteger validateString(String input) throws BigIntegerException {
        errors.clear();
        if (input == null || input.trim().isEmpty()) {
            String msg = "Eingabe darf nicht leer sein.";
            addError(msg);
            throw new BigIntegerException(msg);
        }
        String normalized = input.replace(",", "");
        try {
            BigInteger value = new BigInteger(normalized);
            validate(value);
            return value;
        } catch (NumberFormatException e) {
            String msg = "Ungültige Zahl: " + input;
            addError(msg);
            throw new BigIntegerException(msg);
        }
    }
}
