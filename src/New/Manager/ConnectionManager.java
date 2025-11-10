package New.Manager;

import java.sql.Connection;
import New.DAOs.ConnectionDB;
import New.Exception.ConnectionException;

public class ConnectionManager {

    public ConnectionManager() {
    }

    public Connection getConnection() throws ConnectionException {
        return ConnectionDB.getConnection();
    }
}
