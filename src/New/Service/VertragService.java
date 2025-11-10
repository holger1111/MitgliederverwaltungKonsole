package New.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import New.DAOs.IntervallDAO;
import New.DAOs.MitgliederDAO;
import New.DAOs.MitgliederVertragDAO;
import New.DAOs.OrtDAO;
import New.DAOs.Transaction;
import New.DAOs.VertragDAO;
import New.DAOs.ZahlungDAO;
import New.DAOs.ZahlungsdatenDAO;
import New.Exception.ConnectionException;
import New.Exception.CurrencyException;
import New.Exception.DateException;
import New.Exception.EMailException;
import New.Exception.StringException;
import New.Objekte.Intervall;
import New.Objekte.Mitglieder;
import New.Objekte.MitgliederVertrag;
import New.Objekte.Vertrag;
import New.Objekte.Zahlungsdaten;
import New.Validator.BICValidator;
import New.Validator.CurrencyValidator;
import New.Validator.DateValidator;
import New.Validator.EMailValidator;
import New.Validator.IBANValidator;
import New.Validator.StringValidator;

public class VertragService extends BaseService {

    // ========== DAOs ==========
    private final OrtDAO ortDAO;
    private final ZahlungsdatenDAO zahlungsdatenDAO;
    private final MitgliederDAO mitgliederDAO;
    private final VertragDAO vertragDAO;
    private final IntervallDAO intervallDAO;
    private final MitgliederVertragDAO mitgliederVertragDAO;
    private ZahlungDAO zahlungDAO;

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

    // ========== PUBLIC METHODEN ==========
    
    public void start() {
        boolean zurueck = false;
        while (!zurueck) {
            System.out.println("==== Vertragsverwaltung ====");
            System.out.println("1 - Suche");
            System.out.println("2 - Neukunde anlegen");
            System.out.println("3 - Zurück zum Hauptmenü");
            System.out.print("Bitte wählen: ");
            String eingabe = scanner.nextLine();

            switch (eingabe) {
                case "1":
                    SucheService sucheService = new SucheService(connection, scanner);
                    sucheService.start();
                    if (sucheService.shouldExitToMainMenu()) {
                        return;
                    }
                    break;
                case "2":
                    neukundeAnlegen();
                    break;
                case "3":
                    zurueck = true;
                    break;
                default:
                    System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
            }
        }
    }

    // ========== HAUPTLOGIK: NEUKUNDE ANLEGEN ==========
    
