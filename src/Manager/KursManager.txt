package Manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DAOs.ConnectionDB;
import DAOs.KursDAO;
import DAOs.KursleitungDAO;
import DAOs.KursteilnahmeDAO;
import DAOs.KursterminDAO;
import DAOs.MitarbeiterDAO;
import DAOs.MitgliederDAO;
import Exception.ConnectionException;
import Exception.IntException;
import Exception.NotFoundException;
import Objekte.Kurs;
import Objekte.Kursleitung;
import Objekte.Kursteilnahme;
import Objekte.Kurstermin;
import Objekte.Mitarbeiter;
import Objekte.Mitglieder;

public class KursManager extends BaseManager<Kurs> {

    private final KursDAO kursDAO;
    private final KursterminDAO kursterminDAO;
    private final KursteilnahmeDAO kursteilnahmeDAO;
    private final KursleitungDAO kursleitungDAO;
    private final MitarbeiterDAO mitarbeiterDAO;
    private final MitgliederDAO mitgliederDAO;

    public KursManager() throws ConnectionException, SQLException {
        Connection connection = ConnectionDB.getConnection();
        kursDAO = new KursDAO(connection);
        kursterminDAO = new KursterminDAO(connection);
        kursteilnahmeDAO = new KursteilnahmeDAO(connection);
        kursleitungDAO = new KursleitungDAO(connection);
        mitarbeiterDAO = new MitarbeiterDAO(connection);
        mitgliederDAO = new MitgliederDAO(connection);
    }

    // Getter für DAOs
    public KursDAO getKursDAO() {
        return kursDAO;
    }

    public KursterminDAO getKursterminDAO() {
        return kursterminDAO;
    }

    public KursteilnahmeDAO getKursteilnahmeDAO() {
        return kursteilnahmeDAO;
    }

    public KursleitungDAO getKursleitungDAO() {
        return kursleitungDAO;
    }

    public MitarbeiterDAO getMitarbeiterDAO() {
        return mitarbeiterDAO;
    }

    public MitgliederDAO getMitgliederDAO() {
        return mitgliederDAO;
    }

    @Override
    public void process() {
        // Implementiere hier spezifische Verarbeitung für Kurse
    }

    /**
     * Findet einen Kurs anhand der KursID
     */
    public Kurs findById(int kursID) throws NotFoundException, IntException, SQLException {
        Kurs kurs = kursDAO.findById(kursID);
        if (kurs == null) {
            throw new NotFoundException("Kurs mit ID " + kursID + " nicht gefunden.");
        }
        return kurs;
    }

