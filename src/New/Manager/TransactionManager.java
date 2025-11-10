package New.Manager;

import New.DAOs.Transaction;
import New.Exception.ConnectionException;
import New.DAOs.ConnectionDB;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager extends BaseManager<Transaction> {

    private Transaction currentTransaction;

    public TransactionManager() {
    }

    public void begin() throws ConnectionException, SQLException {
        if (currentTransaction != null && currentTransaction.isActive()) {
            throw new SQLException("Transaktion bereits aktiv");
        }
        Connection connection = ConnectionDB.getConnection();
        currentTransaction = new Transaction(connection);
    }

    public void commit() throws SQLException {
        if (currentTransaction == null || !currentTransaction.isActive()) {
            throw new SQLException("Keine aktive Transaktion");
        }
        currentTransaction.commit();
    }

    public void rollback() throws SQLException {
        if (currentTransaction == null || !currentTransaction.isActive()) {
            throw new SQLException("Keine aktive Transaktion");
        }
        currentTransaction.rollback();
    }

    public void close() throws SQLException {
        if (currentTransaction != null) {
            currentTransaction.close();
        }
    }
    
    @Override
    public void process() {
        for (Transaction tx : items) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        clear();
    }
}
