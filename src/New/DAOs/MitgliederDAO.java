package New.DAOs;

import New.Exception.IntException;
import New.Exception.NotFoundException;
import New.Objekte.Mitglieder;
import New.Objekte.Ort;
import New.Objekte.Zahlungsdaten;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class MitgliederDAO extends BaseDAO<Mitglieder> {

	private final OrtDAO ortDAO;
	private final ZahlungsdatenDAO zahlungsdatenDAO;

	public MitgliederDAO(Connection connection) {
		super(connection);
		ortDAO = new OrtDAO(connection);
		zahlungsdatenDAO = new ZahlungsdatenDAO(connection);
	}

	public List<Mitglieder> searchAllAttributes(String searchTerm) throws SQLException {
		List<Mitglieder> results = new ArrayList<>();
		String sql = "SELECT m.MitgliederID, m.Vorname, m.Nachname, m.Geburtsdatum, m.Aktiv, m.Straße, m.Hausnr, "
				+ "m.OrtID, o.PLZ, o.Ort, m.ZahlungsdatenID, m.Telefon, m.Mail " + "FROM Mitglieder AS m "
				+ "LEFT JOIN Ort AS o ON o.OrtID = m.OrtID "
				+ "WHERE Vorname LIKE ? OR Nachname LIKE ? OR Mail LIKE ? OR Telefon LIKE ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			String pattern = "%" + searchTerm + "%";
			for (int i = 1; i <= 4; i++) {
				stmt.setString(i, pattern);
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Mitglieder m = mapRowToMitglieder(rs);
				results.add(m);
			}
		}
		return results;
	}

	public List<Mitglieder> searchByName(String vorname, String nachname) throws SQLException {
		String sql = "SELECT m.MitgliederID, m.Vorname, m.Nachname, m.Geburtsdatum, m.Aktiv, m.Straße, m.Hausnr, m.OrtID, o.PLZ, o.Ort, m.ZahlungsdatenID, m.Telefon,"
				+ "m.Mail FROM Mitglieder WHERE LOWER(Vorname) LIKE ? AND LOWER(Nachname) LIKE ?";
		List<Mitglieder> result = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, "%" + vorname.toLowerCase() + "%");
			ps.setString(2, "%" + nachname.toLowerCase() + "%");
			rs = ps.executeQuery();
			while (rs.next()) {
				result.add(mapRowToMitglieder(rs));
			}
		} finally {
			closeResources(rs, ps);
		}
		return result;
	}

	public List<Mitglieder> findByOrtId(int ortId) throws SQLException {
		List<Mitglieder> results = new ArrayList<>();
		String sql = "SELECT m.MitgliederID, m.Vorname, m.Nachname, m.Geburtsdatum, m.Aktiv, m.Straße, m.Hausnr, "
				+ "m.OrtID, o.PLZ, o.Ort, m.ZahlungsdatenID, m.Telefon, m.Mail " + "FROM Mitglieder AS m "
				+ "LEFT JOIN Ort AS o ON o.OrtID = m.OrtID " + "WHERE m.OrtID = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, ortId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Mitglieder m = mapRowToMitglieder(rs);
				results.add(m);
			}
		}
		return results;
	}

	public List<Mitglieder> findByZahlungsdatenId(int zahlungsdatenId) throws SQLException {
		List<Mitglieder> results = new ArrayList<>();
		String sql = "SELECT m.MitgliederID, m.Vorname, m.Nachname, m.Geburtsdatum, m.Aktiv, m.Straße, m.Hausnr, "
				+ "m.OrtID, o.PLZ, o.Ort, m.ZahlungsdatenID, m.Telefon, m.Mail " + "FROM Mitglieder AS m "
				+ "LEFT JOIN Ort AS o ON o.OrtID = m.OrtID " + "WHERE m.ZahlungsdatenID = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, zahlungsdatenId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Mitglieder m = mapRowToMitglieder(rs);
				results.add(m);
			}
		}
		return results;
	}

	public List<Mitglieder> findByInteressentenId(int mitgliederID) throws SQLException {
		List<Mitglieder> results = new ArrayList<>();
		String sql = "SELECT m.MitgliederID, m.Vorname, m.Nachname, m.Geburtsdatum, m.Aktiv, m.Straße, m.Hausnr, "
				+ "m.OrtID, o.PLZ, o.Ort, m.ZahlungsdatenID, m.Telefon, m.Mail " + "FROM Mitglieder AS m "
				+ "LEFT JOIN Ort AS o ON o.OrtID = m.OrtID " + "WHERE MitgliederID = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, mitgliederID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Mitglieder m = mapRowToMitglieder(rs);
				results.add(m);
			}
		}
		return results;
	}

	public int getID(Mitglieder mitglied) {
		return mitglied.getMitgliederID();
	}

	private Mitglieder mapRowToMitglieder(ResultSet rs) throws SQLException {
		Mitglieder mitglied = new Mitglieder();
		mitglied.setMitgliederID(rs.getInt("MitgliederID"));
		mitglied.setVorname(rs.getString("Vorname"));
		mitglied.setNachname(rs.getString("Nachname"));
		mitglied.setGeburtstag(rs.getDate("Geburtsdatum"));
		mitglied.setAktiv(rs.getBoolean("Aktiv"));
		mitglied.setStrasse(rs.getString("Straße"));
		mitglied.setHausnr(rs.getString("Hausnr"));
		Ort ort = new Ort(rs.getInt("OrtID"), rs.getString("PLZ"), rs.getString("Ort"));
		mitglied.setOrt(ort);
		mitglied.setZahlungsdatenID(rs.getInt("ZahlungsdatenID"));
		mitglied.setTelefon(rs.getString("Telefon"));
		mitglied.setMail(rs.getString("Mail"));
		return mitglied;
	}

	@Override
	public Mitglieder findById(int id) throws SQLException, IntException, NotFoundException {
		String sql = "SELECT m.MitgliederID, m.Vorname, m.Nachname, m.Geburtsdatum, m.Aktiv, m.Straße, m.Hausnr, "
				+ "m.OrtID, o.PLZ, o.Ort, m.ZahlungsdatenID, m.Telefon, m.Mail " + "FROM Mitglieder AS m "
				+ "LEFT JOIN Ort AS o ON o.OrtID = m.OrtID " + "WHERE m.MitgliederID = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				int ortID = rs.getInt("OrtID");
				int zahlungsdatenID = rs.getInt("ZahlungsdatenID");

				Ort ort = ortDAO.findById(ortID);
				Zahlungsdaten zahlungsdaten = zahlungsdatenDAO.findById(zahlungsdatenID);

				return new Mitglieder(rs.getInt("MitgliederID"), rs.getString("Vorname"), rs.getString("Nachname"),
						rs.getString("Telefon"), rs.getDate("Geburtsdatum"), rs.getBoolean("Aktiv"),
						rs.getString("Straße"), rs.getString("Hausnr"), ort, zahlungsdaten, rs.getString("Mail"));
			}
		} finally {
			closeResources(rs, ps);
		}
		return null;
	}

	@Override
	public void insert(Mitglieder entity) throws SQLException {
		String sql = "INSERT INTO Mitglieder (Vorname, Nachname, Telefon, Geburtsdatum, Aktiv, Straße, Hausnr, OrtID, ZahlungsdatenID, Mail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, entity.getVorname());
			ps.setString(2, entity.getNachname());
			ps.setString(3, entity.getTelefon());
			ps.setDate(4, entity.getGeburtstag() != null ? new Date(entity.getGeburtstag().getTime()) : null);
			ps.setBoolean(5, entity.isAktiv());
			ps.setString(6, entity.getStrasse());
			ps.setString(7, entity.getHausnr());
			ps.setInt(8, entity.getOrtID());
			ps.setInt(9, entity.getZahlungsdatenID());
			ps.setString(10, entity.getMail());
			ps.executeUpdate();

			rs = ps.getGeneratedKeys();
			if (rs.next()) {
				entity.setMitgliederID(rs.getInt(1));
			}
		} finally {
			closeResources(rs, ps);
		}
	}

	@Override
	public void update(Mitglieder entity) throws SQLException {
		String sql = "UPDATE Mitglieder SET Vorname = ?, Nachname = ?, Telefon = ?, Geburtsdatum = ?, Aktiv = ?, Straße = ?, Hausnr = ?, OrtID = ?, ZahlungsdatenID = ?, Mail = ? WHERE MitgliederID = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, entity.getVorname());
			ps.setString(2, entity.getNachname());
			ps.setString(3, entity.getTelefon());
			ps.setDate(4, entity.getGeburtstag() != null ? new Date(entity.getGeburtstag().getTime()) : null);
			ps.setBoolean(5, entity.isAktiv());
			ps.setString(6, entity.getStrasse());
			ps.setString(7, entity.getHausnr());
			ps.setInt(8, entity.getOrtID());
			ps.setInt(9, entity.getZahlungsdatenID());
			ps.setString(10, entity.getMail());
			ps.setInt(11, entity.getMitgliederID());
			ps.executeUpdate();
		} finally {
			closeResources(null, ps);
		}
	}

	@Override
	public void delete(int id) throws SQLException {
		String sql = "DELETE FROM Mitglieder WHERE MitgliederID = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
		} finally {
			closeResources(null, ps);
		}
	}
}
