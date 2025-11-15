package Validator;

import Exception.CurrencyException;
import java.util.Currency;

/**
 * Validator für Währungs-Codes (ISO 4217), prüft null und Gültigkeit.
 * Funktioniert analog zum DoubleValidator, nur mit CurrencyException.
 */
public class CurrencyValidator extends BaseValidator<String, CurrencyException> {

    public CurrencyValidator() {
        super();
    }

    @Override
    public void validate(String code) throws CurrencyException {
        errors.clear();
        if (code == null || code.trim().isEmpty()) {
            String msg = "Währungscode darf nicht leer sein.";
            addError(msg);
            throw new CurrencyException(msg);
        }
        try {
            Currency.getInstance(code.trim().toUpperCase());
        } catch (Exception e) {
            String msg = "Ungültiger Währungscode: " + code;
            addError(msg);
            throw new CurrencyException(msg);
        }
    }

    /**
     * Validiert einen String und gibt ein Currency-Objekt zurück.
     * Akzeptiert beliebige Groß-/Kleinschreibung, entfernt Leerzeichen.
     */
    public Currency validateString(String input) throws CurrencyException {
        errors.clear();
        if (input == null || input.trim().isEmpty()) {
            String msg = "Währungscode darf nicht leer sein.";
            addError(msg);
            throw new CurrencyException(msg);
        }
        String normalizedCode = input.trim().toUpperCase();
        try {
            return Currency.getInstance(normalizedCode);
        } catch (Exception e) {
            String msg = "Ungültiger Währungscode: " + input;
            addError(msg);
            throw new CurrencyException(msg);
        }
    }
}
