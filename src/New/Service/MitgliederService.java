package New.Service;

import java.sql.Connection;
import java.util.Scanner;
import New.Objekte.Mitglieder;
import New.Objekte.Ort;
import New.Objekte.Zahlungsdaten;
import New.Validator.BICValidator;
import New.Validator.EMailValidator;
import New.Validator.IBANValidator;
import New.Validator.StringValidator;
import New.DAOs.OrtDAO;
import New.DAOs.ZahlungsdatenDAO;
import New.Exception.StringException;
import New.Helper.IO;
import New.Manager.MitgliederManager;

public class MitgliederService extends BaseService {

    public MitgliederService(Connection connection, Scanner scanner) {
        super(connection, scanner);
    }

    public void start() {
        boolean zurueck = false;
        while (!zurueck) {
            System.out.println("==== Mitgliederverwaltung ====");
            System.out.println("1 - Suche");
            System.out.println("2 - Interessenten erstellen");
            System.out.println("3 - Mitglieder erstellen");
            System.out.println("4 - Zurück zum Hauptmenü");
            System.out.print("Bitte wählen: ");
            String eingabe = scanner.nextLine();

            switch (eingabe) {
                case "1":
                    SucheService sucheService = new SucheService(connection, scanner);
                    sucheService.start();
                    // Prüfe Exit-Flag
                    if (sucheService.shouldExitToMainMenu()) {
                        return;  // Zurück zum Hauptmenü
                    }
                    break;
                case "2":
                    interessentErstellen();
                    break;
                case "3":
                    mitgliedErstellen();
                    break;
                case "4":
                    zurueck = true;
                    break;
                default:
                    System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
            }
        }
    }


    private void interessentErstellen() {
        System.out.println("Interessentenerstellung ausgewählt");
        try {
            MitgliederManager manager = new MitgliederManager();
            String vorname = IO.readString("Vorname: ");
            String nachname = IO.readString("Nachname: ");
            String telefon = IO.readString("Telefon: ");
            int id = manager.createInteressent(vorname, nachname, telefon);
            System.out.println("Interessent erfolgreich erstellt mit ID: " + id);
        } catch (Exception e) {
            System.out.println("Fehler bei der Interessentenerstellung: " + e.getMessage());
        }
    }

    private void mitgliedErstellen() {
        System.out.println("Mitgliedererstellung ausgewählt");
        try {
            MitgliederManager manager = new MitgliederManager();

            String vorname = IO.readString("Vorname: ");
            String nachname = IO.readString("Nachname: ");
            String telefon = IO.readString("Telefon: ");

            // Geburtsdatum mit Validierungs-Schleife
            java.util.Date geburtstag = null;
            while (true) {
                String geburtsdatumStr = IO.readString("Geburtsdatum (dd.MM.yyyy): ");
                if (geburtsdatumStr.isEmpty())
                    break;
                try {
                    geburtstag = new java.text.SimpleDateFormat("dd.MM.yyyy").parse(geburtsdatumStr);
                    break;
                } catch (Exception e) {
                    System.out.println("Ungültiges Datum: " + e.getMessage());
                }
            }

            boolean aktiv = false;

            String strasse = IO.readString("Straße: ");
            String hausnr = IO.readString("Hausnummer: ");

            // PLZ Validator & Schleife
            String plz = "";
            StringValidator plzValidator = new StringValidator() {
                @Override
                public void validate(Object obj) throws StringException {
                    super.validate(obj);
                    String input = (String) obj;
                    if (input.length() != 5 || !input.matches("\\d{5}")) {
                        errors.add("PLZ muss genau 5 Ziffern haben.");
                        throw new StringException("PLZ muss genau 5 Ziffern haben.");
                    }
                }
            };
            while (true) {
                plz = IO.readString("PLZ: ");
                if (plz.isEmpty())
                    break;
                try {
                    plzValidator.validate(plz);
                    break;
                } catch (Exception e) {
                    System.out.println("Ungültige PLZ: " + e.getMessage());
                }
            }

            String ortname = IO.readString("Ort: ");

            // Mail Validator & Schleife
            String mail = "";
            EMailValidator mailValidator = new EMailValidator();
            while (true) {
                mail = IO.readString("Mail: ");
                if (mail.isEmpty())
                    break;
                try {
                    mailValidator.validate(mail);
                    break;
                } catch (Exception e) {
                    System.out.println("Ungültige Mailadresse: " + e.getMessage());
                }
            }

            // Ort ggf. in DB finden/erstellen
            int ortID = 0;
            Ort ortObj = null;
            if (!plz.isEmpty() && !ortname.isEmpty()) {
                OrtDAO ortDAO = manager.getOrtDAO();
                ortID = ortDAO.findOrCreateOrt(plz, ortname);
                ortObj = ortDAO.findById(ortID);
            }

            // Zahlungsdateneingabe mit Validatoren
            String zahlungsName = IO.readString("Zahlungsdaten - Name: ");
            String iban = "";
            String bic = "";

            IBANValidator ibanValidator = new IBANValidator();
            while (true) {
                iban = IO.readString("IBAN: ");
                if (iban.isEmpty())
                    break;
                try {
                    ibanValidator.validate(iban);
                    break;
                } catch (Exception e) {
                    System.out.println("Ungültige IBAN: " + e.getMessage());
                }
            }

            BICValidator bicValidator = new BICValidator();
            while (true) {
                bic = IO.readString("BIC: ");
                if (bic.isEmpty())
                    break;
                try {
                    bicValidator.validate(bic);
                    break;
                } catch (Exception e) {
                    System.out.println("Ungültiger BIC: " + e.getMessage());
                }
            }

            int zahlungsdatenID = 0;
            Zahlungsdaten zahlungsdaten = null;
            if (!iban.isEmpty() && !bic.isEmpty()) {
                ZahlungsdatenDAO zahlungsdatenDAO = manager.getZahlungsdatenDAO();
                zahlungsdatenID = zahlungsdatenDAO.findOrCreateZahlungsdaten(zahlungsName, iban, bic);
                zahlungsdaten = zahlungsdatenDAO.findById(zahlungsdatenID);
            }

            Mitglieder mitglied = new Mitglieder(vorname, nachname, telefon, geburtstag, aktiv, strasse, hausnr, ortObj, zahlungsdaten, mail);
            manager.getMitgliederDAO().insert(mitglied);

            System.out.println("Mitglied erfolgreich erstellt:\n" + mitglied);

        } catch (Exception e) {
            System.out.println("Fehler bei der Mitgliedererstellung: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
