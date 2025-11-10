package New.DAOs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import New.Exception.ConnectionException;

public class ConnectionDB {

    private static String url = "jdbc:mysql://localhost:3306/Mitgliederverwaltung";
    private static String user = "root";
    private static String password = "meinPasswort";

    public static Connection getConnection() throws ConnectionException {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new ConnectionException("Keine Verbindung zur Datenbank möglich.", e);
            
        }
    }
}
