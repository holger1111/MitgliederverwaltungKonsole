package New.DAOs;

import New.Objekte.Bestellung;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BestellungDAO extends BaseDAO<Bestellung> {

    public BestellungDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(Bestellung bestellung) throws SQLException {
        String sql = "INSERT INTO Bestellung (MitgliederID, Gesamtpreis, Bestelldatum, ZahlungID) " +
                     "VALUES (?, ?, ?, ?)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, bestellung.getMitgliederID());
            ps.setDouble(2, bestellung.getGesamtpreis());
            ps.setTimestamp(3, bestellung.getBestelldatum());
            ps.setInt(4, bestellung.getZahlungID());
            ps.executeUpdate();
            
            // Generierte ID abrufen
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                bestellung.setBestellungID(rs.getInt(1));
            }
        } finally {
            closeResources(rs, ps);
        }
    }

    @Override
    public Bestellung findById(int id) throws SQLException {
        String sql = "SELECT BestellungID, MitgliederID, Gesamtpreis, Bestelldatum, ZahlungID " +
                     "FROM Bestellung WHERE BestellungID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToBestellung(rs);
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }

    @Override
    public void update(Bestellung bestellung) throws SQLException {
        String sql = "UPDATE Bestellung SET MitgliederID = ?, Gesamtpreis = ?, " +
                     "Bestelldatum = ?, ZahlungID = ? WHERE BestellungID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, bestellung.getMitgliederID());
            ps.setDouble(2, bestellung.getGesamtpreis());
            ps.setTimestamp(3, bestellung.getBestelldatum());
            ps.setInt(4, bestellung.getZahlungID());
            ps.setInt(5, bestellung.getBestellungID());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Bestellung WHERE BestellungID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    public List<Bestellung> findByMitgliederId(int mitgliederID) throws SQLException {
        String sql = "SELECT BestellungID, MitgliederID, Gesamtpreis, Bestelldatum, ZahlungID " +
                     "FROM Bestellung WHERE MitgliederID = ?";
        List<Bestellung> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, mitgliederID);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToBestellung(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return list;
    }

    public List<Bestellung> findAll() throws SQLException {
        String sql = "SELECT BestellungID, MitgliederID, Gesamtpreis, Bestelldatum, ZahlungID FROM Bestellung";
        List<Bestellung> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToBestellung(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return list;
    }

    public List<Bestellung> searchAllAttributes(String searchTerm) throws SQLException {
        List<Bestellung> result = new ArrayList<>();
        String sql = "SELECT * FROM Bestellung WHERE " +
                     "CAST(BestellungID AS CHAR) LIKE ? OR " +
                     "CAST(MitgliederID AS CHAR) LIKE ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            String like = "%" + searchTerm.toLowerCase() + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapRowToBestellung(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return result;
    }

    private Bestellung mapRowToBestellung(ResultSet rs) throws SQLException {
        return new Bestellung(
            rs.getInt("BestellungID"),
            rs.getInt("MitgliederID"),
            rs.getDouble("Gesamtpreis"),
            rs.getTimestamp("Bestelldatum"),
            rs.getInt("ZahlungID")
        );
    }
}
