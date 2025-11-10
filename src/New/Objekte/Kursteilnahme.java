package New.Objekte;

import java.sql.Timestamp;

public class Kursteilnahme {
    
    // Zusammengesetzter Primärschlüssel
    private int mitgliederID;
    private int kursterminID;
    
    // Weitere Felder
    private boolean angemeldet;
    private Timestamp anmeldezeit;
    private boolean abgemeldet;
    private Timestamp abmeldezeit;
    private boolean aktiv;
    private String kommentar;
    
    // Konstruktoren
    public Kursteilnahme() {
    }
    
    public Kursteilnahme(int mitgliederID, int kursterminID) {
        this.mitgliederID = mitgliederID;
        this.kursterminID = kursterminID;
    }
    
    // Getter und Setter
    public int getMitgliederID() {
        return mitgliederID;
    }
    
    public void setMitgliederID(int mitgliederID) {
        this.mitgliederID = mitgliederID;
    }
    
    public int getKursterminID() {
        return kursterminID;
    }
    
    public void setKursterminID(int kursterminID) {
        this.kursterminID = kursterminID;
    }
    
    public boolean isAngemeldet() {
        return angemeldet;
    }
    
    public void setAngemeldet(boolean angemeldet) {
        this.angemeldet = angemeldet;
    }
    
    public Timestamp getAnmeldezeit() {
        return anmeldezeit;
    }
    
    public void setAnmeldezeit(Timestamp anmeldezeit) {
        this.anmeldezeit = anmeldezeit;
    }
    
    public boolean isAbgemeldet() {
        return abgemeldet;
    }
    
    public void setAbgemeldet(boolean abgemeldet) {
        this.abgemeldet = abgemeldet;
    }
    
    public Timestamp getAbmeldezeit() {
        return abmeldezeit;
    }
    
    public void setAbmeldezeit(Timestamp abmeldezeit) {
        this.abmeldezeit = abmeldezeit;
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
}
