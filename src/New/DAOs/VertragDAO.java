package New.DAOs;

import New.Objekte.Vertrag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VertragDAO extends BaseDAO<Vertrag> {

    public VertragDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(Vertrag v) throws SQLException {
        String sql = "INSERT INTO Vertrag (Bezeichnung, Laufzeit, Grundpreis) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, v.getBezeichnung());
            ps.setInt(2, v.getLaufzeit());
            ps.setDouble(3, v.getGrundpreis());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                v.setVertragID(rs.getInt(1));
            }
        }
    }

    @Override
    public Vertrag findById(int id) throws SQLException {
        String sql = "SELECT VertragID, Bezeichnung, Laufzeit, Grundpreis FROM Vertrag WHERE VertragID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToVertrag(rs);
            }
        }
        return null;
    }

    @Override
    public void update(Vertrag v) throws SQLException {
        String sql = "UPDATE Vertrag SET Bezeichnung=?, Laufzeit=?, Grundpreis=? WHERE VertragID=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, v.getBezeichnung());
            ps.setInt(2, v.getLaufzeit());
            ps.setDouble(3, v.getGrundpreis());
            ps.setInt(4, v.getVertragID());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Vertrag WHERE VertragID=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Vertrag> findAll() throws SQLException {
        String sql = "SELECT VertragID, Bezeichnung, Laufzeit, Grundpreis FROM Vertrag";
        List<Vertrag> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToVertrag(rs));
            }
        }
        return list;
    }
    
    public List<Vertrag> searchAllAttributes(String searchTerm) throws SQLException {
        List<Vertrag> result = new ArrayList<>();
        String sql = "SELECT * FROM Vertrag WHERE " +
                     "CAST(VertragID AS CHAR) LIKE ? OR " +
                     "LOWER(Bezeichnung) LIKE ? OR " +
                     "CAST(Laufzeit AS CHAR) LIKE ? OR " +
                     "CAST(Grundpreis AS CHAR) LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String like = "%" + searchTerm.toLowerCase() + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToVertrag(rs));
                }
            }
        }
        return result;
    }

    private Vertrag mapRowToVertrag(ResultSet rs) throws SQLException {
        return new Vertrag(
                rs.getInt("VertragID"),
                rs.getString("Bezeichnung"),
                rs.getInt("Laufzeit"),
                rs.getDouble("Grundpreis")
        );
    }
}
