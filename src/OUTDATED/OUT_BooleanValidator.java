//package OUTDATED;
//
//import Exception.BooleanException;
//import Validator.BaseValidator;
//
//public class OUT_BooleanValidator extends BaseValidator<Object> {
//
//    @Override
//    public void validate(Object obj) throws BooleanException {
//        errors.clear();
//        if (!(obj instanceof Boolean)) {
//            String msg = "Eingabe ist kein Boolean (true/false).";
//            errors.add(msg);
//            throw new BooleanException(msg);
//        }
//    }
//}