    private void neukundeAnlegen() {
        Transaction tx = null;
        try {
            tx = new Transaction(connection);
            System.out.println("\n==== Neukunde anlegen ====\n");

            // ========== SEITE 1: Kundendaten ==========
            String vorname = erfasseVorname();
            String nachname = erfasseNachname();
            String strasse = erfasseStrasse();
            String hausnr = erfasseHausnr();
            String plz = erfassePLZ();
            String ort = erfasseOrt();
            String geburtsdatum = erfasseGeburtsdatum();
            String telefon = erfasseTelefon();
            String mail = erfasseMail();

            // Bestätigungsschleife Seite 1
            boolean seite1Bestaetigt = false;
            while (!seite1Bestaetigt) {
                zeigeUebersichtSeite1(vorname, nachname, strasse, hausnr, plz, ort, geburtsdatum, telefon, mail);
                System.out.print("\nEingabe bestätigen? (Enter) oder Nummer zum Bearbeiten: ");
                String auswahl = scanner.nextLine().trim();

                if (auswahl.isEmpty()) {
                    seite1Bestaetigt = true;
                } else {
                    switch (auswahl) {
                        case "1":
                            vorname = erfasseVorname();
                            nachname = erfasseNachname();
                            break;
                        case "2":
                            strasse = erfasseStrasse();
                            hausnr = erfasseHausnr();
                            break;
                        case "3":
                            plz = erfassePLZ();
                            ort = erfasseOrt();
                            break;
                        case "4":
                            geburtsdatum = erfasseGeburtsdatum();
                            break;
                        case "5":
                            telefon = erfasseTelefon();
                            break;
                        case "6":
                            mail = erfasseMail();
                            break;
                        default:
                            System.out.println("Ungültige Auswahl!");
                    }
                }
            }

            // Ort-Prüfung und ggf. Anlegen über OrtDAO
            int ortID = ortDAO.findOrCreateOrt(plz, ort);

            // ========== SEITE 2: Vertragsdaten ==========
            System.out.println("\n✓ Kundendaten erfasst. Weiter zu Vertragsdaten...\n");

            // Vertragsart auswählen über VertragDAO
            Vertrag vertrag = waehleVertragsart();
            int laufzeit = vertrag.getLaufzeit();
            double grundpreis = vertrag.getGrundpreis();

            // Zahlungsintervall auswählen über IntervallDAO
            Intervall intervall = waehleZahlungsintervall();
            String intervallBezeichnung = intervall.getBezeichnung();
            int zahlungsintervall = Integer.parseInt(intervall.getZahlungsintervall());

            // Vertragsbeginn
            Date vertragsbeginn = erfasseVertragsbeginn();
            
            // Vertragsende berechnen
            Date vertragsende = berechneVertragsende(vertragsbeginn, laufzeit);

            // Sonder-Rabatt
            double preisrabatt = erfasseSonderRabatt(grundpreis);

            // Kommentar bei Rabatt
            String kommentar = "";
            if (preisrabatt > 0.0) {
                kommentar = erfasseKommentar();
            }

            // Trainingsbeginn (max. 5 Wochen vor Vertragsbeginn)
            Date trainingsbeginn = erfasseTrainingsbeginn(vertragsbeginn);

            // Berechnungen
            double wochenpreis = grundpreis - preisrabatt;
            double betragJeIntervall = berechneIntervallbetrag(wochenpreis, zahlungsintervall);
            Date zahlungsbeginn = berechneZahlungsbeginn(vertragsbeginn, intervall);

            // Bestätigungsschleife Seite 2
            boolean seite2Bestaetigt = false;
            while (!seite2Bestaetigt) {
                zeigeUebersichtSeite2(vertrag.getBezeichnung(), laufzeit, vertragsbeginn, vertragsende, 
                                      intervallBezeichnung, grundpreis, preisrabatt, wochenpreis,
                                      kommentar, trainingsbeginn, betragJeIntervall, zahlungsbeginn);
                System.out.print("\nEingabe bestätigen? (Enter) oder Nummer zum Bearbeiten: ");
                String auswahl = scanner.nextLine().trim();

                if (auswahl.isEmpty()) {
                    seite2Bestaetigt = true;
                } else {
                    switch (auswahl) {
                        case "1":
                            vertrag = waehleVertragsart();
                            laufzeit = vertrag.getLaufzeit();
                            grundpreis = vertrag.getGrundpreis();
                            vertragsende = berechneVertragsende(vertragsbeginn, laufzeit);
                            wochenpreis = grundpreis - preisrabatt;
                            betragJeIntervall = berechneIntervallbetrag(wochenpreis, zahlungsintervall);
                            break;
                        case "2":
                            vertragsbeginn = erfasseVertragsbeginn();
                            vertragsende = berechneVertragsende(vertragsbeginn, laufzeit);
                            zahlungsbeginn = berechneZahlungsbeginn(vertragsbeginn, intervall);
                            break;
                        case "3":
                            intervall = waehleZahlungsintervall();
                            intervallBezeichnung = intervall.getBezeichnung();
                            zahlungsintervall = Integer.parseInt(intervall.getZahlungsintervall());
                            betragJeIntervall = berechneIntervallbetrag(wochenpreis, zahlungsintervall);
                            zahlungsbeginn = berechneZahlungsbeginn(vertragsbeginn, intervall);
                            break;
                        case "4":
                            preisrabatt = erfasseSonderRabatt(grundpreis);
                            if (preisrabatt > 0.0 && kommentar.isEmpty()) {
                                kommentar = erfasseKommentar();
                            }
                            wochenpreis = grundpreis - preisrabatt;
                            betragJeIntervall = berechneIntervallbetrag(wochenpreis, zahlungsintervall);
                            break;
                        case "5":
                            kommentar = erfasseKommentar();
                            break;
                        case "6":
                            trainingsbeginn = erfasseTrainingsbeginn(vertragsbeginn);
                            break;
                        default:
                            System.out.println("Ungültige Auswahl!");
                    }
                }
            }

            // ========== SEITE 3: Zahlungsdaten ==========
            System.out.println("\n✓ Vertragsdaten erfasst. Weiter zu Zahlungsdaten...\n");

            // Zahlungsdaten erfassen
            String kontoinhaber = erfasseKontoinhaber();
            String iban = erfasseIBAN();
            String bic = erfasseBIC();

            // Bestätigungsschleife Seite 3
            boolean seite3Bestaetigt = false;
            while (!seite3Bestaetigt) {
                zeigeUebersichtSeite3(kontoinhaber, iban, bic);
                System.out.print("\nEingabe bestätigen? (Enter) oder Nummer zum Bearbeiten: ");
                String auswahl = scanner.nextLine().trim();

                if (auswahl.isEmpty()) {
                    seite3Bestaetigt = true;
                } else {
                    switch (auswahl) {
                        case "1":
                            kontoinhaber = erfasseKontoinhaber();
                            break;
                        case "2":
                            iban = erfasseIBAN();
                            break;
                        case "3":
                            bic = erfasseBIC();
                            break;
                        default:
                            System.out.println("Ungültige Auswahl!");
                    }
                }
            }

            // Zahlungsdaten-Prüfung und ggf. Anlegen über ZahlungsdatenDAO
            int zahlungsdatenID = zahlungsdatenDAO.findOrCreateZahlungsdaten(kontoinhaber, iban, bic);

            // ========== FINALE ÜBERSICHT - Alle Daten ==========
            System.out.println("\n✓ Alle Daten erfasst. Prüfen Sie bitte die Gesamtübersicht...\n");

            boolean finaleBestaetigung = false;
            while (!finaleBestaetigung) {
                // Finale Übersicht mit allen Daten anzeigen
                boolean uebersichtBestaetigt = false;
                while (!uebersichtBestaetigt) {
                    zeigeFinaleUebersicht(vorname, nachname, strasse, hausnr, plz, ort, geburtsdatum, 
                                          telefon, mail, vertrag.getBezeichnung(), laufzeit, vertragsbeginn, vertragsende,
                                          intervallBezeichnung, grundpreis, preisrabatt, wochenpreis,
                                          kommentar, trainingsbeginn, betragJeIntervall, zahlungsbeginn,
                                          kontoinhaber, iban, bic);
                    
                    System.out.print("\nEingabe bestätigen? (Enter) oder Nummer zum Bearbeiten (1-15): ");
                    String auswahl = scanner.nextLine().trim();

                    if (auswahl.isEmpty()) {
                        uebersichtBestaetigt = true;
                    } else {
                        switch (auswahl) {
                            case "1": // Vorname
                                vorname = erfasseVorname();
                                break;
                            case "2": // Vertragsart
                                vertrag = waehleVertragsart();
                                laufzeit = vertrag.getLaufzeit();
                                grundpreis = vertrag.getGrundpreis();
                                vertragsende = berechneVertragsende(vertragsbeginn, laufzeit);
                                wochenpreis = grundpreis - preisrabatt;
                                betragJeIntervall = berechneIntervallbetrag(wochenpreis, zahlungsintervall);
                                break;
                            case "3": // Kontoinhaber
                                kontoinhaber = erfasseKontoinhaber();
                                zahlungsdatenID = zahlungsdatenDAO.findOrCreateZahlungsdaten(kontoinhaber, iban, bic);
                                break;
                            case "4": // Nachname
                                nachname = erfasseNachname();
                                break;
                            case "5": // IBAN
                                iban = erfasseIBAN();
                                zahlungsdatenID = zahlungsdatenDAO.findOrCreateZahlungsdaten(kontoinhaber, iban, bic);
                                break;
                            case "6": // BIC
                                bic = erfasseBIC();
                                zahlungsdatenID = zahlungsdatenDAO.findOrCreateZahlungsdaten(kontoinhaber, iban, bic);
                                break;
                            case "7": // Vertragsbeginn
                                vertragsbeginn = erfasseVertragsbeginn();
                                vertragsende = berechneVertragsende(vertragsbeginn, laufzeit);
                                zahlungsbeginn = berechneZahlungsbeginn(vertragsbeginn, intervall);
                                break;
                            case "8": // Zahlungsintervall
                                intervall = waehleZahlungsintervall();
                                intervallBezeichnung = intervall.getBezeichnung();
                                zahlungsintervall = Integer.parseInt(intervall.getZahlungsintervall());
                                betragJeIntervall = berechneIntervallbetrag(wochenpreis, zahlungsintervall);
                                zahlungsbeginn = berechneZahlungsbeginn(vertragsbeginn, intervall);
                                break;
                            case "9": // PLZ + Ort
                                plz = erfassePLZ();
                                ort = erfasseOrt();
                                ortID = ortDAO.findOrCreateOrt(plz, ort);
                                break;
                            case "10": // Geburtsdatum
                                geburtsdatum = erfasseGeburtsdatum();
                                break;
                            case "11": // Telefon
                                telefon = erfasseTelefon();
                                break;
                            case "12": // Sonder-Rabatt
                                preisrabatt = erfasseSonderRabatt(grundpreis);
                                if (preisrabatt > 0.0 && kommentar.isEmpty()) {
                                    kommentar = erfasseKommentar();
                                }
                                wochenpreis = grundpreis - preisrabatt;
                                betragJeIntervall = berechneIntervallbetrag(wochenpreis, zahlungsintervall);
                                break;
                            case "13": // Mail
                                mail = erfasseMail();
                                break;
                            case "14": // Trainingsbeginn
                                trainingsbeginn = erfasseTrainingsbeginn(vertragsbeginn);
                                break;
                            case "15": // Kommentar
                                kommentar = erfasseKommentar();
                                break;
                            default:
                                System.out.println("Ungültige Auswahl!");
                        }
                    }
                }

                // Finale Sicherheitsabfrage
                System.out.print("\n⚠ Sind Sie sicher, dass alle Daten stimmen? (ja/nein): ");
                String bestaetigung = scanner.nextLine().trim().toLowerCase();
                
                if (bestaetigung.equals("ja") || bestaetigung.equals("j")) {
                    finaleBestaetigung = true;
                } else if (bestaetigung.equals("nein") || bestaetigung.equals("n")) {
                    System.out.println("\n↩ Zurück zur Übersicht...\n");
                } else {
                    System.out.println("✗ Ungültige Eingabe! Bitte 'ja' oder 'nein' eingeben.");
                }
            }

            // ========== Daten in DB speichern über DAOs ==========
            System.out.println("\n✓✓✓ Bestätigung erfolgt. Speichere alle Daten in Datenbank...\n");

            // Geburtsdatum String zu Date konvertieren
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date geburtstagDate = sdf.parse(geburtsdatum);

            // 1. Mitglied anlegen über MitgliederDAO
            Mitglieder neuesMitglied = new Mitglieder();
            neuesMitglied.setVorname(vorname);
            neuesMitglied.setNachname(nachname);
            neuesMitglied.setTelefon(telefon);
            neuesMitglied.setGeburtstag(geburtstagDate);
            neuesMitglied.setAktiv(true); // Neukunden sind aktiv
            neuesMitglied.setStrasse(strasse);
            neuesMitglied.setHausnr(hausnr);
            neuesMitglied.setOrtID(ortID);
            neuesMitglied.setZahlungsdatenID(zahlungsdatenID);
            neuesMitglied.setMail(mail);

            mitgliederDAO.insert(neuesMitglied);
            int mitgliedID = neuesMitglied.getMitgliederID();
            System.out.println("✓ Mitglied angelegt (ID: " + mitgliedID + ")");

            // 2. Zahlung anlegen über ZahlungDAO (MUSS VOR MitgliederVertrag sein!)
            New.Objekte.Zahlung zahlung = new New.Objekte.Zahlung();
            zahlung.setZahlungsart("SEPA-Lastschrift"); // Standard-Zahlungsart
            zahlungDAO.insert(zahlung);  // ← Generiert ZahlungID!
            int zahlungID = zahlung.getZahlungID();
            System.out.println("✓ Zahlung angelegt (ID: " + zahlungID + ")");

            // 3. MitgliederVertrag anlegen über MitgliederVertragDAO
            MitgliederVertrag neuerVertrag = new MitgliederVertrag();
            neuerVertrag.setMitgliederID(mitgliedID);
            neuerVertrag.setVertragID(vertrag.getVertragID());
            neuerVertrag.setVertragsbeginn(vertragsbeginn);
            neuerVertrag.setVertragsende(vertragsende);
            neuerVertrag.setPreisrabatt(preisrabatt);
            neuerVertrag.setIntervallID(intervall.getIntervallID());
            neuerVertrag.setZahlungID(zahlungID);  // ← Setze die generierte ZahlungID!
            neuerVertrag.setTrainingsbeginn(trainingsbeginn);
            neuerVertrag.setKommentar(kommentar.isEmpty() ? null : kommentar);
            neuerVertrag.setAktiv(true);
            neuerVertrag.setGekündigt(false);
            neuerVertrag.setVerlängerung(false);

            // Zahlungsdaten-Objekt vom DAO holen und im Mitglied setzen (für Validator)
            Zahlungsdaten vollstaendigeZahlungsdaten = zahlungsdatenDAO.findById(zahlungsdatenID);
            neuesMitglied.setZahlungsdaten(vollstaendigeZahlungsdaten);

            // MitgliederVertragDAO mit erweiterter Signatur aufrufen
            mitgliederVertragDAO.insert(neuerVertrag, vertrag, zahlung, neuesMitglied, laufzeit);
            System.out.println("✓ Vertrag angelegt (ID: " + neuerVertrag.getVertragNr() + ")");

            // Commit Transaction
            tx.commit();
            
            System.out.println("\n════════════════════════════════════════");
            System.out.println("✓✓✓ ERFOLG! Neukunde wurde angelegt! ✓✓✓");
            System.out.println("════════════════════════════════════════");
            System.out.println("Folgende IDs wurden erzeugt/verwendet:");
            System.out.println("  → MitgliedID: " + mitgliedID);
            System.out.println("  → VertragID: " + neuerVertrag.getVertragNr());
            System.out.println("  → ZahlungID: " + zahlungID);
            System.out.println("  → OrtID: " + ortID);
            System.out.println("  → ZahlungsdatenID: " + zahlungsdatenID);
            System.out.println("════════════════════════════════════════\n");


        } catch (ConnectionException | SQLException e) {
            System.err.println("Fehler bei der Transaction: " + e.getMessage());
            if (tx != null) {
                try {
                    tx.rollback();
                    System.out.println("✗ Transaction wurde rückgängig gemacht.");
                } catch (SQLException rollbackEx) {
                    System.err.println("Fehler beim Rollback: " + rollbackEx.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Unerwarteter Fehler: " + e.getMessage());
            e.printStackTrace();
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Fehler beim Rollback: " + rollbackEx.getMessage());
                }
            }
        } finally {
            if (tx != null) {
                try {
                    tx.close();
                } catch (SQLException e) {
                    System.err.println("Fehler beim Schließen der Transaction: " + e.getMessage());
                }
            }
        }
    }
    // ========== HILFSMETHODEN MIT DAO-VERWENDUNG ==========

    private Vertrag waehleVertragsart() throws SQLException {
        List<Vertrag> vertragsarten = vertragDAO.findAll();
        
        System.out.println("\n==== Verfügbare Vertragsarten ====");
        for (Vertrag v : vertragsarten) {
            System.out.println(String.format("%d - %s (Laufzeit: %d Wochen, Grundpreis: %.2f €)", 
                                              v.getVertragID(), v.getBezeichnung(), v.getLaufzeit(), v.getGrundpreis()));
        }

        while (true) {
            try {
                System.out.print("\nVertragsart wählen (ID): ");
                int auswahl = Integer.parseInt(scanner.nextLine().trim());
                
                for (Vertrag v : vertragsarten) {
                    if (v.getVertragID() == auswahl) {
                        System.out.println("✓ Gewählt: " + v.getBezeichnung());
                        return v;
                    }
                }
                System.out.println("✗ Ungültige ID!");
            } catch (NumberFormatException e) {
                System.out.println("✗ Bitte eine Zahl eingeben!");
            }
        }
    }

    private Intervall waehleZahlungsintervall() throws SQLException {
        List<Intervall> intervalle = intervallDAO.findAll();
        
        System.out.println("\n==== Verfügbare Zahlungsintervalle ====");
        for (Intervall iv : intervalle) {
            System.out.println(String.format("%d - %s", iv.getIntervallID(), iv.getBezeichnung()));
        }

        while (true) {
            try {
                System.out.print("\nZahlungsintervall wählen (ID): ");
                int auswahl = Integer.parseInt(scanner.nextLine().trim());
                
                for (Intervall iv : intervalle) {
                    if (iv.getIntervallID() == auswahl) {
                        System.out.println("✓ Gewählt: " + iv.getBezeichnung());
                        return iv;
                    }
                }
                System.out.println("✗ Ungültige ID!");
            } catch (NumberFormatException e) {
                System.out.println("✗ Bitte eine Zahl eingeben!");
            }
        }
    }

    // ========== SEITE 1: ERFASSUNGSMETHODEN ==========

    private String erfasseVorname() {
        StringValidator validator = new StringValidator();
        while (true) {
            try {
                System.out.print("Vorname: ");
                String eingabe = scanner.nextLine().trim();
                validator.validate(eingabe);
                return eingabe;
            } catch (StringException e) {
                System.out.println("✗ " + e.getMessage());
            } catch (Exception e) {
                System.out.println("✗ Unerwarteter Fehler: " + e.getMessage());
            }
        }
    }

    private String erfasseNachname() {
        StringValidator validator = new StringValidator();
        while (true) {
            try {
                System.out.print("Nachname: ");
                String eingabe = scanner.nextLine().trim();
                validator.validate(eingabe);
                return eingabe;
            } catch (StringException e) {
                System.out.println("✗ " + e.getMessage());
            } catch (Exception e) {
                System.out.println("✗ Unerwarteter Fehler: " + e.getMessage());
            }
        }
    }

    private String erfasseStrasse() {
        StringValidator validator = new StringValidator();
        while (true) {
            try {
                System.out.print("Straße: ");
                String eingabe = scanner.nextLine().trim();
                validator.validate(eingabe);
                return eingabe;
            } catch (StringException e) {
                System.out.println("✗ " + e.getMessage());
            } catch (Exception e) {
                System.out.println("✗ Unerwarteter Fehler: " + e.getMessage());
            }
        }
    }

    private String erfasseHausnr() {
        StringValidator validator = new StringValidator();
        while (true) {
            try {
                System.out.print("Hausnummer: ");
                String eingabe = scanner.nextLine().trim();
                validator.validate(eingabe);
                return eingabe;
            } catch (StringException e) {
                System.out.println("✗ " + e.getMessage());
            } catch (Exception e) {
                System.out.println("✗ Unerwarteter Fehler: " + e.getMessage());
            }
        }
    }

    private String erfassePLZ() {
        StringValidator validator = new StringValidator();
        while (true) {
            try {
                System.out.print("PLZ (5-stellig): ");
                String eingabe = scanner.nextLine().trim();
                validator.validate(eingabe);
                
                if (eingabe.length() != 5) {
                    throw new StringException("PLZ muss genau 5 Zeichen lang sein.");
                }
                
                if (!eingabe.matches("\\d{5}")) {
                    throw new StringException("PLZ darf nur Ziffern enthalten.");
                }
                
                return eingabe;
            } catch (StringException e) {
                System.out.println("✗ " + e.getMessage());
            } catch (Exception e) {
                System.out.println("✗ Unerwarteter Fehler: " + e.getMessage());
            }
        }
    }

    private String erfasseOrt() {
        StringValidator validator = new StringValidator();
        while (true) {
            try {
                System.out.print("Ort: ");
                String eingabe = scanner.nextLine().trim();
                validator.validate(eingabe);
                return eingabe;
            } catch (StringException e) {
                System.out.println("✗ " + e.getMessage());
            } catch (Exception e) {
                System.out.println("✗ Unerwarteter Fehler: " + e.getMessage());
            }
        }
    }

    private String erfasseGeburtsdatum() {
        DateValidator validator = new DateValidator();
        while (true) {
            try {
                System.out.print("Geburtsdatum (TT.MM.JJJJ): ");
                String eingabe = scanner.nextLine().trim();
                validator.validate(eingabe);
                return eingabe;
            } catch (DateException e) {
                System.out.println("✗ " + e.getMessage());
            } catch (Exception e) {
                System.out.println("✗ Unerwarteter Fehler: " + e.getMessage());
            }
        }
    }

    private String erfasseTelefon() {
        StringValidator validator = new StringValidator();
        while (true) {
            try {
                System.out.print("Telefon: ");
                String eingabe = scanner.nextLine().trim();
                validator.validate(eingabe);
                return eingabe;
            } catch (StringException e) {
                System.out.println("✗ " + e.getMessage());
            } catch (Exception e) {
                System.out.println("✗ Unerwarteter Fehler: " + e.getMessage());
            }
        }
    }

    private String erfasseMail() {
        EMailValidator validator = new EMailValidator();
        while (true) {
            try {
                System.out.print("E-Mail: ");
                String eingabe = scanner.nextLine().trim();
                validator.validate(eingabe);
                return eingabe;
            } catch (EMailException e) {
                System.out.println("✗ " + e.getMessage());
            } catch (Exception e) {
                System.out.println("✗ Unerwarteter Fehler: " + e.getMessage());
            }
        }
    }

    private void zeigeUebersichtSeite1(String vorname, String nachname, String strasse, String hausnr, 
                                        String plz, String ort, String geburtsdatum, String telefon, String mail) {
        System.out.println(String.format(
            "\n1\nVorname:\t%s\nNachname:\t%s\n2\nStraße:\t\t%s %s\n3\nOrt:\t\t%s %s\n4\nGeburtsdatum:\t%s\n5\nTelefon:\t%s\n6\nMail:\t\t%s\n",
            vorname, nachname, strasse, hausnr, plz, ort, geburtsdatum, telefon, mail
        ));
    }

    // ========== SEITE 2: ERFASSUNGSMETHODEN ==========

    private Date erfasseVertragsbeginn() {
        DateValidator validator = new DateValidator();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        
        while (true) {
            try {
                System.out.print("\nVertragsbeginn (TT.MM.JJJJ): ");
                String eingabe = scanner.nextLine().trim();
                validator.validate(eingabe);
                
                Date datum = sdf.parse(eingabe);
                return datum;
            } catch (DateException e) {
                System.out.println("✗ " + e.getMessage());
            } catch (Exception e) {
                System.out.println("✗ Ungültiges Datum!");
            }
        }
    }

    private Date berechneVertragsende(Date vertragsbeginn, int laufzeitWochen) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(vertragsbeginn);
        cal.add(Calendar.WEEK_OF_YEAR, laufzeitWochen);
        return cal.getTime();
    }

