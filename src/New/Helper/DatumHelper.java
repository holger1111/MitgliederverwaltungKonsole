package New.Helper;

import java.util.Calendar;

public class DatumHelper {
	
	// Validation
	
	public static boolean istSchaltjahr(int jahr) {
		return ((jahr % 4 == 0 && jahr % 100 != 0) || (jahr % 400 == 0));
	}

	public static boolean istJahr(int jahr) {
		return (jahr != 0);
	}

	public static boolean istMonat(int monat) {
		return monat >= 1 && monat <= 12;
	}

	public static boolean istTag(int jahr, int monat, int tag) {
		int maxtage = 31;
		return tag >= 1 && tag <= maxtage ? monat == 4 || monat == 7 || monat == 9 || monat == 11 ? tag <= maxtage - 1
				: monat == 2 ? (istSchaltjahr(jahr) ? tag <= maxtage - 2 : tag <= maxtage - 3) : true : false;
	}
		
	public static String asDE(int dtag, int dmonat, int djahr) {
		String dedat = "";
		if (dtag < 10) {
			dedat = "0" + dtag;
		} else {
			dedat = "" + dtag;
		}
		dedat += ".";
		if (dmonat < 10) {
			dedat += "0" + dmonat;
		} else {
			dedat += dmonat;
		}
		dedat += ".";
		int deneg = 0;
		if (djahr < 0) {
			deneg = -1;
		}
		djahr = Math.abs(djahr);
		if (djahr < 10) {
			dedat += "   " + djahr;
		} else if (djahr < 100) {
			dedat += "  " + djahr;
		} else if (djahr < 1000) {
			dedat += " " + djahr;
		} else if (djahr < 10000) {
			dedat += djahr;
		} else {
			dedat += djahr + " Jahreszahl zu groß! Prüfe auf Fehler!";
		}
		if (deneg < 0) {
			dedat += " v. Chr.";
		}
		return dedat;
	}
	
	// Input

	public static int readJahr() {
		int jahr = IO.readInt("Jahr: ");
		while ((!istSchaltjahr(jahr) || istSchaltjahr(jahr)) && !istJahr(jahr)) {
			System.out.printf("Das Jahr %4d gibt es nicht.\n", jahr);
			jahr = IO.readInt("Jahr: ");
		}
		return jahr;
	}

	public static int readMonat() {
		int monat = IO.readInt("Monat: ");
		while (!istMonat(monat)) {
			System.out.printf("Der Monat %02d gibt es nicht.\n", monat);
			monat = IO.readInt("Monat: ");
		}
		return monat;
	}

	public static int readTag(int jahr, int monat) {
		int tag = IO.readInt("Tag: ");
		while (!istTag(jahr, monat, tag)) {
			System.out.printf("Den Tag %02d gibt es nicht.\n", tag);
			tag = IO.readInt("Tag: ");
		}
		return tag;
	}
	
	// Methoden
	
	public static Datum getAktuellesDatum() {
	    Calendar heute = Calendar.getInstance();
	    return new Datum(
	        heute.get(Calendar.YEAR),
	        heute.get(Calendar.MONTH) + 1,
	        heute.get(Calendar.DAY_OF_MONTH)
	    );
	
	}
}