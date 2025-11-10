package New.Validator;

import New.Exception.PaymentDetailsException;
import New.Exception.StringException;

public class StringValidator extends BaseValidator<Object> {

	@Override
	public void validate(Object obj) throws StringException, PaymentDetailsException {
	    errors.clear();
	    if (!(obj instanceof String)) {
	        String msg = "Eingabe ist kein String.";
	        errors.add(msg);
	        throw new StringException(msg);
	    }
	}
}

//
//StringValidator validator = new StringValidator();
//
//Object input = 123; // Beispiel: kein String
//
//try {
//    validator.validate(input);
//} catch (StringException e) {
//    System.out.println("Exception gefangen: " + e.getMessage());
//}
//
//if (!validator.isValid()) {
//    System.out.println("Fehler gefunden:");
//    for (String error : validator.getErrors()) {
//        System.out.println(error);
//    }
//    validator.saveErrorsToCsv();
//}
