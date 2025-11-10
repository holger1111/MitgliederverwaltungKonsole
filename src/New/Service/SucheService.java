package New.Service;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import New.Helper.Datum;
import New.Helper.DatumHelper;
import New.Manager.MitgliederManager;
import New.Manager.VertragManager;
import New.Manager.VerkaufManager;
import New.Manager.KursManager;
import New.Objekte.Artikel;
import New.Objekte.ArtikelBestellung;
import New.Objekte.Bestellung;
import New.Objekte.Intervall;
import New.Objekte.Mitglieder;
import New.Objekte.MitgliederVertrag;
import New.Objekte.Ort;
import New.Objekte.Vertrag;
import New.Objekte.Kurs;
import New.Objekte.Kurstermin;
import New.Objekte.Kursteilnahme;
import New.Objekte.Trainer;

public class SucheService extends BaseService {

    public SucheService(Connection connection, Scanner scanner) {
        super(connection, scanner);
    }

    public void start() {
        System.out.print("Bitte Suchbegriff eingeben: ");
        String suchbegriff = scanner.nextLine();
        try {
            // Manager initialisieren
            MitgliederManager mitgliederManager = new MitgliederManager();
            VertragManager vertragManager = new VertragManager();
            VerkaufManager verkaufManager = new VerkaufManager();
            KursManager kursManager = new KursManager(); // NEU
            
            // Suche in allen Bereichen
            List<Mitglieder> mitgliederErgebnis = mitgliederManager.search(suchbegriff);
            List<MitgliederVertrag> vertragErgebnis = vertragManager.search(suchbegriff);
            
            // Suche in Verkaufsdaten (Bestellung, Artikel, ArtikelBestellung)
            List<Bestellung> bestellungErgebnis = verkaufManager.getBestellungDAO().searchAllAttributes(suchbegriff);
            List<Artikel> artikelErgebnis = verkaufManager.getArtikelDAO().searchAllAttributes(suchbegriff);
            
            // NEU: Suche in Kursdaten (Kurs, Kurstermin, Trainer, Kursteilnahme)
            List<Kurs> kursErgebnis = kursManager.search(suchbegriff);
            List<Trainer> trainerErgebnis = kursManager.getTrainerDAO().searchAllAttributes(suchbegriff);
            List<Kurstermin> kursterminErgebnis = kursManager.getKursterminDAO().searchAllAttributes(suchbegriff);
            
            // Kombiniere Ergebnisse: alle Mitglieder-IDs sammeln
            Set<Integer> mitgliederIDs = new HashSet<>();
            
            // Von Mitglieder-Suche
            for (Mitglieder m : mitgliederErgebnis) {
                mitgliederIDs.add(m.getMitgliederID());
            }
            
            // Von Vertrag-Suche
            for (MitgliederVertrag mv : vertragErgebnis) {
                mitgliederIDs.add(mv.getMitgliederID());
            }
            
            // Von Bestellung-Suche
            for (Bestellung b : bestellungErgebnis) {
                mitgliederIDs.add(b.getMitgliederID());
            }
            
            // Von Artikel-Suche (über ArtikelBestellung zu Bestellung zu Mitglied)
            for (Artikel artikel : artikelErgebnis) {
                List<ArtikelBestellung> artikelBestellungen = 
                    verkaufManager.getArtikelBestellungDAO().findByArtikelId(artikel.getArtikelID());
                for (ArtikelBestellung ab : artikelBestellungen) {
                    Bestellung bestellung = verkaufManager.getBestellungDAO().findById(ab.getBestellungID());
                    if (bestellung != null) {
                        mitgliederIDs.add(bestellung.getMitgliederID());
                    }
                }
            }
            
            // NEU: Von Kurs-Suche (über Kurstermin zu Kursteilnahme zu Mitglied)
            for (Kurs kurs : kursErgebnis) {
                List<Kurstermin> termine = kursManager.findTermineByKursId(kurs.getKursID());
                for (Kurstermin termin : termine) {
                    List<Kursteilnahme> teilnahmen = kursManager.findTeilnahmenByKursterminId(termin.getKursterminID());
                    for (Kursteilnahme teilnahme : teilnahmen) {
                        mitgliederIDs.add(teilnahme.getMitgliederID());
                    }
                }
            }
            
            // NEU: Von Trainer-Suche (über Kurstermin zu Kursteilnahme zu Mitglied)
            for (Trainer trainer : trainerErgebnis) {
                List<Kurstermin> termine = kursManager.findTermineByTrainerId(trainer.getTrainerID());
                for (Kurstermin termin : termine) {
                    List<Kursteilnahme> teilnahmen = kursManager.findTeilnahmenByKursterminId(termin.getKursterminID());
                    for (Kursteilnahme teilnahme : teilnahmen) {
                        mitgliederIDs.add(teilnahme.getMitgliederID());
                    }
                }
            }
            
            // NEU: Von Kurstermin-Suche (zu Kursteilnahme zu Mitglied)
            for (Kurstermin termin : kursterminErgebnis) {
                List<Kursteilnahme> teilnahmen = kursManager.findTeilnahmenByKursterminId(termin.getKursterminID());
                for (Kursteilnahme teilnahme : teilnahmen) {
                    mitgliederIDs.add(teilnahme.getMitgliederID());
                }
            }
            
            if (mitgliederIDs.isEmpty()) {
                System.out.println("Keine Einträge gefunden.");
                return;
            }
            
            // Sortierte Liste erstellen
            List<Integer> sortierteIDs = new ArrayList<>(mitgliederIDs);
            sortierteIDs.sort(Integer::compareTo);
            
            // Ausgabe
            System.out.printf("%-8s| %-15s| %-15s| %-12s| %-6s%n", "Mitgl.ID", "Vorname", "Nachname", "Geburtsdatum", "Aktiv");
            System.out.println("---------------------------------------------------------------");
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date heute = new Date();
            
            for (Integer mitgliedID : sortierteIDs) {
                Mitglieder mitglied = mitgliederManager.getMitgliederDAO().findById(mitgliedID);
                if (mitglied == null) continue;
                
                String gebDatum = "-";
                if (mitglied.getGeburtstag() != null) {
                    gebDatum = sdf.format(mitglied.getGeburtstag());
                }
                
                // Prüfe aktiven Vertrag
                List<MitgliederVertrag> vertraege = vertragManager.getMitgliederVertragDAO().findByMitgliedId(mitgliedID);
                boolean hatAktivenVertrag = false;
                for (MitgliederVertrag v : vertraege) {
                    if (v.getVertragsbeginn() != null && v.getVertragsende() != null
                            && heute.compareTo(v.getVertragsbeginn()) >= 0
                            && heute.compareTo(v.getVertragsende()) <= 0) {
                        hatAktivenVertrag = true;
                        break;
                    }
                }
                
                // Aktualisiere Aktiv-Status
                if (hatAktivenVertrag && !mitglied.isAktiv()) {
                    mitglied.setAktiv(true);
                    mitgliederManager.getMitgliederDAO().update(mitglied);
                } else if (!hatAktivenVertrag && mitglied.isAktiv()) {
                    mitglied.setAktiv(false);
                    mitgliederManager.getMitgliederDAO().update(mitglied);
                }
                
                System.out.printf("%-8d| %-15s| %-15s| %-12s| %-6s%n", 
                        mitglied.getMitgliederID(), mitglied.getVorname(), mitglied.getNachname(), gebDatum, 
                        hatAktivenVertrag ? "X" : "");
            }
            
            // Detail-Auswahl
            System.out.println("\nBitte die MitgliederID des gewünschten Eintrags eingeben (oder Enter zum Abbrechen):");
            String auswahl = scanner.nextLine();
            if (!auswahl.isBlank()) {
                try {
                    int mitgliederID = Integer.parseInt(auswahl);
                    if (mitgliederIDs.contains(mitgliederID)) {
                        Mitglieder ausgewählt = mitgliederManager.getMitgliederDAO().findById(mitgliederID);
                        if (ausgewählt != null) {
                            zeigeDetail(ausgewählt, mitgliederManager, vertragManager, verkaufManager, kursManager);
                        }
                    } else {
                        System.out.println("Kein Mitglied mit der eingegebenen MitgliederID gefunden.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Bitte eine gültige MitgliederID eingeben!");
                }
            }
        } catch (Exception e) {
            System.out.println("Fehler bei der Suche: " + (e.getMessage() != null ? e.getMessage() : e));
            e.printStackTrace();
        }
    }
    
    private void zeigeDetail(Mitglieder ausgewählt, MitgliederManager mitgliederManager, 
                             VertragManager vertragManager, VerkaufManager verkaufManager,
                             KursManager kursManager) throws Exception {
        boolean exitDetail = false;
        int tab = 1;
        
        while (!exitDetail) {
            System.out.println("\n1 Stammdaten | 2 Mitgliedschaft | 3 Zahlungsdaten | 4 Kurse | 5 Verkauf");
            switch (tab) {
                case 1:
                    showStammdaten(ausgewählt);
                    break;
                case 2:
                    showMitgliedschaft(ausgewählt, vertragManager);
                    break;
                case 3:
                    showZahlungsdaten(ausgewählt);
                    break;
                case 4: // NEU: Tab 4 - Kurse
                    showKurse(ausgewählt, kursManager);
                    break;
                case 5:
                    showVerkauf(ausgewählt, verkaufManager);
                    break;
                default:
                    System.out.println("(Tab nicht belegt)");
            }
            System.out.print("\nTab auswählen (1-5), 6: Zurück, 7: Hauptmenü\n");
            String tabEingabe = scanner.nextLine();
            if (tabEingabe.isBlank()) continue;
            if (tabEingabe.equals("6")) {
                exitDetail = true;
            } else if (tabEingabe.equals("7")) {
                exitToMainMenu = true; 
                return;             
            } else {
                try {
                    int tabWahl = Integer.parseInt(tabEingabe);
                    if (tabWahl >= 1 && tabWahl <= 5) {
                        tab = tabWahl;
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Ungültige Tab-Nummer!");
                }
            }
        }
    }

    // ---- Gemeinsame Hilfsmethoden ----
    public void showStammdaten(Mitglieder ausgewaehlt) {
        String vorname = ausgewaehlt.getVorname() != null ? ausgewaehlt.getVorname() : "-";
        String nachname = ausgewaehlt.getNachname() != null ? ausgewaehlt.getNachname() : "-";
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String geburtsdatum = (ausgewaehlt.getGeburtstag() != null) ? sdf.format(ausgewaehlt.getGeburtstag()) : "-";
        int alterJahre = ausgewaehlt.getGeburtstag() != null ? ausgewaehlt.berechneAlter() : 0;
        boolean istGeburtstag = false;
        if (ausgewaehlt.getGeburtstag() != null) {
            java.time.LocalDate birthDate;
            if (ausgewaehlt.getGeburtstag() instanceof java.sql.Date) {
                birthDate = ((java.sql.Date) ausgewaehlt.getGeburtstag()).toLocalDate();
            } else {
                birthDate = new java.sql.Date(ausgewaehlt.getGeburtstag().getTime()).toLocalDate();
            }
            java.time.LocalDate today = java.time.LocalDate.now();
            istGeburtstag = today.getMonthValue() == birthDate.getMonthValue()
                    && today.getDayOfMonth() == birthDate.getDayOfMonth();
        }
        String geburtstagInfo = istGeburtstag ? "Geburtstag!" : "";
        String strasse = ausgewaehlt.getStrasse() != null ? ausgewaehlt.getStrasse() : "-";
        String hausnr = ausgewaehlt.getHausnr() != null ? ausgewaehlt.getHausnr() : "-";
        Ort ortObj = ausgewaehlt.getOrt();
        String plz = ortObj != null && ortObj.getPLZ() != null ? ortObj.getPLZ() : "-";
        String ort = ortObj != null && ortObj.getOrt() != null ? ortObj.getOrt() : "-";
        String tel = ausgewaehlt.getTelefon() != null ? ausgewaehlt.getTelefon() : "-";
        String mail = ausgewaehlt.getMail() != null ? ausgewaehlt.getMail() : "-";
        
        System.out.printf(
                "\nName:\t\t%s %s\nGeburtsdatum:\t%s\tAlter: %d Jahre %s\nAdresse:\t%s %s\n\t\t%s %s\nTelefon:\t%s\nMail:\t\t%s\n",
                vorname, nachname, geburtsdatum, alterJahre, geburtstagInfo, strasse, hausnr, plz, ort, tel, mail);
    }
    
    public void showMitgliedschaft(Mitglieder ausgewaehlt, VertragManager manager) throws Exception {
        List<MitgliederVertrag> vertraege = manager.getMitgliederVertragDAO().findByMitgliedId(ausgewaehlt.getMitgliederID());
        if (vertraege == null || vertraege.isEmpty()) {
            System.out.println("\nDas Mitglied hat keine Verträge.");
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date heute = new Date();
        for (MitgliederVertrag mv : vertraege) {
            Vertrag v = manager.getVertragDAO().findById(mv.getVertragID());
            Intervall intervall = manager.getIntervallDAO().findById(mv.getIntervallID());
            double grundpreis = v.getGrundpreis();
            double preisrabatt = mv.getPreisrabatt();
            double wochenpreis = grundpreis - preisrabatt;
            int laufzeit = v.getLaufzeit();
            double gesamtwert = laufzeit * grundpreis;
            int wochenSeitVertragsstart = (int) ((heute.getTime() - mv.getVertragsbeginn().getTime())
                    / (1000 * 60 * 60 * 24 * 7));
            double gezahlt = 0;
            switch (mv.getIntervallID()) {
                case 1:
                    gezahlt = getWochenBisErster(heute, mv) * wochenpreis;
                    break;
                case 2:
                    gezahlt = getWochenBisLetzterVormonat(mv) * wochenpreis;
                    break;
                case 3:
                    gezahlt = wochenSeitVertragsstart * wochenpreis;
                    break;
                case 4:
                    gezahlt = ((wochenSeitVertragsstart % 2 == 0) ? wochenSeitVertragsstart
                            : (wochenSeitVertragsstart - 1)) * wochenpreis;
                    break;
            }
            double restwert = gesamtwert - gezahlt;
            double jeZahlungsintervall = 0;
            switch (mv.getIntervallID()) {
                case 1:
                case 2:
                    jeZahlungsintervall = wochenpreis * 52.14 / 12.0;
                    break;
                case 3:
                    jeZahlungsintervall = wochenpreis;
                    break;
                case 4:
                    jeZahlungsintervall = wochenpreis * 2;
                    break;
            }
            Date kuendbarBis = new Date(mv.getVertragsende().getTime() - 5L * 7L * 24L * 60L * 60L * 1000L);
            
            System.out.printf("\nVertragNr.:\t%d\t%s\n", mv.getVertragID(), v.getBezeichnung());
            System.out.printf("Laufzeit:\t%d Wochen Zahlungsintervall: %s\n", laufzeit, intervall.getBezeichnung());
            System.out.println("Trainingsbeginn\t| Vertragsbeginn| Vertragsende \t| Aktiv");
            System.out.printf("%s\t| %s \t| %s\t| %s\n",
                    mv.getTrainingsbeginn() != null ? sdf.format(mv.getTrainingsbeginn()) : "-",
                    mv.getVertragsbeginn() != null ? sdf.format(mv.getVertragsbeginn()) : "-",
                    mv.getVertragsende() != null ? sdf.format(mv.getVertragsende()) : "-",
                    (heute.compareTo(mv.getVertragsbeginn()) >= 0 && heute.compareTo(mv.getVertragsende()) <= 0) ? "X"
                            : "");
            System.out.printf("Grundpreis:\t\t%5.2f €\t| Gesamtwert:\t%6.2f €\n", grundpreis, gesamtwert);
            System.out.printf("Sonder-Rabatt:\t\t%5.2f €\t| Gezahlt:\t%6.2f €\n", preisrabatt, gezahlt);
            System.out.printf("Wochenpreis:\t\t%5.2f €\t| Restwert:\t%6.2f €\n", wochenpreis, restwert);
            System.out.printf("Je Zahlungsintervall: \t%5.2f €\t| Kündbar bis: %s %s %s\n", jeZahlungsintervall,
                    sdf.format(kuendbarBis), mv.isGekündigt() ? "Künd.: X" : "",
                    mv.isVerlängerung() ? "Verl.: X" : "");
        }
    }
    
    public void showZahlungsdaten(Mitglieder ausgewaehlt) {
        New.Objekte.Zahlungsdaten zahlungsdaten = ausgewaehlt.getZahlungsdaten();
        
        if (zahlungsdaten == null) {
            System.out.println("\nKeine Zahlungsdaten vorhanden.");
            return;
        }
        
        String vorname = ausgewaehlt.getVorname() != null ? ausgewaehlt.getVorname() : "-";
        String nachname = ausgewaehlt.getNachname() != null ? ausgewaehlt.getNachname() : "-";
        String iban = zahlungsdaten.getIBAN() != null ? zahlungsdaten.getIBAN() : "-";
        String bic = zahlungsdaten.getBIC() != null ? zahlungsdaten.getBIC() : "-";
        
        System.out.printf("\nName:\t%s %s\nIBAN:\t%s\nBIC:\t%s\n", vorname, nachname, iban, bic);
    }

    // NEU: Tab 4 - Kurse mit sauberer Tabellenformatierung
    public void showKurse(Mitglieder ausgewaehlt, KursManager manager) throws Exception {
        List<Kursteilnahme> teilnahmen = manager.findTeilnahmenByMitgliedId(ausgewaehlt.getMitgliederID());
        
        if (teilnahmen == null || teilnahmen.isEmpty()) {
            System.out.println("\nDas Mitglied hat keine Kursteilnahmen.");
            return;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        
        System.out.println("\n=== Kursteilnahmen ===");
        System.out.printf("%-30s | %-20s | %-20s | %-6s%n", "Kurs", "Trainer", "Termin", "Aktiv");
        System.out.println("=".repeat(90));
        
        for (Kursteilnahme teilnahme : teilnahmen) {
            Kurstermin termin = manager.getKursterminDAO().findById(teilnahme.getKursterminID());
            
            if (termin != null) {
                Kurs kurs = manager.getKursDAO().findById(termin.getKursID());
                Trainer trainer = manager.getTrainerDAO().findById(termin.getTrainerID());
                
                String kursName = kurs != null ? kurs.getBezeichnung() : "-";
                String trainerName = trainer != null ? trainer.getVorname() + " " + trainer.getNachname() : "-";
                String terminZeit = termin.getTermin() != null ? sdf.format(termin.getTermin()) : "-";
                
                // Name auf maximal 30 Zeichen begrenzen
                if (kursName.length() > 30) {
                    kursName = kursName.substring(0, 27) + "...";
                }
                if (trainerName.length() > 20) {
                    trainerName = trainerName.substring(0, 17) + "...";
                }
                
                System.out.printf("%-30s | %-20s | %-20s | %-6s%n",
                    kursName,
                    trainerName,
                    terminZeit,
                    teilnahme.isAktiv() ? "X" : ""
                );
            }
        }
        
        System.out.println("=".repeat(90));
    }

    // Tab 5 - Verkauf mit sauberer Tabellenformatierung
    public void showVerkauf(Mitglieder ausgewaehlt, VerkaufManager manager) throws Exception {
        List<Bestellung> bestellungen = manager.findByMitgliederId(ausgewaehlt.getMitgliederID());
        
        if (bestellungen == null || bestellungen.isEmpty()) {
            System.out.println("\nDas Mitglied hat keine Bestellungen.");
            return;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        
        for (Bestellung bestellung : bestellungen) {
            System.out.printf("\n--- Bestellung ID: %d ---\n", bestellung.getBestellungID());
            System.out.printf("Datum:\t\t%s\n", 
                bestellung.getBestelldatum() != null ? sdf.format(bestellung.getBestelldatum()) : "-");
            System.out.printf("Gesamtpreis:\t%.2f €\n\n", bestellung.getGesamtpreis());
            
            // Artikel in dieser Bestellung
            List<ArtikelBestellung> artikelBestellungen = 
                manager.findArtikelBestellungen(bestellung.getBestellungID());
            
            if (!artikelBestellungen.isEmpty()) {
                System.out.println("Artikel:");
                System.out.printf("%-30s | %5s | %12s | %12s%n", "Name", "Menge", "Einzelpreis", "Summe");
                System.out.println("=".repeat(70));
                
                for (ArtikelBestellung ab : artikelBestellungen) {
                    Artikel artikel = manager.getArtikelDAO().findById(ab.getArtikelID());
                    if (artikel != null) {
                        String name = artikel.getName() != null ? artikel.getName() : "-";
                        
                        // Name auf maximal 30 Zeichen begrenzen
                        if (name.length() > 30) {
                            name = name.substring(0, 27) + "...";
                        }
                        
                        // Einzelpreis berechnen
                        double einzelpreis = ab.getMenge() > 0 ? ab.getAufaddiert() / ab.getMenge() : 0;
                        
                        System.out.printf("%-30s | %5d | %12s | %12s%n",
                            name,
                            ab.getMenge(),
                            String.format("%,.2f €", einzelpreis),
                            String.format("%,.2f €", ab.getAufaddiert())
                        );
                    }
                }
                
                System.out.println("=".repeat(70));
            }
        }
    }
    
    public int getWochenBisErster(Date today, MitgliederVertrag mv) {
        Datum beginn = new Datum(mv.getVertragsbeginn());
        Datum bis = new Datum(today);
        Datum erster = new Datum(bis.getJahr(), bis.getMonat(), 1);
        if (beginn.isBefore(erster)) {
            long millisekunden = ersterZuDate(erster).getTime() - mv.getVertragsbeginn().getTime();
            int tage = (int) (millisekunden / (1000 * 60 * 60 * 24));
            return Math.max(0, tage / 7);
        } else {
            return 0;
        }
    }
    
    public int getWochenBisLetzterVormonat(MitgliederVertrag mv) {
        Datum beginn = new Datum(mv.getVertragsbeginn());
        Datum heute = DatumHelper.getAktuellesDatum();
        int jahr = heute.getMonat() == 1 ? heute.getJahr() - 1 : heute.getJahr();
        int monat = heute.getMonat() == 1 ? 12 : heute.getMonat() - 1;
        Calendar cal = Calendar.getInstance();
        cal.set(jahr, monat - 1, 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Datum letzter = new Datum(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH));
        if (beginn.isBefore(letzter)) {
            long millisekunden = letzterZuDate(letzter).getTime() - mv.getVertragsbeginn().getTime();
            int tage = (int) (millisekunden / (1000 * 60 * 60 * 24));
            return Math.max(0, tage / 7);
        } else {
            return 0;
        }
    }
    
    private Date ersterZuDate(Datum d) {
        Calendar cal = Calendar.getInstance();
        cal.set(d.getJahr(), d.getMonat() - 1, d.getTag(), 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    private Date letzterZuDate(Datum d) {
        Calendar cal = Calendar.getInstance();
        cal.set(d.getJahr(), d.getMonat() - 1, d.getTag(), 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
}
