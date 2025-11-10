package New.Validator;

import New.Objekte.MitgliederVertrag;
import New.Exception.DataIsNullException;
import New.Exception.EndDateException;

import java.util.Calendar;
import java.util.Date;

public class EndDateValidator extends BaseValidator<MitgliederVertrag> {

    private int laufzeitWochen = 0;

    public EndDateValidator(int laufzeitWochen) {
        this.laufzeitWochen = laufzeitWochen;
    }

    @Override
    public void validate(MitgliederVertrag mv) throws Exception {
        checkNotNull(mv.getVertragsbeginn(), "Vertragsbeginn");
        if (laufzeitWochen <= 0) {
            String msg = "Laufzeit muss größer 0 sein!";
            errors.add(msg);
            throw new DataIsNullException(msg);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(mv.getVertragsbeginn());
        cal.add(Calendar.WEEK_OF_YEAR, laufzeitWochen);
        Date autoEndDate = cal.getTime();

        if (mv.getVertragsende() != null && !mv.getVertragsende().equals(autoEndDate)) {
            String msg = "Das Vertragsende wurde manuell gesetzt und stimmt nicht mit berechnetem Termin überein!";
            errors.add(msg);
            throw new EndDateException(msg);
        }
        mv.setVertragsende(autoEndDate);
    }

    public Date calculateEndDate(MitgliederVertrag mv) throws Exception {
        validate(mv);
        return mv.getVertragsende();
    }
}
