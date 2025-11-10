package New.DAOs;

import New.Exception.IntException;
import New.Exception.NotFoundException;
import New.Objekte.Kurstermin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KursterminDAO extends BaseDAO<Kurstermin> {

    public KursterminDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Kurstermin findById(int id) throws SQLException, IntException, NotFoundException {
        String sql = "SELECT KursterminID, KursID, TrainerID, Termin, Aktiv, Kommentar "
                + "FROM Kurstermin WHERE KursterminID = ?";
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
        String sql = "INSERT INTO Kurstermin (KursID, TrainerID, Termin, Aktiv, Kommentar) "
                + "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, entity.getKursID());
            ps.setInt(2, entity.getTrainerID());
            ps.setTimestamp(3, entity.getTermin());
            ps.setBoolean(4, entity.isAktiv());
            ps.setString(5, entity.getKommentar());
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
        String sql = "UPDATE Kurstermin SET KursID = ?, TrainerID = ?, Termin = ?, "
                + "Aktiv = ?, Kommentar = ? WHERE KursterminID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, entity.getKursID());
            ps.setInt(2, entity.getTrainerID());
            ps.setTimestamp(3, entity.getTermin());
            ps.setBoolean(4, entity.isAktiv());
            ps.setString(5, entity.getKommentar());
            ps.setInt(6, entity.getKursterminID());
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
        String sql = "SELECT KursterminID, KursID, TrainerID, Termin, Aktiv, Kommentar FROM Kurstermin";
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
        String sql = "SELECT KursterminID, KursID, TrainerID, Termin, Aktiv, Kommentar "
                + "FROM Kurstermin WHERE KursID = ?";
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

    public List<Kurstermin> findByTrainerId(int trainerID) throws SQLException {
        List<Kurstermin> termine = new ArrayList<>();
        String sql = "SELECT KursterminID, KursID, TrainerID, Termin, Aktiv, Kommentar "
                + "FROM Kurstermin WHERE TrainerID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, trainerID);
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

        String sql = "SELECT KursterminID, KursID, TrainerID, Termin, Aktiv, Kommentar "
                + "FROM Kurstermin "
                + "WHERE CAST(KursterminID AS CHAR) LIKE ? "
                + "OR CAST(KursID AS CHAR) LIKE ? "
                + "OR CAST(TrainerID AS CHAR) LIKE ? "
                + "OR CAST(Termin AS CHAR) LIKE ? "
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
                results.add(mapRowToKurstermin(rs));
            }
        }

        return results;
    }

    private Kurstermin mapRowToKurstermin(ResultSet rs) throws SQLException {
        Kurstermin termin = new Kurstermin();
        termin.setKursterminID(rs.getInt("KursterminID"));
        termin.setKursID(rs.getInt("KursID"));
        termin.setTrainerID(rs.getInt("TrainerID"));
        termin.setTermin(rs.getTimestamp("Termin"));
        termin.setAktiv(rs.getBoolean("Aktiv"));
        termin.setKommentar(rs.getString("Kommentar"));
        return termin;
    }
}
