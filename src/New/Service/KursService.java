package New.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import New.Manager.KursManager;
import New.Objekte.Kurs;
import New.Objekte.Kurstermin;
import New.Objekte.Mitarbeiter;
import New.Validator.IntValidator;
import New.Exception.ConnectionException;
import New.Exception.IntException;
import New.Exception.NotFoundException;

public class KursService extends BaseService {

    private KursManager kursManager;

    public KursService(Connection connection, Scanner scanner) {
        super(connection, scanner);
        try {
            this.kursManager = new KursManager();
        } catch (ConnectionException | SQLException e) {
            System.out.println("Fehler beim Initialisieren des KursManagers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void start() {
        boolean running = true;
        while (running && !exitToMainMenu) {
            System.out.println("\n=== Kursverwaltung ===");
            System.out.println("1. Alle Kurse anzeigen");
            System.out.println("2. Alle Kurstermine anzeigen");
            System.out.println("3. Kurstermine eines Kurses anzeigen");
            System.out.println("4. Für Kurs anmelden");
            System.out.println("5. Von Kurs abmelden");
            System.out.println("6. Teilnehmerliste anzeigen");
            System.out.println("0. Zurück zum Hauptmenü");
            System.out.print("\nBitte wählen Sie: ");
            String eingabe = scanner.nextLine();

            try {
                switch (eingabe) {
                    case "1":
                        alleKurseAnzeigen();
                        break;
                    case "2":
                        alleKurstermineAnzeigen();
                        break;
                    case "3":
                        kurstermineAnzeigen();
                        break;
                    case "4":
                        fuerKursAnmelden();
                        break;
                    case "5":
                        vonKursAbmelden();
                        break;
                    case "6":
                        teilnehmerlisteAnzeigen();
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Ungültige Eingabe!");
                }
            } catch (Exception e) {
                System.out.println("Fehler: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Zeigt alle verfügbaren Kurse an
     */
    private void alleKurseAnzeigen() throws SQLException {
        List<Kurs> kurse = kursManager.getKursDAO().findAll();

        if (kurse.isEmpty()) {
            System.out.println("\nKeine Kurse vorhanden.");
            return;
        }

        System.out.println("\n=== Alle Kurse ===");
        System.out.printf("%-8s | %-30s | %-8s | %10s%n", "KursID", "Bezeichnung", "Termine", "Preis");
        System.out.println("=".repeat(70));

        for (Kurs kurs : kurse) {
            System.out.printf("%-8d | %-30s | %-8d | %9.2f €%n",
                kurs.getKursID(),
                kurs.getBezeichnung(),
                kurs.getAnzahlTermine(),
                kurs.getPreis()
            );
        }
    }

    /**
     * Zeigt alle Kurstermine an
     */
    private void alleKurstermineAnzeigen() throws SQLException, IntException, NotFoundException {
        List<Kurstermin> termine = kursManager.getKursterminDAO().findAll();

        if (termine.isEmpty()) {
            System.out.println("\nKeine Kurstermine vorhanden.");
            return;
        }

        System.out.println("\n=== Alle Kurstermine ===");
        System.out.printf("%-13s | %-16s | %-20s | %-30s%n", 
            "TerminID", "Datum/Zeit", "Trainer", "Kurs");
        System.out.println("=".repeat(90));

        for (Kurstermin termin : termine) {
            // Hole Trainer-Daten aus Kursleitung/Mitarbeiter
            Mitarbeiter trainer = kursManager.findeTrainerFürKurstermin(termin.getKursterminID());
            String trainerName = trainer != null ? trainer.getVorname() + " " + trainer.getNachname() : "-";

            // Hole Kurs-Daten
            Kurs kurs = null;
            String kursName = "-";
            try {
                kurs = kursManager.getKursDAO().findById(termin.getKursID());
                if (kurs != null) {
                    kursName = kurs.getBezeichnung();
                }
            } catch (NotFoundException e) {
                // Kurs nicht gefunden - zeige "-"
            }

            System.out.printf("%-13d | %-16s | %-20s | %-30s%n",
                termin.getKursterminID(),
                formatiereDatumZeit(termin.getTermin()),
                trainerName,
                kursName
            );
        }
    }

    /**
     * Zeigt alle Termine eines Kurses an
     */
    private void kurstermineAnzeigen() throws SQLException, IntException, NotFoundException {
        int kursID = 0;

        // KursID validieren
        while (true) {
            try {
                System.out.print("KursID eingeben: ");
                String kursIDStr = scanner.nextLine();
                kursID = validateInt(kursIDStr, "KursID");
                break;
            } catch (IntException e) {
                System.out.println(e.getMessage());
            }
        }

        Kurs kurs = kursManager.getKursDAO().findById(kursID);
        if (kurs == null) {
            System.out.println("Kurs nicht gefunden!");
            return;
        }

        List<Kurstermin> termine = kursManager.findTermineByKursId(kursID);

        if (termine.isEmpty()) {
            System.out.println("\nKeine Termine für diesen Kurs vorhanden.");
            return;
        }

        System.out.println("\n=== Kurstermine für: " + kurs.getBezeichnung() + " ===");
        System.out.printf("%-12s | %-16s | %-20s | %-6s%n", "TerminID", "Datum/Zeit", "Trainer", "Aktiv");
        System.out.println("=".repeat(70));

        for (Kurstermin termin : termine) {
            Mitarbeiter trainer = kursManager.findeTrainerFürKurstermin(termin.getKursterminID());
            String trainerName = trainer != null ? trainer.getVorname() + " " + trainer.getNachname() : "-";

            System.out.printf("%-12d | %-16s | %-20s | %-6s%n",
                termin.getKursterminID(),
                formatiereDatumZeit(termin.getTermin()),
                trainerName,
                termin.isAktiv() ? "X" : ""
            );
        }
    }

    private void fuerKursAnmelden() throws SQLException, IntException, NotFoundException {
        int mitgliederID = 0;
        int kursterminID = 0;

        // Mitglieder-ID validieren
        while (true) {
            try {
                System.out.print("MitgliederID eingeben: ");
                String mitgliederIDStr = scanner.nextLine();
                mitgliederID = validateInt(mitgliederIDStr, "MitgliederID");
                break;
            } catch (IntException e) {
                System.out.println(e.getMessage());
            }
        }

        // Kurstermin-ID validieren
        while (true) {
            try {
                System.out.print("KursterminID eingeben: ");
                String kursterminIDStr = scanner.nextLine();
                kursterminID = validateInt(kursterminIDStr, "KursterminID");
                break;
            } catch (IntException e) {
                System.out.println(e.getMessage());
            }
        }

        kursManager.meldeAnFuerKurs(mitgliederID, kursterminID);
        System.out.println("\nErfolgreich für Kurs angemeldet!");
    }

    private void vonKursAbmelden() throws SQLException, IntException, NotFoundException {
        int mitgliederID = 0;
        int kursterminID = 0;

        // Mitglieder-ID validieren
        while (true) {
            try {
                System.out.print("MitgliederID eingeben: ");
                String mitgliederIDStr = scanner.nextLine();
                mitgliederID = validateInt(mitgliederIDStr, "MitgliederID");
                break;
            } catch (IntException e) {
                System.out.println(e.getMessage());
            }
        }

        // Kurstermin-ID validieren
        while (true) {
            try {
                System.out.print("KursterminID eingeben: ");
                String kursterminIDStr = scanner.nextLine();
                kursterminID = validateInt(kursterminIDStr, "KursterminID");
                break;
            } catch (IntException e) {
                System.out.println(e.getMessage());
            }
        }

        kursManager.meldeAbVonKurs(mitgliederID, kursterminID);
        System.out.println("\nErfolgreich von Kurs abgemeldet!");
    }

    private void teilnehmerlisteAnzeigen() throws SQLException, IntException, NotFoundException {
        int kursterminID = 0;

        while (true) {
            try {
                System.out.print("KursterminID eingeben: ");
                String kursterminIDStr = scanner.nextLine();
                kursterminID = validateInt(kursterminIDStr, "KursterminID");
                break;
            } catch (IntException e) {
                System.out.println(e.getMessage());
            }
        }

        kursManager.zeigeKursteilnehmer(kursterminID);
    }

    private int validateInt(String eingabe, String feldname) throws IntException {
        try {
            int wert = Integer.parseInt(eingabe);
            IntValidator validator = new IntValidator();
            validator.validate(wert);
            return wert;
        } catch (NumberFormatException e) {
            throw new IntException(feldname + " muss eine ganze Zahl sein!");
        }
    }

    private String formatiereDatumZeit(Timestamp timestamp) {
        if (timestamp == null) {
            return "-";
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);

        int tag = cal.get(Calendar.DAY_OF_MONTH);
        int monat = cal.get(Calendar.MONTH) + 1;
        int jahr = cal.get(Calendar.YEAR);
        int stunde = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        // Format: dd.MM.yyyy HH:mm
        return String.format("%02d.%02d.%4d %02d:%02d", tag, monat, jahr, stunde, minute);
    }
}
