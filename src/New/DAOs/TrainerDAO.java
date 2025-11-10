package New.DAOs;

import New.Exception.IntException;
import New.Exception.NotFoundException;
import New.Objekte.Trainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrainerDAO extends BaseDAO<Trainer> {

    public TrainerDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Trainer findById(int id) throws SQLException, IntException, NotFoundException {
        String sql = "SELECT TrainerID, Vorname, Nachname, Kommentar FROM Trainer WHERE TrainerID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToTrainer(rs);
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }

    @Override
    public void insert(Trainer entity) throws SQLException {
        String sql = "INSERT INTO Trainer (Vorname, Nachname, Kommentar) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getVorname());
            ps.setString(2, entity.getNachname());
            ps.setString(3, entity.getKommentar());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setTrainerID(rs.getInt(1));
            }
        } finally {
            closeResources(rs, ps);
        }
    }

    @Override
    public void update(Trainer entity) throws SQLException {
        String sql = "UPDATE Trainer SET Vorname = ?, Nachname = ?, Kommentar = ? WHERE TrainerID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, entity.getVorname());
            ps.setString(2, entity.getNachname());
            ps.setString(3, entity.getKommentar());
            ps.setInt(4, entity.getTrainerID());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Trainer WHERE TrainerID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    public List<Trainer> findAll() throws SQLException {
        List<Trainer> trainer = new ArrayList<>();
        String sql = "SELECT TrainerID, Vorname, Nachname, Kommentar FROM Trainer";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                trainer.add(mapRowToTrainer(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return trainer;
    }

    public List<Trainer> searchAllAttributes(String searchTerm) throws SQLException {
        List<Trainer> results = new ArrayList<>();
        String sql = "SELECT TrainerID, Vorname, Nachname, Kommentar FROM Trainer " +
                     "WHERE Vorname LIKE ? OR Nachname LIKE ? OR Kommentar LIKE ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            String pattern = "%" + searchTerm + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapRowToTrainer(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return results;
    }

    private Trainer mapRowToTrainer(ResultSet rs) throws SQLException {
        Trainer trainer = new Trainer();
        trainer.setTrainerID(rs.getInt("TrainerID"));
        trainer.setVorname(rs.getString("Vorname"));
        trainer.setNachname(rs.getString("Nachname"));
        trainer.setKommentar(rs.getString("Kommentar"));
        return trainer;
    }
}
