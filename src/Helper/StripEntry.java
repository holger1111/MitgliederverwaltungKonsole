package Helper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Hilfsklasse zur Bereinigung und Basisschutz von Benutzereingaben.
 * Einsatz vorwiegend als zusätzliche Schutzschicht neben PreparedStatements.
 */
public class StripEntry {

    // Konfigurierbare Liste der zu filternden SQL-Wörter/gefährlichen Patterns (Groß-/Kleinschreibung ignoriert)
    private static final Set<String> DANGEROUS_KEYWORDS = new HashSet<>(Arrays.asList(
            "SELECT", "UPDATE", "DELETE", "INSERT",
            "WHERE", "DROP", "SHOW", "TRUNCATE", "ALTER"
    ));

    // Vorcompilierte Pattern für mehrfaches Whitespace
    private static final Pattern MULTIPLE_SPACES = Pattern.compile("\\s+");

    // Unicode Steuerzeichen (außer Tab, LF, CR)
    private static final Pattern CONTROL_CHARS = Pattern.compile("[\\p{Cntrl}&&[^\r\n\t]]");

    /**
     * Bereinigt den Eingabestring:
     * - Entfernt Steuerzeichen (außer CR, LF, Tab)
     * - Ersetzt alle mehrfachen Whitespaces durch einmaliges Leerzeichen
     * - Trimmt führende und abschließende Whitespaces
     * - Entfernt gefährliche SQL-Zeichenketten (schützt zusätzlich zu PreparedStatements)
     *
     * @param input der zu bereinigende Eingabestring
     * @return bereinigter String
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }

        // Steuerzeichen entfernen
        String sanitized = CONTROL_CHARS.matcher(input).replaceAll("");

        // Mehrere Whitespaces durch einen ersetzen
        sanitized = MULTIPLE_SPACES.matcher(sanitized).replaceAll(" ");

        // Trim
        sanitized = sanitized.trim();

        // Gefährliche SQL-Schlüsselwörter entfernen (Groß-/Kleinschreibung ignorieren)
        for (String keyword : DANGEROUS_KEYWORDS) {
            sanitized = sanitized.replaceAll("(?i)\\b" + Pattern.quote(keyword) + "\\b", "");
        }

        return sanitized;
    }
}
