package Helper;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Datum {

	// Attribute

	private int tag;
	private int monat;
	private int jahr;

	// Konstruktoren

	public Datum(int j, int m, int t) {
		jahr = j;
		monat = m;
		tag = t;
	}

	public Datum() {
		jahr = setJahr();
		monat = setMonat();
		tag = setTag();
	}
	
	public Datum(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    this.jahr = cal.get(Calendar.YEAR);
	    this.monat = cal.get(Calendar.MONTH) + 1;
	    this.tag = cal.get(Calendar.DAY_OF_MONTH);
	}

	// Setter und Getter

	public int setTag() {
		tag = DatumHelper.readTag(jahr, monat);
		System.out.printf("Der Tag wurde auf %02d geändert.\n", tag);
		return tag;
	}

	public int setTag(int tag) {
		this.tag = tag;
		System.out.printf("Der Tag wurde auf %02d geändert.\n", tag);
		return tag;
	}

	public int setMonat() {
		monat = DatumHelper.readMonat();
		System.out.printf("Der Monat wurde auf %02d geändert.\n", monat);
		return monat;
	}

	public int setMonat(int monat) {
		this.monat = monat;
		System.out.printf("Der Monat wurde auf %02d geändert.\n", monat);
		return monat;
	}

	public int setJahr() {
		jahr = DatumHelper.readJahr();
		System.out.printf("Das Jahr wurde auf %4d geändert.\n", jahr);
		return jahr;
	}

	public int setJahr(int jahr) {
		this.jahr = jahr;
		System.out.printf("Das Jahr wurde auf %4d geändert.\n", jahr);
		return jahr;
	}

	public int getTag() {
		return tag;
	}

	public int getMonat() {
		return monat;
	}

	public int getJahr() {
		return jahr;
	}

	public String getDatum() {
		return String.format("%02d.%02d.%4d", tag, monat, jahr);
	}

	// Override
	
	@Override
	public String toString() {
		return String.format("%02d.%02d.%4d", tag, monat, jahr);
	}

	@Override
	public int hashCode() {
		return Objects.hash(jahr, monat, tag);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Datum other = (Datum) obj;
		return jahr == other.jahr && monat == other.monat && tag == other.tag;
	}
	
	// Methoden

	public static boolean testeDatum(int tag, int monat, int jahr) {

		return DatumHelper.istJahr(jahr) && DatumHelper.istMonat(monat) && DatumHelper.istTag(jahr, monat, tag);
	}

	public void printDatumDE() {
		System.out.println(toString());
	}

	public boolean isBefore(Datum other) {
	    if (this.jahr < other.jahr) {
	        return true;
	    } else if (this.jahr == other.jahr) {
	        if (this.monat < other.monat) {
	            return true;
	        } else if (this.monat == other.monat) {
	            return this.tag < other.tag;
	        }
	    }
	    return false;
	}

}
