package Objekte;

public class Mitarbeiter {
    private int mitarbeiterID;
    private String vorname;
    private String nachname;
    private String geburtsdatum;
    private String straße;
    private String hausnr;
    private String stadt;
    private Ort ort;
    private Zahlungsdaten zahlungsdaten;
    private String mail;
    private Benutzer benutzer;

    // Standard-Konstruktor
    public Mitarbeiter() {}

    // Vollständiger Konstruktor
    public Mitarbeiter(int mitarbeiterID, String vorname, String nachname,
                       String geburtsdatum, String straße, String hausnr, String stadt,
                       Ort ort, Zahlungsdaten zahlungsdaten, String mail, Benutzer benutzer) {
        this.mitarbeiterID = mitarbeiterID;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.straße = straße;
        this.hausnr = hausnr;
        this.stadt = stadt;
        this.ort = ort;
        this.zahlungsdaten = zahlungsdaten;
        this.mail = mail;
        this.benutzer = benutzer;
    }

    // Getter und Setter
    public int getMitarbeiterID() { return mitarbeiterID; }
    public void setMitarbeiterID(int mitarbeiterID) { this.mitarbeiterID = mitarbeiterID; }
    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }
    public String getNachname() { return nachname; }
    public void setNachname(String nachname) { this.nachname = nachname; }
    public String getGeburtsdatum() { return geburtsdatum; }
    public void setGeburtsdatum(String geburtsdatum) { this.geburtsdatum = geburtsdatum; }
    public String getStraße() { return straße; }
    public void setStraße(String straße) { this.straße = straße; }
    public String getHausnr() { return hausnr; }
    public void setHausnr(String hausnr) { this.hausnr = hausnr; }
    public String getStadt() { return stadt; }
    public void setStadt(String stadt) { this.stadt = stadt; }
    public Ort getOrt() { return ort; }
    public void setOrt(Ort ort) { this.ort = ort; }
    public Zahlungsdaten getZahlungsdaten() { return zahlungsdaten; }
    public void setZahlungsdaten(Zahlungsdaten zahlungsdaten) { this.zahlungsdaten = zahlungsdaten; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    public Benutzer getBenutzer() { return benutzer; }
    public void setBenutzer(Benutzer benutzer) { this.benutzer = benutzer; }

    @Override
    public String toString() {
        return "Mitarbeiter{" +
                "mitarbeiterID=" + mitarbeiterID +
                ", vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", geburtsdatum='" + geburtsdatum + '\'' +
                ", straße='" + straße + '\'' +
                ", hausnr='" + hausnr + '\'' +
                ", stadt='" + stadt + '\'' +
                ", ort=" + (ort != null ? ort.getOrt() : "-") +
                ", zahlungsdaten=" + (zahlungsdaten != null ? zahlungsdaten.getName() : "-") +
                ", mail='" + mail + '\'' +
                ", benutzer=" + (benutzer != null ? benutzer.getBenutzername() : "-") +
                '}';
    }
}
