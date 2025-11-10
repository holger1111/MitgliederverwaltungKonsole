package New.Service;

import java.sql.Connection;
import java.util.Scanner;

public class ÜbersichtService extends BaseService {

    public ÜbersichtService(Connection connection, Scanner scanner) {
        super(connection, scanner);
    }

    public void start() {
        boolean zurueck = false;
        while (!zurueck) {
            System.out.println("==== Übersicht ====");
            System.out.println("1 - Zurück zum Hauptmenü");
            System.out.print("Bitte wählen: ");
            String eingabe = scanner.nextLine();
            if ("1".equals(eingabe)) {
                zurueck = true;
            } else {
                System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
            }
        }
    }
}
