package New.DAOs;

import New.Objekte.ArtikelBestellung;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArtikelBestellungDAO extends BaseDAO<ArtikelBestellung> {

    public ArtikelBestellungDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(ArtikelBestellung artikelBestellung) throws SQLException {
        String sql = "INSERT INTO ArtikelBestellung (BestellungID, ArtikelID, Menge, Aufaddiert) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, artikelBestellung.getBestellungID());
            ps.setInt(2, artikelBestellung.getArtikelID());
            ps.setInt(3, artikelBestellung.getMenge());
            ps.setDouble(4, artikelBestellung.getAufaddiert());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public ArtikelBestellung findById(int id) throws SQLException {
        throw new UnsupportedOperationException(
            "ArtikelBestellung hat keinen einzelnen Primary Key. Nutze findByBestellungIdAndArtikelId()!"
        );
    }

    public ArtikelBestellung findByBestellungIdAndArtikelId(int bestellungID, int artikelID) throws SQLException {
        String sql = "SELECT BestellungID, ArtikelID, Menge, Aufaddiert FROM ArtikelBestellung " +
                     "WHERE BestellungID = ? AND ArtikelID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, bestellungID);
            ps.setInt(2, artikelID);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToArtikelBestellung(rs);
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }

    @Override
    public void update(ArtikelBestellung artikelBestellung) throws SQLException {
        String sql = "UPDATE ArtikelBestellung SET Menge = ?, Aufaddiert = ? WHERE BestellungID = ? AND ArtikelID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, artikelBestellung.getMenge());
            ps.setDouble(2, artikelBestellung.getAufaddiert());
            ps.setInt(3, artikelBestellung.getBestellungID());
            ps.setInt(4, artikelBestellung.getArtikelID());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        throw new UnsupportedOperationException(
            "ArtikelBestellung hat keinen einzelnen Primary Key. Nutze deleteByBestellungIdAndArtikelId()!"
        );
    }

    public void deleteByBestellungIdAndArtikelId(int bestellungID, int artikelID) throws SQLException {
        String sql = "DELETE FROM ArtikelBestellung WHERE BestellungID = ? AND ArtikelID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, bestellungID);
            ps.setInt(2, artikelID);
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    public List<ArtikelBestellung> findByBestellungId(int bestellungID) throws SQLException {
        String sql = "SELECT BestellungID, ArtikelID, Menge, Aufaddiert FROM ArtikelBestellung WHERE BestellungID = ?";
        List<ArtikelBestellung> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, bestellungID);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToArtikelBestellung(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return list;
    }

    public List<ArtikelBestellung> findByArtikelId(int artikelID) throws SQLException {
        String sql = "SELECT BestellungID, ArtikelID, Menge, Aufaddiert FROM ArtikelBestellung WHERE ArtikelID = ?";
        List<ArtikelBestellung> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, artikelID);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToArtikelBestellung(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return list;
    }

    public List<ArtikelBestellung> findAll() throws SQLException {
        String sql = "SELECT BestellungID, ArtikelID, Menge, Aufaddiert FROM ArtikelBestellung";
        List<ArtikelBestellung> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToArtikelBestellung(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return list;
    }

    private ArtikelBestellung mapRowToArtikelBestellung(ResultSet rs) throws SQLException {
        return new ArtikelBestellung(
            rs.getInt("BestellungID"),
            rs.getInt("ArtikelID"),
            rs.getInt("Menge"),
            rs.getDouble("Aufaddiert")
        );
    }
}
