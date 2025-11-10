package New.Service;

import java.sql.Connection;
import java.util.Scanner;
import New.Manager.VerkaufManager;
import New.Objekte.Artikel;
import New.Objekte.ArtikelBestellung;
import New.Objekte.Bestellung;
import New.Objekte.Kategorie;
import New.Objekte.Zahlung;

public class AdminService extends BaseService {

    private VerkaufManager verkaufManager;

    public AdminService(Connection connection, Scanner scanner) {
        super(connection, scanner);
        try {
            this.verkaufManager = new VerkaufManager();
        } catch (Exception e) {
            System.out.println("Fehler beim Initialisieren des VerkaufManagers: " + e.getMessage());
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
                    System.out.println("Vertrag-Menü (Admin-Funktionen folgen)");
                    break;
                case "3":
                    System.out.println("Kurs-Menü (Admin-Funktionen folgen)");
                    break;
                case "4":
                    System.out.println("Personen-Menü (Admin-Funktionen folgen)");
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
        System.out.println("Kategorie-Menü (Einfügen, Ändern, Löschen, Zurück)");
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

            String format = "%-" + maxId + "s | %-" + maxName + "s | %" + maxPreis + "s | %-" + maxKategorie + "s | %-" + maxKommentar + "s%n";
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
        System.out.println("Artikel-Menü (Einfügen, Ändern, Löschen, Zurück)");
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

            String format = "%-" + maxBest + "s | %-" + maxArt + "s | %-" + maxMenge + "s | %" + maxAufaddiert + "s%n";
            int trennBreite = maxBest + maxArt + maxMenge + maxAufaddiert + 11;

            System.out.printf(format, "BestellungID", "ArtikelID", "Menge", "Aufaddiert");
            System.out.println("-".repeat(trennBreite));
            for (ArtikelBestellung ab : list) {
                System.out.printf(format, ab.getBestellungID(), ab.getArtikelID(), ab.getMenge(), String.format("%.2f", ab.getAufaddiert()));
            }
        } catch (Exception e) {
            System.out.println("Fehler beim Laden der ArtikelBestellungen: " + e.getMessage());
        }
        System.out.println("ArtikelBestellung-Menü (Einfügen, Ändern, Löschen, Zurück)");
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

            String format = "%-" + maxId + "s | %-" + maxMitglied + "s | %" + maxPreis + "s%n";
            int trennBreite = maxId + maxMitglied + maxPreis + 7;

            System.out.printf(format, "BestellungID", "MitgliedID", "Gesamtpreis");
            System.out.println("-".repeat(trennBreite));
            for (Bestellung b : bestellungen) {
                System.out.printf(format, b.getBestellungID(), b.getMitgliederID(), String.format("%.2f", b.getGesamtpreis()));
            }
        } catch (Exception e) {
            System.out.println("Fehler beim Laden der Bestellungen: " + e.getMessage());
        }
        System.out.println("Bestellung-Menü (Einfügen, Ändern, Löschen, Zurück)");
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
        System.out.println("Zahlung-Menü (Einfügen, Ändern, Löschen, Zurück)");
    }
}
