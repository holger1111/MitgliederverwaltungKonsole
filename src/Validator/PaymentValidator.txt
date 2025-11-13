package Validator;

import Exception.PayException;
import Exception.PaymentDetailsException;
import Objekte.Mitglieder;
import Objekte.MitgliederVertrag;
import Objekte.Zahlung;
import Objekte.Zahlungsdaten;

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
            (!zahlungsart.equalsIgnoreCase("Überweisung") && 
             !zahlungsart.equalsIgnoreCase("SEPA-Lastschrift"))) {
            String msg = "Zahlungsart muss 'Überweisung' oder 'SEPA-Lastschrift' sein, ist aber '" + zahlungsart + "'";
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
