package Manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import DAOs.BenutzerDAO;
import DAOs.ConnectionDB;
import DAOs.RolleDAO;
import Exception.ConnectionException;
import Exception.IntException;
import Exception.NotFoundException;
import Objekte.Benutzer;
import Objekte.Rolle;

public class AdminManager {

    private final BenutzerDAO benutzerDAO;
    private final RolleDAO rolleDAO;

    public AdminManager() throws ConnectionException, SQLException {
        Connection connection = ConnectionDB.getConnection();
        this.benutzerDAO = new BenutzerDAO(connection);
        this.rolleDAO = new RolleDAO(connection);
    }

    // Benutzer-Methoden

    public Benutzer findBenutzerById(int id) throws SQLException, IntException, NotFoundException {
        return benutzerDAO.findById(id);
    }

    public List<Benutzer> findAllBenutzer() throws SQLException {
        return benutzerDAO.findAll();
    }

    public void createBenutzer(Benutzer benutzer) throws SQLException {
        benutzerDAO.insert(benutzer);
    }

    public void updateBenutzer(Benutzer benutzer) throws SQLException {
        benutzerDAO.update(benutzer);
    }

    public void deleteBenutzer(int id) throws SQLException {
        benutzerDAO.delete(id);
    }

    // Rolle-Methoden

    public Rolle findRolleById(int id) throws SQLException, IntException, NotFoundException {
        return rolleDAO.findById(id);
    }

    public List<Rolle> findAllRollen() throws SQLException {
        return rolleDAO.findAll();
    }

    public void createRolle(Rolle rolle) throws SQLException {
        rolleDAO.insert(rolle);
    }

    public void updateRolle(Rolle rolle) throws SQLException {
        rolleDAO.update(rolle);
    }

    public void deleteRolle(int id) throws SQLException {
        rolleDAO.delete(id);
    }

    // Weitere Admin-Funktionen können hier hinzugefügt werden
}
