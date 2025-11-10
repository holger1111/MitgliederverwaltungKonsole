package New.Objekte;


import New.Helper.Datum;
import New.Helper.StripEntry;
import New.Validator.BooleanValidator;
import New.Validator.CurrencyValidator;
import New.Validator.DiscountValidator;
import New.Validator.EndDateValidator;
import New.Validator.PaymentValidator;
import New.Validator.StringValidator;

import java.util.Date;
import java.util.Objects;

public class MitgliederVertrag {
    private int vertragNr;
    private int mitgliederID;
    private int vertragID;
    private Date vertragsbeginn;
    private Date vertragsende;
    private boolean verlängerung;
    private boolean aktiv;
    private boolean gekündigt;
    private Double preisrabatt;
    private int intervallID;
    private int zahlungID;
    private Date trainingsbeginn;
    private String kommentar;

    public MitgliederVertrag() {}

    public MitgliederVertrag(
            int vertragNr, int mitgliederID, int vertragID, Date vertragsbeginn, Date vertragsende,
            boolean verlängerung, boolean aktiv, boolean gekündigt, Double preisrabatt,
            int intervallID, int zahlungID, Date trainingsbeginn, String kommentar) {
        this.vertragNr = vertragNr;
        this.mitgliederID = mitgliederID;
        this.vertragID = vertragID;
        this.vertragsbeginn = vertragsbeginn;
        this.vertragsende = vertragsende;
        this.verlängerung = verlängerung;
        this.aktiv = aktiv;
        this.gekündigt = gekündigt;
        this.preisrabatt = preisrabatt;
        this.intervallID = intervallID;
        this.zahlungID = zahlungID;
        this.trainingsbeginn = trainingsbeginn;
        this.kommentar = kommentar;
    }

    // Getter & Setter

    public int getVertragNr() { return vertragNr; }
    public void setVertragNr(int vertragNr) { this.vertragNr = vertragNr; }
    public int getMitgliederID() { return mitgliederID; }
    public void setMitgliederID(int mitgliederID) { this.mitgliederID = mitgliederID; }
    public int getVertragID() { return vertragID; }
    public void setVertragID(int vertragID) { this.vertragID = vertragID; }
    public Date getVertragsbeginn() { return vertragsbeginn; }
    public void setVertragsbeginn(Date vertragsbeginn) { this.vertragsbeginn = vertragsbeginn; }
    public Date getVertragsende() { return vertragsende; }
    public void setVertragsende(Date vertragsende) { this.vertragsende = vertragsende; }
    public boolean isVerlängerung() { return verlängerung; }
    public void setVerlängerung(boolean verlängerung) { this.verlängerung = verlängerung; }
    public boolean isAktiv() { return aktiv; }
    public void setAktiv(boolean aktiv) { this.aktiv = aktiv; }
    public boolean isGekündigt() { return gekündigt; }
    public void setGekündigt(boolean gekündigt) { this.gekündigt = gekündigt; }
    public Double getPreisrabatt() { return preisrabatt; }
    public void setPreisrabatt(Double preisrabatt) { this.preisrabatt = preisrabatt; }
    public int getIntervallID() { return intervallID; }
    public void setIntervallID(int intervallID) { this.intervallID = intervallID; }
    public int getZahlungID() { return zahlungID; }
    public void setZahlungID(int zahlungID) { this.zahlungID = zahlungID; }
    public Date getTrainingsbeginn() { return trainingsbeginn; }
    public void setTrainingsbeginn(Date trainingsbeginn) { this.trainingsbeginn = trainingsbeginn; }
    public String getKommentar() { return kommentar; }
    public void setKommentar(String kommentar) { this.kommentar = kommentar; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MitgliederVertrag)) return false;
        MitgliederVertrag that = (MitgliederVertrag) o;
        return vertragNr == that.vertragNr;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertragNr);
    }

    @Override
    public String toString() {
        String vertragsbeginnStr = vertragsbeginn != null ? new Datum(vertragsbeginn).toString() : "-";
        String vertragsendeStr = vertragsende != null ? new Datum(vertragsende).toString() : "-";
        String trainingsbeginnStr = trainingsbeginn != null ? new Datum(trainingsbeginn).toString() : "-";
        String kommentarClean = kommentar != null ? StripEntry.clean(kommentar) : "-";

        String status = verlängerung ? "verlängert" : "nicht verlängert";
        status += aktiv ? ", aktiv" : ", nicht aktiv";
        status += gekündigt ? ", gekündigt" : ", nicht gekündigt";

        return String.format(
            "MitgliederVertrag:\n"
            + "Nr: %d | MitgliedID: %d | VertragID: %d\n"
            + "Vertragsbeginn: %s | Vertragsende: %s\n"
            + "Trainingsbeginn: %s\n"
            + "Status: %s\n"
            + "Preisrabatt: %.2f | IntervallID: %d | ZahlungID: %d\n"
            + "Kommentar: %s",
            vertragNr,
            mitgliederID,
            vertragID,
            vertragsbeginnStr,
            vertragsendeStr,
            trainingsbeginnStr,
            status,
            preisrabatt != null ? preisrabatt : 0.0,
            intervallID,
            zahlungID,
            kommentarClean
        );
    }


    public void validateAll(
        Vertrag vertrag,
        Zahlung zahlung,
        Mitglieder mitglied,
        int laufzeitWochen
    ) throws Exception {
        EndDateValidator endDateValidator = new EndDateValidator(laufzeitWochen);
        endDateValidator.validate(this);

        DiscountValidator discountValidator = new DiscountValidator(vertrag);
        discountValidator.validate(this);

        CurrencyValidator currencyValidator = new CurrencyValidator();
        currencyValidator.validate(this.preisrabatt);
        this.preisrabatt = currencyValidator.getValidatedValue();

        PaymentValidator paymentValidator = new PaymentValidator(zahlung, mitglied);
        paymentValidator.validate(this);

        BooleanValidator boolValidator = new BooleanValidator();
        boolValidator.validate(this.aktiv);
        boolValidator.validate(this.gekündigt);
        boolValidator.validate(this.verlängerung);

        StringValidator stringValidator = new StringValidator();
        stringValidator.checkLength(this.kommentar, "Kommentar", 0, 500);
    }
}
