package Validator;

import Exception.RangeException;
import java.util.ArrayList;

/**
 * Validator, der überprüft, ob ein Wert innerhalb eines festgelegten Bereichs liegt.
 *
 * @param <T> Der Typ, der Comparable implementiert.
 */
public class RangeValidator<T extends Comparable<T>> extends BaseValidator<T> {

    private T minValue;
    private T maxValue;

    /**
     * Konstruktor mit minimalem und maximalem Wert.
     *
     * @param minValue Der minimale zulässige Wert (inklusive).
     * @param maxValue Der maximale zulässige Wert (inklusive).
     */
    public RangeValidator(T minValue, T maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        errors = new ArrayList<>();
    }

    /**
     * Validiert, ob der Wert innerhalb des Bereichs liegt.
     *
     * @param obj Der zu validierende Wert.
     * @throws RangeException Falls obj außerhalb des erlaubten Bereichs liegt.
     */
    @Override
    public void validate(T obj) throws RangeException {
        errors.clear();
        if (obj == null) {
            String msg = "Wert darf nicht null sein.";
            errors.add(msg);
            throw new RangeException(msg);
        }
        if (minValue != null && obj.compareTo(minValue) < 0) {
            String msg = "Wert ist kleiner als das Minimum: " + minValue;
            errors.add(msg);
            throw new RangeException(msg);
        }
        if (maxValue != null && obj.compareTo(maxValue) > 0) {
            String msg = "Wert ist größer als das Maximum: " + maxValue;
            errors.add(msg);
            throw new RangeException(msg);
        }
    }

    public T getMinValue() {
        return minValue;
    }

    public void setMinValue(T minValue) {
        this.minValue = minValue;
    }

    public T getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(T maxValue) {
        this.maxValue = maxValue;
    }
}
