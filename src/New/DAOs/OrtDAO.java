package New.DAOs;

import New.Objekte.Ort;
import New.Validator.StringValidator;
import New.Validator.IntValidator;
import New.Exception.TooLongException;
import New.Exception.TooShortException;
import New.Exception.StringException;
import New.Exception.IntException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrtDAO extends BaseDAO<Ort> {

    // Attribute
    private final IntValidator intValidator = new IntValidator();
    private final StringValidator stringValidator = new StringValidator();

    // Konstruktor
    public OrtDAO(Connection connection) {
        super(connection);
    }

    // Methoden
    
    public List<Ort> searchAllAttributes(String searchTerm) throws SQLException {
        List<Ort> results = new ArrayList<>();
        String sql = "SELECT OrtID, PLZ, Ort FROM Ort WHERE Ort LIKE ? OR PLZ LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String pattern = "%" + searchTerm + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ort o = mapRowToOrt(rs);
                results.add(o);
            }
        }
        return results;
    }

    public int getID(Ort ort) {
        return ort.getOrtID();
    }
    
    private Ort mapRowToOrt(ResultSet rs) throws SQLException {
    	Ort ort = new Ort();
    	ort.setOrtID(rs.getInt("OrtID"));
    	ort.setPLZ(rs.getString("PLZ"));
    	ort.setOrt(rs.getString("Ort"));
        return ort;
    }
    
    /**
     * Findet eine OrtID nach PLZ und Name oder legt den Ort an, falls nicht vorhanden.
     * Gibt immer die passende OrtID zurück (neu oder vorhanden).
     */
    public int findOrCreateOrt(String plz, String ort)
            throws SQLException, StringException, TooShortException, TooLongException {
        
        // Validierung
        stringValidator.validate(plz);
        stringValidator.checkLength(plz, "PLZ", 5, 5);
        stringValidator.validate(ort);
        
        // Suche nach existierendem Ort
        String selectSQL = "SELECT OrtID, PLZ, Ort FROM Ort WHERE PLZ = ? AND Ort = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(selectSQL);
            ps.setString(1, plz);
            ps.setString(2, ort);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("OrtID");
            }
        } finally {
            closeResources(rs, ps);
        }

        // Falls nicht vorhanden: neu anlegen
        String insertSQL = "INSERT INTO Ort (PLZ, Ort) VALUES (?, ?)";
        try {
            ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, plz);
            ps.setString(2, ort);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // ID zurückgeben
            } else {
                throw new SQLException("Fehler: Neue OrtID konnte nicht erfasst werden.");
            }
        } finally {
            closeResources(rs, ps);
        }
    }

    // Override
    @Override
    public Ort findById(int id) throws SQLException, IntException {
        try {
            intValidator.validate(id);
        } catch (Exception e) {
            throw new IntException("Fehler bei der OrtID-Validierung: " + e.getMessage());
        }
        String sql = "SELECT OrtID, PLZ, Ort FROM Ort WHERE OrtID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Ort(
                    rs.getInt("OrtID"),
                    rs.getString("PLZ"),
                    rs.getString("Ort")
                );
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }

    // Override
    @Override
    public void insert(Ort entity)
            throws SQLException, StringException, TooShortException, TooLongException, IntException {
        try {
            intValidator.validate(entity.getOrtID());
        } catch (Exception e) {
            throw new IntException("Fehler bei OrtID: " + e.getMessage());
        }
        try {
            stringValidator.validate(entity.getPLZ());
            stringValidator.checkLength(entity.getPLZ(), "PLZ", 5, 5);
            stringValidator.validate(entity.getOrt());
        } catch (TooShortException | TooLongException | StringException ex) {
            throw ex;
        }

        String sql = "INSERT INTO Ort (OrtID, PLZ, Ort) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, entity.getOrtID());
            ps.setString(2, entity.getPLZ());
            ps.setString(3, entity.getOrt());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    // Override
    @Override
    public void update(Ort entity)
            throws SQLException, StringException, TooShortException, TooLongException, IntException {
        try {
            intValidator.validate(entity.getOrtID());
            stringValidator.validate(entity.getPLZ());
            stringValidator.checkLength(entity.getPLZ(), "PLZ", 5, 5);
            stringValidator.validate(entity.getOrt());
        } catch (TooShortException | TooLongException | StringException | IntException ex) {
            throw ex;
        }

        String sql = "UPDATE Ort SET PLZ = ?, Ort = ? WHERE OrtID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, entity.getPLZ());
            ps.setString(2, entity.getOrt());
            ps.setInt(3, entity.getOrtID());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    // Override
    @Override
    public void delete(int id) throws SQLException, IntException {
        try {
            intValidator.validate(id);
        } catch (Exception e) {
            throw new IntException("Fehler bei OrtID: " + e.getMessage());
        }
        String sql = "DELETE FROM Ort WHERE OrtID = ?";
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
