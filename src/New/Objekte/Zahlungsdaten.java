package New.Objekte;

import java.util.Objects;

public class Zahlungsdaten {

    // Attribute
    private int zahlungsdatenID;
    private String name;
    private String iban;
    private String bic;

    // Konstruktor
    public Zahlungsdaten() {
    }

    public Zahlungsdaten(String name, String iban, String bic) {
        this.name = name;
        this.iban = iban;
        this.bic = bic;
    }

    public Zahlungsdaten(int zahlungsdatenID, String name, String iban, String bic) {
        this.zahlungsdatenID = zahlungsdatenID;
        this.name = name;
        this.iban = iban;
        this.bic = bic;
    }

    // Setter & Getter
    public int getZahlungsdatenID() {
        return zahlungsdatenID;
    }

    public void setZahlungsdatenID(int zahlungsdatenID) {
        this.zahlungsdatenID = zahlungsdatenID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIBAN() {
        return iban;
    }

    public void setIBAN(String iban) {
        this.iban = iban;
    }

    public String getBIC() {
        return bic;
    }

    public void setBIC(String bic) {
        this.bic = bic;
    }

    // Override
    @Override
    public String toString() {
        return "Zahlungsdaten:\nZahlungsdatenID: " + zahlungsdatenID +
               "\nName: " + name +
               "\nIBAN: " + iban +
               "\nBIC: " + bic +
               "\n";
    }

    @Override
    public int hashCode() {
        return Objects.hash(zahlungsdatenID, name, iban, bic);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Zahlungsdaten)) return false;
        Zahlungsdaten other = (Zahlungsdaten) obj;
        return zahlungsdatenID == other.zahlungsdatenID &&
               Objects.equals(name, other.name) &&
               Objects.equals(iban, other.iban) &&
               Objects.equals(bic, other.bic);
    }
}
