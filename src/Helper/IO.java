package Helper;

import java.util.Scanner;

public class IO {

    private static final Scanner scanner = new Scanner(System.in);

    public static int readInt(String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        return Integer.parseInt(input);
    }

    public static double readDouble(String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        return Double.parseDouble(input);
    }

    public static String readString(String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine();
        return StripEntry.clean(input);
    }

    public static char readChar(String prompt) {
        System.out.println(prompt);
        String line = scanner.nextLine();
        String clean = StripEntry.clean(line);
        return clean.isEmpty() ? '\0' : clean.charAt(0);
    }

    public static byte readByte(String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        return Byte.parseByte(input);
    }

    public static short readShort(String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        return Short.parseShort(input);
    }

    public static long readLong(String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        return Long.parseLong(input);
    }

    public static float readFloat(String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        return Float.parseFloat(input);
    }

    public static boolean readBoolean(String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        return Boolean.parseBoolean(input);
    }

    public static String readLine(String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine();
        return StripEntry.clean(input);
    }

    public static String readToken(String prompt) {
        System.out.println(prompt);
        String input = scanner.next();
        input = StripEntry.clean(input);
        return input;
    }
}
