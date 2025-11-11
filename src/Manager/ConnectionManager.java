package Manager;

import java.sql.Connection;

import DAOs.ConnectionDB;
import Exception.ConnectionException;

public class ConnectionManager {

    public ConnectionManager() {
    }

    public Connection getConnection() throws ConnectionException {
        return ConnectionDB.getConnection();
    }
}
