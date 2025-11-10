package New.DAOs;

import New.Objekte.Benutzer;
import New.Objekte.Rolle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BenutzerDAO extends BaseDAO<Benutzer> {

    private final RolleDAO rolleDAO;

    public BenutzerDAO(Connection connection) {
        super(connection);
        this.rolleDAO = new RolleDAO(connection);
    }

    @Override
    public void insert(Benutzer benutzer) throws SQLException {
        String sql = "INSERT INTO Benutzer (Benutzername, Passwort, RolleID) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, benutzer.getBenutzername());
            ps.setString(2, benutzer.getPasswort());
            ps.setInt(3, benutzer.getRolle() != null ? benutzer.getRolle().getRolleID() : 0);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                benutzer.setBenutzerID(rs.getInt(1));
            }
        } finally {
            closeResources(rs, ps);
        }
    }

    @Override
    public Benutzer findById(int id) throws SQLException {
        String sql = "SELECT * FROM Benutzer WHERE BenutzerID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToBenutzer(rs);
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }

    @Override
    public void update(Benutzer benutzer) throws SQLException {
        String sql = "UPDATE Benutzer SET Benutzername = ?, Passwort = ?, RolleID = ? WHERE BenutzerID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, benutzer.getBenutzername());
            ps.setString(2, benutzer.getPasswort());
            ps.setInt(3, benutzer.getRolle() != null ? benutzer.getRolle().getRolleID() : 0);
            ps.setInt(4, benutzer.getBenutzerID());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Benutzer WHERE BenutzerID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    public List<Benutzer> findAll() throws SQLException {
        String sql = "SELECT * FROM Benutzer";
        List<Benutzer> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToBenutzer(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return list;
    }

    private Benutzer mapRowToBenutzer(ResultSet rs) throws SQLException {
        Benutzer benutzer = new Benutzer();
        benutzer.setBenutzerID(rs.getInt("BenutzerID"));
        benutzer.setBenutzername(rs.getString("Benutzername"));
        benutzer.setPasswort(rs.getString("Passwort"));

        int rolleID = rs.getInt("RolleID");
        Rolle rolle = rolleID > 0 ? rolleDAO.findById(rolleID) : null;
        benutzer.setRolle(rolle);

        return benutzer;
    }
}
