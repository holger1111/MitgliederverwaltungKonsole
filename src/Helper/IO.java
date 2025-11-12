package Helper;

import java.util.Scanner;
import java.math.BigInteger;
import java.math.BigDecimal;

import Validator.BasicTypeValidator;
import Exception.*;

public class IO {

    private static final Scanner scanner = new Scanner(System.in);

    public static int readInt(String prompt) throws IntException {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        int value = Integer.parseInt(input);
        BasicTypeValidator.validateInt(value);
        return value;
    }

    public static double readDouble(String prompt) throws IntException {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        double value = Double.parseDouble(input);
        BasicTypeValidator.validateDouble(value);
        return value;
    }

    public static String readString(String prompt) throws StringException {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        BasicTypeValidator.validateString(input);
        return input;
    }

    public static char readChar(String prompt) throws StringException {
        System.out.println(prompt);
        String line = scanner.nextLine();
        String clean = StripEntry.clean(line);
        char value = clean.isEmpty() ? '\0' : clean.charAt(0);
        BasicTypeValidator.validateCharacter(value);
        return value;
    }

    public static byte readByte(String prompt) throws IntException {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        byte value = Byte.parseByte(input);
        BasicTypeValidator.validateByte(value);
        return value;
    }

    public static short readShort(String prompt) throws IntException {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        short value = Short.parseShort(input);
        BasicTypeValidator.validateShort(value);
        return value;
    }

    public static long readLong(String prompt) throws IntException {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        long value = Long.parseLong(input);
        BasicTypeValidator.validateLong(value);
        return value;
    }

    public static float readFloat(String prompt) throws IntException {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        float value = Float.parseFloat(input);
        BasicTypeValidator.validateFloat(value);
        return value;
    }

    public static boolean readBoolean(String prompt) throws BooleanException {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        boolean value = Boolean.parseBoolean(input);
        BasicTypeValidator.validateBoolean(value);
        return value;
    }

    public static String readLine(String prompt) throws StringException {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        BasicTypeValidator.validateString(input);
        return input;
    }

    public static String readToken(String prompt) throws StringException {
        System.out.println(prompt);
        String input = scanner.next();
        input = StripEntry.clean(input);
        BasicTypeValidator.validateString(input);
        return input;
    }

    public static BigInteger readBigInteger(String prompt) throws IntException {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        BigInteger value = new BigInteger(input);
        BasicTypeValidator.validateBigInteger(value);
        return value;
    }

    public static BigDecimal readBigDecimal(String prompt) throws IntException {
        System.out.println(prompt);
        String input = scanner.nextLine();
        input = StripEntry.clean(input);
        input = input.replace(",", ".");
        BigDecimal value = new BigDecimal(input);
        BasicTypeValidator.validateBigDecimal(value);
        return value;
    }
}
