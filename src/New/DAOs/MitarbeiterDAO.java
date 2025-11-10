package New.DAOs;

import New.Objekte.Mitarbeiter;
import New.Objekte.Ort;
import New.Objekte.Zahlungsdaten;
import New.Objekte.Benutzer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MitarbeiterDAO extends BaseDAO<Mitarbeiter> {

    private final OrtDAO ortDAO;
    private final ZahlungsdatenDAO zahlungsdatenDAO;
    private final BenutzerDAO benutzerDAO;

    public MitarbeiterDAO(Connection connection) {
        super(connection);
        this.ortDAO = new OrtDAO(connection);
        this.zahlungsdatenDAO = new ZahlungsdatenDAO(connection);
        this.benutzerDAO = new BenutzerDAO(connection);
    }

    @Override
    public void insert(Mitarbeiter mitarbeiter) throws SQLException {
        String sql = "INSERT INTO Mitarbeiter (Vorname, Nachname, Geburtsdatum, Straße, Hausnr, OrtID, ZahlungsdatenID, Mail, BenutzerID) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, mitarbeiter.getVorname());
            ps.setString(2, mitarbeiter.getNachname());
            ps.setString(3, mitarbeiter.getGeburtsdatum());
            ps.setString(4, mitarbeiter.getStraße());
            ps.setString(5, mitarbeiter.getHausnr());
            ps.setInt(6, mitarbeiter.getOrt() != null ? mitarbeiter.getOrt().getOrtID() : 0);
            ps.setInt(7, mitarbeiter.getZahlungsdaten() != null ? mitarbeiter.getZahlungsdaten().getZahlungsdatenID() : 0);
            ps.setString(8, mitarbeiter.getMail());
            ps.setInt(9, mitarbeiter.getBenutzer() != null ? mitarbeiter.getBenutzer().getBenutzerID() : 0);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                mitarbeiter.setMitarbeiterID(rs.getInt(1));
            }
        } finally {
            closeResources(rs, ps);
        }
    }

    @Override
    public Mitarbeiter findById(int id) throws SQLException {
        String sql = "SELECT * FROM Mitarbeiter WHERE MitarbeiterID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToMitarbeiter(rs);
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }

    @Override
    public void update(Mitarbeiter mitarbeiter) throws SQLException {
        String sql = "UPDATE Mitarbeiter SET Vorname=?, Nachname=?, Geburtsdatum=?, Straße=?, Hausnr=?, OrtID=?, ZahlungsdatenID=?, Mail=?, BenutzerID=? WHERE MitarbeiterID=?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, mitarbeiter.getVorname());
            ps.setString(2, mitarbeiter.getNachname());
            ps.setString(3, mitarbeiter.getGeburtsdatum());
            ps.setString(4, mitarbeiter.getStraße());
            ps.setString(5, mitarbeiter.getHausnr());
            ps.setInt(6, mitarbeiter.getOrt() != null ? mitarbeiter.getOrt().getOrtID() : 0);
            ps.setInt(7, mitarbeiter.getZahlungsdaten() != null ? mitarbeiter.getZahlungsdaten().getZahlungsdatenID() : 0);
            ps.setString(8, mitarbeiter.getMail());
            ps.setInt(9, mitarbeiter.getBenutzer() != null ? mitarbeiter.getBenutzer().getBenutzerID() : 0);
            ps.setInt(10, mitarbeiter.getMitarbeiterID());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Mitarbeiter WHERE MitarbeiterID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    public List<Mitarbeiter> findAll() throws SQLException {
        String sql = "SELECT * FROM Mitarbeiter";
        List<Mitarbeiter> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToMitarbeiter(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return list;
    }

    public List<Mitarbeiter> searchAllAttributes(String searchTerm) throws SQLException {
        List<Mitarbeiter> result = new ArrayList<>();
        String sql = "SELECT * FROM Mitarbeiter WHERE "
                + "LOWER(Vorname) LIKE ? OR LOWER(Nachname) LIKE ? "
                + "OR CAST(MitarbeiterID AS CHAR) LIKE ? "
                + "OR LOWER(Mail) LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String like = "%" + searchTerm.toLowerCase() + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapRowToMitarbeiter(rs));
            }
        }
        return result;
    }

    private Mitarbeiter mapRowToMitarbeiter(ResultSet rs) throws SQLException {
        Mitarbeiter mitarbeiter = new Mitarbeiter();
        mitarbeiter.setMitarbeiterID(rs.getInt("MitarbeiterID"));
        mitarbeiter.setVorname(rs.getString("Vorname"));
        mitarbeiter.setNachname(rs.getString("Nachname"));
        mitarbeiter.setGeburtsdatum(rs.getString("Geburtsdatum"));
        mitarbeiter.setStraße(rs.getString("Straße"));
        mitarbeiter.setHausnr(rs.getString("Hausnr"));

        // Ort über OrtID holen
        int ortID = rs.getInt("OrtID");
        Ort ort = null;
        try {
            ort = ortID > 0 ? ortDAO.findById(ortID) : null;
        } catch (Exception ex) {
            ort = null;
        }
        mitarbeiter.setOrt(ort);

        // Zahlungsdaten und Benutzer
        int zahlungsdatenID = rs.getInt("ZahlungsdatenID");
        int benutzerID = rs.getInt("BenutzerID");
        Zahlungsdaten zahlungsdaten = null;
        Benutzer benutzer = null;
        try {
            zahlungsdaten = zahlungsdatenID > 0 ? zahlungsdatenDAO.findById(zahlungsdatenID) : null;
        } catch (Exception ex) {
            zahlungsdaten = null;
        }
        try {
            benutzer = benutzerID > 0 ? benutzerDAO.findById(benutzerID) : null;
        } catch (Exception ex) {
            benutzer = null;
        }
        mitarbeiter.setZahlungsdaten(zahlungsdaten);
        mitarbeiter.setBenutzer(benutzer);

        mitarbeiter.setMail(rs.getString("Mail"));
        return mitarbeiter;
    }
}
