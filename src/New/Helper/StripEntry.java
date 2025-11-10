package New.Helper;

import java.util.regex.Pattern;

public class StripEntry {

    private static final Pattern DANGEROUS_SQL_PATTERN = Pattern.compile("(['\";-]|(\\b(SELECT|UPDATE|DELETE|INSERT|WHERE|DROP|SHOW|TRUNCATE|ALTER)\\b))", Pattern.CASE_INSENSITIVE);


    /**
     * Entfernt alle Whitespace-Zeichen am Anfang und Ende,
     * ersetzt doppelte/triple Spaces und prüft auf SQL-Injections/Gefahren
     * @param input Der zu säubernde String
     * @return der bereinigte String, mögliche gefährliche Inhalte werden ersetzt
     */
    public static String clean(String input) {
        if (input == null) return null;
        String result = input.trim();
        result = result.replaceAll("\\s{2,}", " ");
        result = DANGEROUS_SQL_PATTERN.matcher(result).replaceAll("");
        result = result.replaceAll("[\\x00\\x08\\x09\\x1a\\n\\r\\'\\\"\\\\]", "");
        return result;
    }
}