    /**
     * Gemeinsame Suche nach einem Suchbegriff über Kurse und Termine
     *
     * @param searchTerm Suchbegriff
     * @return Liste gefundener Kurse
     */
    public List<Kurs> search(String searchTerm) throws SQLException, IntException {
        List<Kurs> result = new ArrayList<>();

        // Suche in Kursen
        List<Kurs> kurse = kursDAO.searchAllAttributes(searchTerm);
        if (kurse != null) {
            result.addAll(kurse);
        }
        // Suche in Kursterminen
        List<Kurstermin> terminResult = kursterminDAO.searchAllAttributes(searchTerm);
        if (terminResult != null) {
            for (Kurstermin termin : terminResult) {
                try {
                    Kurs kurs = kursDAO.findById(termin.getKursID());
                    if (kurs != null && !result.contains(kurs)) {
                        result.add(kurs);
                    }
                } catch (NotFoundException e) {
                    // Kurs nicht gefunden - ignorieren und weitermachen
                }
            }
        }

        // Suche in Kursleitungen/Mitarbeiter
        List<Kursleitung> kursleitungen = kursleitungDAO.searchAllAttributes(searchTerm);
        if (kursleitungen != null) {
            for (Kursleitung leitung : kursleitungen) {
                try {
                    Kurstermin termin = kursterminDAO.findById(leitung.getKursterminID());
                    if (termin != null) {
                        Kurs kurs = kursDAO.findById(termin.getKursID());
                        if (kurs != null && !result.contains(kurs)) {
                            result.add(kurs);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }

        return result;
    }

    /**
     * Alle Kurstermine eines Kurses finden
     */
    public List<Kurstermin> findTermineByKursId(int kursID) throws SQLException {
        return kursterminDAO.findByKursId(kursID);
    }

    /**
     * Alle Kursteilnahmen eines Mitglieds finden
     */
    public List<Kursteilnahme> findTeilnahmenByMitgliedId(int mitgliederID) throws SQLException {
        return kursteilnahmeDAO.findByMitgliederId(mitgliederID);
    }

    /**
     * Alle Kursteilnahmen eines Kurstermins finden
     */
    public List<Kursteilnahme> findTeilnahmenByKursterminId(int kursterminID) throws SQLException {
        return kursteilnahmeDAO.findByKursterminId(kursterminID);
    }

    /**
     * Meldet ein Mitglied für einen Kurstermin an
     */
    public void meldeAnFuerKurs(int mitgliederID, int kursterminID)
            throws SQLException, IntException, NotFoundException {
        Mitglieder mitglied = mitgliederDAO.findById(mitgliederID);
        if (mitglied == null) {
            throw new NotFoundException("Mitglied mit ID " + mitgliederID + " nicht gefunden.");
        }

        Kurstermin termin = kursterminDAO.findById(kursterminID);
        if (termin == null) {
            throw new NotFoundException("Kurstermin mit ID " + kursterminID + " nicht gefunden.");
        }

        try {
            Kursteilnahme existingTeilnahme = kursteilnahmeDAO.findByCompositeKey(mitgliederID, kursterminID);
            if (existingTeilnahme != null) {
                System.out.println("Mitglied ist bereits für diesen Kurstermin angemeldet.");
                return;
            }
        } catch (NotFoundException e) {
            // Teilnahme existiert nicht - das ist OK, wir können fortfahren
        }

        Kursteilnahme teilnahme = new Kursteilnahme();
        teilnahme.setMitgliederID(mitgliederID);
        teilnahme.setKursterminID(kursterminID);
        teilnahme.setAngemeldet(true);
        teilnahme.setAnmeldezeit(new java.sql.Timestamp(System.currentTimeMillis()));
        teilnahme.setAbgemeldet(false);
        teilnahme.setAbmeldezeit(null);
        teilnahme.setAktiv(true);
        teilnahme.setKommentar(null);

        kursteilnahmeDAO.insert(teilnahme);
    }

    /**
     * Meldet ein Mitglied von einem Kurstermin ab
     */
    public void meldeAbVonKurs(int mitgliederID, int kursterminID) throws SQLException, NotFoundException {
        Kursteilnahme teilnahme = kursteilnahmeDAO.findByCompositeKey(mitgliederID, kursterminID);

        if (teilnahme == null) {
            throw new NotFoundException("Keine Anmeldung für diesen Kurstermin gefunden.");
        }
        kursteilnahmeDAO.deleteByCompositeKey(mitgliederID, kursterminID);
    }

    /**
     * Trainer (Mitarbeiter) für einen Kurstermin über Kursleitung ermitteln
     */
    public Mitarbeiter findeTrainerFürKurstermin(int kursterminID) throws SQLException, IntException {
        // Nimmt an, es gibt nur eine aktive Kursleitung je Kurstermin; sonst ersten nehmen
        List<Kursleitung> leitungen = kursleitungDAO.findAktiveLeitungByKursterminId(kursterminID);
        for (Kursleitung kursleitung : leitungen) {
            Mitarbeiter trainer = mitarbeiterDAO.findById(kursleitung.getMitarbeiterID());
            if (trainer != null) {
                return trainer;
            }
        }
        return null;
    }

    /**
     * Zeigt alle Kursteilnehmer eines Kurstermins an
     */
    public void zeigeKursteilnehmer(int kursterminID) throws SQLException, IntException, NotFoundException {
        Kurstermin termin = kursterminDAO.findById(kursterminID);
        if (termin == null) {
            System.out.println("Kurstermin nicht gefunden!");
            return;
        }

        Kurs kurs = kursDAO.findById(termin.getKursID());
        Mitarbeiter trainer = findeTrainerFürKurstermin(kursterminID);

        String terminDatumZeit = "-";
        if (termin.getTermin() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(termin.getTermin());
            int tag = cal.get(Calendar.DAY_OF_MONTH);
            int monat = cal.get(Calendar.MONTH) + 1;
            int jahr = cal.get(Calendar.YEAR);
            int stunde = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            terminDatumZeit = String.format("%02d.%02d.%4d %02d:%02d", tag, monat, jahr, stunde, minute);
        }

        System.out.println("\n=== Kursteilnehmer ===");
        System.out.println("Kurs: " + (kurs != null ? kurs.getBezeichnung() : "-"));
        System.out.println("Trainer: " + (trainer != null ? trainer.getVorname() + " " + trainer.getNachname() : "-"));
        System.out.println("Termin: " + terminDatumZeit);
        System.out.println();

        List<Kursteilnahme> teilnahmen = kursteilnahmeDAO.findByKursterminId(kursterminID);
        if (teilnahmen.isEmpty()) {
            System.out.println("Keine Teilnehmer angemeldet.");
            return;
        }

        System.out.printf("%-12s | %-20s | %-20s%n", "MitgliederID", "Vorname", "Nachname");
        System.out.println("-".repeat(60));

        for (Kursteilnahme teilnahme : teilnahmen) {
            Mitglieder mitglied = mitgliederDAO.findById(teilnahme.getMitgliederID());
            if (mitglied != null) {
                System.out.printf("%-12d | %-20s | %-20s%n", mitglied.getMitgliederID(), mitglied.getVorname(),
                        mitglied.getNachname());
            }
        }
    }

}
