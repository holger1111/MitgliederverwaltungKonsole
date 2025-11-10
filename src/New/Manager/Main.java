package New.Manager;

import java.sql.Connection;
import java.util.Scanner;

import New.Exception.ConnectionException;
import New.Service.*;

public class Main {

    public static void main(String[] args) {
        Connection connection = null;
        Scanner scanner = new Scanner(System.in);
        ConnectionManager connectionManager = new ConnectionManager();

        try {
            // ========== DATENBANKVERBINDUNG ÜBER ConnectionManager ==========
            connection = connectionManager.getConnection();
            System.out.println("✓ Datenbankverbindung erfolgreich!\n");

            // ========== SERVICES INITIALISIEREN ==========
            MitgliederService mitgliederService = new MitgliederService(connection, scanner);
            VerkaufService verkaufService = new VerkaufService(connection, scanner);
            KursService kursService = new KursService(connection, scanner);
            VertragService vertragService = new VertragService(connection, scanner);
            ÜbersichtService übersichtService = new ÜbersichtService(connection, scanner);
            SucheService sucheService = new SucheService(connection, scanner);
//            AdminService adminService = new AdminService(connection, scanner);

            HauptmenüService hauptmenüService = new HauptmenüService(
                connection, scanner,
                mitgliederService,
                verkaufService,
                kursService,
                vertragService,
                übersichtService,
                sucheService
//                ,
//                adminService
            );

            // ========== PROGRAMM STARTEN ==========
            hauptmenüService.start();

        } catch (ConnectionException e) {
            System.err.println("✗ Fehler beim Verbinden mit der Datenbank:");
            System.err.println("  " + e.getMessage());
            System.err.println("\nBitte überprüfen Sie:");
            System.err.println("  - Ist der MySQL-Server gestartet?");
            System.err.println("  - Sind die Verbindungsdaten korrekt?");
            System.err.println("  - Existiert die Datenbank 'Mitgliederverwaltung'?");
        } finally {
            // ========== AUFRÄUMEN ==========
            scanner.close();
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("\n✓ Datenbankverbindung geschlossen.");
                } catch (Exception e) {
                    System.err.println("✗ Fehler beim Schließen der Verbindung: " + e.getMessage());
                }
            }
        }
    }
}
