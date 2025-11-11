package Objekte;

public class Artikel {
    private int artikelID;
    private String name;
    private double einzelpreis;
    private String kommentar;
    private Kategorie kategorie; // Änderung: Kategorie als Objekt

    // Konstruktor
    public Artikel(int artikelID, String name, double einzelpreis, String kommentar, Kategorie kategorie) {
        this.artikelID = artikelID;
        this.name = name;
        this.einzelpreis = einzelpreis;
        this.kommentar = kommentar;
        this.kategorie = kategorie;
    }

    // Leerer Konstruktor
    public Artikel() {
    }

    // Getter und Setter
    public int getArtikelID() { return artikelID; }
    public void setArtikelID(int artikelID) { this.artikelID = artikelID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getEinzelpreis() { return einzelpreis; }
    public void setEinzelpreis(double einzelpreis) { this.einzelpreis = einzelpreis; }

    public String getKommentar() { return kommentar; }
    public void setKommentar(String kommentar) { this.kommentar = kommentar; }

    public Kategorie getKategorie() { return kategorie; }
    public void setKategorie(Kategorie kategorie) { this.kategorie = kategorie; }

    @Override
    public String toString() {
        return "Artikel{" +
            "artikelID=" + artikelID +
            ", name='" + name + '\'' +
            ", einzelpreis=" + einzelpreis +
            ", kommentar='" + kommentar + '\'' +
            ", kategorie=" + (kategorie != null ? kategorie.getBezeichnung() : null) +
            '}';
    }
}
