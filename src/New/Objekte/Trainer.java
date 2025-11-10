package New.Objekte;

public class Trainer {
    private int trainerID;
    private String vorname;
    private String nachname;
    private String kommentar;

    // Konstruktoren
    public Trainer() {
    }

    public Trainer(int trainerID, String vorname, String nachname, String kommentar) {
        this.trainerID = trainerID;
        this.vorname = vorname;
        this.nachname = nachname;
        this.kommentar = kommentar;
    }

    // Getter und Setter
    public int getTrainerID() {
        return trainerID;
    }

    public void setTrainerID(int trainerID) {
        this.trainerID = trainerID;
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

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "trainerID=" + trainerID +
                ", vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", kommentar='" + kommentar + '\'' +
                '}';
    }
}
