//package OUTDATED;
//
//import Exception.IntException;
//import Validator.BaseValidator;
//
//public class OUT_IntValidator extends BaseValidator<Object> {
//
//    @Override
//    public void validate(Object obj) throws IntException {
//        errors.clear();
//        if (!(obj instanceof Integer)) {
//            String msg = "Eingabe ist keine ganze Zahl (Integer).";
//            errors.add(msg);
//            throw new IntException(msg);
//        }
//    }
//}
