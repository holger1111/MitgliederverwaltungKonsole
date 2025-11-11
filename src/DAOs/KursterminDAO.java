package DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Exception.IntException;
import Exception.NotFoundException;
import Objekte.Kurstermin;

public class KursterminDAO extends BaseDAO<Kurstermin> {

    public KursterminDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Kurstermin findById(int id) throws SQLException, IntException, NotFoundException {
        String sql = "SELECT KursterminID, KursID, Termin, Teilnehmerfrei, Anmeldebar, Aktiv, Kommentar FROM Kurstermin WHERE KursterminID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToKurstermin(rs);
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }

    @Override
    public void insert(Kurstermin entity) throws SQLException {
        String sql = "INSERT INTO Kurstermin (KursID, Termin, Teilnehmerfrei, Anmeldebar, Aktiv, Kommentar) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, entity.getKursID());
            ps.setTimestamp(2, entity.getTermin());
            ps.setInt(3, entity.getTeilnehmerfrei());
            ps.setBoolean(4, entity.isAnmeldebar());
            ps.setBoolean(5, entity.isAktiv());
            ps.setString(6, entity.getKommentar());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setKursterminID(rs.getInt(1));
            }
        } finally {
            closeResources(rs, ps);
        }
    }

    @Override
    public void update(Kurstermin entity) throws SQLException {
        String sql = "UPDATE Kurstermin SET KursID = ?, Termin = ?, Teilnehmerfrei = ?, Anmeldebar = ?, Aktiv = ?, Kommentar = ? WHERE KursterminID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, entity.getKursID());
            ps.setTimestamp(2, entity.getTermin());
            ps.setInt(3, entity.getTeilnehmerfrei());
            ps.setBoolean(4, entity.isAnmeldebar());
            ps.setBoolean(5, entity.isAktiv());
            ps.setString(6, entity.getKommentar());
            ps.setInt(7, entity.getKursterminID());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Kurstermin WHERE KursterminID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    public List<Kurstermin> findAll() throws SQLException {
        List<Kurstermin> termine = new ArrayList<>();
        String sql = "SELECT KursterminID, KursID, Termin, Teilnehmerfrei, Anmeldebar, Aktiv, Kommentar FROM Kurstermin";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                termine.add(mapRowToKurstermin(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return termine;
    }

    public List<Kurstermin> findByKursId(int kursID) throws SQLException {
        List<Kurstermin> termine = new ArrayList<>();
        String sql = "SELECT KursterminID, KursID, Termin, Teilnehmerfrei, Anmeldebar, Aktiv, Kommentar FROM Kurstermin WHERE KursID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, kursID);
            rs = ps.executeQuery();
            while (rs.next()) {
                termine.add(mapRowToKurstermin(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return termine;
    }

    public List<Kurstermin> searchAllAttributes(String searchTerm) throws SQLException {
        List<Kurstermin> results = new ArrayList<>();
        String sql = "SELECT KursterminID, KursID, Termin, Teilnehmerfrei, Anmeldebar, Aktiv, Kommentar FROM Kurstermin WHERE " + 
                     "CAST(KursterminID AS CHAR) LIKE ? OR CAST(KursID AS CHAR) LIKE ? OR " + 
                     "CAST(Termin AS CHAR) LIKE ? OR CAST(Teilnehmerfrei AS CHAR) LIKE ? OR " + 
                     "CAST(Anmeldebar AS CHAR) LIKE ? OR CAST(Aktiv AS CHAR) LIKE ? OR LOWER(Kommentar) LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String like = "%" + searchTerm.toLowerCase() + "%";
            pstmt.setString(1, like);
            pstmt.setString(2, like);
            pstmt.setString(3, like);
            pstmt.setString(4, like);
            pstmt.setString(5, like);
            pstmt.setString(6, like);
            pstmt.setString(7, like);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(mapRowToKurstermin(rs));
            }
        }
        return results;
    }

    private Kurstermin mapRowToKurstermin(ResultSet rs) throws SQLException {
        Kurstermin termin = new Kurstermin();
        termin.setKursterminID(rs.getInt("KursterminID"));
        termin.setKursID(rs.getInt("KursID"));
        termin.setTermin(rs.getTimestamp("Termin"));
        termin.setTeilnehmerfrei(rs.getInt("Teilnehmerfrei"));
        termin.setAnmeldebar(rs.getBoolean("Anmeldebar"));
        termin.setAktiv(rs.getBoolean("Aktiv"));
        termin.setKommentar(rs.getString("Kommentar"));
        return termin;
    }
}
