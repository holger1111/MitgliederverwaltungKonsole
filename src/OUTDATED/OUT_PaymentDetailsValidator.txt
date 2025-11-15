//package Validator;
//
//import Exception.PaymentDetailsException;
//import Exception.StringException;
//import OUTDATED.OUT_BasicTypeValidator;
//
//import java.util.regex.Pattern;
//import java.util.regex.Matcher;
//
//public class PaymentDetailsValidator extends OUT_BasicTypeValidator {
//
//    private static final String IBAN_REGEX = "^[A-Z]{2}[0-9A-Z]{13,32}$";
//    private static final Pattern IBAN_PATTERN = Pattern.compile(IBAN_REGEX);
//    private static final String BIC_REGEX = "^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$";
//    private static final Pattern BIC_PATTERN = Pattern.compile(BIC_REGEX);
//
//    // IBAN Validierung mit Formatierung
//    public String validateIBAN(Object obj) throws Exception, StringException, PaymentDetailsException {
//        super.validate(obj);
//        if (isValid()) {
//            String iban = ((String) obj).replaceAll("\\s+", "");
//            if (iban.equalsIgnoreCase("TEST")) {
//                return formatIbanGroups(iban);
//            }
//            if (iban.length() < 15 || iban.length() > 34) {
//                String msg = "IBAN muss zwischen 15 und 34 Zeichen lang sein.";
//                errors.add(msg);
//                throw new PaymentDetailsException(msg);
//            }
//            if (iban.startsWith("DE")) {
//                if (iban.length() != 22) {
//                    String msg = "Deutsche IBAN muss exakt 22 Zeichen lang sein.";
//                    errors.add(msg);
//                    throw new PaymentDetailsException(msg);
//                }
//                String bankCode = iban.substring(4, 12);
//                String accountNum = iban.substring(12, 22);
//                if (!bankCode.matches("\\d{8}") || !accountNum.matches("\\d{10}")) {
//                    String msg = "BLZ oder Kontonummer hat falsches Format (BLZ=8, Kto=10 Ziffern).";
//                    errors.add(msg);
//                    throw new PaymentDetailsException(msg);
//                }
//            }
//            Matcher matcher = IBAN_PATTERN.matcher(iban);
//            if (!matcher.matches()) {
//                String msg = "Eingabe entspricht nicht dem IBAN-Format.";
//                errors.add(msg);
//                throw new PaymentDetailsException(msg);
//            } else if (!isValidIBAN(iban)) {
//                String msg = "IBAN ist ungültig (Prüfziffernfehler).";
//                errors.add(msg);
//                throw new PaymentDetailsException(msg);
//            }
//            // Nach erfolgreicher Validierung Rückgabe als formatierter String
//            return formatIbanGroups(iban);
//        }
//        return null;
//    }
//
//    // Fügt nach je vier Zeichen ein Leerzeichen ein
//    private String formatIbanGroups(String iban) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < iban.length(); i++) {
//            if (i > 0 && i % 4 == 0) sb.append(' ');
//            sb.append(iban.charAt(i));
//        }
//        return sb.toString();
//    }
//
//    private boolean isValidIBAN(String iban) {
//        String rearranged = iban.substring(4) + iban.substring(0, 4);
//        StringBuilder numericIBAN = new StringBuilder();
//        for (char ch : rearranged.toCharArray()) {
//            if (Character.isDigit(ch)) {
//                numericIBAN.append(ch);
//            } else {
//                numericIBAN.append(Character.getNumericValue(ch));
//            }
//        }
//        String checkString = numericIBAN.toString();
//        int mod = 0;
//        for (int i = 0; i < checkString.length(); i += 7) {
//            int end = Math.min(i + 7, checkString.length());
//            String part = mod + checkString.substring(i, end);
//            mod = Integer.parseInt(part) % 97;
//        }
//        return mod == 1;
//    }
//
//    // BIC Validierung
//    public void validateBIC(Object obj) throws StringException, PaymentDetailsException {
//        super.validate(obj);
//        if (isValid()) {
//            String bic = (String) obj;
//            if (bic.equalsIgnoreCase("TEST")) {
//                return;
//            }
//            Matcher matcher = BIC_PATTERN.matcher(bic);
//            if (!matcher.matches()) {
//                String msg = "Eingabe ist kein gültiger BIC.";
//                errors.add(msg);
//                throw new PaymentDetailsException(msg);
//            }
//        }
//    }
//}
package OUTDATED;


