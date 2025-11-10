package New.DAOs;

import New.Exception.IntException;
import New.Exception.NotFoundException;
import New.Objekte.Kurs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KursDAO extends BaseDAO<Kurs> {

    public KursDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Kurs findById(int id) throws SQLException, IntException, NotFoundException {
        String sql = "SELECT KursID, Bezeichnung, AnzahlTermine, Preis, Kommentar FROM Kurs WHERE KursID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToKurs(rs);
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }

    @Override
    public void insert(Kurs entity) throws SQLException {
        String sql = "INSERT INTO Kurs (Bezeichnung, AnzahlTermine, Preis, Kommentar) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getBezeichnung());
            ps.setInt(2, entity.getAnzahlTermine());
            ps.setDouble(3, entity.getPreis());
            ps.setString(4, entity.getKommentar());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setKursID(rs.getInt(1));
            }
        } finally {
            closeResources(rs, ps);
        }
    }

    @Override
    public void update(Kurs entity) throws SQLException {
        String sql = "UPDATE Kurs SET Bezeichnung = ?, AnzahlTermine = ?, Preis = ?, Kommentar = ? WHERE KursID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, entity.getBezeichnung());
            ps.setInt(2, entity.getAnzahlTermine());
            ps.setDouble(3, entity.getPreis());
            ps.setString(4, entity.getKommentar());
            ps.setInt(5, entity.getKursID());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Kurs WHERE KursID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    public List<Kurs> findAll() throws SQLException {
        List<Kurs> kurse = new ArrayList<>();
        String sql = "SELECT KursID, Bezeichnung, AnzahlTermine, Preis, Kommentar FROM Kurs";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                kurse.add(mapRowToKurs(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return kurse;
    }

    public List<Kurs> searchAllAttributes(String searchTerm) throws SQLException {
        List<Kurs> results = new ArrayList<>();
        String sql = "SELECT KursID, Bezeichnung, AnzahlTermine, Preis, Kommentar FROM Kurs " +
                     "WHERE Bezeichnung LIKE ? OR Kommentar LIKE ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            String pattern = "%" + searchTerm + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapRowToKurs(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return results;
    }

    private Kurs mapRowToKurs(ResultSet rs) throws SQLException {
        Kurs kurs = new Kurs();
        kurs.setKursID(rs.getInt("KursID"));
        kurs.setBezeichnung(rs.getString("Bezeichnung"));
        kurs.setAnzahlTermine(rs.getInt("AnzahlTermine"));
        kurs.setPreis(rs.getDouble("Preis"));
        kurs.setKommentar(rs.getString("Kommentar"));
        return kurs;
    }
}
