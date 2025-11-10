package New.DAOs;

import New.Objekte.Artikel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ArtikelDAO extends BaseDAO<Artikel> {

    public ArtikelDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(Artikel artikel) throws SQLException {
        String sql = "INSERT INTO Artikel (Name, Einzelpreis, Kommentar, Kategorie) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, artikel.getName());
            ps.setDouble(2, artikel.getEinzelpreis());
            ps.setString(3, artikel.getKommentar());
            ps.setString(4, artikel.getKategorie());
            ps.executeUpdate();
            
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                artikel.setArtikelID(rs.getInt(1));
            }
        } finally {
            closeResources(rs, ps);
        }
    }

    @Override
    public Artikel findById(int id) throws SQLException {
        String sql = "SELECT ArtikelID, Name, Einzelpreis, Kommentar, Kategorie FROM Artikel WHERE ArtikelID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToArtikel(rs);
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }

    @Override
    public void update(Artikel artikel) throws SQLException {
        String sql = "UPDATE Artikel SET Name = ?, Einzelpreis = ?, Kommentar = ?, Kategorie = ? WHERE ArtikelID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, artikel.getName());
            ps.setDouble(2, artikel.getEinzelpreis());
            ps.setString(3, artikel.getKommentar());
            ps.setString(4, artikel.getKategorie());
            ps.setInt(5, artikel.getArtikelID());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Artikel WHERE ArtikelID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    public List<Artikel> findAll() throws SQLException {
        // ✅ WICHTIG: Kategorie in SELECT aufnehmen
        String sql = "SELECT ArtikelID, Name, Einzelpreis, Kommentar, Kategorie FROM Artikel";
        List<Artikel> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToArtikel(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return list;
    }

    public List<Artikel> searchAllAttributes(String searchTerm) throws SQLException {
        List<Artikel> result = new ArrayList<>();
        // ✅ WICHTIG: Kategorie in SELECT und WHERE aufnehmen
        String sql = "SELECT ArtikelID, Name, Einzelpreis, Kommentar, Kategorie FROM Artikel WHERE " +
                     "CAST(ArtikelID AS CHAR) LIKE ? OR " +
                     "LOWER(Name) LIKE ? OR " +
                     "LOWER(Kommentar) LIKE ? OR " +
                     "LOWER(Kategorie) LIKE ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            String like = "%" + searchTerm.toLowerCase() + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapRowToArtikel(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return result;
    }

    // ✅ WICHTIG: Kategorie aus ResultSet auslesen
    private Artikel mapRowToArtikel(ResultSet rs) throws SQLException {
        Artikel artikel = new Artikel(
            rs.getInt("ArtikelID"),
            rs.getString("Name"),
            rs.getDouble("Einzelpreis"),
            rs.getString("Kommentar")
        );
        artikel.setKategorie(rs.getString("Kategorie"));
        return artikel;
    }
}
