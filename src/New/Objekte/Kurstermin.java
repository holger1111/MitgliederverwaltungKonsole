package New.Objekte;

import java.sql.Timestamp;

public class Kurstermin {
    private int kursterminID;
    private int kursID;
    private int trainerID;
    private Timestamp termin;
    private int trainerfrei;
    private boolean aktiv;
    private String kommentar;

    // Konstruktoren
    public Kurstermin() {
    }

    public Kurstermin(int kursterminID, int kursID, int trainerID, Timestamp termin, 
                      int trainerfrei, boolean aktiv, String kommentar) {
        this.kursterminID = kursterminID;
        this.kursID = kursID;
        this.trainerID = trainerID;
        this.termin = termin;
        this.trainerfrei = trainerfrei;
        this.aktiv = aktiv;
        this.kommentar = kommentar;
    }

    // Getter und Setter
    public int getKursterminID() {
        return kursterminID;
    }

    public void setKursterminID(int kursterminID) {
        this.kursterminID = kursterminID;
    }

    public int getKursID() {
        return kursID;
    }

    public void setKursID(int kursID) {
        this.kursID = kursID;
    }

    public int getTrainerID() {
        return trainerID;
    }

    public void setTrainerID(int trainerID) {
        this.trainerID = trainerID;
    }

    public Timestamp getTermin() {
        return termin;
    }

    public void setTermin(Timestamp termin) {
        this.termin = termin;
    }

    public int getTrainerfrei() {
        return trainerfrei;
    }

    public void setTrainerfrei(int trainerfrei) {
        this.trainerfrei = trainerfrei;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    @Override
    public String toString() {
        return "Kurstermin{" +
                "kursterminID=" + kursterminID +
                ", kursID=" + kursID +
                ", trainerID=" + trainerID +
                ", termin=" + termin +
                ", trainerfrei=" + trainerfrei +
                ", aktiv=" + aktiv +
                ", kommentar='" + kommentar + '\'' +
                '}';
    }
}
