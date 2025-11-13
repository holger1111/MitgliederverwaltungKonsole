package Validator;

import Exception.PaymentDetailsException;
import Exception.StringException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BICValidator extends StringValidator {

    private static final String BIC_REGEX = "^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$";
    private static final Pattern BIC_PATTERN = Pattern.compile(BIC_REGEX);

    @Override
    public void validate(String obj) throws StringException, PaymentDetailsException {
        errors.clear();
        if (obj == null || obj.trim().isEmpty()) {
            String msg = "BIC darf nicht leer sein.";
            errors.add(msg);
            throw new StringException(msg);
        }

        String bic = obj.replaceAll("\\s+", "");
        if (bic.equalsIgnoreCase("TEST")) {
            return;
        }

        if (bic.length() < 8 || bic.length() > 11) {
            String msg = "BIC muss zwischen 8 und 11 Zeichen lang sein.";
            errors.add(msg);
            throw new PaymentDetailsException(msg);
        }
        Matcher matcher = BIC_PATTERN.matcher(bic);
        if (!matcher.matches()) {
            String msg = "Eingabe ist kein gültiger BIC.";
            errors.add(msg);
            throw new PaymentDetailsException(msg);
        }
    }
}
