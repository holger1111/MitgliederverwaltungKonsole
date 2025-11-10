package New.Validator;

import New.Exception.IntException;

public class IntValidator extends BaseValidator<Object> {

    @Override
    public void validate(Object obj) throws IntException {
        errors.clear();
        if (!(obj instanceof Integer)) {
            String msg = "Eingabe ist keine ganze Zahl (Integer).";
            errors.add(msg);
            throw new IntException(msg);
        }
    }
}

//
//IntValidator validator = new IntValidator();
//
//Object input = 123; // oder beliebiger Input
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
//    System.out.println("Die Eingabe ist eine gültige ganze Zahl.");
//}
