package Objekte;

import java.sql.Timestamp;

public class Kursleitung {
    private int kursterminID;
    private int mitarbeiterID;
    private boolean bestätigt;
    private Timestamp bestätigungszeit;
    private boolean abgemeldet;
    private Timestamp abmeldezeit;
    private boolean aktiv;
    private String kommentar;

    public Kursleitung() {}

    public Kursleitung(int kursterminID, int mitarbeiterID, boolean bestätigt, Timestamp bestätigungszeit,
                       boolean abgemeldet, Timestamp abmeldezeit, boolean aktiv, String kommentar) {
        this.kursterminID = kursterminID;
        this.mitarbeiterID = mitarbeiterID;
        this.bestätigt = bestätigt;
        this.bestätigungszeit = bestätigungszeit;
        this.abgemeldet = abgemeldet;
        this.abmeldezeit = abmeldezeit;
        this.aktiv = aktiv;
        this.kommentar = kommentar;
    }

    public int getKursterminID() { return kursterminID; }
    public void setKursterminID(int kursterminID) { this.kursterminID = kursterminID; }

    public int getMitarbeiterID() { return mitarbeiterID; }
    public void setMitarbeiterID(int mitarbeiterID) { this.mitarbeiterID = mitarbeiterID; }

    public boolean isBestätigt() { return bestätigt; }
    public void setBestätigt(boolean bestätigt) { this.bestätigt = bestätigt; }

    public Timestamp getBestätigungszeit() { return bestätigungszeit; }
    public void setBestätigungszeit(Timestamp bestätigungszeit) { this.bestätigungszeit = bestätigungszeit; }

    public boolean isAbgemeldet() { return abgemeldet; }
    public void setAbgemeldet(boolean abgemeldet) { this.abgemeldet = abgemeldet; }

    public Timestamp getAbmeldezeit() { return abmeldezeit; }
    public void setAbmeldezeit(Timestamp abmeldezeit) { this.abmeldezeit = abmeldezeit; }

    public boolean isAktiv() { return aktiv; }
    public void setAktiv(boolean aktiv) { this.aktiv = aktiv; }

    public String getKommentar() { return kommentar; }
    public void setKommentar(String kommentar) { this.kommentar = kommentar; }

    @Override
    public String toString() {
        return "Kursleitung{" +
                "kursterminID=" + kursterminID +
                ", mitarbeiterID=" + mitarbeiterID +
                ", bestätigt=" + bestätigt +
                ", bestätigungszeit=" + bestätigungszeit +
                ", abgemeldet=" + abgemeldet +
                ", abmeldezeit=" + abmeldezeit +
                ", aktiv=" + aktiv +
                ", kommentar='" + kommentar + '\'' +
                '}';
    }
}
