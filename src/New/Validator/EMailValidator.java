package New.Validator;

import New.Exception.EMailException;
import New.Exception.StringException;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class EMailValidator extends StringValidator {

    // Einfache Regex für E-Mail-Adressen (für Demo-Zwecke)
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public void validate(Object obj) throws StringException {
        super.validate(obj);
        if (isValid()) {
            String email = (String) obj;
            Matcher matcher = EMAIL_PATTERN.matcher(email);
            if (!matcher.matches()) {
                String msg = "Eingabe ist keine gültige E-Mail-Adresse.";
                errors.add(msg);
                throw new EMailException(msg); // Keine Override-Fehler!
            }
        }
    }
}



//EMailValidator emailValidator = new EmailValidator();
//
//String testEmail = "test@example.com";
//emailValidator.validate(testEmail);
//
//if (!emailValidator.isValid()) {
//    System.out.println("Fehler bei der E-Mail:");
//    for (String err : emailValidator.getErrors()) {
//        System.out.println(err);
//    }
//    emailValidator.saveErrorsToCsv();
//} else {
//    System.out.println("E-Mail ist gültig.");
//}
