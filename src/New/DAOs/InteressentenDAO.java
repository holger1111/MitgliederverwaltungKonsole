package New.DAOs;

import New.Objekte.Interessenten;
import New.Objekte.Mitglieder;
import New.Validator.StringValidator;
import New.Exception.StringException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InteressentenDAO extends BaseDAO<Interessenten> {

    // Attribute
    private final StringValidator stringValidator = new StringValidator();

    // Konstruktor
    public InteressentenDAO(Connection connection) {
        super(connection);
    }
    
    public List<Interessenten> searchAllAttributes(String searchTerm) throws SQLException {
        List<Interessenten> results = new ArrayList<>();
        String sql = "SELECT MitgliederID, Vorname, Nachname, Telefon FROM Mitglieder WHERE " +
                     "Vorname LIKE ? OR Nachname LIKE ? OR Telefon LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String pattern = "%" + searchTerm + "%";
            for (int i = 1; i <= 3; i++) {
                stmt.setString(i, pattern);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Interessenten i = mapRowToInteressenten(rs);
                results.add(i);
            }
        }
        return results;
    }

    public int getID(Interessenten interessent) {
        return interessent.getMitgliederID();
    }
    
    private Mitglieder mapRowToInteressenten(ResultSet rs) throws SQLException {
        Mitglieder mitglied = new Mitglieder();
        mitglied.setMitgliederID(rs.getInt("MitgliederID"));
        mitglied.setVorname(rs.getString("Vorname"));
        mitglied.setNachname(rs.getString("Nachname"));
        mitglied.setTelefon(rs.getString("Telefon"));
        return mitglied;
    }
    
    /**
     * Findet oder legt einen Interessenten an (Vorname, Nachname, Telefon).
     * Gibt immer eine MitgliederID zurück (vorhanden oder neu).
     */
    public int findOrCreateInteressent(String vorname, String nachname, String telefon)
            throws SQLException, StringException {
        stringValidator.validate(vorname);
        stringValidator.validate(nachname);
        stringValidator.validate(telefon);

        String selectSQL = "SELECT MitgliederID FROM Mitglieder WHERE Vorname = ? AND Nachname = ? AND Telefon = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(selectSQL);
            ps.setString(1, vorname);
            ps.setString(2, nachname);
            ps.setString(3, telefon);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("MitgliederID");
            }
        } finally {
            closeResources(rs, ps);
        }

        String insertSQL = "INSERT INTO Mitglieder (Vorname, Nachname, Telefon) VALUES (?, ?, ?)";
        try {
            ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, vorname);
            ps.setString(2, nachname);
            ps.setString(3, telefon);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("Fehler: Neue MitgliederID konnte nicht erfasst werden.");
            }
        } finally {
            closeResources(rs, ps);
        }
    }

    // Override
    @Override
    public Interessenten findById(int id) throws SQLException {
        String sql = "SELECT MitgliederID, Vorname, Nachname, Telefon FROM Mitglieder WHERE MitgliederID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Interessenten(
                    rs.getInt("MitgliederID"),
                    rs.getString("Vorname"),
                    rs.getString("Nachname"),
                    rs.getString("Telefon")
                );
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }

    // Override
    @Override
    public void insert(Interessenten entity) throws SQLException {
        String sql = "INSERT INTO Mitglieder (Vorname, Nachname, Telefon) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getVorname());
            ps.setString(2, entity.getNachname());
            ps.setString(3, entity.getTelefon());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                entity.setMitgliederID(keys.getInt(1));
            }
        } finally {
            closeResources(null, ps);
        }
    }

    // Override
    @Override
    public void update(Interessenten entity) throws SQLException {
        String sql = "UPDATE Mitglieder SET Vorname = ?, Nachname = ?, Telefon = ? WHERE MitgliederID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, entity.getVorname());
            ps.setString(2, entity.getNachname());
            ps.setString(3, entity.getTelefon());
            ps.setInt(4, entity.getMitgliederID());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    // Override
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Mitglieder WHERE MitgliederID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }
}
