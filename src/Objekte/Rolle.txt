package Objekte;

public class Rolle {
    private int rolleID;
    private String bezeichnung;
    private String kommentar;

    public Rolle() {}

    public Rolle(int rolleID, String bezeichnung, String kommentar) {
        this.rolleID = rolleID;
        this.bezeichnung = bezeichnung;
        this.kommentar = kommentar;
    }

    public int getRolleID() { return rolleID; }
    public void setRolleID(int rolleID) { this.rolleID = rolleID; }

    public String getBezeichnung() { return bezeichnung; }
    public void setBezeichnung(String bezeichnung) { this.bezeichnung = bezeichnung; }

    public String getKommentar() { return kommentar; }
    public void setKommentar(String kommentar) { this.kommentar = kommentar; }

    @Override
    public String toString() {
        return "Rolle{" +
                "rolleID=" + rolleID +
                ", bezeichnung='" + bezeichnung + '\'' +
                ", kommentar='" + kommentar + '\'' +
                '}';
    }
}
