package New.Validator;

import New.Exception.DateException;
import New.Helper.DatumHelper;

public class DateValidator extends BaseValidator<Object> {

    @Override
    public void validate(Object obj) throws Exception {
        errors.clear();

        if (!(obj instanceof String)) {
            String msg = "Eingabe ist kein String.";
            errors.add(msg);
            throw new DateException(msg);
        }

        String dateStr = (String) obj;
        String[] parts = dateStr.split("\\.");
        if (parts.length != 3) {
            String msg = "Datum muss im Format TT.MM.JJJJ sein.";
            errors.add(msg);
            throw new DateException(msg);
        }

        try {
            int tag = Integer.parseInt(parts[0]);
            int monat = Integer.parseInt(parts[1]);
            int jahr = Integer.parseInt(parts[2]);

            if (!DatumHelper.istJahr(jahr)) {
                String msg = "Ungültiges Jahr.";
                errors.add(msg);
                throw new DateException(msg);
            }
            if (!DatumHelper.istMonat(monat)) {
                String msg = "Ungültiger Monat.";
                errors.add(msg);
                throw new DateException(msg);
            }
            if (!DatumHelper.istTag(jahr, monat, tag)) {
                String msg = "Ungültiger Tag.";
                errors.add(msg);
                throw new DateException(msg);
            }
        } catch (NumberFormatException e) {
            String msg = "Datum enthält keine gültigen Zahlenwerte.";
            errors.add(msg);
            throw new DateException(msg);
        }
    }
}


//
//DateValidator validator = new DateValidator();
//
//validator.validate("29.02.2025");
//
//if (!validator.isValid()) {
//    for (String error : validator.getErrors()) {
//        System.out.println(error);
//    }
//    validator.saveErrorsToCsv();
//} else {
//    System.out.println("Das Datum ist gültig.");
//}
