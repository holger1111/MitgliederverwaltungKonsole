package Validator;

import Exception.NumericException;

public class DoubleValidator extends NumericValidator<Double, NumericException> {

    @Override
    public void validate(Double obj) throws NumericException {
        errors.clear();
        if (obj == null) {
            addError("Eingabe darf nicht leer sein.");
            throw new NumericException("Eingabe darf nicht leer sein.");
        }
        // Da es schon ein Double ist, muss nicht extra konvertiert werden
    }

    /**
     * Hilfsmethode für die Validierung aus String (Akzeptiert Komma und Punkt)
     */
    public Double validateString(String input) throws NumericException {
        if (input == null || input.isEmpty()) {
            addError("Eingabe darf nicht leer sein.");
            throw new NumericException("Eingabe darf nicht leer sein.");
        }
        String normalized = input.replace(',', '.');
        try {
            Double value = Double.parseDouble(normalized);
            validate(value);
            return value;
        } catch (NumberFormatException e) {
            addError("Ungültige Zahl: " + input);
            throw new NumericException("Ungültige Zahl: " + input);
        }
    }
}
