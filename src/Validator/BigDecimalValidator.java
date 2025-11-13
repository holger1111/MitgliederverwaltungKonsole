package Validator;

import Exception.BigDecimalException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class BigDecimalValidator extends NumericValidator<BigDecimal> {

    public BigDecimalValidator() {
        super();
        errors = new ArrayList<>();
    }

    @Override
    public void validate(BigDecimal value) throws BigDecimalException {
        errors.clear();
        if (value == null) {
            String msg = "BigDecimal-Wert darf nicht null sein.";
            errors.add(msg);
            throw new BigDecimalException(msg);
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            String msg = "BigDecimal-Wert darf nicht negativ sein.";
            errors.add(msg);
            throw new BigDecimalException(msg);
        }
    }
}
