package New.Manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import New.DAOs.ArtikelDAO;
import New.DAOs.KategorieDAO;
import New.DAOs.BestellungDAO;
import New.DAOs.ArtikelBestellungDAO;
import New.DAOs.ZahlungDAO;
import New.DAOs.MitgliederDAO;
import New.Objekte.Artikel;
import New.Objekte.Bestellung;
import New.Objekte.Mitglieder;
import New.Objekte.ArtikelBestellung;
import New.Objekte.Zahlung;
import New.Exception.ConnectionException;
import New.Exception.IntException;
import New.Exception.NotFoundException;
import New.DAOs.ConnectionDB;

public class VerkaufManager extends BaseManager<Bestellung> {

    private final ArtikelDAO artikelDAO;
    private final KategorieDAO kategorieDAO;
    private final BestellungDAO bestellungDAO;
    private final ArtikelBestellungDAO artikelBestellungDAO;
    private final ZahlungDAO zahlungDAO;
    private final MitgliederDAO mitgliederDAO;

    public VerkaufManager() throws ConnectionException, SQLException {
        Connection connection = ConnectionDB.getConnection();
        artikelDAO = new ArtikelDAO(connection);
        kategorieDAO = new KategorieDAO(connection);
        bestellungDAO = new BestellungDAO(connection);
        artikelBestellungDAO = new ArtikelBestellungDAO(connection);
        zahlungDAO = new ZahlungDAO(connection);
        mitgliederDAO = new MitgliederDAO(connection);
    }

    // Getter für DAOs
    public ArtikelDAO getArtikelDAO() {
        return artikelDAO;
    }

    public KategorieDAO getKategorieDAO() {
        return kategorieDAO;
    }

    public BestellungDAO getBestellungDAO() {
        return bestellungDAO;
    }

    public ArtikelBestellungDAO getArtikelBestellungDAO() {
        return artikelBestellungDAO;
    }

    public ZahlungDAO getZahlungDAO() {
        return zahlungDAO;
    }

    public MitgliederDAO getMitgliederDAO() {
        return mitgliederDAO;
    }

    @Override
    public void process() {
        // Hier ggf. die spezifische Verarbeitung für Bestellungen implementieren
    }

    public Bestellung findById(int bestellungID) throws NotFoundException, IntException, SQLException {
        Bestellung bestellung = bestellungDAO.findById(bestellungID);
        if (bestellung == null) {
            throw new NotFoundException("Bestellung mit ID " + bestellungID + " nicht gefunden.");
        }
        return bestellung;
    }

