package Helper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class Formater {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_BOLD = "\u001B[1m";

    // FORMAT DATUM
    public static String formatDatum(LocalDate date) {
        return formatDatum(date, false, "n.a.");
    }

    public static String formatDatum(LocalDate date, boolean useColor, String defaultOnError) {
        if (date == null) return colored(defaultOnError, ANSI_RED, true); // Exceptions immer rot
        try {
            return date.format(DATE_FORMATTER);
        } catch (Exception e) {
            return colored(defaultOnError, ANSI_RED, true); // Exceptions immer rot
        }
    }

    public static LocalDate parseDatum(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // FORMAT ZEIT
    public static String formatZeit(LocalTime time) {
        return formatZeit(time, false, "n.a.");
    }

    public static String formatZeit(LocalTime time, boolean useColor, String defaultOnError) {
        if (time == null) return colored(defaultOnError, ANSI_RED, true); // Exceptions immer rot
        try {
            return time.format(TIME_FORMATTER);
        } catch (Exception e) {
            return colored(defaultOnError, ANSI_RED, true);
        }
    }

    public static LocalTime parseZeit(String timeStr) {
        try {
            return LocalTime.parse(timeStr, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // FORMAT WÄHRUNG
    public static String formatWaehrung(double betrag) {
        return formatWaehrung(BigDecimal.valueOf(betrag), Locale.GERMANY, false, "n.a.");
    }

    public static String formatWaehrung(BigDecimal betrag, Locale locale, boolean useColor, String defaultOnError) {
        if (betrag == null) return colored(defaultOnError, ANSI_RED, true); // Exceptions immer rot
        try {
            NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
            String result = nf.format(betrag);
            return colored(result, ANSI_GREEN, useColor);
        } catch (Exception e) {
            return colored(defaultOnError, ANSI_RED, true);
        }
    }

    public static String formatWaehrung(Timestamp ts, Locale locale, boolean useColor, String defaultOnError) {
        if (ts == null) return colored(defaultOnError, ANSI_RED, true);
        try {
            BigDecimal betrag = new BigDecimal(ts.getTime());
            return formatWaehrung(betrag, locale, useColor, defaultOnError);
        } catch (Exception e) {
            return colored(defaultOnError, ANSI_RED, true);
        }
    }

    // FORMAT ANDERE ZAHLEN

    public static String formatInteger(Integer value) {
        return formatInteger(value, false, "n.a.");
    }

    public static String formatInteger(Integer value, boolean useColor, String defaultOnError) {
        if (value == null) return colored(defaultOnError, ANSI_RED, true); // Exceptions immer rot
        return colored(value.toString(), ANSI_YELLOW, useColor);
    }

    public static String formatLong(Long value) {
        return formatLong(value, false, "n.a.");
    }

    public static String formatLong(Long value, boolean useColor, String defaultOnError) {
        if (value == null) return colored(defaultOnError, ANSI_RED, true);
        return colored(value.toString(), ANSI_YELLOW, useColor);
    }

    public static String formatFloat(Float value) {
        return formatFloat(value, false, "n.a.");
    }

    public static String formatFloat(Float value, boolean useColor, String defaultOnError) {
        if (value == null) return colored(defaultOnError, ANSI_RED, true);
        return colored(String.format("%.2f", value), ANSI_YELLOW, useColor);
    }

    public static String formatDouble(Double value) {
        return formatDouble(value, false, "n.a.");
    }

    public static String formatDouble(Double value, boolean useColor, String defaultOnError) {
        if (value == null) return colored(defaultOnError, ANSI_RED, true);
        return colored(String.format("%.2f", value), ANSI_YELLOW, useColor);
    }

    // TABELLENFORMATIERUNG

    public static String formatTable(List<String> headers, List<List<String>> rows, List<Boolean> rightAlign, boolean useColor) {
        int cols = headers.size();

        List<List<List<String>>> splittedRows = new java.util.ArrayList<>();
        List<List<String>> splittedHeaders = new java.util.ArrayList<>();
        for (String header : headers) {
            splittedHeaders.add(splitLines(header));
        }
        int[] colWidths = new int[cols];
        int[] colHeights = new int[cols];
        for (int c = 0; c < cols; c++) {
            colWidths[c] = maxWidth(splittedHeaders.get(c));
            colHeights[c] = splittedHeaders.get(c).size();
        }
        for (List<String> row : rows) {
            List<List<String>> splittedRow = new java.util.ArrayList<>();
            for (int c = 0; c < cols; c++) {
                String cell = (row.size() > c && row.get(c) != null) ? row.get(c) : "";
                List<String> splitCell = splitLines(cell);
                splittedRow.add(splitCell);
                colWidths[c] = Math.max(colWidths[c], maxWidth(splitCell));
                colHeights[c] = Math.max(colHeights[c], splitCell.size());
            }
            splittedRows.add(splittedRow);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(buildMultiLineRow(splittedHeaders, colWidths, colHeights, rightAlign, useColor, true));
        sb.append(buildSeparator(colWidths, useColor));
        for (List<List<String>> splittedRow : splittedRows) {
            sb.append(buildMultiLineRow(splittedRow, colWidths, colHeights, rightAlign, useColor, false));
        }
        return sb.toString();
    }

    private static List<String> splitLines(String cell) {
        String[] lines = cell.split("\n");
        List<String> result = new java.util.ArrayList<>();
        for (String line : lines) {
            result.add(line);
        }
        return result;
    }

    private static int maxWidth(List<String> lines) {
        int max = 0;
        for (String line : lines) {
            max = Math.max(max, line.length());
        }
        return max;
    }

    private static String buildMultiLineRow(List<List<String>> cells, int[] colWidths, int[] colHeights,
                                            List<Boolean> rightAlign, boolean useColor, boolean isHeader) {
        StringBuilder sb = new StringBuilder();
        int maxHeight = 0;
        for (int i = 0; i < colWidths.length; i++) {
            maxHeight = Math.max(maxHeight, colHeights[i]);
        }
        for (int lineNr = 0; lineNr < maxHeight; lineNr++) {
            sb.append("| ");
            for (int c = 0; c < colWidths.length; c++) {
                List<String> cellLines = cells.get(c);
                String line = (lineNr < cellLines.size()) ? cellLines.get(lineNr) : "";
                if (isHeader) {
                    line = colored(line, ANSI_BLUE + ANSI_BOLD, useColor);
                }
                sb.append(pad(line, colWidths[c], rightAlign.get(c)));
                sb.append(" | ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static String buildSeparator(int[] colWidths, boolean useColor) {
        StringBuilder sb = new StringBuilder();
        sb.append("|");
        for (int width : colWidths) {
            String line = "-".repeat(width + 2);
            if (useColor) {
                line = colored(line, ANSI_BLUE, true);
            }
            sb.append(line);
            sb.append("|");
        }
        sb.append("\n");
        return sb.toString();
    }

    private static String pad(String s, int length, boolean rightAlign) {
        if (s.length() >= length) return s;
        int padLen = length - s.length();
        String padding = " ".repeat(padLen);
        return rightAlign ? padding + s : s + padding;
    }

    private static String colored(String text, String colorCode, boolean useColor) {
        if (useColor) {
            return colorCode + text + ANSI_RESET;
        } else {
            return text;
        }
    }
}
