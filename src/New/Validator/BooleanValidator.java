package New.Validator;

import New.Exception.BooleanException;

public class BooleanValidator extends BaseValidator<Object> {

    @Override
    public void validate(Object obj) throws BooleanException {
        errors.clear();
        if (!(obj instanceof Boolean)) {
            String msg = "Eingabe ist kein Boolean (true/false).";
            errors.add(msg);
            throw new BooleanException(msg);
        }
    }
}


//
//BooleanValidator validator = new BooleanValidator();
//
//Object input = true; // oder beliebiger Input
//
//validator.validate(input);
//
//if (!validator.isValid()) {
//    System.out.println("Fehler gefunden:");
//    for (String error : validator.getErrors()) {
//        System.out.println(error);
//    }
//    validator.saveErrorsToCsv();
//} else {
//    System.out.println("Die Eingabe ist ein gültiger Boolean.");
//}
