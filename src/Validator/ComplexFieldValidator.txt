package Validator;

import Exception.StringException;
import Objekte.Mitglieder;
import Objekte.Mitarbeiter;
import Objekte.Ort;

import java.util.regex.Pattern;

public class ComplexFieldValidator {

    private static final Pattern PLZ_PATTERN = Pattern.compile("\\d{5}"); // 5-stellige PLZ

    /**
     * Validiert Adresse von Mitgliedern
     */
    public void validate(Mitglieder mitglied) throws StringException {
        if (mitglied == null) {
            throw new StringException("Mitglied darf nicht null sein.");
        }
        validateStrasseHausnr(mitglied.getStrasse(), mitglied.getHausnr());
        validateOrt(mitglied.getOrt());
    }

    /**
     * Validiert Adresse von Mitarbeitern
     */
    public void validate(Mitarbeiter mitarbeiter) throws StringException {
        if (mitarbeiter == null) {
            throw new StringException("Mitarbeiter darf nicht null sein.");
        }
        validateStrasseHausnr(mitarbeiter.getStraße(), mitarbeiter.getHausnr());
        validateOrt(mitarbeiter.getOrt());
    }

    private void validateStrasseHausnr(String strasse, String hausnr) throws StringException {
        if (strasse == null || strasse.trim().isEmpty()) {
            throw new StringException("Straße darf nicht leer sein.");
        }
        if (strasse.length() > 100) {
            throw new StringException("Straße ist zu lang (maximal 100 Zeichen).");
        }
        if (hausnr == null || hausnr.trim().isEmpty()) {
            throw new StringException("Hausnummer darf nicht leer sein.");
        }
        if (hausnr.length() > 10) {
            throw new StringException("Hausnummer ist zu lang (maximal 10 Zeichen).");
        }
    }

    private void validateOrt(Ort ort) throws StringException {
        if (ort == null) {
            throw new StringException("Ort darf nicht null sein.");
        }
        if (ort.getPLZ() == null || !PLZ_PATTERN.matcher(ort.getPLZ()).matches()) {
            throw new StringException("PLZ ist ungültig. Erwartet wird eine 5-stellige Zahl.");
        }
        if (ort.getOrt() == null || ort.getOrt().trim().isEmpty()) {
            throw new StringException("Ort darf nicht leer sein.");
        }
        if (ort.getOrt().length() > 50) {
            throw new StringException("Ort ist zu lang (maximal 50 Zeichen).");
        }
    }
}
