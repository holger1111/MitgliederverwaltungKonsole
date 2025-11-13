//package OUTDATED;
//
//import Exception.StringException;
//import Validator.BaseValidator;
//
//public class OUT_StringValidator extends BaseValidator<Object> {
//
//	@Override
//	public void validate(Object obj) throws StringException {
//	    errors.clear();
//	    if (!(obj instanceof String)) {
//	        String msg = "Eingabe ist kein String.";
//	        errors.add(msg);
//	        throw new StringException(msg);
//	    }
//	}
//}
