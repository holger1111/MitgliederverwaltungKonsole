package New.Objekte;

import java.sql.Timestamp;

public class Kurstermin {
    private int kursterminID;
    private int kursID;
    private Timestamp termin;
    private int teilnehmerfrei;
    private boolean anmeldebar;
    private boolean aktiv;
    private String kommentar;

    // Konstruktoren
    public Kurstermin() {}

    public Kurstermin(int kursterminID, int kursID, Timestamp termin,
                      int teilnehmerfrei, boolean anmeldebar, boolean aktiv, String kommentar) {
        this.kursterminID = kursterminID;
        this.kursID = kursID;
        this.termin = termin;
        this.teilnehmerfrei = teilnehmerfrei;
        this.anmeldebar = anmeldebar;
        this.aktiv = aktiv;
        this.kommentar = kommentar;
    }

    public int getKursterminID() { return kursterminID; }
    public void setKursterminID(int kursterminID) { this.kursterminID = kursterminID; }

    public int getKursID() { return kursID; }
    public void setKursID(int kursID) { this.kursID = kursID; }

    public Timestamp getTermin() { return termin; }
    public void setTermin(Timestamp termin) { this.termin = termin; }

    public int getTeilnehmerfrei() { return teilnehmerfrei; }
    public void setTeilnehmerfrei(int teilnehmerfrei) { this.teilnehmerfrei = teilnehmerfrei; }

    public boolean isAnmeldebar() { return anmeldebar; }
    public void setAnmeldebar(boolean anmeldebar) { this.anmeldebar = anmeldebar; }

    public boolean isAktiv() { return aktiv; }
    public void setAktiv(boolean aktiv) { this.aktiv = aktiv; }

    public String getKommentar() { return kommentar; }
    public void setKommentar(String kommentar) { this.kommentar = kommentar; }

    @Override
    public String toString() {
        return "Kurstermin{" +
                "kursterminID=" + kursterminID +
                ", kursID=" + kursID +
                ", termin=" + termin +
                ", teilnehmerfrei=" + teilnehmerfrei +
                ", anmeldebar=" + anmeldebar +
                ", aktiv=" + aktiv +
                ", kommentar='" + kommentar + '\'' +
                '}';
    }
}
