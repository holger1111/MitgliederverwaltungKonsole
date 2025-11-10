package New.Validator;

import New.Exception.CurrencyException;

public class CurrencyValidator extends BaseValidator<Object> {

    private double result;

    @Override
    public void validate(Object value) throws Exception {
        Double doubleValue = null;

        if (value instanceof Double) {
            doubleValue = (Double) value;
        } else if (value instanceof Number) {
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
        doubleValue = Math.floor(doubleValue * 100) / 100.0;
        this.result = doubleValue;
    }

    public double getValidatedValue() {
        return result;
    }
}
