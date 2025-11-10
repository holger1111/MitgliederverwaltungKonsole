package New.DAOs;

import New.Objekte.Artikel;
import New.Objekte.Kategorie;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtikelDAO extends BaseDAO<Artikel> {
    private KategorieDAO kategorieDAO;

    public ArtikelDAO(Connection connection) {
        super(connection);
        this.kategorieDAO = new KategorieDAO(connection);
    }

    @Override
    public void insert(Artikel artikel) throws SQLException {
        String sql = "INSERT INTO Artikel (Name, Einzelpreis, Kommentar, KategorieID) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, artikel.getName());
            ps.setDouble(2, artikel.getEinzelpreis());
            ps.setString(3, artikel.getKommentar());
            ps.setInt(4, artikel.getKategorie().getKategorieID()); // Änderung!
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
        String sql = "SELECT ArtikelID, Name, Einzelpreis, Kommentar, KategorieID FROM Artikel WHERE ArtikelID = ?";
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
        String sql = "UPDATE Artikel SET Name = ?, Einzelpreis = ?, Kommentar = ?, KategorieID = ? WHERE ArtikelID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, artikel.getName());
            ps.setDouble(2, artikel.getEinzelpreis());
            ps.setString(3, artikel.getKommentar());
            ps.setInt(4, artikel.getKategorie().getKategorieID()); // Änderung!
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
        String sql = "SELECT ArtikelID, Name, Einzelpreis, Kommentar, KategorieID FROM Artikel";
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

    // Hilfsmethode: Sucht nach allen Attributen
    public List<Artikel> searchAllAttributes(String searchTerm) throws SQLException {
        List<Artikel> result = new ArrayList<>();
        String sql = "SELECT ArtikelID, Name, Einzelpreis, Kommentar, KategorieID FROM Artikel WHERE " +
                     "CAST(ArtikelID AS CHAR) LIKE ? OR " +
                     "LOWER(Name) LIKE ? OR " +
                     "LOWER(Kommentar) LIKE ? OR " +
                     "CAST(KategorieID AS CHAR) LIKE ?";
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

    // Änderung: Kategorie als Objekt holen
    private Artikel mapRowToArtikel(ResultSet rs) throws SQLException {
        int kategorieID = rs.getInt("KategorieID");
        Kategorie kategorie = kategorieDAO.findById(kategorieID);

        Artikel artikel = new Artikel(
            rs.getInt("ArtikelID"),
            rs.getString("Name"),
            rs.getDouble("Einzelpreis"),
            rs.getString("Kommentar"),
            kategorie
        );
        return artikel;
    }
}
