package Validator;

import Exception.StringException;
import Exception.PaymentDetailsException;

import java.util.Arrays;
import java.util.List;

public class PaymentValidator extends StringValidator {

    public static final String UEBERWEISUNG = "Überweisung";
    public static final String SEPA_LASTSCHRIFT = "SEPA-Lastschrift";

    private static final List<String> PAYMENT_TYPES =
            Arrays.asList(UEBERWEISUNG, SEPA_LASTSCHRIFT);

    @Override
    public void validate(String obj) throws StringException, PaymentDetailsException {
        errors.clear();

        if (obj == null || obj.trim().isEmpty()) {
            String msg = "Zahlungsart darf nicht leer sein.";
            errors.add(msg);
            throw new StringException(msg);
        }

        String paymentType = obj.trim();
        if (!PAYMENT_TYPES.contains(paymentType)) {
            String msg = "Ungültige Zahlungsart. Erlaubt sind: \"Überweisung\" oder \"SEPA-Lastschrift\".";
            errors.add(msg);
            throw new PaymentDetailsException(msg);
        }
    }
}
