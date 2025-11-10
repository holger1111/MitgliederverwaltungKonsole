package New.Service;

import java.sql.Connection;
import java.util.Scanner;

public class HauptmenüService extends BaseService {

    private final MitgliederService mitgliederService;
    private final VerkaufService verkaufService;
    private final KursService kursService;
    private final VertragService vertragService;
    private final ÜbersichtService übersichtService;
    private final SucheService sucheService;
//    private final AdminService adminService;

    public HauptmenüService(Connection connection, Scanner scanner,
                           MitgliederService mitgliederService,
                           VerkaufService verkaufService,
                           KursService kursService,
                           VertragService vertragService,
                           ÜbersichtService übersichtService,
                           SucheService sucheService
//                           , AdminService adminService
                           ) {
        super(connection, scanner);
        this.mitgliederService = mitgliederService;
        this.verkaufService = verkaufService;
        this.kursService = kursService;
        this.vertragService = vertragService;
        this.übersichtService = übersichtService;
        this.sucheService = sucheService;
//        this.adminService = adminService;
    }

    public void start() {
        boolean weiter = true;

        while (weiter) {
            System.out.println();
            System.out.println("==== Hauptmenü ====");
            System.out.println("1 - Übersicht");
            System.out.println("2 - Mitglieder");
            System.out.println("3 - Verkauf");
            System.out.println("4 - Kurse");
            System.out.println("5 - Verträge");
//            System.out.println("6 - Admin");
            System.out.println("7 - Suche");
            System.out.println("0 - Programm beenden");
            System.out.print("Bitte eine Zahl eingeben: ");

            String eingabe = scanner.nextLine();

            switch (eingabe) {
                case "1":
                    übersichtService.start();
                    break;
                case "2":
                    mitgliederService.start();
                    break;
                case "3":
                    verkaufService.start();
                    break;
                case "4":
                    kursService.start();
                    break;
                case "5":
                    vertragService.start();
                    break;
//                case "6":
//                    adminService.start();
//                    break;
                case "7":
                    sucheService.start();
                    break;
                case "0":
                    System.out.println("Programm wird beendet.");
                    weiter = false;
                    break;
                default:
                    System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
            }

            System.out.println();
        }
    }
}
