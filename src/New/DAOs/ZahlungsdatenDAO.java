package New.DAOs;

import New.Objekte.Zahlungsdaten;
import New.Validator.StringValidator;
import New.Exception.PaymentDetailsException;
import New.Exception.StringException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ZahlungsdatenDAO extends BaseDAO<Zahlungsdaten> {

    // Attribute
    private final StringValidator stringValidator = new StringValidator();

    // Konstruktor
    public ZahlungsdatenDAO(Connection connection) {
        super(connection);
    }
    
    // Methoden
    
    public List<Zahlungsdaten> searchAllAttributes(String searchTerm) throws SQLException {
        List<Zahlungsdaten> results = new ArrayList<>();
        String sql = "SELECT * FROM zahlungsdaten WHERE " +
                     "Name LIKE ? OR IBAN LIKE ? OR BIC LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String pattern = "%" + searchTerm + "%";
            for (int i = 1; i <= 3; i++) {
                stmt.setString(i, pattern);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Zahlungsdaten z = mapRowToZahlungsdaten(rs);
                results.add(z);
            }
        }
        return results;
    }

    public int getID(Zahlungsdaten zahlungsdaten) {
        return zahlungsdaten.getZahlungsdatenID();
    }
    
    private Zahlungsdaten mapRowToZahlungsdaten(ResultSet rs) throws SQLException {
    	Zahlungsdaten zahlungsdaten = new Zahlungsdaten();
    	zahlungsdaten.setZahlungsdatenID(rs.getInt("zahlungsdatenid"));
    	zahlungsdaten.setName(rs.getString("name"));
    	zahlungsdaten.setIBAN(rs.getString("IBAN"));
    	zahlungsdaten.setBIC(rs.getString("BIC"));
        return zahlungsdaten;
    }
    
    /**
     * Findet oder erstellt Zahlungsdaten nach Name, IBAN & BIC, liefert immer die ID aus der DB.
     */
    public int findOrCreateZahlungsdaten(String name, String iban, String bic)
            throws SQLException, StringException, PaymentDetailsException {
        stringValidator.validate(name);
        stringValidator.validate(iban);
        stringValidator.validate(bic);

        String selectSQL = "SELECT ZahlungsdatenID FROM Zahlungsdaten WHERE IBAN = ? AND BIC = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(selectSQL);
            ps.setString(1, iban);
            ps.setString(2, bic);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("ZahlungsdatenID");
            }
        } finally {
            closeResources(rs, ps);
        }

        String insertSQL = "INSERT INTO Zahlungsdaten (Name, IBAN, BIC) VALUES (?, ?, ?)";
        try {
            ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, iban);
            ps.setString(3, bic);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("Fehler: Neue ZahlungsdatenID konnte nicht erfasst werden.");
            }
        } finally {
            closeResources(rs, ps);
        }
    }

    // Override
    @Override
    public Zahlungsdaten findById(int id) throws SQLException {
        String sql = "SELECT ZahlungsdatenID, Name, IBAN, BIC FROM Zahlungsdaten WHERE ZahlungsdatenID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Zahlungsdaten(
                    rs.getInt("ZahlungsdatenID"),
                    rs.getString("Name"),
                    rs.getString("IBAN"),
                    rs.getString("BIC")
                );
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }

    // Override
    @Override
    public void insert(Zahlungsdaten entity) throws SQLException, StringException, PaymentDetailsException {
        stringValidator.validate(entity.getName());
        stringValidator.validate(entity.getIBAN());
        stringValidator.validate(entity.getBIC());

        String sql = "INSERT INTO Zahlungsdaten (Name, IBAN, BIC) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getIBAN());
            ps.setString(3, entity.getBIC());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                entity.setZahlungsdatenID(keys.getInt(1));
            }
        } finally {
            closeResources(null, ps);
        }
    }

    // Override
    @Override
    public void update(Zahlungsdaten entity) throws SQLException, StringException, PaymentDetailsException {
        stringValidator.validate(entity.getName());
        stringValidator.validate(entity.getIBAN());
        stringValidator.validate(entity.getBIC());

        String sql = "UPDATE Zahlungsdaten SET Name = ?, IBAN = ?, BIC = ? WHERE ZahlungsdatenID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getIBAN());
            ps.setString(3, entity.getBIC());
            ps.setInt(4, entity.getZahlungsdatenID());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    // Override
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Zahlungsdaten WHERE ZahlungsdatenID = ?";
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
