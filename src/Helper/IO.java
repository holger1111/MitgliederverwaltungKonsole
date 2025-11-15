package Helper;

import java.util.Scanner;
import java.math.BigInteger;
import java.math.BigDecimal;

import Exception.*;
import Validator.*;

public class IO {
    private static final Scanner scanner = new Scanner(System.in);

    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                String input = scanner.nextLine();
                input = StripEntry.clean(input);
                int value = Integer.parseInt(input);
                IntValidator.validate(value);
                return value;
            } catch (IntException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe, bitte eine ganze Zahl eingeben.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                String input = scanner.nextLine();
                input = StripEntry.clean(input);
                double value = Double.parseDouble(input);
                DoubleValidator.validate(value);
                return value;
            } catch (DoubleException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe, bitte eine Gleitkommazahl eingeben.");
            }
        }
    }

    public static String readString(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                String input = scanner.nextLine();
                input = StripEntry.clean(input);
                StringValidator.validate(input);
                return input;
            } catch (StringException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static char readChar(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                String line = scanner.nextLine();
                String clean = StripEntry.clean(line);
                if (clean.isEmpty())
                    throw new StringException("Keine Eingabe erhalten.");
                char value = clean.charAt(0);
                CharValidator.validate(value);
                return value;
            } catch (StringException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static byte readByte(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                String input = scanner.nextLine();
                input = StripEntry.clean(input);
                byte value = Byte.parseByte(input);
                ByteValidator.validate(value);
                return value;
            } catch (ByteException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe, bitte eine Zahl im Byte-Bereich eingeben.");
            }
        }
    }

    public static short readShort(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                String input = scanner.nextLine();
                input = StripEntry.clean(input);
                short value = Short.parseShort(input);
                ShortValidator.validate(value);
                return value;
            } catch (ShortException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe, bitte eine kurze Zahl eingeben.");
            }
        }
    }

    public static long readLong(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                String input = scanner.nextLine();
                input = StripEntry.clean(input);
                long value = Long.parseLong(input);
                LongValidator.validate(value);
                return value;
            } catch (LongException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe, bitte eine lange Zahl eingeben.");
            }
        }
    }

    public static float readFloat(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                String input = scanner.nextLine();
                input = StripEntry.clean(input);
                float value = Float.parseFloat(input);
                FloatValidator.validate(value);
                return value;
            } catch (FloatException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe, bitte eine Fließkommazahl eingeben.");
            }
        }
    }

    public static boolean readBoolean(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                String input = scanner.nextLine();
                input = StripEntry.clean(input);
                boolean value = Boolean.parseBoolean(input);
                BooleanValidator.validate(value);
                return value;
            } catch (BooleanException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static BigInteger readBigInteger(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                String input = scanner.nextLine();
                input = StripEntry.clean(input);
                BigInteger value = new BigInteger(input);
                BigIntegerValidator.validate(value);
                return value;
            } catch (BigIntegerException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe, bitte eine ganze Zahl eingeben.");
            }
        }
    }

    public static BigDecimal readBigDecimal(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                String input = scanner.nextLine();
                input = StripEntry.clean(input);
                input = input.replace(',', '.');
                BigDecimal value = new BigDecimal(input);
                BigDecimalValidator.validate(value);
                return value;
            } catch (BigDecimalException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe, bitte eine Dezimalzahl eingeben.");
            }
        }
    }
}
