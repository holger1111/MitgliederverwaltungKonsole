package New.DAOs;

import New.Objekte.Zahlung;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ZahlungDAO extends BaseDAO<Zahlung> {

    public ZahlungDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(Zahlung z) throws SQLException {
        String sql = "INSERT INTO Zahlung (Zahlungsart) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, z.getZahlungsart());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                z.setZahlungID(rs.getInt(1));
            }
        }
    }

    @Override
    public Zahlung findById(int id) throws SQLException {
        String sql = "SELECT ZahlungID, Zahlungsart FROM Zahlung WHERE ZahlungID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToZahlung(rs);
            }
        }
        return null;
    }

    @Override
    public void update(Zahlung z) throws SQLException {
        String sql = "UPDATE Zahlung SET Zahlungsart=? WHERE ZahlungID=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, z.getZahlungsart());
            ps.setInt(2, z.getZahlungID());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Zahlung WHERE ZahlungID=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Zahlung> findAll() throws SQLException {
        String sql = "SELECT ZahlungID, Zahlungsart FROM Zahlung";
        List<Zahlung> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToZahlung(rs));
            }
        }
        return list;
    }
    
    public List<Zahlung> searchAllAttributes(String searchTerm) throws SQLException {
        List<Zahlung> result = new ArrayList<>();
        String sql = "SELECT * FROM Zahlung WHERE " +
                     "CAST(ZahlungID AS CHAR) LIKE ? OR " +
                     "LOWER(Zahlungsart) LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String like = "%" + searchTerm.toLowerCase() + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToZahlung(rs));
                }
            }
        }
        return result;
    }

    
    private Zahlung mapRowToZahlung(ResultSet rs) throws SQLException {
        return new Zahlung(
            rs.getInt("ZahlungID"),
            rs.getString("Zahlungsart")
        );
    }
}
