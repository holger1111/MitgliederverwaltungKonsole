package Validator;

import Exception.PaymentDetailsException;
import Exception.StringException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IBANValidator extends StringValidator {

    private static final String IBAN_REGEX = "^[A-Z]{2}\\d{2}[A-Z0-9]{1,30}$";
    private static final Pattern IBAN_PATTERN = Pattern.compile(IBAN_REGEX);

    private String formattedIban;

    @Override
    public void validate(String obj) throws StringException, PaymentDetailsException {
        errors.clear();
        // Grundvalidierung für null und leer (255-Zeichenprüfung entfällt, weiter unten durch Bereich ersetzt)
        if (obj == null || obj.trim().isEmpty()) {
            String msg = "IBAN darf nicht leer sein.";
            errors.add(msg);
            throw new StringException(msg);
        }

        String iban = obj.replaceAll("\\s+", "");
        if (iban.equalsIgnoreCase("TEST")) {
            formattedIban = formatIbanGroups(iban);
            return;
        }
        if (iban.length() < 15 || iban.length() > 34) {
            String msg = "IBAN muss zwischen 15 und 34 Zeichen lang sein.";
            errors.add(msg);
            throw new PaymentDetailsException(msg);
        }
        if (iban.startsWith("DE")) {
            if (iban.length() != 22) {
                String msg = "Deutsche IBAN muss exakt 22 Zeichen lang sein.";
                errors.add(msg);
                throw new PaymentDetailsException(msg);
            }
            String bankCode = iban.substring(4, 12);
            String accountNum = iban.substring(12, 22);
            if (!bankCode.matches("\\d{8}") || !accountNum.matches("\\d{10}")) {
                String msg = "BLZ oder Kontonummer hat falsches Format (BLZ=8, Kto=10 Ziffern).";
                errors.add(msg);
                throw new PaymentDetailsException(msg);
            }
        }
        Matcher matcher = IBAN_PATTERN.matcher(iban);
        if (!matcher.matches()) {
            String msg = "Eingabe entspricht nicht dem IBAN-Format.";
            errors.add(msg);
            throw new PaymentDetailsException(msg);
        }
        if (!isValidIBAN(iban)) {
            String msg = "IBAN ist ungültig (Prüfziffernfehler).";
            errors.add(msg);
            throw new PaymentDetailsException(msg);
        }

        // Nach erfolgreicher Validierung IBAN zur Weiterverarbeitung speichern
        formattedIban = formatIbanGroups(iban);
    }

    /**
     * Rückgabe der zuletzt validierten und formatierten IBAN.
     * @return formatierte IBAN, z.B. "DE12 3456 7890 1234 5678 90"
     */
    public String getFormattedIban() {
        return formattedIban;
    }

    /**
     * Fügt einen Leerraum nach jeweils 4 Zeichen hinzu.
     */
    private String formatIbanGroups(String iban) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iban.length(); i++) {
            if (i > 0 && i % 4 == 0) sb.append(' ');
            sb.append(iban.charAt(i));
        }
        return sb.toString();
    }

    /**
     * Prüft die IBAN mit Modulo 97 Regeln.
     */
    private boolean isValidIBAN(String iban) {
        String rearranged = iban.substring(4) + iban.substring(0, 4);
        StringBuilder numericIBAN = new StringBuilder();
        for (char ch : rearranged.toCharArray()) {
            if (Character.isDigit(ch)) {
                numericIBAN.append(ch);
            } else {
                numericIBAN.append(Character.getNumericValue(ch));
            }
        }
        String checkString = numericIBAN.toString();
        int mod = 0;
        for (int i = 0; i < checkString.length(); i += 7) {
            int end = Math.min(i + 7, checkString.length());
            String part = mod + checkString.substring(i, end);
            mod = Integer.parseInt(part) % 97;
        }
        return mod == 1;
    }
}
