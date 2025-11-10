package New.Objekte;

import java.util.Objects;
import New.Validator.IntValidator;
import New.Validator.StringValidator;

public class Intervall {
    private int intervallID;
    private String zahlungsintervall;
    private String bezeichnung;

    public Intervall() {}

    public Intervall(int intervallID, String zahlungsintervall, String bezeichnung) {
        this.intervallID = intervallID;
        this.zahlungsintervall = zahlungsintervall;
        this.bezeichnung = bezeichnung;
    }

    public Intervall(String zahlungsintervall, String bezeichnung) {
        this.zahlungsintervall = zahlungsintervall;
        this.bezeichnung = bezeichnung;
    }

    public int getIntervallID() {
        return intervallID;
    }
    public void setIntervallID(int intervallID) {
        this.intervallID = intervallID;
    }
    public String getZahlungsintervall() {
        return zahlungsintervall;
    }
    public void setZahlungsintervall(String zahlungsintervall) {
        this.zahlungsintervall = zahlungsintervall;
    }
    public String getBezeichnung() {
        return bezeichnung;
    }
    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Intervall)) return false;
        Intervall that = (Intervall) o;
        return intervallID == that.intervallID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(intervallID);
    }

    @Override
    public String toString() {
        return String.format("Intervall:\nID: %d\nZahlungsintervall: %s\nBezeichnung: %s", 
            intervallID, zahlungsintervall, bezeichnung);
    }
    
    public void validateAll() throws Exception {
        IntValidator intVal = new IntValidator();
        intVal.validate(intervallID);

        StringValidator strVal = new StringValidator();
        strVal.checkLength(bezeichnung, "Bezeichnung", 1, 100);
    }
}
