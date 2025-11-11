package Service;

import java.sql.Connection;
import java.util.Scanner;

import Manager.KursManager;
import Manager.MitgliederManager;
import Manager.MitarbeiterManager;
import Manager.VerkaufManager;
import Manager.VertragManager;
import Objekte.Artikel;
import Objekte.ArtikelBestellung;
import Objekte.Benutzer;
import Objekte.Bestellung;
import Objekte.Intervall;
import Objekte.Kategorie;
import Objekte.Kurs;
import Objekte.Kursleitung;
import Objekte.Kursteilnahme;
import Objekte.Kurstermin;
import Objekte.Mitarbeiter;
import Objekte.Mitglieder;
import Objekte.MitgliederVertrag;
import Objekte.Ort;
import Objekte.Rolle;
import Objekte.Vertrag;
import Objekte.Zahlung;
import Objekte.Zahlungsdaten;

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

	private void verwalteKategorie() {
		try {
			var kategorien = verkaufManager.getKategorieDAO().findAll();

			int maxId = "KategorieID".length();
			int maxBez = "Bezeichnung".length();

			for (Kategorie k : kategorien) {
				maxId = Math.max(maxId, String.valueOf(k.getKategorieID()).length());
				maxBez = Math.max(maxBez, k.getBezeichnung() != null ? k.getBezeichnung().length() : 0);
			}
			System.out.println("==== Kategorie ====");
			String format = "%-" + maxId + "s | %-" + maxBez + "s%n";
			int trennBreite = maxId + maxBez + 3;

			System.out.printf(format, "KategorieID", "Bezeichnung");
			System.out.println("-".repeat(trennBreite));
			for (Kategorie k : kategorien) {
				System.out.printf(format, k.getKategorieID(), k.getBezeichnung());
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Kategorien: " + e.getMessage());
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

	private void verwalteArtikel() {
		try {
			var artikelList = verkaufManager.getArtikelDAO().findAll();

			int maxId = "ArtikelID".length();
			int maxName = "Name".length();
			int maxPreis = "Preis".length();
			int maxKategorie = "Kategorie".length();
			int maxKommentar = "Kommentar".length();

			for (Artikel a : artikelList) {
				maxId = Math.max(maxId, String.valueOf(a.getArtikelID()).length());
				maxName = Math.max(maxName, a.getName() != null ? a.getName().length() : 0);
				maxPreis = Math.max(maxPreis, String.format("%.2f", a.getEinzelpreis()).length());
				String kategorie = (a.getKategorie() != null ? a.getKategorie().getBezeichnung() : "-");
				maxKategorie = Math.max(maxKategorie, kategorie != null ? kategorie.length() : 0);
				maxKommentar = Math.max(maxKommentar, a.getKommentar() != null ? a.getKommentar().length() : 0);
			}
			System.out.println("==== Artikel ====");

			String format = "%-" + maxId + "s | %-" + maxName + "s | %" + maxPreis + "s | %-" + maxKategorie + "s | %-"
					+ maxKommentar + "s%n";
			int trennBreite = maxId + maxName + maxPreis + maxKategorie + maxKommentar + 13;

			System.out.printf(format, "ArtikelID", "Name", "Preis", "Kategorie", "Kommentar");
			System.out.println("-".repeat(trennBreite));
			for (Artikel a : artikelList) {
				String name = a.getName() != null ? a.getName() : "-";
				String preis = String.format("%.2f", a.getEinzelpreis());
				String kategorie = (a.getKategorie() != null ? a.getKategorie().getBezeichnung() : "-");
				String kommentar = a.getKommentar() != null ? a.getKommentar() : "-";
				System.out.printf(format, a.getArtikelID(), name, preis, kategorie, kommentar);
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Artikel: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteArtikelBestellung() {
		try {
			var list = verkaufManager.getArtikelBestellungDAO().findAll();

			int maxBest = "BestellungID".length();
			int maxArt = "ArtikelID".length();
			int maxMenge = "Menge".length();
			int maxAufaddiert = "Aufaddiert".length();

			for (ArtikelBestellung ab : list) {
				maxBest = Math.max(maxBest, String.valueOf(ab.getBestellungID()).length());
				maxArt = Math.max(maxArt, String.valueOf(ab.getArtikelID()).length());
				maxMenge = Math.max(maxMenge, String.valueOf(ab.getMenge()).length());
				maxAufaddiert = Math.max(maxAufaddiert, String.format("%.2f", ab.getAufaddiert()).length());
			}
			System.out.println("==== ArtikelBestellung ====");

			String format = "%-" + maxBest + "s | %-" + maxArt + "s | %-" + maxMenge + "s | %" + maxAufaddiert + "s%n";
			int trennBreite = maxBest + maxArt + maxMenge + maxAufaddiert + 11;

			System.out.printf(format, "BestellungID", "ArtikelID", "Menge", "Aufaddiert");
			System.out.println("-".repeat(trennBreite));
			for (ArtikelBestellung ab : list) {
				System.out.printf(format, ab.getBestellungID(), ab.getArtikelID(), ab.getMenge(),
						String.format("%.2f", ab.getAufaddiert()));
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der ArtikelBestellungen: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteBestellung() {
		try {
			var bestellungen = verkaufManager.getBestellungDAO().findAll();

			int maxId = "BestellungID".length();
			int maxMitglied = "MitgliedID".length();
			int maxPreis = "Gesamtpreis".length();

			for (Bestellung b : bestellungen) {
				maxId = Math.max(maxId, String.valueOf(b.getBestellungID()).length());
				maxMitglied = Math.max(maxMitglied, String.valueOf(b.getMitgliederID()).length());
				maxPreis = Math.max(maxPreis, String.format("%.2f", b.getGesamtpreis()).length());
			}
			System.out.println("==== Bestellung ====");

			String format = "%-" + maxId + "s | %-" + maxMitglied + "s | %" + maxPreis + "s%n";
			int trennBreite = maxId + maxMitglied + maxPreis + 7;

			System.out.printf(format, "BestellungID", "MitgliedID", "Gesamtpreis");
			System.out.println("-".repeat(trennBreite));
			for (Bestellung b : bestellungen) {
				System.out.printf(format, b.getBestellungID(), b.getMitgliederID(),
						String.format("%.2f", b.getGesamtpreis()));
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Bestellungen: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteZahlung() {
		try {
			var zahlungen = verkaufManager.getZahlungDAO().findAll();

			int maxId = "ZahlungID".length();
			int maxArt = "Zahlungsart".length();

			for (Zahlung z : zahlungen) {
				maxId = Math.max(maxId, String.valueOf(z.getZahlungID()).length());
				maxArt = Math.max(maxArt, z.getZahlungsart() != null ? z.getZahlungsart().length() : 0);
			}
			System.out.println("==== Zahlung ====");

			String format = "%-" + maxId + "s | %-" + maxArt + "s%n";
			int trennBreite = maxId + maxArt + 3;

			System.out.printf(format, "ZahlungID", "Zahlungsart");
			System.out.println("-".repeat(trennBreite));
			for (Zahlung z : zahlungen) {
				System.out.printf(format, z.getZahlungID(), z.getZahlungsart());
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Zahlungen: " + e.getMessage());
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

			int maxId = "VertragID".length();
			int maxBezeichnung = "Bezeichnung".length();

			for (Vertrag v : vertragListe) {
				maxId = Math.max(maxId, String.valueOf(v.getVertragID()).length());
				maxBezeichnung = Math.max(maxBezeichnung, v.getBezeichnung() != null ? v.getBezeichnung().length() : 0);
			}
			System.out.println("==== Vertrag ====");
			String format = "%-" + maxId + "s | %-" + maxBezeichnung + "s%n";
			int trennBreite = maxId + maxBezeichnung + 3;

			System.out.printf(format, "VertragID", "Name");
			System.out.println("-".repeat(trennBreite));
			for (Vertrag v : vertragListe) {
				System.out.printf(format, v.getVertragID(), v.getBezeichnung());
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Verträge: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteIntervall() {
		try {
			var intervallListe = vertragManager.getIntervallDAO().findAll();

			int maxId = "IntervallID".length();
			int maxBeschreibung = "Beschreibung".length();

			for (Intervall i : intervallListe) {
				maxId = Math.max(maxId, String.valueOf(i.getIntervallID()).length());
				maxBeschreibung = Math.max(maxBeschreibung,
						i.getBezeichnung() != null ? i.getBezeichnung().length() : 0);
			}
			System.out.println("==== Intervall ====");
			String format = "%-" + maxId + "s | %-" + maxBeschreibung + "s%n";
			int trennBreite = maxId + maxBeschreibung + 3;

			System.out.printf(format, "IntervallID", "Beschreibung");
			System.out.println("-".repeat(trennBreite));
			for (Intervall i : intervallListe) {
				System.out.printf(format, i.getIntervallID(), i.getBezeichnung());
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Intervalle: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteMitgliederVertrag() {
		try {
			var mitgliederVertragListe = vertragManager.getMitgliederVertragDAO().findAll();

			int maxMitgliederId = "MitgliedID".length();
			int maxVertragId = "VertragID".length();

			for (MitgliederVertrag mv : mitgliederVertragListe) {
				maxMitgliederId = Math.max(maxMitgliederId, String.valueOf(mv.getMitgliederID()).length());
				maxVertragId = Math.max(maxVertragId, String.valueOf(mv.getVertragID()).length());
			}
			System.out.println("==== MitgliederVertrag ====");
			String format = "%-" + maxMitgliederId + "s | %-" + maxVertragId + "s%n";
			int trennBreite = maxMitgliederId + maxVertragId + 3;

			System.out.printf(format, "MitgliedID", "VertragID");
			System.out.println("-".repeat(trennBreite));
			for (MitgliederVertrag mv : mitgliederVertragListe) {
				System.out.printf(format, mv.getMitgliederID(), mv.getVertragID());
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der MitgliederVerträge: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteKurs() {
		try {
			var kurse = kursManager.getKursDAO().findAll();
			int maxId = "KursID".length();
			int maxBez = "Bezeichnung".length();

			for (Kurs k : kurse) {
				maxId = Math.max(maxId, String.valueOf(k.getKursID()).length());
				maxBez = Math.max(maxBez, k.getBezeichnung() != null ? k.getBezeichnung().length() : 0);
			}

			String format = "%-" + maxId + "s | %-" + maxBez + "s%n";
			System.out.println("==== Kurse ====");
			System.out.printf(format, "KursID", "Bezeichnung");
			System.out.println("-".repeat(maxId + maxBez + 3));
			for (Kurs k : kurse) {
				System.out.printf(format, k.getKursID(), k.getBezeichnung());
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Kurse: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteKurstermin() {
		try {
			var termine = kursManager.getKursterminDAO().findAll();
			int maxId = "KursterminID".length();
			int maxDatum = "Termin".length();

			for (Kurstermin kt : termine) {
				maxId = Math.max(maxId, String.valueOf(kt.getKursterminID()).length());
				maxDatum = Math.max(maxDatum, kt.getTermin().toString().length());
			}

			String format = "%-" + maxId + "s | %-" + maxDatum + "s%n";
			System.out.println("==== Kurstermine ====");
			System.out.printf(format, "KursterminID", "Termin");
			System.out.println("-".repeat(maxId + maxDatum + 3));
			for (Kurstermin kt : termine) {
				System.out.printf(format, kt.getKursterminID(), kt.getTermin());
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Kurstermine: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteKursteilnahme() {
		try {
			var teilnahmen = kursManager.getKursteilnahmeDAO().findAll();
			int maxMitgliederId = "MitgliederID".length();
			int maxTerminId = "KursterminID".length();

			for (Kursteilnahme kt : teilnahmen) {
				maxMitgliederId = Math.max(maxMitgliederId, String.valueOf(kt.getMitgliederID()).length());
				maxTerminId = Math.max(maxTerminId, String.valueOf(kt.getKursterminID()).length());
			}

			String format = "%-" + maxMitgliederId + "s | %-" + maxTerminId + "s%n";
			System.out.println("==== Kursteilnahmen ====");
			System.out.printf(format, "MitgliederID", "KursterminID");
			System.out.println("-".repeat(maxMitgliederId + maxTerminId + 3));
			for (Kursteilnahme kt : teilnahmen) {
				System.out.printf(format, kt.getMitgliederID(), kt.getKursterminID());
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Kursteilnahmen: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteKursleitung() {
		try {
			var leitungen = kursManager.getKursleitungDAO().findAll();
			int maxTerminId = "KursterminID".length();
			int maxMitarbeiterId = "MitarbeiterID".length();

			for (Kursleitung kl : leitungen) {
				maxTerminId = Math.max(maxTerminId, String.valueOf(kl.getKursterminID()).length());
				maxMitarbeiterId = Math.max(maxMitarbeiterId, String.valueOf(kl.getMitarbeiterID()).length());
			}

			String format = "%-" + maxTerminId + "s | %-" + maxMitarbeiterId + "s%n";
			System.out.println("==== Kursleitungen ====");
			System.out.printf(format, "KursterminID", "MitarbeiterID");
			System.out.println("-".repeat(maxTerminId + maxMitarbeiterId + 3));
			for (Kursleitung kl : leitungen) {
				System.out.printf(format, kl.getKursterminID(), kl.getMitarbeiterID());
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Kursleitungen: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteMitglieder() {
		try {
			var mitglieder = mitgliederManager.getMitgliederDAO().findAll();

			int maxId = "MitgliedID".length();
			int maxVorname = "Vorname".length();
			int maxNachname = "Nachname".length();
			int maxGeburtsdatum = "Geburtsdatum".length();
			int maxAktiv = "Aktiv".length();
			int maxStrasse = "Strasse".length();
			int maxHausnr = "Hausnr".length();
			int maxOrtID = "OrtID".length();
			int maxZahlungsdatenID = "ZahlungsdatenID".length();
			int maxTelefon = "Telefon".length();
			int maxMail = "Mail".length();

			for (Mitglieder m : mitglieder) {
				maxId = Math.max(maxId, String.valueOf(m.getMitgliederID()).length());
				maxVorname = Math.max(maxVorname, m.getVorname() != null ? m.getVorname().length() : 0);
				maxNachname = Math.max(maxNachname, m.getNachname() != null ? m.getNachname().length() : 0);
				maxGeburtsdatum = Math.max(maxGeburtsdatum,
						m.getGeburtsdatum() != null ? m.getGeburtsdatum().toString().length() : 0);
				maxAktiv = Math.max(maxAktiv, String.valueOf(m.isAktiv()).length());
				maxStrasse = Math.max(maxStrasse, m.getStrasse() != null ? m.getStrasse().length() : 0);
				maxHausnr = Math.max(maxHausnr, m.getHausnr() != null ? m.getHausnr().length() : 0);
				maxOrtID = Math.max(maxOrtID, String.valueOf(m.getOrtID()).length());
				maxZahlungsdatenID = Math.max(maxZahlungsdatenID, String.valueOf(m.getZahlungsdatenID()).length());
				maxTelefon = Math.max(maxTelefon, m.getTelefon() != null ? m.getTelefon().length() : 0);
				maxMail = Math.max(maxMail, m.getMail() != null ? m.getMail().length() : 0);
			}

			String format = "%-" + maxId + "s | %-" + maxVorname + "s | %-" + maxNachname + "s | %-" + maxGeburtsdatum
					+ "s | %-" + maxAktiv + "s | %-" + maxStrasse + "s | %-" + maxHausnr + "s | %-" + maxOrtID
					+ "s | %-" + maxZahlungsdatenID + "s | %-" + maxTelefon + "s | %-" + maxMail + "s%n";

			int trennBreite = maxId + maxVorname + maxNachname + maxGeburtsdatum + maxAktiv + maxStrasse + maxHausnr
					+ maxOrtID + maxZahlungsdatenID + maxTelefon + maxMail + 11 * 3; // für Trennzeichen

			System.out.println("==== Mitglieder ====");
			System.out.printf(format, "MitgliedID", "Vorname", "Nachname", "Geburtsdatum", "Aktiv", "Strasse", "Hausnr",
					"OrtID", "ZahlungsdatenID", "Telefon", "Mail");
			System.out.println("-".repeat(trennBreite));
			for (Mitglieder m : mitglieder) {
				System.out.printf(format, m.getMitgliederID(), m.getVorname(), m.getNachname(), m.getGeburtsdatum(),
						m.isAktiv(), m.getStrasse(), m.getHausnr(), m.getOrtID(), m.getZahlungsdatenID(),
						m.getTelefon(), m.getMail());
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Mitglieder: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteMitarbeiter() {
		try {
			var mitarbeiter = mitarbeiterManager.getMitarbeiterDAO().findAll();

			int maxId = "MitarbeiterID".length();
			int maxVorname = "Vorname".length();
			int maxNachname = "Nachname".length();
			int maxGeburtsdatum = "Geburtsdatum".length();
			int maxAktiv = "Aktiv".length();
			int maxStrasse = "Strasse".length();
			int maxHausnr = "Hausnr".length();
			int maxTelefon = "Telefon".length();
			int maxMail = "Mail".length();
			int maxOrtID = "OrtID".length();
			int maxOrtName = "Ort".length();
			int maxZahlungsdatenID = "ZahlungsdatenID".length();
			int maxZName = "Z-Name".length();
			int maxZIBAN = "IBAN".length();
			int maxZBIC = "BIC".length();
			int maxBenutzerID = "BenutzerID".length();
			int maxBenutzername = "Benutzername".length();
			int maxRolleID = "RolleID".length();
			int maxRolleBez = "Rolle".length();

			for (Mitarbeiter ma : mitarbeiter) {
				maxId = Math.max(maxId, String.valueOf(ma.getMitarbeiterID()).length());
				maxVorname = Math.max(maxVorname, ma.getVorname() != null ? ma.getVorname().length() : 0);
				maxNachname = Math.max(maxNachname, ma.getNachname() != null ? ma.getNachname().length() : 0);
				maxGeburtsdatum = Math.max(maxGeburtsdatum,
						ma.getGeburtsdatum() != null ? ma.getGeburtsdatum().toString().length() : 0);
				maxAktiv = Math.max(maxAktiv, String.valueOf(ma.isAktiv()).length());
				maxStrasse = Math.max(maxStrasse, ma.getStraße() != null ? ma.getStraße().length() : 0);
				maxHausnr = Math.max(maxHausnr, ma.getHausnr() != null ? ma.getHausnr().length() : 0);
				maxTelefon = Math.max(maxTelefon, ma.getTelefon() != null ? ma.getTelefon().length() : 0);
				maxMail = Math.max(maxMail, ma.getMail() != null ? ma.getMail().length() : 0);
				if (ma.getOrt() != null) {
					maxOrtID = Math.max(maxOrtID, String.valueOf(ma.getOrt().getOrtID()).length());
					maxOrtName = Math.max(maxOrtName, ma.getOrt().getOrt() != null ? ma.getOrt().getOrt().length() : 0);
				}
				if (ma.getZahlungsdaten() != null) {
					maxZahlungsdatenID = Math.max(maxZahlungsdatenID,
							String.valueOf(ma.getZahlungsdaten().getZahlungsdatenID()).length());
					maxZName = Math.max(maxZName,
							ma.getZahlungsdaten().getName() != null ? ma.getZahlungsdaten().getName().length() : 0);
					maxZIBAN = Math.max(maxZIBAN,
							ma.getZahlungsdaten().getIBAN() != null ? ma.getZahlungsdaten().getIBAN().length() : 0);
					maxZBIC = Math.max(maxZBIC,
							ma.getZahlungsdaten().getBIC() != null ? ma.getZahlungsdaten().getBIC().length() : 0);
				}
				if (ma.getBenutzer() != null) {
					maxBenutzerID = Math.max(maxBenutzerID, String.valueOf(ma.getBenutzer().getBenutzerID()).length());
					maxBenutzername = Math.max(maxBenutzername,
							ma.getBenutzer().getBenutzername() != null ? ma.getBenutzer().getBenutzername().length()
									: 0);
					if (ma.getBenutzer().getRolle() != null) {
						maxRolleID = Math.max(maxRolleID,
								String.valueOf(ma.getBenutzer().getRolle().getRolleID()).length());
						maxRolleBez = Math.max(maxRolleBez,
								ma.getBenutzer().getRolle().getBezeichnung() != null
										? ma.getBenutzer().getRolle().getBezeichnung().length()
										: 0);
					}
				}
			}

			String format = "%-" + maxId + "s | %-" + maxVorname + "s | %-" + maxNachname + "s | %-" + maxGeburtsdatum
					+ "s | %-" + maxAktiv + "s | %-" + maxStrasse + "s | %-" + maxHausnr + "s | %-" + maxTelefon
					+ "s | %-" + maxMail + "s | %-" + maxOrtID + "s | %-" + maxOrtName + "s | %-" + maxZahlungsdatenID
					+ "s | %-" + maxZName + "s | %-" + maxZIBAN + "s | %-" + maxZBIC + "s | %-" + maxBenutzerID
					+ "s | %-" + maxBenutzername + "s | %-" + maxRolleID + "s | %-" + maxRolleBez + "s%n";

			int trennBreite = maxId + maxVorname + maxNachname + maxGeburtsdatum + maxAktiv + maxStrasse + maxHausnr
					+ maxTelefon + maxMail + maxOrtID + maxOrtName + maxZahlungsdatenID + maxZName + maxZIBAN + maxZBIC
					+ maxBenutzerID + maxBenutzername + maxRolleID + maxRolleBez + 19 * 3;

			System.out.println("==== Mitarbeiter ====");
			System.out.printf(format, "MitarbeiterID", "Vorname", "Nachname", "Geburtsdatum", "Aktiv", "Strasse",
					"Hausnr", "Telefon", "Mail", "OrtID", "Ort", "ZahlungsdatenID", "Z-Name", "IBAN", "BIC",
					"BenutzerID", "Benutzername", "RolleID", "Rolle");
			System.out.println("-".repeat(trennBreite));
			for (Mitarbeiter ma : mitarbeiter) {
				Ort ort = ma.getOrt();
				Zahlungsdaten zd = ma.getZahlungsdaten();
				Benutzer benutzer = ma.getBenutzer();
				Rolle rolle = benutzer != null ? benutzer.getRolle() : null;
				System.out.printf(format, ma.getMitarbeiterID(), ma.getVorname(), ma.getNachname(),
						ma.getGeburtsdatum(), ma.isAktiv(), ma.getStraße(), ma.getHausnr(), ma.getTelefon(),
						ma.getMail(), ort != null ? ort.getOrtID() : "-", ort != null ? ort.getOrt() : "-",
						zd != null ? zd.getZahlungsdatenID() : "-", zd != null ? zd.getName() : "-",
						zd != null ? zd.getIBAN() : "-", zd != null ? zd.getBIC() : "-",
						benutzer != null ? benutzer.getBenutzerID() : "-",
						benutzer != null ? benutzer.getBenutzername() : "-", rolle != null ? rolle.getRolleID() : "-",
						rolle != null ? rolle.getBezeichnung() : "-");
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Mitarbeiter: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteOrt() {
		try {
			var orte = mitarbeiterManager.getOrtDAO().findAll();
			int maxId = "OrtID".length();
			int maxPLZ = "PLZ".length();
			int maxOrt = "Ort".length();

			for (Ort o : orte) {
				maxId = Math.max(maxId, String.valueOf(o.getOrtID()).length());
				maxPLZ = Math.max(maxPLZ, o.getPLZ() != null ? o.getPLZ().length() : 0);
				maxOrt = Math.max(maxOrt, o.getOrt() != null ? o.getOrt().length() : 0);
			}

			String format = "%-" + maxId + "s | %-" + maxPLZ + "s | %-" + maxOrt + "s%n";
			System.out.println("==== Orte ====");
			System.out.printf(format, "OrtID", "PLZ", "Ort");
			System.out.println("-".repeat(maxId + maxPLZ + maxOrt + 6));
			for (Ort o : orte) {
				System.out.printf(format, o.getOrtID(), o.getPLZ(), o.getOrt());
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Orte: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteZahlungsdaten() {
		try {
			var zahlungen = mitarbeiterManager.getZahlungsdatenDAO().findAll();
			int maxId = "ZahlungsdatenID".length();
			int maxName = "Name".length();

			for (Zahlungsdaten z : zahlungen) {
				maxId = Math.max(maxId, String.valueOf(z.getZahlungsdatenID()).length());
				maxName = Math.max(maxName, z.getName() != null ? z.getName().length() : 0);
			}

			String format = "%-" + maxId + "s | %-" + maxName + "s%n";
			System.out.println("==== Zahlungsdaten ====");
			System.out.printf(format, "ZahlungsdatenID", "Name");
			System.out.println("-".repeat(maxId + maxName + 3));
			for (Zahlungsdaten z : zahlungen) {
				System.out.printf(format, z.getZahlungsdatenID(), z.getName());
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Zahlungsdaten: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteBenutzer() {
		try {
			var benutzer = mitarbeiterManager.getBenutzerDAO().findAll();
			int maxId = "BenutzerID".length();
			int maxName = "Benutzername".length();

			for (Benutzer b : benutzer) {
				maxId = Math.max(maxId, String.valueOf(b.getBenutzerID()).length());
				maxName = Math.max(maxName, b.getBenutzername() != null ? b.getBenutzername().length() : 0);
			}

			String format = "%-" + maxId + "s | %-" + maxName + "s%n";
			System.out.println("==== Benutzer ====");
			System.out.printf(format, "BenutzerID", "Benutzername");
			System.out.println("-".repeat(maxId + maxName + 3));
			for (Benutzer b : benutzer) {
				System.out.printf(format, b.getBenutzerID(), b.getBenutzername());
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Benutzer: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

	private void verwalteRolle() {
		try {
			var rollen = mitarbeiterManager.getRolleDAO().findAll();
			int maxId = "RolleID".length();
			int maxName = "Bezeichnung".length();

			for (Rolle r : rollen) {
				maxId = Math.max(maxId, String.valueOf(r.getRolleID()).length());
				maxName = Math.max(maxName, r.getBezeichnung() != null ? r.getBezeichnung().length() : 0);
			}

			String format = "%-" + maxId + "s | %-" + maxName + "s%n";
			System.out.println("==== Rollen ====");
			System.out.printf(format, "RolleID", "Bezeichnung");
			System.out.println("-".repeat(maxId + maxName + 3));
			for (Rolle r : rollen) {
				System.out.printf(format, r.getRolleID(), r.getBezeichnung());
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Rollen: " + e.getMessage());
		}
		System.out.println("1 Einfügen | 2 Ändern | 3 Löschen | 4 Zurück");
	}

}
