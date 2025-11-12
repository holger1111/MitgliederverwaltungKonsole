package Service;

import java.sql.Connection;
import java.util.*;

import Helper.Formater;
import Manager.KursManager;
import Manager.MitgliederManager;
import Manager.MitarbeiterManager;
import Manager.VerkaufManager;
import Manager.VertragManager;

import Objekte.*;

public class AdminService extends BaseService {

	private VerkaufManager verkaufManager;
	private VertragManager vertragManager;
	private KursManager kursManager;
	private MitgliederManager mitgliederManager;
	private MitarbeiterManager mitarbeiterManager;

	public AdminService(Connection connection, Scanner scanner) {
		super(connection, scanner);
		try {
			this.verkaufManager = new VerkaufManager();
		} catch (Exception e) {
			System.out.println("Fehler beim Initialisieren des VerkaufManagers: " + e.getMessage());
		}
		try {
			this.vertragManager = new VertragManager();
		} catch (Exception e) {
			System.out.println("Fehler beim Initialisieren des VertragManagers: " + e.getMessage());
		}
		try {
			this.kursManager = new KursManager();
		} catch (Exception e) {
			System.out.println("Fehler beim Initialisieren des KursManagers: " + e.getMessage());
		}
		try {
			this.mitgliederManager = new MitgliederManager();
		} catch (Exception e) {
			System.out.println("Fehler beim Initialisieren des MitgliederManagers: " + e.getMessage());
		}
		try {
			this.mitarbeiterManager = new MitarbeiterManager();
		} catch (Exception e) {
			System.out.println("Fehler beim Initialisieren des MitarbeiterManagers: " + e.getMessage());
		}
	}

	public void start() {
		boolean zurueck = false;
		while (!zurueck) {
			System.out.println("==== Admin-Menü ====");
			System.out.println("1 - Verkauf");
			System.out.println("2 - Vertrag");
			System.out.println("3 - Kurs");
			System.out.println("4 - Personen");
			System.out.println("5 - Zurück zum Hauptmenü");
			System.out.print("Bitte wählen: ");
			String eingabe = scanner.nextLine();

			switch (eingabe) {
			case "1":
				verkaufMenue();
				break;
			case "2":
				vertragMenue();
				break;
			case "3":
				kursMenue();
				break;
			case "4":
				personenMenue();
				break;
			case "5":
				zurueck = true;
				break;
			default:
				System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
			}
		}
	}

	private void verkaufMenue() {
		boolean zurueck = false;
		while (!zurueck) {
			System.out.println("==== Verkauf-Menü ====");
			System.out.println("1 - Kategorie");
			System.out.println("2 - Artikel");
			System.out.println("3 - ArtikelBestellung");
			System.out.println("4 - Bestellung");
			System.out.println("5 - Zahlung");
			System.out.println("6 - Zurück");
			System.out.print("Bitte wählen: ");
			String eingabe = scanner.nextLine();

			switch (eingabe) {
			case "1":
				verwalteKategorie();
				break;
			case "2":
				verwalteArtikel();
				break;
			case "3":
				verwalteArtikelBestellung();
				break;
			case "4":
				verwalteBestellung();
				break;
			case "5":
				verwalteZahlung();
				break;
			case "6":
				zurueck = true;
				break;
			default:
				System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
			}
		}
	}

	private void personenMenue() {
		boolean zurueck = false;
		while (!zurueck) {
			System.out.println("==== Personen-Menü ====");
			System.out.println("1 - Mitglieder-Menü");
			System.out.println("2 - Mitarbeiter-Menü");
			System.out.println("3 - Zurück");
			System.out.print("Bitte wählen: ");
			String eingabe = scanner.nextLine();

			switch (eingabe) {
			case "1":
				mitgliederMenue();
				break;
			case "2":
				mitarbeiterMenue();
				break;
			case "3":
				zurueck = true;
				break;
			default:
				System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
			}
		}
	}

	private void mitgliederMenue() {
		boolean zurueck = false;
		while (!zurueck) {
			System.out.println("==== Mitglieder-Menü ====");
			System.out.println("1 - Mitglieder");
			System.out.println("2 - Ort");
			System.out.println("3 - Zahlungsdaten");
			System.out.println("4 - Zurück");
			System.out.print("Bitte wählen: ");
			String eingabe = scanner.nextLine();

			switch (eingabe) {
			case "1":
				verwalteMitglieder();
				break;
			case "2":
				verwalteOrt();
				break;
			case "3":
				verwalteZahlungsdaten();
				break;
			case "4":
				zurueck = true;
				break;
			default:
				System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
			}
		}
	}

