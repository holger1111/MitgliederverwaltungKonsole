package Validator;

import Exception.StringException;
import Exception.RolleException;

import java.util.List;
import java.util.Arrays;

public class RolleValidator extends StringValidator {

    private static final List<String> ERLAUBTE_ROLLEN = Arrays.asList(
        "ADMIN",
        "MODERATOR",
        "USER",
        "GUEST"
    );

    /**
     * Validiert eine Benutzerrolle.
     * 
     * @param obj Die zu prüfende Rolle (als String)
     * @throws StringException Wenn die Rolle null oder leer ist
     * @throws RolleException Wenn die Rolle nicht erlaubt ist
     */
    @Override
    public void validate(String obj) throws StringException {
        errors.clear();
        if (obj == null || obj.trim().isEmpty()) {
            String msg = "Rolle darf nicht leer sein.";
            addError(msg);
            throw new StringException(msg);
        }
        if (!ERLAUBTE_ROLLEN.contains(obj.trim().toUpperCase())) {
            String msg = "Rolle '" + obj + "' ist nicht erlaubt.";
            addError(msg);
            throw new StringException(msg);
        }
    }

    /**
     * Optional: Validierung mit spezieller RolleException.
     */
    public void validateStrict(String obj) throws RolleException, StringException {
        errors.clear();
        if (obj == null || obj.trim().isEmpty()) {
            throw new StringException("Rolle darf nicht leer sein.");
        }
        if (!ERLAUBTE_ROLLEN.contains(obj.trim().toUpperCase())) {
            throw new RolleException("Rolle '" + obj + "' ist nicht erlaubt.");
        }
    }
}
