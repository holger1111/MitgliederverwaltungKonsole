package New.Manager;

import New.DAOs.BenutzerDAO;
import New.DAOs.RolleDAO;
import New.Exception.ConnectionException;
import New.Exception.IntException;
import New.Exception.NotFoundException;
import New.Objekte.Benutzer;
import New.Objekte.Rolle;
import New.DAOs.ConnectionDB;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
