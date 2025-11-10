package New.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Abstrakte generische Basisklasse für alle DAOs.
 *
 * @param <T> Der Typ des zu verwaltenden Objekts
 */
public abstract class BaseDAO<T> {

    protected Connection connection;

    public BaseDAO(Connection connection) {
        this.connection = connection;
    }

    // CRUD-Methoden mit genereller Exception erlaubt
    public abstract T findById(int id) throws Exception;
    public abstract void insert(T entity) throws Exception;
    public abstract void update(T entity) throws Exception;
    public abstract void delete(int id) throws Exception;

    // Schließt die Verbindung
    protected void closeResources(ResultSet rs, PreparedStatement ps) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getter
    
    protected Connection getConnection() {
        return connection;
    }
}