    private double erfasseSonderRabatt(double grundpreis) {
        CurrencyValidator validator = new CurrencyValidator();
        
        while (true) {
            try {
                System.out.print(String.format("\nGrundpreis: %.2f €\nSonder-Rabatt eingeben (0.00 für keinen Rabatt): ", grundpreis));
                String eingabe = scanner.nextLine().trim();
                validator.validate(eingabe);
                double rabatt = validator.getValidatedValue();
                
                if (rabatt < 0.0) {
                    throw new CurrencyException("Rabatt kann nicht negativ sein!");
                }
                
                if (rabatt > grundpreis) {
                    throw new CurrencyException("Rabatt kann nicht höher als Grundpreis sein!");
                }
                
                return rabatt;
            } catch (CurrencyException e) {
                System.out.println("✗ " + e.getMessage());
            } catch (Exception e) {
                System.out.println("✗ Ungültige Eingabe!");
            }
        }
    }

    private String erfasseKommentar() {
        System.out.print("Kommentar zum Rabatt: ");
        return scanner.nextLine().trim();
    }

    private Date erfasseTrainingsbeginn(Date vertragsbeginn) {
        DateValidator validator = new DateValidator();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        
        Calendar minCal = Calendar.getInstance();
        minCal.setTime(vertragsbeginn);
        minCal.add(Calendar.WEEK_OF_YEAR, -5);
        Date fruehesterBeginn = minCal.getTime();
        
        while (true) {
            try {
                System.out.print(String.format("\nTrainingsbeginn (TT.MM.JJJJ, max. 5 Wochen vor Vertragsbeginn %s): ", 
                                                sdf.format(vertragsbeginn)));
                String eingabe = scanner.nextLine().trim();
                validator.validate(eingabe);
                
                Date datum = sdf.parse(eingabe);
                
                if (datum.before(fruehesterBeginn)) {
                    System.out.println("✗ Trainingsbeginn darf maximal 5 Wochen vor Vertragsbeginn liegen!");
                    continue;
                }
                
                if (datum.after(vertragsbeginn)) {
                    System.out.println("✗ Trainingsbeginn darf nicht nach Vertragsbeginn liegen!");
                    continue;
                }
                
                return datum;
            } catch (DateException e) {
                System.out.println("✗ " + e.getMessage());
            } catch (Exception e) {
                System.out.println("✗ Ungültiges Datum!");
            }
        }
    }

