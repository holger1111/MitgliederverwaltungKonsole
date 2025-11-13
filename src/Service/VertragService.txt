package Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import DAOs.IntervallDAO;
import DAOs.MitarbeiterDAO;
import DAOs.MitgliederDAO;
import DAOs.MitgliederVertragDAO;
import DAOs.OrtDAO;
import DAOs.VertragDAO;
import DAOs.ZahlungDAO;
import DAOs.ZahlungsdatenDAO;
import Exception.IntException;
import Exception.StringException;
import Exception.TooLongException;
import Exception.TooShortException;
import Objekte.Intervall;
import Objekte.Mitarbeiter;
import Objekte.Mitglieder;
import Objekte.MitgliederVertrag;
import Objekte.Ort;
import Objekte.Vertrag;
import Objekte.Zahlung;
import Objekte.Zahlungsdaten;

public class VertragService extends BaseService {

	// ========== DAOs ==========
	private final OrtDAO ortDAO;
	private final ZahlungsdatenDAO zahlungsdatenDAO;
	private final MitgliederDAO mitgliederDAO;
	private final VertragDAO vertragDAO;
	private final IntervallDAO intervallDAO;
	private final MitgliederVertragDAO mitgliederVertragDAO;
	private final ZahlungDAO zahlungDAO;

	// ========== KONSTRUKTOR ==========
	public VertragService(Connection connection, Scanner scanner) {
		super(connection, scanner);
		this.ortDAO = new OrtDAO(connection);
		this.zahlungsdatenDAO = new ZahlungsdatenDAO(connection);
		this.mitgliederDAO = new MitgliederDAO(connection);
		this.vertragDAO = new VertragDAO(connection);
		this.intervallDAO = new IntervallDAO(connection);
		this.mitgliederVertragDAO = new MitgliederVertragDAO(connection);
		this.zahlungDAO = new ZahlungDAO(connection);
	}
	// ========================
	// Start Methoden
	// ========================

	public void start() {
		boolean exit = false;
		while (!exit) {
			System.out.println("==== Vertragsverwaltung ====");
			System.out.println("1 - Neukunde anlegen");
			System.out.println("2 - Vertrag verlängern");
			System.out.println("3 - Vertrag kündigen");
			System.out.println("4 - Zurück zum Hauptmenü");
			System.out.print("Bitte wählen: ");
			String eingabe = scanner.nextLine();

			switch (eingabe) {
			case "1":
				try {
					neukundeAnlegen();
					break;
				} catch (TooLongException | IntException | SQLException | TooShortException | StringException e) {
					e.printStackTrace();
				}
			case "2":
			case "3":
				System.out.println("WIP");
				break;
			case "4":
				exit = true;
				break;
			default:
				System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
			}
		}
	}

	public double berechneBetragJeIntervall(Vertrag vertrag, MitgliederVertrag mv, int IntervallID) {
		double basis = vertrag.getGrundpreis() - mv.getPreisrabatt();
		switch (IntervallID) {
		case 3:
			return basis;
		case 4:
			return basis * 2;
		case 1:
		case 2:
			return basis * 53.0 / 12.0;
		default:
			return 0;
		}
	}

