package Validator;

import Exception.DateException;

public class DateValidator extends BaseValidator<String> {

	@Override
	public void validate(String obj) throws DateException {
		errors.clear();
		if (obj == null) {
			String msg = "Eingabe ist null.";
			errors.add(msg);
			throw new DateException(msg);
		}
		String dateStr = obj.trim();

		// Überprüfen, ob das Format dd.mm.yyyy eingehalten wird (z.B. 2 Ziffern, Punkt,
		// 2 Ziffern, Punkt, 4 Ziffern)
		if (!dateStr.matches("^\\d{2}\\.\\d{2}\\.\\d{4}$")) {
			String msg = "Datum muss im Format dd.mm.yyyy sein.";
			errors.add(msg);
			throw new DateException(msg);
		}

	}

}