	private void mitarbeiterMenue() {
		boolean zurueck = false;
		while (!zurueck) {
			System.out.println("==== Mitarbeiter-Menü ====");
			System.out.println("1 - Mitarbeiter");
			System.out.println("2 - Ort");
			System.out.println("3 - Zahlungsdaten");
			System.out.println("4 - Benutzer");
			System.out.println("5 - Rolle");
			System.out.println("6 - Zurück");
			System.out.print("Bitte wählen: ");
			String eingabe = scanner.nextLine();

			switch (eingabe) {
			case "1":
				verwalteMitarbeiter();
				break;
			case "2":
				verwalteOrt();
				break;
			case "3":
				verwalteZahlungsdaten();
				break;
			case "4":
				verwalteBenutzer();
				break;
			case "5":
				verwalteRolle();
				break;
			case "6":
				zurueck = true;
				break;
			default:
				System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
			}
		}
	}

	private void verwalteKategorie() {
		try {
			var kategorien = verkaufManager.getKategorieDAO().findAll();

			List<String> headers = Arrays.asList("KategorieID", "Bezeichnung");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>(Arrays.asList("KategorieID".length(), "Bezeichnung".length()));

			for (Kategorie k : kategorien) {
				List<String> row = Arrays.asList(String.valueOf(k.getKategorieID()),
						k.getBezeichnung() != null ? k.getBezeichnung() : "-");
				widths.set(0, Math.max(widths.get(0), row.get(0).length()));
				widths.set(1, Math.max(widths.get(1), row.get(1).length()));
				rows.add(row);
			}
			System.out.println("==== Kategorie ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Kategorien: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteArtikel() {
		try {
			var artikelList = verkaufManager.getArtikelDAO().findAll();

			List<String> headers = Arrays.asList("ArtikelID", "Name", "Preis", "Kategorie", "Kommentar");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>();
			for (String header : headers)
				widths.add(header.length());

			for (Artikel a : artikelList) {
				String name = a.getName() != null ? a.getName() : "-";
				String preis = String.format("%.2f", a.getEinzelpreis());
				String kategorie = (a.getKategorie() != null ? a.getKategorie().getBezeichnung() : "-");
				String kommentar = a.getKommentar() != null ? a.getKommentar() : "-";
				List<String> row = Arrays.asList(String.valueOf(a.getArtikelID()), name, preis, kategorie, kommentar);
				for (int i = 0; i < headers.size(); i++)
					widths.set(i, Math.max(widths.get(i), row.get(i).length()));
				rows.add(row);
			}
			System.out.println("==== Artikel ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Artikel: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteArtikelBestellung() {
		try {
			var list = verkaufManager.getArtikelBestellungDAO().findAll();
			List<String> headers = Arrays.asList("BestellungID", "ArtikelID", "Menge", "Aufaddiert");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>();
			for (String header : headers)
				widths.add(header.length());

			for (ArtikelBestellung ab : list) {
				List<String> row = Arrays.asList(String.valueOf(ab.getBestellungID()),
						String.valueOf(ab.getArtikelID()), String.valueOf(ab.getMenge()),
						String.format("%.2f", ab.getAufaddiert()));
				for (int i = 0; i < headers.size(); i++)
					widths.set(i, Math.max(widths.get(i), row.get(i).length()));
				rows.add(row);
			}
			System.out.println("==== ArtikelBestellung ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der ArtikelBestellungen: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteBestellung() {
		try {
			var bestellungen = verkaufManager.getBestellungDAO().findAll();
			List<String> headers = Arrays.asList("BestellungID", "MitgliedID", "Bestelldatum", "ZahlungID",
					"MitarbeiterID", "Gesamtpreis");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>();
			for (String header : headers)
				widths.add(header.length());

			for (Bestellung b : bestellungen) {
				List<String> row = Arrays.asList(Formater.printID(b.getBestellungID(), 5),
						String.valueOf(b.getMitgliederID()), Formater.printDatum(b.getBestelldatum()), // neues Feld
						String.valueOf(b.getZahlungID()), // neues Feld
						String.valueOf(b.getMitarbeiterID()), // neues Feld
						Formater.printWährung(b.getGesamtpreis(), 12));
				for (int i = 0; i < headers.size(); i++)
					widths.set(i, Math.max(widths.get(i), row.get(i).length()));
				rows.add(row);
			}
			System.out.println("==== Bestellung ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Bestellungen: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteZahlung() {
		try {
			var zahlungen = verkaufManager.getZahlungDAO().findAll();
			List<String> headers = Arrays.asList("ZahlungID", "Zahlungsart");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>();
			for (String header : headers)
				widths.add(header.length());

			for (Zahlung z : zahlungen) {
				List<String> row = Arrays.asList(String.valueOf(z.getZahlungID()),
						z.getZahlungsart() != null ? z.getZahlungsart() : "-");
				for (int i = 0; i < headers.size(); i++)
					widths.set(i, Math.max(widths.get(i), row.get(i).length()));
				rows.add(row);
			}
			System.out.println("==== Zahlung ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Zahlungen: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void kursMenue() {
		boolean zurueck = false;
		while (!zurueck) {
			System.out.println("==== Kurs-Menü ====");
			System.out.println("1 - Kurs");
			System.out.println("2 - Kurstermin");
			System.out.println("3 - Kursteilnahme");
			System.out.println("4 - Kursleitung");
			System.out.println("5 - Zurück");
			System.out.print("Bitte wählen: ");
			String eingabe = scanner.nextLine();

			switch (eingabe) {
			case "1":
				verwalteKurs();
				break;
			case "2":
				verwalteKurstermin();
				break;
			case "3":
				verwalteKursteilnahme();
				break;
			case "4":
				verwalteKursleitung();
				break;
			case "5":
				zurueck = true;
				break;
			default:
				System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
			}
		}
	}

	private void verwalteKurs() {
		try {
			var kurse = kursManager.getKursDAO().findAll();
			List<String> headers = Arrays.asList("KursID", "Bezeichnung", "Kostenfrei", "Aktiv", "Teilnehmerzahl",
					"Preis", "AnzahlTermine", "Kommentar");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>();
			for (String header : headers)
				widths.add(header.length());
			for (Kurs k : kurse) {
			    List<String> row = Arrays.asList(
			        Formater.printID(k.getKursID(), 5),
			        k.getBezeichnung() != null ? k.getBezeichnung() : "-",
			        k.isKostenfrei() ? "Ja" : "Nein",
			        k.isAktiv() ? "Ja" : "Nein",
			        String.valueOf(k.getTeilnehmerzahl()),
			        Formater.printWährung(k.getPreis(), 12),
			        String.valueOf(k.getAnzahlTermine()),
			        k.getKommentar() != null ? k.getKommentar() : "-"
			    );
				for (int i = 0; i < headers.size(); i++)
					widths.set(i, Math.max(widths.get(i), row.get(i).length()));
				rows.add(row);
			}
			System.out.println("==== Kurse ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Kurse: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteKurstermin() {
		try {
			var termine = kursManager.getKursterminDAO().findAll();
			List<String> headers = Arrays.asList("KursterminID", "KursID", "Termin", "Teilnehmerfrei",
				    "Anmeldebar", "Aktiv", "Kommentar");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>();
			for (String header : headers)
				widths.add(header.length());
			for (Kurstermin kt : termine) {
			    List<String> row = Arrays.asList(
			        Formater.printID(kt.getKursterminID(), 5),
			        Formater.printID(kt.getKursID(), 5),
			        kt.getTermin() != null ? Formater.printDatum(kt.getTermin()) : "-",
			        kt.getTeilnehmerfrei()> 0 ? "Ja" : "Nein",
			        kt.isAnmeldebar() ? "Ja" : "Nein",
			        kt.isAktiv() ? "Ja" : "Nein",
			        kt.getKommentar() != null ? kt.getKommentar() : "-"
			    );
				for (int i = 0; i < headers.size(); i++)
					widths.set(i, Math.max(widths.get(i), row.get(i).length()));
				rows.add(row);
			}
			System.out.println("==== Kurstermine ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Kurstermine: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteKursteilnahme() {
		try {
			var teilnahmen = kursManager.getKursteilnahmeDAO().findAll();
			List<String> headers = Arrays.asList("MitgliederID", "KursterminID", "Angemeldet", "Anmeldezeit",
				    "Abgemeldet", "Abmeldezeit", "Aktiv", "Kommentar");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>();
			for (String header : headers)
				widths.add(header.length());
			for (Kursteilnahme kt : teilnahmen) {
			    List<String> row = Arrays.asList(
			        Formater.printID(kt.getMitgliederID(), 5),
			        Formater.printID(kt.getKursterminID(), 5),
			        kt.isAngemeldet() ? "Ja" : "Nein",
			        kt.getAnmeldezeit() != null ? Formater.printDatum(kt.getAnmeldezeit()) : "-",
			        kt.isAbgemeldet() ? "Ja" : "Nein",
			        kt.getAbmeldezeit() != null ? Formater.printDatum(kt.getAbmeldezeit()) : "-",
			        kt.isAktiv() ? "Ja" : "Nein",
			        kt.getKommentar() != null ? kt.getKommentar() : "-"
			    );
				for (int i = 0; i < headers.size(); i++)
					widths.set(i, Math.max(widths.get(i), row.get(i).length()));
				rows.add(row);
			}
			System.out.println("==== Kursteilnahmen ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Kursteilnahmen: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteKursleitung() {
		try {
			var leitungen = kursManager.getKursleitungDAO().findAll();
			List<String> headers = Arrays.asList("KursterminID", "MitarbeiterID", "Bestaetigt", "Bestaetigungszeit",
				    "Abgemeldet", "Abmeldezeit", "Aktiv", "Kommentar");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>();
			for (String header : headers)
				widths.add(header.length());
			for (Kursleitung kl : leitungen) {
			    List<String> row = Arrays.asList(
			        Formater.printID(kl.getKursterminID(), 5),
			        Formater.printID(kl.getMitarbeiterID(), 5),
			        kl.isBestätigt() ? "Ja" : "Nein",
			        kl.getBestätigungszeit() != null ? Formater.printDatum(kl.getBestätigungszeit()) : "-",
			        kl.isAbgemeldet() ? "Ja" : "Nein",
			        kl.getAbmeldezeit() != null ? Formater.printDatum(kl.getAbmeldezeit()) : "-",
			        kl.isAktiv() ? "Ja" : "Nein",
			        kl.getKommentar() != null ? kl.getKommentar() : "-"
			    );
				for (int i = 0; i < headers.size(); i++)
					widths.set(i, Math.max(widths.get(i), row.get(i).length()));
				rows.add(row);
			}
			System.out.println("==== Kursleitungen ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Kursleitungen: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void vertragMenue() {
		boolean zurueck = false;
		while (!zurueck) {
			System.out.println("==== Vertrag-Menü ====");
			System.out.println("1 - Vertrag");
			System.out.println("2 - Intervall");
			System.out.println("3 - MitgliederVertrag");
			System.out.println("4 - Zahlung");
			System.out.println("6 - Zurück");
			System.out.print("Bitte wählen: ");
			String eingabe = scanner.nextLine();

			switch (eingabe) {
			case "1":
				verwalteVertrag();
				break;
			case "2":
				verwalteIntervall();
				break;
			case "3":
				verwalteMitgliederVertrag();
				break;
			case "4":
				verwalteZahlung();
				break;
			case "6":
				zurueck = true;
				break;
			default:
				System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
			}
		}
	}

	private void verwalteVertrag() {
		try {
			var vertragListe = vertragManager.getVertragDAO().findAll();
			List<String> headers = Arrays.asList("VertragID", "Bezeichnung", "Laufzeit", "Grundpreis");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>();
			for (String header : headers)
				widths.add(header.length());
			for (Vertrag v : vertragListe) {
				List<String> row = Arrays.asList(Formater.printID(v.getVertragID(), 5),
						v.getBezeichnung() != null ? v.getBezeichnung() : "-",
						v.getLaufzeit() != 0 ? Integer.toString(v.getLaufzeit()) : "-", // neues Feld
						Formater.printWährung(v.getGrundpreis(), 10) // neues Feld
				);
				for (int i = 0; i < headers.size(); i++)
					widths.set(i, Math.max(widths.get(i), row.get(i).length()));
				rows.add(row);
			}
			System.out.println("==== Vertrag ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Verträge: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteIntervall() {
		try {
			var intervallListe = vertragManager.getIntervallDAO().findAll();
			List<String> headers = Arrays.asList("IntervallID", "Beschreibung", "Zahlungsintervall");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>();
			for (String header : headers)
				widths.add(header.length());
			for (Intervall i : intervallListe) {
				List<String> row = Arrays.asList(Formater.printID(i.getIntervallID(), 5),
						i.getBezeichnung() != null ? i.getBezeichnung() : "-",
						i.getZahlungsintervall() != null ? i.getZahlungsintervall().toString() : "-" // neues Feld
				);
				for (int j = 0; j < headers.size(); j++)
					widths.set(j, Math.max(widths.get(j), row.get(j).length()));
				rows.add(row);
			}
			System.out.println("==== Intervall ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Intervalle: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteMitgliederVertrag() {
		try {
			var mitgliederVertragListe = vertragManager.getMitgliederVertragDAO().findAll();
			List<String> headers = Arrays.asList("MitgliedID", "VertragID", "VertragNr", "Vertragsbeginn",
					"Vertragsende", "Verlaengerung", "Aktiv", "Gekuendigt", "Preisrabatt", "IntervallID", "ZahlungID",
					"Trainingsbeginn", "MitarbeiterID", "Kommentar");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>();
			for (String header : headers)
				widths.add(header.length());
			for (MitgliederVertrag mv : mitgliederVertragListe) {
				List<String> row = Arrays.asList(Formater.printID(mv.getMitgliederID(), 5),
						Formater.printID(mv.getVertragID(), 5),
						mv.getVertragNr() != 0 ? Integer.toString(mv.getVertragNr()) : "-",
						Formater.printDatum(mv.getVertragsbeginn()), Formater.printDatum(mv.getVertragsende()),
						mv.isVerlängerung() ? "Ja" : "Nein", mv.isAktiv() ? "Ja" : "Nein",
						mv.isGekündigt() ? "Ja" : "Nein", String.format("%.2f", mv.getPreisrabatt()),
						Formater.printID(mv.getIntervallID(), 5), Formater.printID(mv.getZahlungID(), 5),
						Formater.printDatum(mv.getTrainingsbeginn()), Formater.printID(mv.getMitarbeiterID(), 5),
						mv.getKommentar() != null ? mv.getKommentar() : "-");
				for (int i = 0; i < headers.size(); i++)
					widths.set(i, Math.max(widths.get(i), row.get(i).length()));
				rows.add(row);
			}
			System.out.println("==== MitgliederVertrag ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der MitgliederVerträge: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteOrt() {
		try {
			var orte = mitarbeiterManager.getOrtDAO().findAll();
			List<String> headers = Arrays.asList("OrtID", "PLZ", "Ort");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>();
			for (String header : headers)
				widths.add(header.length());
			for (Ort o : orte) {
				List<String> row = Arrays.asList(String.valueOf(o.getOrtID()), o.getPLZ() != null ? o.getPLZ() : "-",
						o.getOrt() != null ? o.getOrt() : "-");
				for (int i = 0; i < headers.size(); i++)
					widths.set(i, Math.max(widths.get(i), row.get(i).length()));
				rows.add(row);
			}
			System.out.println("==== Orte ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Orte: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteZahlungsdaten() {
		try {
			var zahlungen = mitarbeiterManager.getZahlungsdatenDAO().findAll();
			List<String> headers = Arrays.asList("ZahlungsdatenID", "Name", "IBAN", "BIC");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>();
			for (String header : headers)
				widths.add(header.length());
			for (Zahlungsdaten z : zahlungen) {
				List<String> row = Arrays.asList(String.valueOf(z.getZahlungsdatenID()),
						z.getName() != null ? z.getName() : "-", z.getIBAN() != null ? z.getIBAN() : "-",
						z.getBIC() != null ? z.getBIC() : "-");
				for (int i = 0; i < headers.size(); i++)
					widths.set(i, Math.max(widths.get(i), row.get(i).length()));
				rows.add(row);
			}
			System.out.println("==== Zahlungsdaten ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Zahlungsdaten: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteBenutzer() {
		try {
			var benutzer = mitarbeiterManager.getBenutzerDAO().findAll();
			List<String> headers = Arrays.asList("BenutzerID", "Benutzername", "Passwort", "RolleID");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>();
			for (String header : headers)
				widths.add(header.length());
			for (Benutzer b : benutzer) {
			    List<String> row = Arrays.asList(
			        Formater.printID(b.getBenutzerID(), 5),
			        b.getBenutzername() != null ? b.getBenutzername() : "-",
			        "******",                                     // maskiertes Passwort
			        Formater.printID(b.getRolleID(), 5)
			    );
				for (int i = 0; i < headers.size(); i++)
					widths.set(i, Math.max(widths.get(i), row.get(i).length()));
				rows.add(row);
			}
			System.out.println("==== Benutzer ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Benutzer: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteMitglieder() {
		try {
			var mitglieder = mitgliederManager.getMitgliederDAO().findAll();

			List<String> headers = Arrays.asList("MitgliedID", "Vorname", "Nachname", "Geburtsdatum", "Aktiv",
					"Strasse", "Hausnr", "OrtID", "ZahlungsdatenID", "Telefon", "Mail");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>(headers.size());
			for (String h : headers)
				widths.add(h.length());

			for (Mitglieder m : mitglieder) {
				List<String> row = Arrays.asList(String.valueOf(m.getMitgliederID()),
						m.getVorname() != null ? m.getVorname() : "-", m.getNachname() != null ? m.getNachname() : "-",
						m.getGeburtsdatum() != null ? m.getGeburtsdatum().toString() : "-", String.valueOf(m.isAktiv()),
						m.getStrasse() != null ? m.getStrasse() : "-", m.getHausnr() != null ? m.getHausnr() : "-",
						String.valueOf(m.getOrtID()), String.valueOf(m.getZahlungsdatenID()),
						m.getTelefon() != null ? m.getTelefon() : "-", m.getMail() != null ? m.getMail() : "-");
				for (int i = 0; i < headers.size(); i++)
					widths.set(i, Math.max(widths.get(i), row.get(i).length()));
				rows.add(row);
			}

			System.out.println("==== Mitglieder ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Mitglieder: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteMitarbeiter() {
		try {
			var mitarbeiter = mitarbeiterManager.getMitarbeiterDAO().findAll();
			List<String> headers = Arrays.asList("MitarbeiterID", "Vorname", "Nachname", "Geburtsdatum", "Aktiv",
					"Strasse", "Hausnr", "OrtID", "ZahlungsdatenID", "Telefon", "Mail");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>(headers.size());
			for (String h : headers)
				widths.add(h.length());

			for (Mitarbeiter m : mitarbeiter) {
				List<String> row = Arrays.asList(String.valueOf(m.getMitarbeiterID()),
						m.getVorname() != null ? m.getVorname() : "-", m.getNachname() != null ? m.getNachname() : "-",
						m.getGeburtsdatum() != null ? m.getGeburtsdatum().toString() : "-", String.valueOf(m.isAktiv()),
						m.getStraße() != null ? m.getStraße() : "-", m.getHausnr() != null ? m.getHausnr() : "-",
						m.getOrt() != null ? String.valueOf(m.getOrt().getOrtID()) : "-",
						m.getZahlungsdaten() != null ? String.valueOf(m.getZahlungsdaten().getZahlungsdatenID()) : "-",
						m.getTelefon() != null ? m.getTelefon() : "-", m.getMail() != null ? m.getMail() : "-");
				for (int i = 0; i < headers.size(); i++)
					widths.set(i, Math.max(widths.get(i), row.get(i).length()));
				rows.add(row);
			}

			System.out.println("==== Mitarbeiter ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Mitarbeiter: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteRolle() {
		try {
			var rollen = mitarbeiterManager.getRolleDAO().findAll();
			List<String> headers = Arrays.asList("RolleID", "Bezeichnung", "Kommentar");
			List<List<String>> rows = new ArrayList<>();
			List<Integer> widths = new ArrayList<>();
			for (String header : headers)
				widths.add(header.length());
			for (Rolle r : rollen) {
			    List<String> row = Arrays.asList(
			        Formater.printID(r.getRolleID(), 5),
			        r.getBezeichnung() != null ? r.getBezeichnung() : "-",
			        r.getKommentar() != null ? r.getKommentar() : "-"
			    );
				for (int i = 0; i < headers.size(); i++)
					widths.set(i, Math.max(widths.get(i), row.get(i).length()));
				rows.add(row);
			}
			System.out.println("==== Rollen ====");
			Formater.printTabelle(widths, headers, rows);
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Rollen: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

}
