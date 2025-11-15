package Validator;

import Exception.DateException;

/**
 * Generischer abstrakter Validator für Datums-Prüfungen.
 */
public abstract class DateValidator<T, E extends Exception> extends BaseValidator<T, E> {

    /**
     * Prüft ein Datums-String-Format (dd.MM.yyyy).
     */
    protected void validateDateFormat(String dateStr) throws DateException {
        if (dateStr == null || !dateStr.matches("^\\d{2}\\.\\d{2}\\.\\d{4}$")) {
            throw new DateException("Datum muss im Format dd.MM.yyyy sein.");
        }
    }
}