    /**
     * Gemeinsame Suche nach einem Suchbegriff über Bestellungen, Artikel und Zahlungen.
     * 
     * @param searchTerm Suchbegriff
     * @return Liste gefundener Bestellungen
     */
    public List<Bestellung> search(String searchTerm) throws SQLException, IntException {
        List<Bestellung> result = new ArrayList<>();

        List<Bestellung> bestellungen = bestellungDAO.searchAllAttributes(searchTerm);
        if (bestellungen != null) {
            result.addAll(bestellungen);
        }

        List<Artikel> artikelResult = artikelDAO.searchAllAttributes(searchTerm);
        if (artikelResult != null) {
            for (Artikel artikel : artikelResult) {
                List<ArtikelBestellung> artikelBestellungen =
                    artikelBestellungDAO.findByArtikelId(artikel.getArtikelID());
                for (ArtikelBestellung ab : artikelBestellungen) {
                    Bestellung bestellung = bestellungDAO.findById(ab.getBestellungID());
                    if (bestellung != null && !result.contains(bestellung)) {
                        result.add(bestellung);
                    }
                }
            }
        }

        List<Zahlung> zahlungResult = zahlungDAO.searchAllAttributes(searchTerm);
        if (zahlungResult != null) {
            for (Zahlung zahlung : zahlungResult) {
                List<Bestellung> bestellungenByZahlung =
                    bestellungDAO.findAll().stream()
                        .filter(b -> b.getZahlungID() == zahlung.getZahlungID())
                        .toList();
                for (Bestellung bestellung : bestellungenByZahlung) {
                    if (!result.contains(bestellung)) {
                        result.add(bestellung);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Findet alle Bestellungen eines Mitglieds
     */
    public List<Bestellung> findByMitgliederId(int mitgliederID) throws SQLException {
        return bestellungDAO.findByMitgliederId(mitgliederID);
    }

    /**
     * Findet alle ArtikelBestellungen zu einer Bestellung
     */
    public List<ArtikelBestellung> findArtikelBestellungen(int bestellungID) throws SQLException {
        return artikelBestellungDAO.findByBestellungId(bestellungID);
    }

    /**
     * Druckt eine formatierte Bestellungsquittung für eine gespeicherte Bestellung
     */
    public void druckeBestellungsQuittung(int bestellungID) throws SQLException, IntException, NotFoundException {
        // Bestellung laden
        Bestellung bestellung = bestellungDAO.findById(bestellungID);
        if (bestellung == null) {
            System.out.println("Bestellung nicht gefunden!");
            return;
        }

        // Mitglied laden
        Mitglieder mitglied = mitgliederDAO.findById(bestellung.getMitgliederID());

        // Zahlungsart laden
        Zahlung zahlung = zahlungDAO.findById(bestellung.getZahlungID());

        // Artikel der Bestellung laden
        List<ArtikelBestellung> artikelBestellungen = artikelBestellungDAO.findByBestellungId(bestellungID);

        System.out.println("\n" + "=".repeat(80));
        System.out.println("                          BESTELLUNGSQUITTUNG");
        System.out.println("=".repeat(80));
        System.out.println();
        System.out.println("--- Bestellung ID: " + bestellungID + " ---");
        System.out.println("Datum:         " + bestellung.getBestelldatum());
        System.out.println("Gesamtpreis:   " + String.format("%.2f €", bestellung.getGesamtpreis()));
        System.out.println();

        // Kundendaten
        if (mitglied != null) {
            System.out.println("Kunde:         " + mitglied.getVorname() + " " + mitglied.getNachname());
            System.out.println("Mitglied-ID:   " + mitglied.getMitgliederID());
        }
        System.out.println();

        // Zahlungsart
        if (zahlung != null) {
            System.out.println("Zahlungsart:   " + zahlung.getZahlungsart());
        }
        System.out.println();

        // Artikelliste mit ArtikelID-Spalte
        System.out.println("Artikel:");
        System.out.printf("%-10s | %-30s | %-15s | %5s | %12s | %12s%n", "ArtikelID", "Name", "Kategorie", "Menge", "Einzelpreis", "Summe");
        System.out.println("=".repeat(110));

        for (ArtikelBestellung ab : artikelBestellungen) {
            Artikel artikel = artikelDAO.findById(ab.getArtikelID());
            if (artikel != null) {
                String name = artikel.getName() != null ? artikel.getName() : "-";
                String kategorie = (artikel.getKategorie() != null && artikel.getKategorie().getBezeichnung() != null)
                                  ? artikel.getKategorie().getBezeichnung() : "-";
                if (name.length() > 30) {
                    name = name.substring(0, 27) + "...";
                }
                if (kategorie.length() > 15) {
                    kategorie = kategorie.substring(0, 12) + "...";
                }
                double einzelpreis = ab.getMenge() > 0 ? ab.getAufaddiert() / ab.getMenge() : 0;

                System.out.printf("%-10d | %-30s | %-15s | %5d | %12s | %12s%n",
                    artikel.getArtikelID(),
                    name,
                    kategorie,
                    ab.getMenge(),
                    String.format("%,.2f €", einzelpreis),
                    String.format("%,.2f €", ab.getAufaddiert())
                );
            }
        }

        System.out.println("=".repeat(110));
        System.out.printf("%-10s %-30s %-15s %5s %12s %12s%n", "", "", "", "", "Gesamtpreis:", String.format("%,.2f €", bestellung.getGesamtpreis()));
        System.out.println("=".repeat(80));
    }

}
