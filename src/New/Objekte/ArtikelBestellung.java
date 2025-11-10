package New.Objekte;

public class ArtikelBestellung {
    private int bestellungID;
    private int artikelID;
    private int menge;
    private double aufaddiert;

    public ArtikelBestellung() {
    }

    public ArtikelBestellung(int bestellungID, int artikelID, int menge, double aufaddiert) {
        this.bestellungID = bestellungID;
        this.artikelID = artikelID;
        this.menge = menge;
        this.aufaddiert = aufaddiert;
    }

    // Getter und Setter
    public int getBestellungID() { return bestellungID; }
    public void setBestellungID(int bestellungID) { this.bestellungID = bestellungID; }

    public int getArtikelID() { return artikelID; }
    public void setArtikelID(int artikelID) { this.artikelID = artikelID; }

    public int getMenge() { return menge; }
    public void setMenge(int menge) { this.menge = menge; }

    public double getAufaddiert() { return aufaddiert; }
    public void setAufaddiert(double aufaddiert) { this.aufaddiert = aufaddiert; }

    @Override
    public String toString() {
        return "ArtikelBestellung{" +
            "bestellungID=" + bestellungID +
            ", artikelID=" + artikelID +
            ", menge=" + menge +
            ", aufaddiert=" + aufaddiert +
            '}';
    }
}
