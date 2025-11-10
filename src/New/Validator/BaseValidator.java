package New.Validator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import New.Exception.TooLongException;
import New.Exception.TooShortException;
import New.Exception.NotAllNecessaryDataEnteredException;
import New.Exception.DataIsNullException;

public abstract class BaseValidator<T> {

    // Liste, um Fehler zu speichern
    protected List<String> errors = new ArrayList<>();

    // Validierungsmethode, kann beliebige Exceptions werfen
    public abstract void validate(T obj) throws Exception;

    // Ist die Eingabe gültig?
    public boolean isValid() {
        return errors.isEmpty();
    }

    // Fehler ausgeben
    public List<String> getErrors() {
        return errors;
    }

    // Wenn ein Wert null ist, Fehlermeldung und Exception
    protected void checkNotNull(Object value, String fieldName) throws DataIsNullException {
        if (value == null) {
            String msg = fieldName + " darf nicht null sein.";
            errors.add(msg);
            throw new DataIsNullException(msg);
        }
    }

    // Wenn ein String leer oder null ist, Fehlermeldung und Exception
    protected void checkNotEmpty(String value, String fieldName) throws NotAllNecessaryDataEnteredException {
        if (value == null || value.trim().isEmpty()) {
            String msg = fieldName + " darf nicht leer sein.";
            errors.add(msg);
            throw new NotAllNecessaryDataEnteredException(msg);
        }
    }

    // Wenn die Länge nicht passt, Fehlermeldung und Exception
    public void checkLength(String value, String fieldName, int min, int max) throws TooShortException, TooLongException {
        if (value != null) {
            int length = value.length();
            if (length < min) {
                String msg = fieldName + " ist zu kurz (Minimum: " + min + ")";
                errors.add(msg);
                throw new TooShortException(msg);
            }
            if (length > max) {
                String msg = fieldName + " ist zu lang (Maximum: " + max + ")";
                errors.add(msg);
                throw new TooLongException(msg);
            }
        }
    }

    // Fehler als CSV-Log abspeichern
    public void saveErrorsToCsv() {
        try (FileWriter writer = new FileWriter("Fehler-Log.txt", true)) {
            for (String error : errors) {
                writer.append("\"").append(error.replace("\"", "\"\"")).append("\"").append("\n");
            }
            writer.flush();
        } catch (IOException e) {
            System.err.println("Fehler beim Schreiben der Fehler-Log-Datei: " + e.getMessage());
        }
    }
}
