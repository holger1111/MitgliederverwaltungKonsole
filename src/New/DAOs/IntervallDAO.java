package New.DAOs;

import New.Objekte.Intervall;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IntervallDAO extends BaseDAO<Intervall> {

    public IntervallDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(Intervall i) throws SQLException {
        String sql = "INSERT INTO Intervall (Zahlungsintervall, Bezeichnung) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, i.getZahlungsintervall());
            ps.setString(2, i.getBezeichnung());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                i.setIntervallID(rs.getInt(1));
            }
        }
    }

    @Override
    public Intervall findById(int id) throws SQLException {
        String sql = "SELECT IntervallID, Zahlungsintervall, Bezeichnung FROM Intervall WHERE IntervallID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToIntervall(rs);
            }
        }
        return null;
    }

    @Override
    public void update(Intervall i) throws SQLException {
        String sql = "UPDATE Intervall SET Zahlungsintervall=?, Bezeichnung=? WHERE IntervallID=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, i.getZahlungsintervall());
            ps.setString(2, i.getBezeichnung());
            ps.setInt(3, i.getIntervallID());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Intervall WHERE IntervallID=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Intervall> findAll() throws SQLException {
        String sql = "SELECT IntervallID, Zahlungsintervall, Bezeichnung FROM Intervall";
        List<Intervall> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToIntervall(rs));
            }
        }
        return list;
    }
    
    public List<Intervall> searchAllAttributes(String searchTerm) throws SQLException {
        List<Intervall> result = new ArrayList<>();
        String sql = "SELECT * FROM Intervall WHERE " +
                     "CAST(IntervallID AS CHAR) LIKE ? OR " +
                     "LOWER(Bezeichnung) LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String like = "%" + searchTerm.toLowerCase() + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToIntervall(rs));
                }
            }
        }
        return result;
    }

    
    private Intervall mapRowToIntervall(ResultSet rs) throws SQLException {
        return new Intervall(
            rs.getInt("IntervallID"),
            rs.getString("Zahlungsintervall"),
            rs.getString("Bezeichnung")
        );
    }
}
