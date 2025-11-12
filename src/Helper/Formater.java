package Helper;

import java.util.List;
import java.util.Locale;

public class Formater {

	public static void printTabelle(List<Integer> spaltenBreiten, List<String> spaltenNamen, List<List<String>> daten) {
		// Formatstring bauen
		StringBuilder formatBuilder = new StringBuilder();
		for (int breite : spaltenBreiten) {
			formatBuilder.append("| %-").append(breite).append("s ");
		}
		formatBuilder.append("|\n");
		String format = formatBuilder.toString();

		// Trennlinie berechnen
		int trennBreite = spaltenBreiten.stream().mapToInt(b -> b + 3).sum() + 1;

		// Header
		System.out.printf(format, spaltenNamen.toArray());
		System.out.println("-".repeat(trennBreite));

		// Datensätze
		for (List<String> zeile : daten) {
			System.out.printf(format, zeile.toArray());
		}
	}

	public static String printDatum(java.util.Date date) {
		if (date == null)
			return "-";
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");
		return sdf.format(date);
	}

	public static String printZeit(java.util.Date date) {
		if (date == null)
			return "-";
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
		return sdf.format(date);
	}

	// Alternativ direkt für java.sql.Time:
	public static String printZeit(java.sql.Time zeit) {
		if (zeit == null)
			return "-";
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
		return sdf.format(zeit);
	}

	public static String printWährung(double betrag, int feldBreite) {
		java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance(Locale.GERMANY);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);

		String formatted = nf.format(betrag) + " €";
		// Mit führenden Leerzeichen rechtsbündig (z. B. für Tabellenform)
		return String.format("%" + feldBreite + "s", formatted);
	}

	// Alternative für Float oder String:
	public static String printWährung(float betrag, int feldBreite) {
		return printWährung((double) betrag, feldBreite);
	}

	public static String printID(int id, int feldBreite) {
		String idStr = String.valueOf(id);
		return String.format("%" + feldBreite + "s", idStr);
	}

}
