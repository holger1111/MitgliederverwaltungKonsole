package DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Objekte.Rolle;

public class RolleDAO extends BaseDAO<Rolle> {

    public RolleDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(Rolle rolle) throws SQLException {
        String sql = "INSERT INTO Rolle (Bezeichnung, Kommentar) VALUES (?, ?)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, rolle.getBezeichnung());
            ps.setString(2, rolle.getKommentar());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                rolle.setRolleID(rs.getInt(1));
            }
        } finally {
            closeResources(rs, ps);
        }
    }

    @Override
    public Rolle findById(int id) throws SQLException {
        String sql = "SELECT * FROM Rolle WHERE RolleID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToRolle(rs);
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }

    @Override
    public void update(Rolle rolle) throws SQLException {
        String sql = "UPDATE Rolle SET Bezeichnung = ?, Kommentar = ? WHERE RolleID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, rolle.getBezeichnung());
            ps.setString(2, rolle.getKommentar());
            ps.setInt(3, rolle.getRolleID());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Rolle WHERE RolleID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    public List<Rolle> findAll() throws SQLException {
        String sql = "SELECT * FROM Rolle";
        List<Rolle> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToRolle(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return list;
    }

    public List<Rolle> searchAllAttributes(String searchTerm) throws SQLException {
        List<Rolle> result = new ArrayList<>();
        String query = 
            "SELECT * FROM Rolle " +
            "WHERE CAST(RolleID AS CHAR) LIKE ? " +
            "OR Bezeichnung LIKE ? " +
            "OR Kommentar LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String like = "%" + searchTerm + "%";
            stmt.setString(1, like);
            stmt.setString(2, like);
            stmt.setString(3, like);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToRolle(rs));
                }
            }
        }
        return result;
    }
    
    private Rolle mapRowToRolle(ResultSet rs) throws SQLException {
        Rolle rolle = new Rolle();
        rolle.setRolleID(rs.getInt("RolleID"));
        rolle.setBezeichnung(rs.getString("Bezeichnung"));
        rolle.setKommentar(rs.getString("Kommentar"));
        return rolle;
    }
}
