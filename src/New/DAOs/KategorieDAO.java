package New.DAOs;

import New.Objekte.Kategorie;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KategorieDAO extends BaseDAO<Kategorie> {
    public KategorieDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(Kategorie kategorie) throws SQLException {
        String sql = "INSERT INTO Kategorie (Bezeichnung) VALUES (?)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, kategorie.getBezeichnung());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                kategorie.setKategorieID(rs.getInt(1));
            }
        } finally {
            closeResources(rs, ps);
        }
    }

    @Override
    public Kategorie findById(int id) throws SQLException {
        String sql = "SELECT KategorieID, Bezeichnung FROM Kategorie WHERE KategorieID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToKategorie(rs);
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }

    @Override
    public void update(Kategorie kategorie) throws SQLException {
        String sql = "UPDATE Kategorie SET Bezeichnung = ? WHERE KategorieID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, kategorie.getBezeichnung());
            ps.setInt(2, kategorie.getKategorieID());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Kategorie WHERE KategorieID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    public List<Kategorie> findAll() throws SQLException {
        String sql = "SELECT KategorieID, Bezeichnung FROM Kategorie";
        List<Kategorie> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToKategorie(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return list;
    }

    private Kategorie mapRowToKategorie(ResultSet rs) throws SQLException {
        Kategorie kategorie = new Kategorie();
        kategorie.setKategorieID(rs.getInt("KategorieID"));
        kategorie.setBezeichnung(rs.getString("Bezeichnung"));
        return kategorie;
    }
}
