package New.Validator;

import New.Objekte.MitgliederVertrag;
import New.Objekte.Zahlung;
import New.Objekte.Mitglieder;
import New.Objekte.Zahlungsdaten;
import New.Exception.PayException;
import New.Exception.PaymentDetailsException;

public class PaymentValidator extends BaseValidator<MitgliederVertrag> {

    private Zahlung zahlung;
    private Mitglieder mitglied;

    public PaymentValidator(Zahlung zahlung, Mitglieder mitglied) {
        this.zahlung = zahlung;
        this.mitglied = mitglied;
    }

    @Override
    public void validate(MitgliederVertrag mv) throws Exception {
        // Prüfe Zahlungsart "Abbuchung" ODER "SEPA-Lastschrift"
        String zahlungsart = zahlung.getZahlungsart();
        if (zahlungsart == null || 
            (!zahlungsart.equalsIgnoreCase("Abbuchung") && 
             !zahlungsart.equalsIgnoreCase("SEPA-Lastschrift"))) {
            String msg = "Zahlungsart muss 'Abbuchung' oder 'SEPA-Lastschrift' sein, ist aber '" + zahlungsart + "'";
            errors.add(msg);
            throw new PayException(msg);
        }

        // Prüfe Zahlungsdaten
        Zahlungsdaten zahlungsdaten = mitglied.getZahlungsdaten();
        if (zahlungsdaten == null
                || zahlungsdaten.getName() == null || zahlungsdaten.getName().trim().isEmpty()
                || zahlungsdaten.getIBAN() == null || zahlungsdaten.getIBAN().trim().isEmpty()
                || zahlungsdaten.getBIC() == null || zahlungsdaten.getBIC().trim().isEmpty()) {
            String msg = "Zahlungsdaten unvollständig für Mitglied (" + mitglied.getMitgliederID() + ")";
            errors.add(msg);
            throw new PaymentDetailsException(msg);
        }
    }
}
