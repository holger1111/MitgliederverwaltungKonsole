package Service;

import java.sql.Connection;
import java.util.Scanner;

import DAOs.OrtDAO;
import DAOs.ZahlungsdatenDAO;
import Exception.StringException;
import Helper.IO;
import Manager.MitgliederManager;
import OUTDATED.OUT_StringValidator;
import Objekte.Mitglieder;
import Objekte.Ort;
import Objekte.Zahlungsdaten;
import Validator.ContactValidator;
import Validator.PaymentDetailsValidator;

public class MitgliederService extends BaseService {

    public MitgliederService(Connection connection, Scanner scanner) {
        super(connection, scanner);
    }

    public void start() {
        boolean zurueck = false;
        while (!zurueck) {
            System.out.println("==== Mitgliederverwaltung ====");
            System.out.println("1 - Interessenten erstellen");
            System.out.println("2 - Mitglieder erstellen");
            System.out.println("3 - Zurück zum Hauptmenü");
            System.out.print("Bitte wählen: ");
            String eingabe = scanner.nextLine();

            switch (eingabe) {
                case "1":
                    interessentErstellen();
                    break;
                case "2":
                    mitgliedErstellen();
                    break;
                case "3":
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

            // ContactValidator für Telefon und Mail initialisieren
            ContactValidator contactValidator = new ContactValidator();

            // Telefon validieren und formatieren
            String telefon = "";
            while (true) {
                telefon = IO.readString("Telefon: ");
                if (telefon.isEmpty()) break;
                try {
                    telefon = contactValidator.validateUndFormatTelefon(telefon);
                    break;
                } catch (Exception e) {
                    System.out.println("Ungültige Telefonnummer: " + e.getMessage());
                }
            }

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
            OUT_StringValidator plzValidator = new OUT_StringValidator() {
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
            while (true) {
                mail = IO.readString("Mail: ");
                if (mail.isEmpty())
                    break;
                try {
                    contactValidator.validateEmail(mail);
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

            // PaymentDetailsValidator für IBAN und BIC initialisieren
            PaymentDetailsValidator paymentValidator = new PaymentDetailsValidator();

            String zahlungsName = IO.readString("Zahlungsdaten - Name: ");
            String iban = "";
            while (true) {
                iban = IO.readString("IBAN: ");
                if (iban.isEmpty())
                    break;
                try {
                    iban = paymentValidator.validateIBAN(iban);
                    break;
                } catch (Exception e) {
                    System.out.println("Ungültige IBAN: " + e.getMessage());
                }
            }

            String bic = "";
            while (true) {
                bic = IO.readString("BIC: ");
                if (bic.isEmpty())
                    break;
                try {
                    paymentValidator.validateBIC(bic);
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
