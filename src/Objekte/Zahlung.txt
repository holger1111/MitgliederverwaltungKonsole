package Objekte;

import java.util.Objects;

import OUTDATED.OUT_IntValidator;
import OUTDATED.OUT_StringValidator;

public class Zahlung {
    private int zahlungID;
    private String zahlungsart;

    public Zahlung() {}

    public Zahlung(int zahlungID, String zahlungsart) {
        this.zahlungID = zahlungID;
        this.zahlungsart = zahlungsart;
    }

    public Zahlung(String zahlungsart) {
        this.zahlungsart = zahlungsart;
    }

    public int getZahlungID() {
        return zahlungID;
    }
    public void setZahlungID(int zahlungID) {
        this.zahlungID = zahlungID;
    }
    public String getZahlungsart() {
        return zahlungsart;
    }
    public void setZahlungsart(String zahlungsart) {
        this.zahlungsart = zahlungsart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zahlung)) return false;
        Zahlung that = (Zahlung) o;
        return zahlungID == that.zahlungID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(zahlungID);
    }

    @Override
    public String toString() {
        return String.format("Zahlung:\nID: %d\nArt: %s", zahlungID, zahlungsart);
    }
    
    public void validateAll() throws Exception {
        OUT_IntValidator intVal = new OUT_IntValidator();
        intVal.validate(zahlungID);

        OUT_StringValidator strVal = new OUT_StringValidator();
        strVal.checkLength(zahlungsart, "Zahlungsart", 1, 50);
    }
}
