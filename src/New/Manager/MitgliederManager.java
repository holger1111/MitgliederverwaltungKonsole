package New.Manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import New.DAOs.MitgliederDAO;
import New.DAOs.InteressentenDAO;
import New.DAOs.OrtDAO;
import New.DAOs.ZahlungsdatenDAO;
import New.Objekte.Mitglieder;
import New.Objekte.Interessenten;
import New.Objekte.Ort;
import New.Objekte.Zahlungsdaten;
import New.Exception.ConnectionException;
import New.Exception.IntException;
import New.Exception.NotFoundException;
import New.DAOs.ConnectionDB;

public class MitgliederManager extends BaseManager<Mitglieder> {

	private final MitgliederDAO mitgliederDAO;
	private final InteressentenDAO interessentenDAO;
	private final OrtDAO ortDAO;
	private final ZahlungsdatenDAO zahlungsdatenDAO;

	public MitgliederManager() throws ConnectionException, SQLException {
		Connection connection = ConnectionDB.getConnection();
		mitgliederDAO = new MitgliederDAO(connection);
		interessentenDAO = new InteressentenDAO(connection);
		ortDAO = new OrtDAO(connection);
		zahlungsdatenDAO = new ZahlungsdatenDAO(connection);
	}
	
	public int createInteressent(String vorname, String nachname, String telefon) throws Exception {
	    return interessentenDAO.findOrCreateInteressent(vorname, nachname, telefon);
	}

	public MitgliederDAO getMitgliederDAO() {
	    return mitgliederDAO;
	}

	public OrtDAO getOrtDAO() {
	    return ortDAO;
	}

	public ZahlungsdatenDAO getZahlungsdatenDAO() {
	    return zahlungsdatenDAO;
	}

	@Override
	public void add(Mitglieder item) {
		super.add(item);
	}

	@Override
	public void remove(Mitglieder item) {
		super.remove(item);
	}

	@Override
	public List<Mitglieder> getAll() {
		return super.getAll();
	}

	@Override
	public void clear() {
		super.clear();
	}

	@Override
	public void process() {
	}

	public Mitglieder findById(int MitgliederID) throws NotFoundException, IntException, SQLException {
		Mitglieder mitglied = mitgliederDAO.findById(MitgliederID);
		if (mitglied == null) {
			throw new NotFoundException("Mitglied mit ID " + MitgliederID + " nicht gefunden.");
		}
		return mitglied;
	}

	/**
	 * Gemeinsame Suche nach einem Suchbegriff über alle Attribute von Mitgliedern,
	 * Interessenten, Ort und Zahlungsdaten.
	 * 
	 * @param searchTerm Suchbegriff
	 * @return Liste gefundener Mitglieder
	 */
	public List<Mitglieder> search(String searchTerm) throws SQLException, IntException {
		List<Mitglieder> result = new ArrayList<>();

		List<Mitglieder> mitglieder = mitgliederDAO.searchAllAttributes(searchTerm);
		if (mitglieder != null) {
			result.addAll(mitglieder);
		}

		List<Interessenten> interessenten = interessentenDAO.searchAllAttributes(searchTerm);
		if (interessenten != null) {
			for (Interessenten i : interessenten) {
				List<Mitglieder> mitgliederByInteressent = mitgliederDAO.findByInteressentenId(i.getMitgliederID());
				for (Mitglieder m : mitgliederByInteressent) {
					if (!result.contains(m)) {
						result.add(m);
					}
				}
			}
		}

		List<Ort> orte = ortDAO.searchAllAttributes(searchTerm);
		if (orte != null) {
			for (Ort o : orte) {
				List<Mitglieder> ortMitglieder = mitgliederDAO.findByOrtId(o.getOrtID());
				for (Mitglieder m : ortMitglieder) {
					if (!result.contains(m)) {
						result.add(m);
					}
				}
			}
		}

		List<Zahlungsdaten> zahlungsdatenListe = zahlungsdatenDAO.searchAllAttributes(searchTerm);
		if (zahlungsdatenListe != null) {
			for (Zahlungsdaten z : zahlungsdatenListe) {
				List<Mitglieder> zahlungsdatenMitglieder = mitgliederDAO.findByZahlungsdatenId(z.getZahlungsdatenID());
				for (Mitglieder m : zahlungsdatenMitglieder) {
					if (!result.contains(m)) {
						result.add(m);
					}
				}
			}
		}
		return result;
	}
}