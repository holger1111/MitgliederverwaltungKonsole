package New.Objekte;

import java.util.Objects;

public class Interessenten {

    // Attribute
    private int mitgliederID;
    private String vorname;
    private String nachname;
    private String telefon;

    // Konstruktor
    public Interessenten() { }

    public Interessenten(String vorname, String nachname, String telefon) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.telefon = telefon;
    }

    public Interessenten(int mitgliederID, String vorname, String nachname, String telefon) {
        this.mitgliederID = mitgliederID;
        this.vorname = vorname;
        this.nachname = nachname;
        this.telefon = telefon;
    }

    // Getter & Setter
    public int getMitgliederID() {
        return mitgliederID;
    }

    public void setMitgliederID(int mitgliederID) {
        this.mitgliederID = mitgliederID;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    // Override
    @Override
    public String toString() {
        return "Interessent:\nMitgliederID: " + mitgliederID + "\nName: " + vorname + " " + nachname + "\nTelefon: " + telefon + "\n";
    }

    @Override
    public int hashCode() {
        return Objects.hash(mitgliederID);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Interessenten)) return false;
        Interessenten other = (Interessenten) obj;
        return this.mitgliederID == other.mitgliederID;
    }
}
