package Validator;

import java.util.ArrayList;

/**
 * Abstrakte Basisklasse für numerische Validatoren.
 * @param <T> numerischer Typ (Integer, Double, Long ...)
 * @param <E> Exception-Typ
 */
public abstract class NumericValidator<T extends Number, E extends Exception> extends BaseValidator<T, E> {

    public NumericValidator() {
        errors = new ArrayList<>();
    }

    // validate muss in Subklassen realisiert werden
}
