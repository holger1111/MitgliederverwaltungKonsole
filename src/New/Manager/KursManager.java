package New.Manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import New.DAOs.KursDAO;
import New.DAOs.KursterminDAO;
import New.DAOs.KursteilnahmeDAO;
import New.DAOs.TrainerDAO;
import New.DAOs.MitgliederDAO;
import New.DAOs.ConnectionDB;
import New.Objekte.Kurs;
import New.Objekte.Kurstermin;
import New.Objekte.Kursteilnahme;
import New.Objekte.Trainer;
import New.Objekte.Mitglieder;
import New.Exception.ConnectionException;
import New.Exception.IntException;
import New.Exception.NotFoundException;

public class KursManager extends BaseManager<Kurs> {

	private final KursDAO kursDAO;
	private final KursterminDAO kursterminDAO;
	private final KursteilnahmeDAO kursteilnahmeDAO;
	private final TrainerDAO trainerDAO;
	private final MitgliederDAO mitgliederDAO;

	public KursManager() throws ConnectionException, SQLException {
		Connection connection = ConnectionDB.getConnection();
		kursDAO = new KursDAO(connection);
		kursterminDAO = new KursterminDAO(connection);
		kursteilnahmeDAO = new KursteilnahmeDAO(connection);
		trainerDAO = new TrainerDAO(connection);
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

	public TrainerDAO getTrainerDAO() {
		return trainerDAO;
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
	 * Gemeinsame Suche nach einem Suchbegriff über Kurse, Trainer und Termine
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

		// Suche in Trainern (finde Kurse über Kurstermine)
		List<Trainer> trainerResult = trainerDAO.searchAllAttributes(searchTerm);
		if (trainerResult != null) {
			for (Trainer trainer : trainerResult) {
				List<Kurstermin> termine = kursterminDAO.findByTrainerId(trainer.getTrainerID());
				for (Kurstermin termin : termine) {
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

		return result;
	}

	/**
	 * Hilfsmethode: Findet alle Kurstermine eines Kurses
	 */
	public List<Kurstermin> findTermineByKursId(int kursID) throws SQLException {
		return kursterminDAO.findByKursId(kursID);
	}

	/**
	 * Hilfsmethode: Findet alle Kurstermine eines Trainers
	 */
	public List<Kurstermin> findTermineByTrainerId(int trainerID) throws SQLException {
		return kursterminDAO.findByTrainerId(trainerID);
	}

	/**
	 * Hilfsmethode: Findet alle Kursteilnahmen eines Mitglieds
	 */
	public List<Kursteilnahme> findTeilnahmenByMitgliedId(int mitgliederID) throws SQLException {
		return kursteilnahmeDAO.findByMitgliederId(mitgliederID);
	}

	/**
	 * Hilfsmethode: Findet alle Kursteilnahmen eines Kurstermins
	 */
	public List<Kursteilnahme> findTeilnahmenByKursterminId(int kursterminID) throws SQLException {
		return kursteilnahmeDAO.findByKursterminId(kursterminID);
	}

	/**
	 * Meldet ein Mitglied für einen Kurstermin an
	 */
	public void meldeAnFuerKurs(int mitgliederID, int kursterminID)
			throws SQLException, IntException, NotFoundException {
		// Prüfe, ob Mitglied existiert
		Mitglieder mitglied = mitgliederDAO.findById(mitgliederID);
		if (mitglied == null) {
			throw new NotFoundException("Mitglied mit ID " + mitgliederID + " nicht gefunden.");
		}

		// Prüfe, ob Kurstermin existiert
		Kurstermin termin = kursterminDAO.findById(kursterminID);
		if (termin == null) {
			throw new NotFoundException("Kurstermin mit ID " + kursterminID + " nicht gefunden.");
		}

		// Prüfe, ob Anmeldung bereits existiert
		try {
			Kursteilnahme existingTeilnahme = kursteilnahmeDAO.findByCompositeKey(mitgliederID, kursterminID);
			if (existingTeilnahme != null) {
				System.out.println("Mitglied ist bereits für diesen Kurstermin angemeldet.");
				return;
			}
		} catch (NotFoundException e) {
			// Teilnahme existiert nicht - das ist OK, wir können fortfahren
		}

		// Erstelle Kursteilnahme
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
		// Prüfe, ob Anmeldung existiert
		Kursteilnahme teilnahme = kursteilnahmeDAO.findByCompositeKey(mitgliederID, kursterminID);

		if (teilnahme == null) {
			throw new NotFoundException("Keine Anmeldung für diesen Kurstermin gefunden.");
		}

		// Option 1: Komplett löschen
		kursteilnahmeDAO.deleteByCompositeKey(mitgliederID, kursterminID);

		// Option 2: Nur als abgemeldet markieren (auskommentiert)
		/*
		 * teilnahme.setAbgemeldet(true); teilnahme.setAbmeldezeit(new
		 * java.sql.Timestamp(System.currentTimeMillis())); teilnahme.setAktiv(false);
		 * kursteilnahmeDAO.update(teilnahme);
		 */
	}

	/**
	 * Zeigt alle Kursteilnehmer eines Kurstermins an
	 */
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
		Trainer trainer = trainerDAO.findById(termin.getTrainerID());

		// Formatiere Datum/Zeit
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
