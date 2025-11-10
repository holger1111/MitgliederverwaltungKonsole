package New.Objekte;

public class Kurs {
    private int kursID;
    private String bezeichnung;
    private int anzahlTermine;
    private double preis;
    private String kommentar;

    // Konstruktoren
    public Kurs() {
    }

    public Kurs(int kursID, String bezeichnung, int anzahlTermine, double preis, String kommentar) {
        this.kursID = kursID;
        this.bezeichnung = bezeichnung;
        this.anzahlTermine = anzahlTermine;
        this.preis = preis;
        this.kommentar = kommentar;
    }

    // Getter und Setter
    public int getKursID() {
        return kursID;
    }

    public void setKursID(int kursID) {
        this.kursID = kursID;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public int getAnzahlTermine() {
        return anzahlTermine;
    }

    public void setAnzahlTermine(int anzahlTermine) {
        this.anzahlTermine = anzahlTermine;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    @Override
    public String toString() {
        return "Kurs{" +
                "kursID=" + kursID +
                ", bezeichnung='" + bezeichnung + '\'' +
                ", anzahlTermine=" + anzahlTermine +
                ", preis=" + preis +
                ", kommentar='" + kommentar + '\'' +
                '}';
    }
}
