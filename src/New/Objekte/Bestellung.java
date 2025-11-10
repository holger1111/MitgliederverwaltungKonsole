package New.Objekte;

import java.sql.Timestamp;

public class Bestellung {
    private int bestellungID;
    private int mitgliederID;
    private double gesamtpreis;
    private Timestamp bestelldatum;
    private int zahlungID;

    // Konstruktoren
    public Bestellung() {
    }

    public Bestellung(int bestellungID, int mitgliederID, double gesamtpreis, 
                      Timestamp bestelldatum, int zahlungID) {
        this.bestellungID = bestellungID;
        this.mitgliederID = mitgliederID;
        this.gesamtpreis = gesamtpreis;
        this.bestelldatum = bestelldatum;
        this.zahlungID = zahlungID;
    }

    // Getter und Setter
    public int getBestellungID() {
        return bestellungID;
    }

    public void setBestellungID(int bestellungID) {
        this.bestellungID = bestellungID;
    }

    public int getMitgliederID() {
        return mitgliederID;
    }

    public void setMitgliederID(int mitgliederID) {
        this.mitgliederID = mitgliederID;
    }

    public double getGesamtpreis() {
        return gesamtpreis;
    }

    public void setGesamtpreis(double gesamtpreis) {
        this.gesamtpreis = gesamtpreis;
    }

    public Timestamp getBestelldatum() {
        return bestelldatum;
    }

    public void setBestelldatum(Timestamp bestelldatum) {
        this.bestelldatum = bestelldatum;
    }

    public int getZahlungID() {
        return zahlungID;
    }

    public void setZahlungID(int zahlungID) {
        this.zahlungID = zahlungID;
    }

    @Override
    public String toString() {
        return "Bestellung{" +
                "bestellungID=" + bestellungID +
                ", mitgliederID=" + mitgliederID +
                ", gesamtpreis=" + gesamtpreis +
                ", bestelldatum=" + bestelldatum +
                ", zahlungID=" + zahlungID +
                '}';
    }
}
