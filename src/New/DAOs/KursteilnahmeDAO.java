package New.DAOs;

import New.Exception.IntException;
import New.Exception.NotFoundException;
import New.Objekte.Kursteilnahme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KursteilnahmeDAO extends BaseDAO<Kursteilnahme> {

    public KursteilnahmeDAO(Connection connection) {
        super(connection);
    }

    /**
     * Findet eine Kursteilnahme anhand des zusammengesetzten Primärschlüssels
     * (MitgliederID + KursterminID)
     */
    @Override
    public Kursteilnahme findById(int id) throws SQLException, IntException, NotFoundException {
        throw new UnsupportedOperationException(
            "Use findByCompositeKey(mitgliederID, kursterminID) instead - no single ID primary key exists");
    }

    /**
     * Findet eine Kursteilnahme anhand des zusammengesetzten Primärschlüssels
     */
    public Kursteilnahme findByCompositeKey(int mitgliederID, int kursterminID) 
            throws SQLException, NotFoundException {
        String sql = "SELECT MitgliederID, KursterminID, Angemeldet, Anmeldezeit, "
                + "Abgemeldet, Abmeldezeit, Aktiv, Kommentar "
                + "FROM Kursteilnahme WHERE MitgliederID = ? AND KursterminID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, mitgliederID);
            ps.setInt(2, kursterminID);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToKursteilnahme(rs);
            }
            throw new NotFoundException(
                "Kursteilnahme nicht gefunden: MitgliederID=" + mitgliederID 
                + ", KursterminID=" + kursterminID);
        } finally {
            closeResources(rs, ps);
        }
    }

    @Override
    public void insert(Kursteilnahme entity) throws SQLException {
        String sql = "INSERT INTO Kursteilnahme "
                + "(MitgliederID, KursterminID, Angemeldet, Anmeldezeit, Abgemeldet, Abmeldezeit, Aktiv, Kommentar) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, entity.getMitgliederID());
            ps.setInt(2, entity.getKursterminID());
            ps.setBoolean(3, entity.isAngemeldet());
            ps.setTimestamp(4, entity.getAnmeldezeit());
            ps.setBoolean(5, entity.isAbgemeldet());
            ps.setTimestamp(6, entity.getAbmeldezeit());
            ps.setBoolean(7, entity.isAktiv());
            ps.setString(8, entity.getKommentar());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void update(Kursteilnahme entity) throws SQLException {
        String sql = "UPDATE Kursteilnahme SET Angemeldet = ?, Anmeldezeit = ?, Abgemeldet = ?, "
                + "Abmeldezeit = ?, Aktiv = ?, Kommentar = ? "
                + "WHERE MitgliederID = ? AND KursterminID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setBoolean(1, entity.isAngemeldet());
            ps.setTimestamp(2, entity.getAnmeldezeit());
            ps.setBoolean(3, entity.isAbgemeldet());
            ps.setTimestamp(4, entity.getAbmeldezeit());
            ps.setBoolean(5, entity.isAktiv());
            ps.setString(6, entity.getKommentar());
            ps.setInt(7, entity.getMitgliederID());
            ps.setInt(8, entity.getKursterminID());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        throw new UnsupportedOperationException(
            "Use deleteByCompositeKey(mitgliederID, kursterminID) instead - no single ID primary key exists");
    }

    /**
     * Löscht eine Kursteilnahme anhand des zusammengesetzten Primärschlüssels
     */
    public void deleteByCompositeKey(int mitgliederID, int kursterminID) throws SQLException {
        String sql = "DELETE FROM Kursteilnahme WHERE MitgliederID = ? AND KursterminID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, mitgliederID);
            ps.setInt(2, kursterminID);
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    public List<Kursteilnahme> findAll() throws SQLException {
        List<Kursteilnahme> teilnahmen = new ArrayList<>();
        String sql = "SELECT MitgliederID, KursterminID, Angemeldet, Anmeldezeit, "
                + "Abgemeldet, Abmeldezeit, Aktiv, Kommentar FROM Kursteilnahme";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                teilnahmen.add(mapRowToKursteilnahme(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return teilnahmen;
    }

    public List<Kursteilnahme> findByMitgliederId(int mitgliederID) throws SQLException {
        List<Kursteilnahme> teilnahmen = new ArrayList<>();
        String sql = "SELECT MitgliederID, KursterminID, Angemeldet, Anmeldezeit, "
                + "Abgemeldet, Abmeldezeit, Aktiv, Kommentar "
                + "FROM Kursteilnahme WHERE MitgliederID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, mitgliederID);
            rs = ps.executeQuery();
            while (rs.next()) {
                teilnahmen.add(mapRowToKursteilnahme(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return teilnahmen;
    }

    public List<Kursteilnahme> findByKursterminId(int kursterminID) throws SQLException {
        List<Kursteilnahme> teilnahmen = new ArrayList<>();
        String sql = "SELECT MitgliederID, KursterminID, Angemeldet, Anmeldezeit, "
                + "Abgemeldet, Abmeldezeit, Aktiv, Kommentar "
                + "FROM Kursteilnahme WHERE KursterminID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, kursterminID);
            rs = ps.executeQuery();
            while (rs.next()) {
                teilnahmen.add(mapRowToKursteilnahme(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return teilnahmen;
    }

    public List<Kursteilnahme> searchAllAttributes(String searchTerm) throws SQLException {
        List<Kursteilnahme> results = new ArrayList<>();

        String sql = "SELECT MitgliederID, KursterminID, Angemeldet, Anmeldezeit, "
                + "Abgemeldet, Abmeldezeit, Aktiv, Kommentar "
                + "FROM Kursteilnahme "
                + "WHERE CAST(MitgliederID AS CHAR) LIKE ? "
                + "OR CAST(KursterminID AS CHAR) LIKE ? "
                + "OR CAST(Anmeldezeit AS CHAR) LIKE ? "
                + "OR CAST(Abmeldezeit AS CHAR) LIKE ? "
                + "OR Kommentar LIKE ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            pstmt.setString(5, searchPattern);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                results.add(mapRowToKursteilnahme(rs));
            }
        }

        return results;
    }

    private Kursteilnahme mapRowToKursteilnahme(ResultSet rs) throws SQLException {
        Kursteilnahme teilnahme = new Kursteilnahme();
        teilnahme.setMitgliederID(rs.getInt("MitgliederID"));
        teilnahme.setKursterminID(rs.getInt("KursterminID"));
        teilnahme.setAngemeldet(rs.getBoolean("Angemeldet"));
        teilnahme.setAnmeldezeit(rs.getTimestamp("Anmeldezeit"));
        teilnahme.setAbgemeldet(rs.getBoolean("Abgemeldet"));
        teilnahme.setAbmeldezeit(rs.getTimestamp("Abmeldezeit"));
        teilnahme.setAktiv(rs.getBoolean("Aktiv"));
        teilnahme.setKommentar(rs.getString("Kommentar"));
        return teilnahme;
    }
}
