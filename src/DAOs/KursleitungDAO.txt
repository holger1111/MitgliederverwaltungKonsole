package DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Objekte.Kursleitung;

public class KursleitungDAO extends BaseDAO<Kursleitung> {

    public KursleitungDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(Kursleitung kursleitung) throws SQLException {
        String sql = "INSERT INTO Kursleitung (KursterminID, MitarbeiterID, Bestätigt, Bestätigungszeit, Abgemeldet, Abmeldezeit, Aktiv, Kommentar) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, kursleitung.getKursterminID());
            ps.setInt(2, kursleitung.getMitarbeiterID());
            ps.setBoolean(3, kursleitung.isBestätigt());
            ps.setTimestamp(4, kursleitung.getBestätigungszeit());
            ps.setBoolean(5, kursleitung.isAbgemeldet());
            ps.setTimestamp(6, kursleitung.getAbmeldezeit());
            ps.setBoolean(7, kursleitung.isAktiv());
            ps.setString(8, kursleitung.getKommentar());
            ps.executeUpdate();
        }
    }

    @Override
    public Kursleitung findById(int kursterminID) throws SQLException {
        String sql = "SELECT * FROM Kursleitung WHERE KursterminID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, kursterminID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToKursleitung(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void update(Kursleitung kursleitung) throws SQLException {
        String sql = "UPDATE Kursleitung SET MitarbeiterID = ?, Bestätigt = ?, Bestätigungszeit = ?, Abgemeldet = ?, Abmeldezeit = ?, Aktiv = ?, Kommentar = ? WHERE KursterminID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, kursleitung.getMitarbeiterID());
            ps.setBoolean(2, kursleitung.isBestätigt());
            ps.setTimestamp(3, kursleitung.getBestätigungszeit());
            ps.setBoolean(4, kursleitung.isAbgemeldet());
            ps.setTimestamp(5, kursleitung.getAbmeldezeit());
            ps.setBoolean(6, kursleitung.isAktiv());
            ps.setString(7, kursleitung.getKommentar());
            ps.setInt(8, kursleitung.getKursterminID());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int kursterminID) throws SQLException {
        String sql = "DELETE FROM Kursleitung WHERE KursterminID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, kursterminID);
            ps.executeUpdate();
        }
    }

    public List<Kursleitung> findAll() throws SQLException {
        List<Kursleitung> leitungen = new ArrayList<>();
        String sql = "SELECT * FROM Kursleitung";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                leitungen.add(mapRowToKursleitung(rs));
            }
        }
        return leitungen;
    }

    public List<Kursleitung> findByKursterminId(int kursterminID) throws SQLException {
        List<Kursleitung> leitungen = new ArrayList<>();
        String sql = "SELECT * FROM Kursleitung WHERE KursterminID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, kursterminID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    leitungen.add(mapRowToKursleitung(rs));
                }
            }
        }
        return leitungen;
    }

    public List<Kursleitung> findAktiveLeitungByKursterminId(int kursterminID) throws SQLException {
        List<Kursleitung> leitungen = new ArrayList<>();
        String sql = "SELECT * FROM Kursleitung WHERE KursterminID = ? AND Aktiv = TRUE";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, kursterminID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    leitungen.add(mapRowToKursleitung(rs));
                }
            }
        }
        return leitungen;
    }

    public List<Kursleitung> searchAllAttributes(String searchTerm) throws SQLException {
        List<Kursleitung> results = new ArrayList<>();
        String sql = "SELECT * FROM Kursleitung WHERE " +
                "CAST(KursterminID AS CHAR) LIKE ? OR " +
                "CAST(MitarbeiterID AS CHAR) LIKE ? OR " +
                "LOWER(Kommentar) LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String pattern = "%" + searchTerm.toLowerCase() + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRowToKursleitung(rs));
                }
            }
        }
        return results;
    }

    private Kursleitung mapRowToKursleitung(ResultSet rs) throws SQLException {
        Kursleitung kursleitung = new Kursleitung();
        kursleitung.setKursterminID(rs.getInt("KursterminID"));
        kursleitung.setMitarbeiterID(rs.getInt("MitarbeiterID"));
        kursleitung.setBestätigt(rs.getBoolean("Bestätigt"));
        kursleitung.setBestätigungszeit(rs.getTimestamp("Bestätigungszeit"));
        kursleitung.setAbgemeldet(rs.getBoolean("Abgemeldet"));
        kursleitung.setAbmeldezeit(rs.getTimestamp("Abmeldezeit"));
        kursleitung.setAktiv(rs.getBoolean("Aktiv"));
        kursleitung.setKommentar(rs.getString("Kommentar"));
        return kursleitung;
    }
}
