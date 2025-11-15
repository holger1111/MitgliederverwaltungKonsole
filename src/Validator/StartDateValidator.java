package Validator;

import Exception.StartDateException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Validator für den Trainingsstart bei Mitgliederverträgen.
 * Der Trainingsstart darf maximal 5 Wochen vor Vertragsbeginn liegen und nicht in der Vergangenheit sein.
 */
public class StartDateValidator extends BaseValidator<LocalDate, StartDateException> {

    private LocalDate contractStartDate;

    public StartDateValidator(LocalDate contractStartDate) {
        super();
        this.contractStartDate = contractStartDate;
    }

    @Override
    public void validate(LocalDate trainingStart) throws StartDateException {
        errors.clear();
        if (trainingStart == null) {
            String msg = "Trainingsstart darf nicht null sein.";
            addError(msg);
            throw new StartDateException(msg);
        }
        if (trainingStart.isBefore(LocalDate.now())) {
            String msg = "Trainingsstart darf nicht in der Vergangenheit liegen.";
            addError(msg);
            throw new StartDateException(msg);
        }
        if (trainingStart.isAfter(contractStartDate)) {
            String msg = "Trainingsstart darf nicht nach Vertragsbeginn liegen.";
            addError(msg);
            throw new StartDateException(msg);
        }
        long weeksBefore = ChronoUnit.WEEKS.between(trainingStart, contractStartDate);
        if (weeksBefore > 5) {
            String msg = "Trainingsstart darf maximal 5 Wochen vor Vertragsbeginn liegen.";
            addError(msg);
            throw new StartDateException(msg);
        }
    }
}
