//package Validator;
//
//import java.util.regex.Pattern;
//
//import Exception.PaymentDetailsException;
//import Exception.StringException;
//
//import java.util.regex.Matcher;
//
//public class OUT_BICValidator extends StringValidator {
//
//    private static final String BIC_REGEX = "^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$";
//    private static final Pattern BIC_PATTERN = Pattern.compile(BIC_REGEX);
//
//    @Override
//    public void validate(Object obj) throws StringException {
//        super.validate(obj);
//        
//        if (isValid()) {
//            String bic = (String) obj;
//            
//            // ========== TEST-BYPASS ==========
//            if (bic.equalsIgnoreCase("TEST")) {
//                // TEST wird akzeptiert - keine Validierung
//                return;
//            }
//            // =================================
//            
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