    private double berechneIntervallbetrag(double wochenpreis, int zahlungsintervall) {
        return Math.round((wochenpreis / 7.0 * zahlungsintervall) * 100.0) / 100.0;
    }

    private Date berechneZahlungsbeginn(Date vertragsbeginn, Intervall intervall) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(vertragsbeginn);
        
        switch (intervall.getIntervallID()) {
            case 1: // 1. Tag des Monats
                cal.set(Calendar.DAY_OF_MONTH, 1);
                if (cal.getTime().before(vertragsbeginn)) {
                    cal.add(Calendar.MONTH, 1);
                }
                break;
            case 2: // Letzter Tag des Monats
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                if (cal.getTime().before(vertragsbeginn)) {
                    cal.add(Calendar.MONTH, 1);
                    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                }
                break;
            case 3: // wöchentlich (7 Tage)
            case 4: // zweiwöchentlich (14 Tage)
                break;
            default:
                break;
        }
        
        return cal.getTime();
    }

    private void zeigeUebersichtSeite2(String bezeichnung, int laufzeit, Date vertragsbeginn, Date vertragsende,
                                        String intervallBezeichnung, double grundpreis, double preisrabatt,
                                        double wochenpreis, String kommentar, Date trainingsbeginn,
                                        double betragJeIntervall, Date zahlungsbeginn) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        
        System.out.println(String.format(
            "\n1\nBezeichnung:\t\t%s\nLaufzeit:\t\t%d Wochen\n" +
            "2\nVertragsbeginn:\t\t%s\tZahlungsintervall:\n" +
            "Vertragsende:\t\t%s\t3 %s\n" +
            "Grundpreis:\t\t  %6.2f €\n" +
            "4\nSonder-Rabatt:\t\t- %6.2f €\n" +
            "Wochenpreis:\t\t  %6.2f €\n" +
            "5\nKommentar:\t\t%s\n" +
            "6\nTrainingsbeginn:\t%s\n" +
            "Je Zahlungsintervall:\t  %6.2f €\n" +
            "Zahlungsbeginn:\t\t%s\n",
            bezeichnung, laufzeit,
            sdf.format(vertragsbeginn),
            sdf.format(vertragsende), intervallBezeichnung,
            grundpreis,
            preisrabatt,
            wochenpreis,
            kommentar.isEmpty() ? "(kein Kommentar)" : kommentar,
            sdf.format(trainingsbeginn),
            betragJeIntervall,
            sdf.format(zahlungsbeginn)
        ));
    }

    // ========== SEITE 3: ERFASSUNGSMETHODEN ==========

    private String erfasseKontoinhaber() {
        while (true) {
            try {
                System.out.print("\nName des Kontoinhabers (mind. 2 Wörter): ");
                String eingabe = scanner.nextLine().trim();
                
                String[] woerter = eingabe.split("\\s+");
                if (woerter.length < 2) {
                    throw new StringException("Name muss mindestens 2 Wörter enthalten (z.B. 'Max Mustermann').");
                }
                
                boolean alleGueltig = true;
                for (String wort : woerter) {
                    if (wort.isEmpty()) {
                        alleGueltig = false;
                        break;
                    }
                }
                
                if (!alleGueltig) {
                    throw new StringException("Alle Wörter müssen mindestens 1 Zeichen haben.");
                }
                
                return eingabe;
            } catch (StringException e) {
                System.out.println("✗ " + e.getMessage());
            } catch (Exception e) {
                System.out.println("✗ Unerwarteter Fehler: " + e.getMessage());
            }
        }
    }

    private String erfasseIBAN() {
        IBANValidator validator = new IBANValidator();
        while (true) {
            try {
                System.out.print("IBAN: ");
                String eingabe = scanner.nextLine().trim().toUpperCase();
                validator.validate(eingabe);
                return eingabe;
            } catch (Exception e) {
                System.out.println("✗ " + e.getMessage());
            }
        }
    }

    private String erfasseBIC() {
        BICValidator validator = new BICValidator();
        while (true) {
            try {
                System.out.print("BIC: ");
                String eingabe = scanner.nextLine().trim().toUpperCase();
                validator.validate(eingabe);
                return eingabe;
            } catch (Exception e) {
                System.out.println("✗ " + e.getMessage());
            }
        }
    }

    private void zeigeUebersichtSeite3(String name, String iban, String bic) {
        System.out.println(String.format(
            "\n1\nName:\t%s\n2\nIBAN:\t%s\n3\nBIC:\t%s\n",
            name, iban, bic
        ));
    }

    // ========== FINALE ÜBERSICHT ==========

    private void zeigeFinaleUebersicht(String vorname, String nachname, String strasse, String hausnr,
                                        String plz, String ort, String geburtsdatum, String telefon, String mail,
                                        String bezeichnung, int laufzeit, Date vertragsbeginn, Date vertragsende,
                                        String intervallBezeichnung, double grundpreis, double preisrabatt,
                                        double wochenpreis, String kommentar, Date trainingsbeginn,
                                        double betragJeIntervall, Date zahlungsbeginn,
                                        String kontoinhaber, String iban, String bic) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        
        System.out.println(String.format(
            "\n1 Vorname:\t%s\t\t2 Bezeichnung:\t%s\t\t3 Name:\t%s\n" +
            "4 Nachname:\t%s\t\tLaufzeit:\t%d Wochen\t\t5 IBAN:\t%s\n" +
            "\t\t\t\t\t\t\t6 BIC:\t%s\n" +
            "Straße:\t\t%s %s\t7 Vertragsbeginn:\t%s\t8 Zahlungsintervall:\n" +
            "9 Ort:\t\t%s %s\t\tVertragsende:\t\t%s\t\t%s\n" +
            "10 Geburtsdatum:\t%s\t\t\tGrundpreis:\t%6.2f €\n" +
            "11 Telefon:\t%s\t\t12 Sonder-Rabatt:\t%6.2f €\n" +
            "13 Mail:\t%s\t\t\tWochenpreis:\t%6.2f €\n" +
            "14 Trainingsbeginn:\t%s\t\t15 Kommentar:\t%s\n" +
            "Je Zahlungsintervall:\t%6.2f €\n" +
            "Zahlungsbeginn:\t\t%s\n",
            vorname, bezeichnung, kontoinhaber,
            nachname, laufzeit, iban,
            bic,
            strasse, hausnr, sdf.format(vertragsbeginn),
            plz, ort, sdf.format(vertragsende), intervallBezeichnung,
            geburtsdatum, grundpreis,
            telefon, preisrabatt,
            mail, wochenpreis,
            sdf.format(trainingsbeginn), kommentar.isEmpty() ? "(kein Kommentar)" : kommentar,
            betragJeIntervall,
            sdf.format(zahlungsbeginn)
        ));
    }
}
