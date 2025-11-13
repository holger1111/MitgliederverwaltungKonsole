package Manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAOs.ConnectionDB;
import DAOs.MitarbeiterDAO;
import DAOs.OrtDAO;
import DAOs.ZahlungsdatenDAO;
import DAOs.BenutzerDAO;
import DAOs.RolleDAO;
import Exception.ConnectionException;
import Exception.IntException;
import Exception.NotFoundException;
import Objekte.Mitarbeiter;
import Objekte.Ort;
import Objekte.Zahlungsdaten;
import Objekte.Benutzer;
import Objekte.Rolle;

public class MitarbeiterManager extends BaseManager<Mitarbeiter> {

    private final MitarbeiterDAO mitarbeiterDAO;
    private final OrtDAO ortDAO;
    private final ZahlungsdatenDAO zahlungsdatenDAO;
    private final BenutzerDAO benutzerDAO;
    private final RolleDAO rolleDAO;

    public MitarbeiterManager() throws ConnectionException, SQLException {
        Connection connection = ConnectionDB.getConnection();
        mitarbeiterDAO = new MitarbeiterDAO(connection);
        ortDAO = new OrtDAO(connection);
        zahlungsdatenDAO = new ZahlungsdatenDAO(connection);
        benutzerDAO = new BenutzerDAO(connection);
        rolleDAO = new RolleDAO(connection);
    }

    public MitarbeiterDAO getMitarbeiterDAO() {
        return mitarbeiterDAO;
    }

    public OrtDAO getOrtDAO() {
        return ortDAO;
    }

    public ZahlungsdatenDAO getZahlungsdatenDAO() {
        return zahlungsdatenDAO;
    }

    public BenutzerDAO getBenutzerDAO() {
        return benutzerDAO;
    }

    public RolleDAO getRolleDAO() {
        return rolleDAO;
    }

    @Override
    public void add(Mitarbeiter item) {
        super.add(item);
    }

    @Override
    public void remove(Mitarbeiter item) {
        super.remove(item);
    }

    @Override
    public List<Mitarbeiter> getAll() {
        return super.getAll();
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public void process() {
    }

    public Mitarbeiter findById(int mitarbeiterID) throws NotFoundException, IntException, SQLException {
        Mitarbeiter mitarbeiter = mitarbeiterDAO.findById(mitarbeiterID);
        if (mitarbeiter == null) {
            throw new NotFoundException("Mitarbeiter mit ID " + mitarbeiterID + " nicht gefunden.");
        }
        return mitarbeiter;
    }

    /**
     * Gemeinsame Suche nach einem Suchbegriff über alle Attribute von Mitarbeitern,
     * Ort, Zahlungsdaten, Benutzer und Rolle.
     * 
     * @param searchTerm Suchbegriff
     * @return Liste gefundener Mitarbeiter
     */
    public List<Mitarbeiter> search(String searchTerm) throws SQLException, IntException {
        List<Mitarbeiter> result = new ArrayList<>();

        List<Mitarbeiter> mitarbeiter = mitarbeiterDAO.searchAllAttributes(searchTerm);
        if (mitarbeiter != null) {
            result.addAll(mitarbeiter);
        }

        List<Ort> orte = ortDAO.searchAllAttributes(searchTerm);
        if (orte != null) {
            for (Ort o : orte) {
                List<Mitarbeiter> ortMitarbeiter = mitarbeiterDAO.findByOrtId(o.getOrtID());
                for (Mitarbeiter m : ortMitarbeiter) {
                    if (!result.contains(m)) {
                        result.add(m);
                    }
                }
            }
        }

        List<Zahlungsdaten> zahlungsdatenListe = zahlungsdatenDAO.searchAllAttributes(searchTerm);
        if (zahlungsdatenListe != null) {
            for (Zahlungsdaten z : zahlungsdatenListe) {
                List<Mitarbeiter> zahlungsdatenMitarbeiter = mitarbeiterDAO.findByZahlungsdatenId(z.getZahlungsdatenID());
                for (Mitarbeiter m : zahlungsdatenMitarbeiter) {
                    if (!result.contains(m)) {
                        result.add(m);
                    }
                }
            }
        }

        List<Benutzer> benutzerListe = benutzerDAO.searchAllAttributes(searchTerm);
        if (benutzerListe != null) {
            for (Benutzer b : benutzerListe) {
                List<Mitarbeiter> benutzerMitarbeiter = mitarbeiterDAO.findByBenutzerId(b.getBenutzerID());
				for (Mitarbeiter m : benutzerMitarbeiter) {
					if (!result.contains(m)) {
                        result.add(m);
                    }
                }
            }
        }

        List<Rolle> rollenListe = rolleDAO.searchAllAttributes(searchTerm);
        if (rollenListe != null) {
            for (Rolle r : rollenListe) {
                List<Mitarbeiter> rollenMitarbeiter = mitarbeiterDAO.findByRolleId(r.getRolleID());
                for (Mitarbeiter m : rollenMitarbeiter) {
                    if (!result.contains(m)) {
                        result.add(m);
                    }
                }
            }
        }

        return result;
    }
}
