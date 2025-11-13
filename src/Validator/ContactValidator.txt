package Validator;

import Exception.StringException;
import Exception.MailException;
import Exception.TelefonException;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.*;

public class ContactValidator extends BasicTypeValidator {

	// Regex für E-Mail
	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

	// Land -> {minLänge, maxLänge}
	private static final Map<String, int[]> COUNTRY_CODES = Map.ofEntries(Map.entry("+49", new int[] { 11, 13 }), // Deutschland
			Map.entry("+43", new int[] { 11, 13 }), // Österreich
			Map.entry("+41", new int[] { 11, 12 }), // Schweiz
			Map.entry("+33", new int[] { 11, 12 }), // Frankreich
			Map.entry("+32", new int[] { 11, 12 }), // Belgien
			Map.entry("+31", new int[] { 11, 12 }), // Niederlande
			Map.entry("+352", new int[] { 11, 13 }), // Luxemburg
			Map.entry("+39", new int[] { 11, 13 }), // Italien
			Map.entry("+34", new int[] { 11, 12 }), // Spanien
			Map.entry("+351", new int[] { 11, 13 }), // Portugal
			Map.entry("+45", new int[] { 9, 11 }), // Dänemark
			Map.entry("+46", new int[] { 11, 13 }), // Schweden
			Map.entry("+47", new int[] { 9, 11 }), // Norwegen
			Map.entry("+358", new int[] { 11, 13 }), // Finnland
			Map.entry("+48", new int[] { 11, 12 }), // Polen
			Map.entry("+420", new int[] { 12, 13 }), // Tschechien
			Map.entry("+36", new int[] { 11, 12 }), // Ungarn
			Map.entry("+30", new int[] { 11, 13 }), // Griechenland
			Map.entry("+421", new int[] { 12, 13 }), // Slowakei
			Map.entry("+40", new int[] { 11, 12 }), // Rumänien
			Map.entry("+359", new int[] { 11, 13 }), // Bulgarien
			Map.entry("+385", new int[] { 11, 13 }), // Kroatien
			Map.entry("+381", new int[] { 11, 12 }), // Serbien
			Map.entry("+386", new int[] { 11, 13 }), // Slowenien
			Map.entry("+387", new int[] { 11, 13 }), // Bosnien und Herzegowina
			Map.entry("+389", new int[] { 11, 13 }), // Mazedonien
			Map.entry("+382", new int[] { 11, 13 }), // Montenegro
			Map.entry("+355", new int[] { 11, 13 }), // Albanien
			Map.entry("+372", new int[] { 11, 13 }), // Estland
			Map.entry("+371", new int[] { 11, 13 }), // Lettland
			Map.entry("+370", new int[] { 11, 13 }), // Litauen
			Map.entry("+353", new int[] { 11, 13 }), // Irland
			Map.entry("+44", new int[] { 11, 13 }), // Großbritannien / Vereinigtes Königreich
			Map.entry("+354", new int[] { 10, 11 }), // Island
			Map.entry("+423", new int[] { 10, 12 }) // Liechtenstein
	);

	// Land -> Internationale Beispielnummer
	private static final Map<String, String> COUNTRY_INTL_FORMATS = Map.ofEntries(Map.entry("+49", "+49 170 1234567"),
			Map.entry("+43", "+43 660 1234567"), Map.entry("+41", "+41 79 123 45 67"),
			Map.entry("+33", "+33 1 23 45 67 89"), Map.entry("+32", "+32 12 34 56 78"),
			Map.entry("+31", "+31 6 12345678"), Map.entry("+352", "+352 26 123456"),
			Map.entry("+39", "+39 347 1234567"), Map.entry("+34", "+34 612 34 56 78"),
			Map.entry("+351", "+351 912 345 678"), Map.entry("+45", "+45 12 34 56 78"),
			Map.entry("+46", "+46 70 123 45 67"), Map.entry("+47", "+47 123 45 678"),
			Map.entry("+358", "+358 40 1234567"), Map.entry("+48", "+48 123 456 789"),
			Map.entry("+420", "+420 123 456 789"), Map.entry("+36", "+36 30 123 4567"),
			Map.entry("+30", "+30 210 1234567"), Map.entry("+421", "+421 901 234 567"),
			Map.entry("+40", "+40 721 234 567"), Map.entry("+359", "+359 88 123 4567"),
			Map.entry("+385", "+385 91 234 5678"), Map.entry("+381", "+381 64 1234567"),
			Map.entry("+386", "+386 40 123 456"), Map.entry("+387", "+387 61 123 456"),
			Map.entry("+389", "+389 70 123 456"), Map.entry("+382", "+382 67 123 456"),
			Map.entry("+355", "+355 69 123 4567"), Map.entry("+372", "+372 5123 4567"),
			Map.entry("+371", "+371 22 123 456"), Map.entry("+370", "+370 612 34567"),
			Map.entry("+353", "+353 85 123 4567"), Map.entry("+44", "+44 7700 900123"),
			Map.entry("+354", "+354 660 1234"), Map.entry("+423", "+423 123 4567"));

