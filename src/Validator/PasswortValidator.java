package Validator;

import Exception.PasswortException;
import java.util.regex.Pattern;

public class PasswortValidator extends BaseValidator<String, PasswortException> {

    private static final int MIN_LENGTH = 8;
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile(".*[A-Z].*");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile(".*[a-z].*");
    private static final Pattern DIGIT_PATTERN     = Pattern.compile(".*\\d.*");
    private static final Pattern SPECIAL_PATTERN   = Pattern.compile(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");

    @Override
    public void validate(String password) throws PasswortException {
        errors.clear();
        if (password == null || password.isEmpty()) {
            String msg = "Passwort darf nicht leer sein.";
            addError(msg);
            throw new PasswortException(msg);
        }
        if (password.length() < MIN_LENGTH) {
            throw new PasswortException("Passwort muss mindestens " + MIN_LENGTH + " Zeichen lang sein.");
        }
        if (!UPPERCASE_PATTERN.matcher(password).matches()) {
            throw new PasswortException("Passwort muss mindestens einen Großbuchstaben enthalten.");
        }
        if (!LOWERCASE_PATTERN.matcher(password).matches()) {
            throw new PasswortException("Passwort muss mindestens einen Kleinbuchstaben enthalten.");
        }
        if (!DIGIT_PATTERN.matcher(password).matches()) {
            throw new PasswortException("Passwort muss mindestens eine Ziffer enthalten.");
        }
        if (!SPECIAL_PATTERN.matcher(password).matches()) {
            throw new PasswortException("Passwort muss mindestens ein Sonderzeichen enthalten.");
        }
    }
}
