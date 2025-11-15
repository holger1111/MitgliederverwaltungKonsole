package Validator;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import Exception.DateException;
import Exception.EndDateException;
import Objekte.MitgliederVertrag;

/**
 * Validiert das Vertragsende anhand des Vertragsbeginns und der Laufzeit (Wochen).
 * Nutzt DateValidator als Basisklasse und prüft auch das Datumsformat.
 */
public class EndDateValidator extends DateValidator<MitgliederVertrag, EndDateException> {

    private int laufzeitWochen = 0;

    public EndDateValidator(int laufzeitWochen) {
        this.laufzeitWochen = laufzeitWochen;
    }

    @Override
    public void validate(MitgliederVertrag mv) throws EndDateException {
        errors.clear();
        if (mv == null) {
            String msg = "Vertrag darf nicht null sein!";
            errors.add(msg);
            throw new EndDateException(msg);
        }
        if (mv.getVertragsbeginn() == null) {
            String msg = "Vertragsbeginn darf nicht null sein!";
            errors.add(msg);
            throw new EndDateException(msg);
        }
        if (laufzeitWochen <= 0) {
            String msg = "Laufzeit muss größer 0 sein!";
            errors.add(msg);
            throw new EndDateException(msg);
        }

        // Formatvalidierung des Vertragsbeginns
        String beginnStr = new SimpleDateFormat("dd.MM.yyyy").format(mv.getVertragsbeginn());
        try {
            validateDateFormat(beginnStr); // Methode aus DateValidator
        } catch (DateException ex) {
            String msg = "Vertragsbeginn hat ungültiges Format: " + beginnStr;
            errors.add(msg);
            throw new EndDateException(msg);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(mv.getVertragsbeginn());
        cal.add(Calendar.WEEK_OF_YEAR, laufzeitWochen);
        Date autoEndDate = cal.getTime();

        // Falls ein Enddatum gesetzt ist, prüfe, ob es dem automatisch berechneten entspricht
        if (mv.getVertragsende() != null && !mv.getVertragsende().equals(autoEndDate)) {
            String msg = "Das Vertragsende wurde manuell gesetzt und stimmt nicht mit dem berechneten Termin überein!";
            errors.add(msg);
            throw new EndDateException(msg);
        }
        // Enddatum setzen, falls nicht gesetzt oder korrekt
        mv.setVertragsende(autoEndDate);
    }

    /**
     * Berechnet und gibt das Vertragsende zurück (nach Validierung)
     */
    public Date calculateEndDate(MitgliederVertrag mv) throws EndDateException {
        validate(mv);
        return mv.getVertragsende();
    }
}
