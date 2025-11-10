package New.DAOs;

import New.Objekte.MitgliederVertrag;
import New.Objekte.Vertrag;
import New.Objekte.Zahlung;
import New.Objekte.Mitglieder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MitgliederVertragDAO extends BaseDAO<MitgliederVertrag> {

    public MitgliederVertragDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(MitgliederVertrag entity) throws Exception {
        throw new UnsupportedOperationException(
            "Nutze insert(MitgliederVertrag, Vertrag, Zahlung, Mitglieder, int laufzeitWochen)!"
        );
    }

    @Override
    public void update(MitgliederVertrag entity) throws Exception {
        throw new UnsupportedOperationException(
            "Nutze update(MitgliederVertrag, Vertrag, Zahlung, Mitglieder, int laufzeitWochen)!"
        );
    }

    public void insert(MitgliederVertrag mv, Vertrag vertrag, Zahlung zahlung, Mitglieder mitglied, int laufzeitWochen)
            throws Exception {
        mv.validateAll(vertrag, zahlung, mitglied, laufzeitWochen);
        String sql = "INSERT INTO MitgliederVertrag (MitgliederID, VertragID, Vertragsbeginn, Vertragsende, Verlängerung, Aktiv, Gekündigt, Preisrabatt, IntervallID, ZahlungID, Trainingsbeginn, Kommentar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, mv.getMitgliederID());
            ps.setInt(2, mv.getVertragID());
            ps.setDate(3, mv.getVertragsbeginn() != null ? new java.sql.Date(mv.getVertragsbeginn().getTime()) : null);
            ps.setDate(4, mv.getVertragsende() != null ? new java.sql.Date(mv.getVertragsende().getTime()) : null);
            ps.setBoolean(5, mv.isVerlängerung());
            ps.setBoolean(6, mv.isAktiv());
            ps.setBoolean(7, mv.isGekündigt());
            ps.setDouble(8, mv.getPreisrabatt());
            ps.setInt(9, mv.getIntervallID());
            ps.setInt(10, mv.getZahlungID());
            ps.setDate(11, mv.getTrainingsbeginn() != null ? new java.sql.Date(mv.getTrainingsbeginn().getTime()) : null);
            ps.setString(12, mv.getKommentar());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                mv.setVertragNr(rs.getInt(1));
            }
        }
    }

    @Override
    public MitgliederVertrag findById(int id) throws SQLException {
        String sql = "SELECT * FROM MitgliederVertrag WHERE VertragNr = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToMitgliederVertrag(rs);
            }
        }
        return null;
    }

    public void update(MitgliederVertrag mv, Vertrag vertrag, Zahlung zahlung, Mitglieder mitglied, int laufzeitWochen)
            throws Exception {
        mv.validateAll(vertrag, zahlung, mitglied, laufzeitWochen);
        String sql = "UPDATE MitgliederVertrag SET MitgliederID=?, VertragID=?, Vertragsbeginn=?, Vertragsende=?, Verlängerung=?, Aktiv=?, Gekündigt=?, Preisrabatt=?, IntervallID=?, ZahlungID=?, Trainingsbeginn=?, Kommentar=? WHERE VertragNr=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, mv.getMitgliederID());
            ps.setInt(2, mv.getVertragID());
            ps.setDate(3, mv.getVertragsbeginn() != null ? new java.sql.Date(mv.getVertragsbeginn().getTime()) : null);
            ps.setDate(4, mv.getVertragsende() != null ? new java.sql.Date(mv.getVertragsende().getTime()) : null);
            ps.setBoolean(5, mv.isVerlängerung());
            ps.setBoolean(6, mv.isAktiv());
            ps.setBoolean(7, mv.isGekündigt());
            ps.setDouble(8, mv.getPreisrabatt());
            ps.setInt(9, mv.getIntervallID());
            ps.setInt(10, mv.getZahlungID());
            ps.setDate(11, mv.getTrainingsbeginn() != null ? new java.sql.Date(mv.getTrainingsbeginn().getTime()) : null);
            ps.setString(12, mv.getKommentar());
            ps.setInt(13, mv.getVertragNr());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM MitgliederVertrag WHERE VertragNr=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<MitgliederVertrag> findAll() throws SQLException {
        String sql = "SELECT * FROM MitgliederVertrag";
        List<MitgliederVertrag> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToMitgliederVertrag(rs));
            }
        }
        return list;
    }
    
    public List<MitgliederVertrag> searchAllAttributes(String searchTerm) throws SQLException {
        List<MitgliederVertrag> result = new ArrayList<>();
        String sql = "SELECT * FROM MitgliederVertrag WHERE " +
                     "CAST(VertragNr AS CHAR) LIKE ? OR " +
                     "CAST(MitgliederID AS CHAR) LIKE ? OR " +
                     "CAST(VertragID AS CHAR) LIKE ? OR " +
                     "LOWER(Kommentar) LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String like = "%" + searchTerm.toLowerCase() + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToMitgliederVertrag(rs));
                }
            }
        }
        return result;
    }
    
    public List<MitgliederVertrag> findByMitgliedId(int mitgliedId) throws SQLException {
        List<MitgliederVertrag> result = new ArrayList<>();
        String sql = "SELECT * FROM MitgliederVertrag WHERE MitgliederID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, mitgliedId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(mapRowToMitgliederVertrag(rs));
            }
        }
        return result;
    }

    public List<MitgliederVertrag> findByZahlungId(int zahlungId) throws SQLException {
        List<MitgliederVertrag> result = new ArrayList<>();
        String sql = "SELECT * FROM MitgliederVertrag WHERE ZahlungID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, zahlungId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToMitgliederVertrag(rs));
                }
            }
        }
        return result;
    }

    public List<MitgliederVertrag> findByVertragId(int vertragId) throws SQLException {
        List<MitgliederVertrag> result = new ArrayList<>();
        String sql = "SELECT * FROM MitgliederVertrag WHERE VertragID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, vertragId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToMitgliederVertrag(rs));
                }
            }
        }
        return result;
    }

    public List<MitgliederVertrag> findByIntervallId(int intervallId) throws SQLException {
        List<MitgliederVertrag> result = new ArrayList<>();
        String sql = "SELECT * FROM MitgliederVertrag WHERE IntervallID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, intervallId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToMitgliederVertrag(rs));
                }
            }
        }
        return result;
    }

    private MitgliederVertrag mapRowToMitgliederVertrag(ResultSet rs) throws SQLException {
        MitgliederVertrag mv = new MitgliederVertrag();
        mv.setVertragNr(rs.getInt("VertragNr"));
        mv.setMitgliederID(rs.getInt("MitgliederID"));
        mv.setVertragID(rs.getInt("VertragID"));
        mv.setVertragsbeginn(rs.getDate("Vertragsbeginn"));
        mv.setVertragsende(rs.getDate("Vertragsende"));
        mv.setVerlängerung(rs.getBoolean("Verlängerung"));
        mv.setAktiv(rs.getBoolean("Aktiv"));
        mv.setGekündigt(rs.getBoolean("Gekündigt"));
        mv.setPreisrabatt(rs.getDouble("Preisrabatt"));
        mv.setIntervallID(rs.getInt("IntervallID"));
        mv.setZahlungID(rs.getInt("ZahlungID"));
        mv.setTrainingsbeginn(rs.getDate("Trainingsbeginn"));
        mv.setKommentar(rs.getString("Kommentar"));
        return mv;
    }
}
