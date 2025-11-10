package New.Validator;

import New.Exception.DateException;
import New.Exception.TimeException;
import New.Helper.ZeitHelper;

public class DateTimeValidator extends DateValidator {

    @Override
    public void validate(Object obj) throws Exception {
        errors.clear();

        if (!(obj instanceof String)) {
            String msg = "Eingabe ist kein String.";
            errors.add(msg);
            throw new DateException(msg);
        }

        String input = (String) obj;
        String[] parts = input.split(" ");
        if (parts.length != 2) {
            String msg = "Format muss 'TT.MM.JJJJ HH:mm:ss' sein.";
            errors.add(msg);
            throw new DateException(msg);
        }

     // Datumsteil validieren (mit DateException)
        try {
            super.validate(parts[0]);
        } catch (DateException e) {
            errors.add(e.getMessage());
            throw e;
        }


        // Zeitteil validieren
        String[] timeParts = parts[1].split(":");
        if (timeParts.length != 3) {
            String msg = "Uhrzeit muss im Format HH:mm:ss sein.";
            errors.add(msg);
            throw new TimeException(msg);
        }

        try {
            int stunde = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            int sekunde = Integer.parseInt(timeParts[2]);

            if (!ZeitHelper.istStunde(stunde)) {
                String msg = "Ungültige Stunde: " + stunde;
                errors.add(msg);
                throw new TimeException(msg);
            }
            if (!ZeitHelper.istMinute(minute)) {
                String msg = "Ungültige Minute: " + minute;
                errors.add(msg);
                throw new TimeException(msg);
            }
            if (!ZeitHelper.istSekunde(sekunde)) {
                String msg = "Ungültige Sekunde: " + sekunde;
                errors.add(msg);
                throw new TimeException(msg);
            }
        } catch (NumberFormatException e) {
            String msg = "Uhrzeit enthält keine gültigen Zahlenwerte.";
            errors.add(msg);
            throw new TimeException(msg);
        }
    }
}

//
//DateTimeValidator validator = new DateTimeValidator();
//
//validator.validate("03.11.2025 09:25:30");
//
//if (!validator.isValid()) {
//    for (String error : validator.getErrors()) {
//        System.out.println(error);
//    }
//    validator.saveErrorsToCsv();
//} else {
//    System.out.println("Datum und Uhrzeit sind gültig.");
//}
