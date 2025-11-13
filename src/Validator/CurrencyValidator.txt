package Validator;

import Exception.CurrencyException;

public class CurrencyValidator extends BasicTypeValidator {

    private double result;

    @Override
    public void validate(Object value) throws Exception, CurrencyException {
        errors.clear();

        Double doubleValue = null;

        // Nutze BasicTypeValidator um Double typische Typen zu prüfen
        if (value instanceof Double || value instanceof Float || value instanceof Integer || value instanceof Long) {
            doubleValue = ((Number) value).doubleValue();
        } else {
            try {
                doubleValue = Double.parseDouble(value.toString().replace(",", "."));
            } catch (Exception e) {
                String msg = "Der Wert '" + value + "' ist keine gültige Währungsangabe.";
                errors.add(msg);
                throw new CurrencyException(msg);
            }
        }
        // Abrunden auf 2 Dezimalstellen
        doubleValue = Math.floor(doubleValue * 100) / 100.0;
        this.result = doubleValue;
    }

    public double getValidatedValue() {
        return result;
    }
}
