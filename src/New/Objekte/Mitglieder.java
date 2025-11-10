package New.Objekte;

import java.util.Date;
import java.util.Objects;

import New.Helper.Datum;
import New.Helper.DatumHelper;

public class Mitglieder extends Interessenten {

	// Attribute

	private Date geburtstag;
	private boolean aktiv;
	private String strasse;
	private String hausnr;
	private Ort ort;
	private int ortID;
	private Zahlungsdaten zahlungsdaten;
	private int zahlungsdatenID;
	private String mail;

	// Konstruktor

	public Mitglieder() {
		super();
	}

	public Mitglieder(String vorname, String nachname, String telefon, Date geburtstag, boolean aktiv, String strasse,
			String hausnr, int ortID, int zahlungsdatenID, String mail) {
		super(vorname, nachname, telefon);
		this.geburtstag = geburtstag;
		this.aktiv = aktiv;
		this.strasse = strasse;
		this.hausnr = hausnr;
		this.ortID = ortID;
		this.zahlungsdatenID = zahlungsdatenID;
		this.mail = mail;
	}

	// Neuer Konstruktor, der direkt das Zahlungsdaten-Objekt akzeptiert
	public Mitglieder(String vorname, String nachname, String telefon, Date geburtstag, boolean aktiv, String strasse,
			String hausnr, Ort ort, Zahlungsdaten zahlungsdaten, String mail) {
		super(vorname, nachname, telefon);
		this.geburtstag = geburtstag;
		this.aktiv = aktiv;
		this.strasse = strasse;
		this.hausnr = hausnr;
		this.ort = ort;
		this.zahlungsdaten = zahlungsdaten;
		this.ortID = ort != null ? ort.getOrtID() : 0;
		this.zahlungsdatenID = zahlungsdaten != null ? zahlungsdaten.getZahlungsdatenID() : 0;
		this.mail = mail;
	}

	public Mitglieder(int mitgliederID, String vorname, String nachname, String telefon, Date geburtstag, boolean aktiv,
			String strasse, String hausnr, Ort ort, Zahlungsdaten zahlungsdaten, String mail) {
		super(mitgliederID, vorname, nachname, telefon);
		this.geburtstag = geburtstag;
		this.aktiv = aktiv;
		this.strasse = strasse;
		this.hausnr = hausnr;
		this.ort = ort;
		this.zahlungsdaten = zahlungsdaten;
		this.ortID = ort != null ? ort.getOrtID() : 0;
		this.zahlungsdatenID = zahlungsdaten != null ? zahlungsdaten.getZahlungsdatenID() : 0;
		this.mail = mail;
	}

	// Setter & Getter

	public Date getGeburtstag() {
		return geburtstag;
	}

	public void setGeburtstag(Date geburtstag) {
		this.geburtstag = geburtstag;
	}

	public int berechneAlter() {
		if (geburtstag == null) return 0;
		Datum heute = DatumHelper.getAktuellesDatum();
		Datum geburt = new Datum(getGeburtstag());

		int alter = heute.getJahr() - geburt.getJahr();

		// Prüfe, ob aktuelles Datum vor dem Geburtstag in diesem Jahr liegt
		Datum geburtAktuellesJahr = new Datum(heute.getJahr(), geburt.getMonat(), geburt.getTag());
		if (heute.isBefore(geburtAktuellesJahr)) {
			alter--;
		}
		return alter;
	}

	public boolean isAktiv() {
		return aktiv;
	}

	public void setAktiv(boolean aktiv) {
		this.aktiv = aktiv;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getHausnr() {
		return hausnr;
	}

	public void setHausnr(String hausnr) {
		this.hausnr = hausnr;
	}

	public String getPLZ() {
		return ort != null ? ort.getPLZ() : null;
	}

	public Ort getOrt() {
		return ort;
	}

	public void setOrt(Ort ort) {
		this.ort = ort;
		this.ortID = ort != null ? ort.getOrtID() : 0;
	}

	public int getOrtID() {
		return ortID;
	}

	public void setOrtID(int ortID) {
		this.ortID = ortID;
	}

	public Zahlungsdaten getZahlungsdaten() {
		return zahlungsdaten;
	}

	public void setZahlungsdaten(Zahlungsdaten zahlungsdaten) {
		this.zahlungsdaten = zahlungsdaten;
		this.zahlungsdatenID = zahlungsdaten != null ? zahlungsdaten.getZahlungsdatenID() : 0;
	}

	public int getZahlungsdatenID() {
		return zahlungsdatenID;
	}

	public void setZahlungsdatenID(int zahlungsdatenID) {
		this.zahlungsdatenID = zahlungsdatenID;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	// Override

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();

	    boolean hasGeburtstag = geburtstag != null;
	    boolean hasAlter = geburtstag != null && berechneAlter() > 0;
	    boolean hasAdresse = (strasse != null && !strasse.isEmpty()) 
	            || (hausnr != null && !hausnr.isEmpty())
	            || (ort != null && (
	                    (ort.getPLZ() != null && !ort.getPLZ().isEmpty()) ||
	                    (ort.getOrt() != null && !ort.getOrt().isEmpty())
	            ));
	    boolean hasMail = mail != null && !mail.isEmpty();

	    if (!hasGeburtstag && !hasAlter && !hasAdresse && !hasMail) {
	        sb.append("Interessent:\n");
	    } else {
	        sb.append("Mitglied:\n");
	    }
	    sb.append("MitgliederID: \t").append(getMitgliederID()).append("\n")
	      .append("Name: \t\t").append(getVorname()).append(" ").append(getNachname()).append("\n");

	    if (hasGeburtstag) {
	        String geburtsStr = new java.text.SimpleDateFormat("dd.MM.yyyy").format(geburtstag);
	        sb.append("Geburtstag: \t").append(geburtsStr).append("\n");
	    }
	    if (hasAlter) {
	        sb.append("Alter: \t\t").append(berechneAlter()).append("\n");
	    }
	    sb.append("Aktiv: \t\t").append(aktiv).append("\n");

	    if (hasAdresse) {
	        sb.append("Addresse:\n");
	        if (strasse != null && !strasse.isEmpty()) 
	            sb.append("\t\t").append(strasse);
	        if (hausnr != null && !hausnr.isEmpty())
	            sb.append(" ").append(hausnr);
	        if ((strasse != null && !strasse.isEmpty()) || (hausnr != null && !hausnr.isEmpty()))
	            sb.append("\n");
	        if (ort != null) {
	            if (ort.getPLZ() != null && !ort.getPLZ().isEmpty())
	                sb.append("\t\t").append(ort.getPLZ());
	            if (ort.getOrt() != null && !ort.getOrt().isEmpty())
	                sb.append(" ").append(ort.getOrt());
	            if ((ort.getPLZ() != null && !ort.getPLZ().isEmpty()) 
	                    || (ort.getOrt() != null && !ort.getOrt().isEmpty()))
	                sb.append("\n");
	        }
	    }

	    sb.append("Telefon: \t").append(getTelefon()).append("\n");
	    if (hasMail) {
	        sb.append("Mail: \t\t").append(mail).append("\n");
	    }

	    return sb.toString();
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(getMitgliederID());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Mitglieder))
			return false;
		Mitglieder other = (Mitglieder) obj;
		return getMitgliederID() == other.getMitgliederID();
	}
}
