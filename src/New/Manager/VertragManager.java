package New.Manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import New.DAOs.MitgliederVertragDAO;
import New.DAOs.MitgliederDAO;
import New.DAOs.VertragDAO;
import New.DAOs.ZahlungDAO;
import New.DAOs.IntervallDAO;
import New.Objekte.MitgliederVertrag;
import New.Objekte.Vertrag;
import New.Objekte.Zahlung;
import New.Objekte.Intervall;
import New.Exception.ConnectionException;
import New.Exception.IntException;
import New.Exception.NotFoundException;
import New.DAOs.ConnectionDB;

public class VertragManager extends BaseManager<MitgliederVertrag> {

    private final MitgliederVertragDAO mitgliederVertragDAO;
    private final VertragDAO vertragDAO;
    private final ZahlungDAO zahlungDAO;
    private final IntervallDAO intervallDAO;
    private final MitgliederDAO mitgliederDAO;

    public VertragManager() throws ConnectionException, SQLException {
        Connection connection = ConnectionDB.getConnection();
        mitgliederVertragDAO = new MitgliederVertragDAO(connection);
        vertragDAO = new VertragDAO(connection);
        zahlungDAO = new ZahlungDAO(connection);
        intervallDAO = new IntervallDAO(connection);
        mitgliederDAO = new MitgliederDAO(connection);
    }

    public MitgliederVertragDAO getMitgliederVertragDAO() {
        return mitgliederVertragDAO;
    }

    public VertragDAO getVertragDAO() {
        return vertragDAO;
    }

    public ZahlungDAO getZahlungDAO() {
        return zahlungDAO;
    }

    public IntervallDAO getIntervallDAO() {
        return intervallDAO;
    }

    public MitgliederDAO getMitgliederDAO() {
        return mitgliederDAO;
    }
    
    @Override
    public void add(MitgliederVertrag item) {
        super.add(item);
    }

    @Override
    public void remove(MitgliederVertrag item) {
        super.remove(item);
    }

    @Override
    public List<MitgliederVertrag> getAll() {
        return super.getAll();
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public void process() {
    }

    public MitgliederVertrag findById(int vertragNr) throws NotFoundException, IntException, SQLException {
        MitgliederVertrag mv = mitgliederVertragDAO.findById(vertragNr);
        if (mv == null) {
            throw new NotFoundException("MitgliederVertrag mit Nummer " + vertragNr + " nicht gefunden.");
        }
        return mv;
    }

    /**
     * Gemeinsame Suche nach einem Suchbegriff über MitgliederVertrag, Vertrag, Zahlung, Intervall.
     * @param searchTerm Suchbegriff
     * @return Liste gefundener MitgliederVerträge
     */
    public List<MitgliederVertrag> search(String searchTerm) throws SQLException, IntException {
        List<MitgliederVertrag> result = new ArrayList<>();

        List<MitgliederVertrag> vertragsListe = mitgliederVertragDAO.searchAllAttributes(searchTerm);
        if (vertragsListe != null) {
            result.addAll(vertragsListe);
        }

        List<Vertrag> vertragResult = vertragDAO.searchAllAttributes(searchTerm);
        if (vertragResult != null) {
            for (Vertrag v : vertragResult) {
                List<MitgliederVertrag> mvByVertrag = mitgliederVertragDAO.findByVertragId(v.getVertragID());
                for (MitgliederVertrag mv : mvByVertrag) {
                    if (!result.contains(mv)) {
                        result.add(mv);
                    }
                }
            }
        }

        List<Zahlung> zahlungResult = zahlungDAO.searchAllAttributes(searchTerm);
        if (zahlungResult != null) {
            for (Zahlung z : zahlungResult) {
                List<MitgliederVertrag> mvByZahlung = mitgliederVertragDAO.findByZahlungId(z.getZahlungID());
                for (MitgliederVertrag mv : mvByZahlung) {
                    if (!result.contains(mv)) {
                        result.add(mv);
                    }
                }
            }
        }

        List<Intervall> intervallResult = intervallDAO.searchAllAttributes(searchTerm);
        if (intervallResult != null) {
            for (Intervall i : intervallResult) {
                List<MitgliederVertrag> mvByIntervall = mitgliederVertragDAO.findByIntervallId(i.getIntervallID());
                for (MitgliederVertrag mv : mvByIntervall) {
                    if (!result.contains(mv)) {
                        result.add(mv);
                    }
                }
            }
        }

        return result;
    }
}