	private static final String DE_VORWAHL = "+49";
	private static final String LANDESVORWAHL_REGEX = "^(\\+\\d{1,4}|00\\d{1,4})";
	private static final Pattern LANDESVORWAHL_PATTERN = Pattern.compile(LANDESVORWAHL_REGEX);

	private static final String NUMMER_REGEX = "^[+]?\\d{1,4}[\\d \\-()/]*$";
	private static final Pattern NUMMER_PATTERN = Pattern.compile(NUMMER_REGEX);

	/**
	 * Validiert eine E-Mail-Adresse.
	 * 
	 * @param obj Eingabe (String)
	 * @throws MailException bei ungültiger E-Mail-Adresse
	 */
	public void validateEmail(Object obj) throws Exception, StringException, MailException {
		super.validate(obj);
		if (isValid()) {
			String email = (String) obj;
			Matcher matcher = EMAIL_PATTERN.matcher(email);
			if (!matcher.matches()) {
				throw new MailException("Ungültige E-Mail-Adresse.");
			}
		}
	}

	/**
	 * Validiert und formatiert eine Telefonnummer.
	 * 
	 * @param obj Eingabe (String)
	 * @return formatierte Telefonnummer im internationalen Format
	 * @throws TelefonException bei ungültiger Telefonnummer
	 */
	public String validateUndFormatTelefon(Object obj) throws Exception, StringException, TelefonException {
		super.validate(obj);
		if (!isValid()) {
			throw new TelefonException("Ungültige Eingabe.");
		}

		String rawInput = (String) obj;
		String cleaned = rawInput.replaceAll("[^\\d+]", "");
		Matcher vorwahlMatcher = LANDESVORWAHL_PATTERN.matcher(cleaned);

		String ländervorwahl;
		String restNummer;

		if (vorwahlMatcher.find()) {
			ländervorwahl = vorwahlMatcher.group();
			restNummer = cleaned.substring(vorwahlMatcher.end());
			if (ländervorwahl.startsWith("00")) {
				ländervorwahl = "+" + ländervorwahl.substring(2);
			}
		} else {
			ländervorwahl = DE_VORWAHL;
			restNummer = cleaned.replaceFirst("^0+", "");
		}

		String validNummer = ländervorwahl + restNummer;

		int gesamtLänge = validNummer.length();
		int[] lengthRange = COUNTRY_CODES.getOrDefault(ländervorwahl, new int[] { 10, 16 });
		if (gesamtLänge < lengthRange[0] || gesamtLänge > lengthRange[1]) {
			throw new TelefonException("Nummer passt nicht zur Ländervorwahl oder ist zu kurz/lang.");
		}

		Matcher nummerMatcher = NUMMER_PATTERN.matcher(validNummer);
		if (!nummerMatcher.matches()) {
			throw new TelefonException("Ungültiges Nummernformat!");
		}

		String intlFormat = COUNTRY_INTL_FORMATS.getOrDefault(ländervorwahl, validNummer);
		return formatTelefonnummer(validNummer, intlFormat);
	}

	/**
	 * Formatiert die Telefonnummer anhand eines Beispielmusters (mit Leerzeichen).
	 */
	public static String formatTelefonnummer(String rawNumber, String laenderBeispiel) {
		String[] gruppen = laenderBeispiel.split(" ");
		String cleanNumber = rawNumber.replaceAll("[^\\d+]", "");
		String laenderVorwahl = gruppen[0];
		String nummerohneVorwahl;
		if (cleanNumber.startsWith(laenderVorwahl)) {
			nummerohneVorwahl = cleanNumber.substring(laenderVorwahl.length());
		} else {
			nummerohneVorwahl = cleanNumber;
		}

		List<Integer> gruppenLaenge = new ArrayList<>();
		for (int i = 1; i < gruppen.length; i++) {
			gruppenLaenge.add(gruppen[i].length());
		}

		List<String> nummerGruppiert = new ArrayList<>();
		int start = 0;
		for (int len : gruppenLaenge) {
			if (start + len <= nummerohneVorwahl.length()) {
				nummerGruppiert.add(nummerohneVorwahl.substring(start, start + len));
				start += len;
			} else {
				nummerGruppiert.add(nummerohneVorwahl.substring(start));
				break;
			}
		}

		return laenderVorwahl + " " + String.join(" ", nummerGruppiert);
	}

}
