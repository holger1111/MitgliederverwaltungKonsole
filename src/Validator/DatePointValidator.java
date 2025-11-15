package Validator;

import Exception.DateException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Validator für Datumsangaben im Format dd.MM.yyyy.
 */
public class DatePointValidator extends DateValidator<String, DateException> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public void validate(String dateStr) throws DateException {
        errors.clear();
        validateDateFormat(dateStr);

        try {
            LocalDate.parse(dateStr, FORMATTER);
        } catch (DateTimeParseException e) {
            addError("Ungültiges Datum: " + dateStr);
        }

        if (!errors.isEmpty()) {
            throw new DateException(String.join("; ", errors));
        }
    }
}
