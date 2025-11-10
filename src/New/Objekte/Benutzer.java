package New.Objekte;

public class Benutzer {
    private int benutzerID;
    private String benutzername;
    private String passwort;
    private Rolle rolle;

    public Benutzer() {}

    public Benutzer(int benutzerID, String benutzername, String passwort, Rolle rolle) {
        this.benutzerID = benutzerID;
        this.benutzername = benutzername;
        this.passwort = passwort;
        this.rolle = rolle;
    }

    public int getBenutzerID() { return benutzerID; }
    public void setBenutzerID(int benutzerID) { this.benutzerID = benutzerID; }

    public String getBenutzername() { return benutzername; }
    public void setBenutzername(String benutzername) { this.benutzername = benutzername; }

    public String getPasswort() { return passwort; }
    public void setPasswort(String passwort) { this.passwort = passwort; }

    public Rolle getRolle() { return rolle; }
    public void setRolle(Rolle rolle) { this.rolle = rolle; }

    @Override
    public String toString() {
        return "Benutzer{" +
                "benutzerID=" + benutzerID +
                ", benutzername='" + benutzername + '\'' +
                ", passwort='" + passwort + '\'' +
                ", rolle=" + (rolle != null ? rolle.getBezeichnung() : "-") +
                '}';
    }
}
