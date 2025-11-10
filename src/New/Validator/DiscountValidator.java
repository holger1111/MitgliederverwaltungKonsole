package New.Validator;

import New.Objekte.MitgliederVertrag;
import New.Objekte.Vertrag;
import New.Exception.DiscountException;

public class DiscountValidator extends BaseValidator<MitgliederVertrag> {

    private Vertrag vertrag;

    public DiscountValidator(Vertrag vertrag) {
        this.vertrag = vertrag;
    }

    @Override
    public void validate(MitgliederVertrag mv) throws Exception {
        Double rabatt = mv.getPreisrabatt();
        Double grundpreis = vertrag.getGrundpreis();
        if (rabatt == null) rabatt = 0.0;
//        if (grundpreis == null) grundpreis = 0.0;
        if (rabatt > grundpreis) {
            String msg = "Der Rabatt (" + rabatt + ") ist größer als der Grundpreis (" + grundpreis + ")!";
            errors.add(msg);
            throw new DiscountException(msg);
        }
    }
}
