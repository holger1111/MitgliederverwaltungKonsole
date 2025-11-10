package New.Objekte;

import java.util.Objects;
import New.Validator.IntValidator;
import New.Validator.StringValidator;

public class Vertrag {
    private int vertragID;
    private String bezeichnung;
    private int laufzeit;
    private double grundpreis;

    public Vertrag() {
    }

    public Vertrag(int vertragID, String bezeichnung, int laufzeit, double grundpreis) {
        this.vertragID = vertragID;
        this.bezeichnung = bezeichnung;
        this.laufzeit = laufzeit;
        this.grundpreis = grundpreis;
    }

    public Vertrag(String bezeichnung, int laufzeit, double grundpreis) {
        this.bezeichnung = bezeichnung;
        this.laufzeit = laufzeit;
        this.grundpreis = grundpreis;
    }

    public int getVertragID() {
        return vertragID;
    }

    public void setVertragID(int vertragID) {
        this.vertragID = vertragID;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public int getLaufzeit() {
        return laufzeit;
    }

    public void setLaufzeit(int laufzeit) {
        this.laufzeit = laufzeit;
    }

    public double getGrundpreis() {
        return grundpreis;
    }

    public void setGrundpreis(double grundpreis) {
        this.grundpreis = grundpreis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Vertrag))
            return false;
        Vertrag that = (Vertrag) o;
        return vertragID == that.vertragID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertragID);
    }

    @Override
    public String toString() {
        return String.format(
            "Vertrag:\nVertragID: %d\nBezeichnung: %s\nLaufzeit: %d\nGrundpreis: %.2f €\n",
            vertragID, bezeichnung, laufzeit, grundpreis
        );
    }

    public void validateAll() throws Exception {
        IntValidator intVal = new IntValidator();
        intVal.validate(vertragID);
        intVal.validate(laufzeit);

        StringValidator strVal = new StringValidator();
        strVal.checkLength(bezeichnung, "Bezeichnung", 1, 100);
    }
}
