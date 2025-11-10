package New.DAOs;

import java.sql.Connection;
import java.sql.SQLException;

import New.Exception.ConnectionException;

public class Transaction {
    private final Connection connection;
    private boolean active = false;

    public Transaction(Connection connection) throws ConnectionException, SQLException {
        if (connection == null || connection.isClosed()) {
            throw new ConnectionException("Transaktion kann nicht gestartet werden: Keine gültige Datenbankverbindung.");
        }
        this.connection = connection;
        this.connection.setAutoCommit(false);
        this.active = true;
    }


    public void commit() throws SQLException {
        if (active) {
            connection.commit();
            active = false;
            connection.setAutoCommit(true);
        }
    }

    public void rollback() throws SQLException {
        if (active) {
            connection.rollback();
            active = false;
            connection.setAutoCommit(true);
        }
    }

    public void close() throws SQLException {
        if (active) {
            rollback();
        }
        if (!connection.isClosed()) {
            connection.close();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isActive() {
        return active;
    }
}

//
//
//Transaction tx = new Transaction(conn);
//try {
//    // DB-Operationen
//    tx.commit();
//} catch (Exception e) {
//    tx.rollback();
//} finally {
//    tx.close();
//}
