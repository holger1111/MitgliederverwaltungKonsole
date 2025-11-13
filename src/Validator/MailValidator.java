package Validator;

import Exception.MailException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class MailValidator extends StringValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Override
    public void validate(String email) throws MailException {
        errors.clear();
        if (email == null || email.trim().isEmpty()) {
            addError("Mail darf nicht leer sein.");
            throw new MailException("Mail darf nicht leer sein.");
        }

        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            addError("Ungültige Mail-Adresse.");
            throw new MailException("Ungültige Mail-Adresse.");
        }
    }
}
