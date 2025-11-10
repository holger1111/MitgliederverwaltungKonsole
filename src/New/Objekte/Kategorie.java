package New.Objekte;

public class Kategorie {
    private int kategorieID;
    private String bezeichnung;

    // Konstruktor
    public Kategorie(int kategorieID, String bezeichnung) {
        this.kategorieID = kategorieID;
        this.bezeichnung = bezeichnung;
    }

    // Leerer Konstruktor
    public Kategorie() {
    }

    // Getter und Setter
    public int getKategorieID() { return kategorieID; }
    public void setKategorieID(int kategorieID) { this.kategorieID = kategorieID; }

    public String getBezeichnung() { return bezeichnung; }
    public void setBezeichnung(String bezeichnung) { this.bezeichnung = bezeichnung; }

    @Override
    public String toString() {
        return "Kategorie{" +
            "kategorieID=" + kategorieID +
            ", bezeichnung='" + bezeichnung + '\'' +
            '}';
    }
}
