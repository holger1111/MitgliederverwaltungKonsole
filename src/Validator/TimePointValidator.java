package Validator;

import Exception.TimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Validator für Zeitangaben im Format HH:mm:ss.
 */
public class TimePointValidator extends BaseValidator<String, TimeException> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void validate(String timeStr) throws TimeException {
        errors.clear();

        if (timeStr == null || !timeStr.matches("^\\d{2}:\\d{2}:\\d{2}$")) {
            addError("Zeit muss im Format HH:mm:ss sein.");
        } else {
            try {
                LocalTime.parse(timeStr, FORMATTER);
            } catch (DateTimeParseException e) {
                addError("Ungültige Zeit: " + timeStr);
            }
        }

        if (!errors.isEmpty()) {
            throw new TimeException(String.join("; ", errors));
        }
    }
}
