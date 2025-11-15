package Validator;

import Exception.BigDecimalException;
import java.math.BigDecimal;

public class BigDecimalValidator extends NumericValidator<BigDecimal, BigDecimalException> {

    @Override
    public void validate(BigDecimal value) throws BigDecimalException {
        errors.clear();
        if (value == null) {
            String msg = "BigDecimal-Wert darf nicht null sein.";
            addError(msg);
            throw new BigDecimalException(msg);
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            String msg = "BigDecimal-Wert darf nicht negativ sein.";
            addError(msg);
            throw new BigDecimalException(msg);
        }
    }

    /**
     * Optional: Validiert einen String und konvertiert ihn zu BigDecimal
     */
    public BigDecimal validateString(String input) throws BigDecimalException {
        errors.clear();
        if (input == null || input.trim().isEmpty()) {
            String msg = "Eingabe darf nicht leer sein.";
            addError(msg);
            throw new BigDecimalException(msg);
        }
        String normalized = input.replace(',', '.');
        try {
            BigDecimal value = new BigDecimal(normalized);
            validate(value);
            return value;
        } catch (NumberFormatException | ArithmeticException e) {
            String msg = "Ungültige Zahl: " + input;
            addError(msg);
            throw new BigDecimalException(msg);
        }
    }
}
