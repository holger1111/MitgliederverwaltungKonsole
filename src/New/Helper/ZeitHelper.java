package New.Helper;

public class ZeitHelper {
	
	// Validation
	
    public static boolean istStunde(int stunde) {
        return stunde >= 0 && stunde <= 23;
    }

    public static boolean istMinute(int minute) {
        return minute >= 0 && minute <= 59;
    }

    public static boolean istSekunde(int sekunde) {
        return sekunde >= 0 && sekunde <= 59;
    }
    
    // Input
    
    public static int readStunde() {
        int stunde = IO.readInt("Stunde: ");
        while (!istStunde(stunde)) {
            System.out.printf("Die Stunde %02d gibt es nicht.\n", stunde);
            stunde = IO.readInt("Stunde: ");
        }
        return stunde;
    }
    
    public static int readMinute() {
        int minute = IO.readInt("Minute: ");
        while (!istMinute(minute)) {
            System.out.printf("Die Minute %02d gibt es nicht.\n", minute);
            minute = IO.readInt("Minute: ");
        }
        return minute;
    }

    public static int readSekunde() {
        int sekunde = IO.readInt("Sekunde: ");
        while (!istSekunde(sekunde)) {
            System.out.printf("Die Sekunde %02d gibt es nicht.\n", sekunde);
            sekunde = IO.readInt("Sekunde: ");
        }
        return sekunde;
    }
    
}