	public Date berechneZahlungsbeginn(Date vertragsbeginn, int IntervallID) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(vertragsbeginn);
		switch (IntervallID) {
		case 3:
			cal.add(Calendar.DAY_OF_MONTH, 7);
			break;
		case 4:
			cal.add(Calendar.DAY_OF_MONTH, 14);
			break;
		case 1:
			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			break;
		case 2:
			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			break;
		default:
			return null;
		}
		return cal.getTime();
	}

	// ========================
	// Neukunde Anlegen Methode
	// ========================

	private void neukundeAnlegen()
	        throws SQLException, StringException, IntException, TooShortException, TooLongException {
	    Mitglieder mitglied = new Mitglieder();
	    System.out.println("==== Neukunde anlegen ====");

	    // --- Stammdaten erfassen ---
	    System.out.print("Vorname: ");
	    mitglied.setVorname(scanner.nextLine());
	    System.out.print("Nachname: ");
	    mitglied.setNachname(scanner.nextLine());
	    System.out.print("Straße: ");
	    mitglied.setStrasse(scanner.nextLine());
	    System.out.print("Hausnummer: ");
	    mitglied.setHausnr(scanner.nextLine());
	    System.out.print("PLZ (5-stellig): ");
	    String plz = scanner.nextLine();
	    while (plz.length() != 5) {
	        System.out.println("✗ PLZ muss genau 5 Zeichen lang sein.");
	        System.out.print("PLZ (5-stellig): ");
	        plz = scanner.nextLine();
	    }
	    System.out.print("Ort: ");
	    String ortName = scanner.nextLine();
	    int ortId = ortDAO.findOrCreateOrt(plz, ortName);
	    Ort ortObjekt = ortDAO.findById(ortId);
	    mitglied.setOrt(ortObjekt);
	    System.out.print("Geburtsdatum (TT.MM.JJJJ): ");
	    String geburtsdatumString = scanner.nextLine();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy"), sdfView = new SimpleDateFormat("dd.MM.yyyy");
	    try {
	        Date geburtsdatum = sdf.parse(geburtsdatumString);
	        mitglied.setGeburtsdatum(geburtsdatum);
	    } catch (ParseException e) {
	        System.out.println("Ungültiges Datumsformat! Bitte TT.MM.JJJJ eingeben.");
	    }
	    System.out.print("Telefon: ");
	    mitglied.setTelefon(scanner.nextLine());
	    System.out.print("E-Mail: ");
	    mitglied.setMail(scanner.nextLine());

	    boolean bearbeiten = true;
	    while (bearbeiten) {
	        System.out.println("==== Abfrage Stammdaten ====");
	        System.out.println("1 Vorname:\t\t" + mitglied.getVorname());
	        System.out.println("2 Nachname:\t\t" + mitglied.getNachname());
	        System.out.println("3 Straße:\t\t" + mitglied.getStrasse() + " " + mitglied.getHausnr());
	        System.out.println("4 Ort:\t\t\t" + (mitglied.getOrt() != null ? mitglied.getOrt().getPLZ() : "-") + " "
	                + (mitglied.getOrt() != null ? mitglied.getOrt().getOrt() : "-"));
	        System.out.println("5 Geburtsdatum:\t\t" + (mitglied.getGeburtsdatum() != null ? sdfView.format(mitglied.getGeburtsdatum()) : "-"));
	        System.out.println("6 Telefon:\t\t" + mitglied.getTelefon());
	        System.out.println("7 Mail:\t\t\t" + mitglied.getMail());
	        System.out.println("Eingabe bestätigen? (Enter) oder Nummer zum Bearbeiten: ");
	        String eingabe = scanner.nextLine();
	        if (eingabe.isEmpty()) {
	            bearbeiten = false;
	        } else {
	            int nummer = Integer.parseInt(eingabe);
	            switch (nummer) {
	                case 1:
	                    System.out.print("Vorname: ");
	                    mitglied.setVorname(scanner.nextLine());
	                    break;
	                case 2:
	                    System.out.print("Nachname: ");
	                    mitglied.setNachname(scanner.nextLine());
	                    break;
	                case 3:
	                    System.out.print("Straße: ");
	                    mitglied.setStrasse(scanner.nextLine());
	                    System.out.print("Hausnummer: ");
	                    mitglied.setHausnr(scanner.nextLine());
	                    break;
	                case 4:
	                    System.out.print("PLZ (5-stellig): ");
	                    plz = scanner.nextLine();
	                    while (plz.length() != 5) {
	                        System.out.println("✗ PLZ muss genau 5 Zeichen lang sein.");
	                        System.out.print("PLZ (5-stellig): ");
	                        plz = scanner.nextLine();
	                    }
	                    System.out.print("Ort: ");
	                    ortName = scanner.nextLine();
	                    ortId = ortDAO.findOrCreateOrt(plz, ortName);
	                    ortObjekt = ortDAO.findById(ortId);
	                    mitglied.setOrt(ortObjekt);
	                    break;
	                case 5:
	                    System.out.print("Geburtsdatum (TT.MM.JJJJ): ");
	                    geburtsdatumString = scanner.nextLine();
	                    try {
	                        Date geburtsdatum = sdf.parse(geburtsdatumString);
	                        mitglied.setGeburtsdatum(geburtsdatum);
	                    } catch (ParseException e) {
	                        System.out.println("Ungültiges Datum! Bitte TT.MM.JJJJ.");
	                    }
	                    break;
	                case 6:
	                    System.out.print("Telefon: ");
	                    mitglied.setTelefon(scanner.nextLine());
	                    break;
	                case 7:
	                    System.out.print("E-Mail: ");
	                    mitglied.setMail(scanner.nextLine());
	                    break;
	                default:
	                    System.out.println("Ungültige Auswahl!");
	            }
	        }
	    }
	    System.out.println("✓ Kundendaten erfasst. Weiter zu Vertragsdaten...");

	    // --- Vertragsdaten erfassen ---
	    Vertrag vertrag = null;
	    MitgliederVertrag mv = new MitgliederVertrag();
	    System.out.println("==== Vertragsdaten ====");
	    System.out.println("==== Verträge Übersicht ====");
	    List<Vertrag> vertragListe = vertragDAO.findAll();
	    System.out.printf("%-5s %-20s %-10s %-12s\n", "ID", "Bezeichnung", "Preis", "Laufzeit");
	    for (Vertrag v : vertragListe) {
	        System.out.printf("%-5d %-20s %-10.2f %-12d\n",
	        v.getVertragID(), v.getBezeichnung(), v.getGrundpreis(), v.getLaufzeit());
	    }
	    System.out.print("VertragID auswählen: ");
	    int ausgewaehlteVertragID = Integer.parseInt(scanner.nextLine());
	    vertrag = vertragDAO.findById(ausgewaehlteVertragID);
	    if (vertrag == null) {
	        System.out.println("Ungültige VertragID! Prozess abgebrochen.");
	        return;
	    }
	    System.out.print("Vertragsbeginn (TT.MM.JJJJ): ");
	    String vertragsbeginnString = scanner.nextLine();
	    try {
	        Date vertragsbeginn = sdf.parse(vertragsbeginnString);
	        mv.setVertragsbeginn(vertragsbeginn);
	        if (vertrag != null && vertragsbeginn != null) {
	            Calendar cal = Calendar.getInstance();
	            cal.setTime(vertragsbeginn);
	            cal.add(Calendar.WEEK_OF_YEAR, vertrag.getLaufzeit());
	            mv.setVertragsende(cal.getTime());
	        }
	    } catch (ParseException e) {
	        System.out.println("Ungültiges Format! Bitte TT.MM.JJJJ verwenden.");
	    }
	    System.out.print("Preisrabatt (€): ");
	    String rabattEingabe = scanner.nextLine().replace(',', '.');
	    try {
	        mv.setPreisrabatt(Double.parseDouble(rabattEingabe));
	    } catch (NumberFormatException e) {
	        System.out.println("✗ Ungültiges Format! Bitte mit Punkt oder Komma schreiben, z.B. 5.20 oder 5,20");
	        mv.setPreisrabatt(0.0);
	    }
	    System.out.print("Kommentar: ");
	    mv.setKommentar(scanner.nextLine());

	    System.out.println("==== Intervall Übersicht ====");
	    List<Intervall> intervallListe = intervallDAO.findAll();
	    System.out.printf("%-5s %-20s\n", "ID", "Bezeichnung");
	    for (Intervall intervall : intervallListe) {
	        System.out.printf("%-5d %-20s\n", intervall.getIntervallID(), intervall.getBezeichnung());
	    }
	    System.out.print("IntervallID auswählen: ");
	    int ausgewaehlteIntervallID = Integer.parseInt(scanner.nextLine());
	    mv.setIntervallID(ausgewaehlteIntervallID);

	    System.out.print("Trainingsbeginn (TT.MM.JJJJ): ");
	    String trainingsbeginnString = scanner.nextLine();
	    try {
	        Date trainingsbeginn = sdf.parse(trainingsbeginnString);
	        mv.setTrainingsbeginn(trainingsbeginn);
	    } catch (ParseException e) {
	        System.out.println("Ungültiges Format! Bitte TT.MM.JJJJ verwenden.");
	    }

	    boolean vertragsdatenBearbeiten = true;
	    while (vertragsdatenBearbeiten) {
	        System.out.println("==== Abfrage Vertragsdaten ====");
	        System.out.println("1 Vertragsart:\t\t" + vertrag.getBezeichnung());
	        System.out.println("2 Laufzeit (Wochen):\t" + vertrag.getLaufzeit());
	        System.out.println("3 Vertragsbeginn:\t"
	                + (mv.getVertragsbeginn() != null ? sdfView.format(mv.getVertragsbeginn()) : "-"));
	        System.out.println("4 Vertragsende:\t\t"
	                + (mv.getVertragsende() != null ? sdfView.format(mv.getVertragsende()) : "-"));
	        System.out.println("5 Preisrabatt (€):\t" + String.format("%.2f", mv.getPreisrabatt()));
	        System.out.println("6 Kommentar:\t\t" + mv.getKommentar());
	        String intervallBezeichnung = "-";
	        if (mv.getIntervallID() > 0) {
	            Intervall intervallObjekt = intervallDAO.findById(mv.getIntervallID());
	            if (intervallObjekt != null) {
	                intervallBezeichnung = intervallObjekt.getBezeichnung();
	            }
	        }
	        System.out.println("7 Zahlungsintervall:\t" + intervallBezeichnung);
	        System.out.println("8 Trainingsbeginn:\t"
	                + (mv.getTrainingsbeginn() != null ? sdfView.format(mv.getTrainingsbeginn()) : "-"));
	        System.out.println("Eingabe bestätigen? (Enter) oder Nummer zum Bearbeiten: ");
	        String eingabe = scanner.nextLine();
	        if (eingabe.isEmpty()) {
	            vertragsdatenBearbeiten = false;
	        } else {
	            int nummer = Integer.parseInt(eingabe);
	            switch (nummer) {
	                case 3:
	                    System.out.print("Vertragsbeginn (TT.MM.JJJJ): ");
	                    vertragsbeginnString = scanner.nextLine();
	                    try {
	                        Date vertragsbeginn = sdfView.parse(vertragsbeginnString);
	                        mv.setVertragsbeginn(vertragsbeginn);
	                        if (vertrag != null && vertragsbeginn != null) {
	                            Calendar cal = Calendar.getInstance();
	                            cal.setTime(vertragsbeginn);
	                            cal.add(Calendar.WEEK_OF_YEAR, vertrag.getLaufzeit());
	                            mv.setVertragsende(cal.getTime());
	                        }
	                    } catch (ParseException e) {
	                        System.out.println("Ungültiges Format!");
	                    }
	                    break;
	                case 2:
	                    System.out.print("Laufzeit (Wochen): ");
	                    int neueLaufzeit = Integer.parseInt(scanner.nextLine());
	                    vertrag.setLaufzeit(neueLaufzeit);
	                    if (mv.getVertragsbeginn() != null) {
	                        Calendar cal = Calendar.getInstance();
	                        cal.setTime(mv.getVertragsbeginn());
	                        cal.add(Calendar.WEEK_OF_YEAR, vertrag.getLaufzeit());
	                        mv.setVertragsende(cal.getTime());
	                    }
	                    break;
	                case 5:
	                    System.out.print("Preisrabatt (€): ");
	                    rabattEingabe = scanner.nextLine().replace(',', '.');
	                    try {
	                        mv.setPreisrabatt(Double.parseDouble(rabattEingabe));
	                    } catch (NumberFormatException e) {
	                        System.out.println("Ungültiges Format!");
	                    }
	                    break;
	                case 6:
	                    System.out.print("Kommentar: ");
	                    mv.setKommentar(scanner.nextLine());
	                    break;
	                case 7:
	                    System.out.println("==== Intervall Übersicht ====");
	                    intervallListe = intervallDAO.findAll();
	                    System.out.printf("%-5s %-20s\n", "ID", "Bezeichnung");
	                    for (Intervall intervall : intervallListe) {
	                        System.out.printf("%-5d %-20s\n", intervall.getIntervallID(), intervall.getBezeichnung());
	                    }
	                    System.out.print("IntervallID auswählen: ");
	                    mv.setIntervallID(Integer.parseInt(scanner.nextLine()));
	                    break;
	                case 8:
	                    System.out.print("Trainingsbeginn (TT.MM.JJJJ): ");
	                    trainingsbeginnString = scanner.nextLine();
	                    try {
	                        Date trainingsbeginn = sdfView.parse(trainingsbeginnString);
	                        mv.setTrainingsbeginn(trainingsbeginn);
	                    } catch (ParseException e) {
	                        System.out.println("Ungültiges Format!");
	                    }
	                    break;
	                default:
	                    System.out.println("Nicht bearbeitbar oder ungültige Auswahl!");
	            }
	        }
	    }

	    // --- Zahlungsdaten erfassen und im Methodenscope deklarieren ---
	    System.out.println("==== Zahlungsdaten ====");
	    String name, iban, bic;
	    do {
	        System.out.print("Kontoinhaber: ");
	        name = scanner.nextLine().trim();
	        if (name.isEmpty())
	            System.out.println("✗ Kontoinhaber darf nicht leer sein.");
	    } while (name.isEmpty());
	    do {
	        System.out.print("IBAN: ");
	        iban = scanner.nextLine().trim();
	        if (iban.isEmpty())
	            System.out.println("✗ IBAN darf nicht leer sein.");
	    } while (iban.isEmpty());
	    do {
	        System.out.print("BIC: ");
	        bic = scanner.nextLine().trim();
	        if (bic.isEmpty())
	            System.out.println("✗ BIC darf nicht leer sein.");
	    } while (bic.isEmpty());

	    int zahlungsdatenID = zahlungsdatenDAO.findOrCreateZahlungsdaten(name, iban, bic);
	    mitglied.setZahlungsdatenID(zahlungsdatenID);
	    Zahlungsdaten check = zahlungsdatenDAO.findById(zahlungsdatenID);  // EINMAL ANLEGEN UND ÜBERALL NUTZEN

	    System.out.print("MitarbeiterID: ");
	    int mitarbeiterID = Integer.parseInt(scanner.nextLine());

	    // --- Übersicht anzeigen (check wird genutzt!) ---
	    zeigeFinaleUebersicht(
	        mitglied.getVorname(), mitglied.getNachname(), mitglied.getStrasse(), mitglied.getHausnr(),
	        mitglied.getOrt() != null ? mitglied.getOrt().getPLZ() : "-",
	        mitglied.getOrt() != null ? mitglied.getOrt().getOrt() : "-",
	        mitglied.getGeburtsdatum() != null ? sdf.format(mitglied.getGeburtsdatum()) : "-",
	        mitglied.getTelefon(), mitglied.getMail(), vertrag.getBezeichnung(), vertrag.getLaufzeit(),
	        mv.getVertragsbeginn() != null ? sdf.format(mv.getVertragsbeginn()) : "-",
	        mv.getVertragsende() != null ? sdf.format(mv.getVertragsende()) : "-",
	        intervallDAO.findById(mv.getIntervallID()).getBezeichnung(), vertrag.getGrundpreis(),
	        mv.getPreisrabatt(), (vertrag.getGrundpreis() - mv.getPreisrabatt()), mv.getKommentar(),
	        mv.getTrainingsbeginn() != null ? sdf.format(mv.getTrainingsbeginn()) : "-",
	        berechneBetragJeIntervall(vertrag, mv, mv.getIntervallID()),
	        berechneZahlungsbeginn(mv.getVertragsbeginn(), mv.getIntervallID()) != null
	            ? sdf.format(berechneZahlungsbeginn(mv.getVertragsbeginn(), mv.getIntervallID())) : "-",
	        check != null ? check.getName() : "",
	        check != null ? check.getIBAN() : "",
	        check != null ? check.getBIC() : "",
	        mitarbeiterID
	    );

	    // --- Bestätigung und Mitglied+Vertrag speichern ---
	    System.out.print("Vertrag speichern? (ja/nein): ");
	    String bestaetigung = scanner.nextLine();
	    if (bestaetigung.equalsIgnoreCase("ja")) {
	        try {
	            mitgliederDAO.insert(mitglied);
	            Mitglieder geladenesMitglied = mitgliederDAO.findById(mitglied.getMitgliederID());
	            Zahlungsdaten checkFinal = geladenesMitglied != null
	                ? zahlungsdatenDAO.findById(geladenesMitglied.getZahlungsdatenID())
	                : null;

	            if (geladenesMitglied == null) {
	                System.out.println("Fehler: Mitglied nicht gefunden nach Insert!");
	                return;
	            }
	            if (checkFinal == null || checkFinal.getName().isEmpty() || checkFinal.getIBAN().isEmpty() || checkFinal.getBIC().isEmpty()) {
	                System.out.println("✗ Die Zahlungsdaten in der Datenbank sind nicht vollständig für Mitglied (" +
	                        geladenesMitglied.getMitgliederID() + ")!");
	                return;
	            }
	            mv.setMitgliederID(geladenesMitglied.getMitgliederID());
	            mv.setVertragID(vertrag.getVertragID());
	            mv.setVertragsbeginn(mv.getVertragsbeginn());
	            mv.setVertragsende(mv.getVertragsende());
	            mv.setVerlängerung(mv.isVerlängerung());
	            mv.setAktiv(mv.isAktiv());
	            mv.setGekündigt(mv.isGekündigt());
	            mv.setPreisrabatt(mv.getPreisrabatt());
	            mv.setIntervallID(mv.getIntervallID());
	            mv.setZahlungID(3); // z.B. SEPA-Lastschrift
	            Zahlung zahlung = zahlungDAO.findById(mv.getZahlungID());
	            if (zahlung == null) zahlung = new Zahlung();
	            zahlung.setZahlungID(3);
	            zahlung.setZahlungsart("SEPA-Lastschrift");
	            mv.setMitarbeiterID(mitarbeiterID);
	            mv.setTrainingsbeginn(mv.getTrainingsbeginn());
	            mv.setKommentar(mv.getKommentar());

	            mitgliederVertragDAO.insert(mv, vertrag, zahlung, geladenesMitglied, vertrag.getLaufzeit());
	            System.out.println("✓ Vertrag erfolgreich gespeichert.");
	        } catch (Exception e) {
	            System.out.println("Fehler beim Speichern: " + e.getMessage());
	        }
	    } else {
	        System.out.println("Vertrag wurde nicht gespeichert.");
	    }
	}


	// ========================
	// Finale Übersicht anzeigen
	// ========================

	private void zeigeFinaleUebersicht(String vorname, String nachname, String strasse, String hausnr, String plz,
			String ort, String geburtsdatum, String telefon, String mail, String bezeichnung, int laufzeit,
			String vertragsbeginn, String vertragsende, String intervallBezeichnung, double grundpreis,
			double preisrabatt, double wochenpreis, String kommentar, String trainingsbeginn, double betragJeIntervall,
			String zahlungsbeginn, String kontoinhaber, String iban, String bic, int mitarbeiterID)
			throws SQLException {

		MitarbeiterDAO mitarbeiterDAO = new MitarbeiterDAO(connection);
		String mitarbeiterName = "Unbekannt";
		try {
			Mitarbeiter m = mitarbeiterDAO.findById(mitarbeiterID);
			if (m != null) {
				mitarbeiterName = m.getVorname() + " " + m.getNachname();
			}
		} catch (Exception e) {
			System.out.println("Fehler beim Laden des Mitarbeiters: " + e.getMessage());
		}

		System.out.println("==================================================");
		System.out.println("          Gesamtübersicht - Vertragsdaten");
		System.out.println("==================================================");
		System.out.println("\nKundendaten:");
		System.out.println("   Vorname:\t\t" + vorname);
		System.out.println("   Nachname:\t\t" + nachname);
		System.out.println("   Geburtstag:\t\t" + geburtsdatum);
		System.out.println("   Telefon:\t\t" + telefon);
		System.out.println("   E-Mail:\t\t" + mail);
		System.out.println("   Adresse:\t\t" + strasse + " " + hausnr);
		System.out.println("   Ort:\t\t\t" + plz + " " + ort);
		System.out.println("---------------------------------------------------");
		System.out.println("\nVertragsdaten:");
		System.out.println("   Vertragsart:\t\t" + bezeichnung);
		System.out.println("   Laufzeit:\t\t" + laufzeit + " Wochen");
		System.out.println("   Vertragsbeginn:\t" + vertragsbeginn);
		System.out.println("   Vertragsende:\t" + vertragsende);
		System.out.println("   Zahlungsintervall:\t" + intervallBezeichnung);
		System.out.println("   Grundpreis:\t\t" + String.format("%.2f €", grundpreis));
		System.out.println("   Sonder-Rabatt:\t" + String.format("%.2f €", preisrabatt));
		System.out.println("   Wochenpreis:\t\t" + String.format("%.2f €", wochenpreis));
		System.out.println("   Kommentar:\t\t" + (kommentar.isEmpty() ? "kein Kommentar" : kommentar));
		System.out.println("   Trainingsbeginn:\t" + trainingsbeginn);
		System.out.println("   Je Zahlungsintervall:" + String.format("%.2f €", betragJeIntervall));
		System.out.println("   Zahlungsbeginn:\t" + zahlungsbeginn);
		System.out.println("---------------------------------------------------");
		System.out.println("\nZahlungsdaten:");
		System.out.println("   Kontoinhaber:\t" + kontoinhaber);
		System.out.println("   IBAN:\t\t" + iban);
		System.out.println("   BIC:\t\t\t" + bic);

		System.out.println("\nMitarbeiter:");
		System.out.println("   " + mitarbeiterName);
		System.out.println("==================================================");
	}
	// ========================
	// Hilfsmethoden
	// ========================

}
