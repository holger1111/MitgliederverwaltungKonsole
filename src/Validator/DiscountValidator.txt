package Validator;

import Exception.DiscountException;

public class DiscountValidator extends NumericValidator<Double, DiscountException> {

    private final double basePrice;

    public DiscountValidator(double basePrice) {
        super();
        this.basePrice = basePrice;
    }

    @Override
    public void validate(Double discount) throws DiscountException {
        errors.clear();
        if (discount == null) {
            String msg = "Rabatt darf nicht null sein.";
            addError(msg);
            throw new DiscountException(msg);
        }
        if (discount < 0) {
            String msg = "Rabatt darf nicht negativ sein.";
            addError(msg);
            throw new DiscountException(msg);
        }
        if (discount > basePrice) {
            String msg = "Rabatt darf nicht größer als der Grundpreis (" + basePrice + ") sein.";
            addError(msg);
            throw new DiscountException(msg);
        }
    }

    public double getBasePrice() {
        return basePrice;
    }
}
