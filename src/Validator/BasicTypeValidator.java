package Validator;

import Exception.StringException;
import Exception.IntException;
import Exception.BooleanException;
import Exception.DateException;
import Exception.TimeException;
import Exception.DiscountException;
import Exception.DataIsNullException;

import java.math.BigInteger;
import java.math.BigDecimal;

public class BasicTypeValidator extends BaseValidator<Object> {

    @Override
    public void validate(Object obj) throws Exception, StringException, IntException, DateException, TimeException, DiscountException, BooleanException {
        errors.clear();
        if (obj == null) {
            errors.add("Eingabe ist null");
            throw new DataIsNullException("Eingabe ist null");
        }

        if (obj instanceof String) {
            validateString(obj);
        } else if (obj instanceof Integer) {
            validateInt(obj);
        } else if (obj instanceof Boolean) {
            validateBoolean(obj);
        } else if (obj instanceof Long) {
            validateLong(obj);
        } else if (obj instanceof Double) {
            validateDouble(obj);
        } else if (obj instanceof Float) {
            validateFloat(obj);
        } else if (obj instanceof Short) {
            validateShort(obj);
        } else if (obj instanceof Byte) {
            validateByte(obj);
        } else if (obj instanceof Character) {
            validateCharacter(obj);
        } else if (obj instanceof BigInteger) {
            validateBigInteger(obj);
        } else if (obj instanceof BigDecimal) {
            validateBigDecimal(obj);
        } else {
            String msg = "Unbekannter Typ: " + obj.getClass().getName();
            errors.add(msg);
            throw new Exception(msg);
        }
    }

    public static void validateString(Object obj) throws StringException {
        if (!(obj instanceof String)) {
            throw new StringException("Eingabe ist kein String.");
        }
    }

    public static void validateInt(Object obj) throws IntException {
        if (!(obj instanceof Integer)) {
            throw new IntException("Eingabe ist keine ganze Zahl.");
        }
    }

    public static void validateBoolean(Object obj) throws BooleanException {
        if (!(obj instanceof Boolean)) {
            throw new BooleanException("Eingabe ist kein Boolean (true/false).");
        }
    }

    public static void validateLong(Object obj) throws IntException {
        if (!(obj instanceof Long)) {
            throw new IntException("Eingabe ist kein Long.");
        }
    }

    public static void validateDouble(Object obj) throws IntException {
        if (!(obj instanceof Double)) {
            throw new IntException("Eingabe ist kein Double.");
        }
    }

    public static void validateFloat(Object obj) throws IntException {
        if (!(obj instanceof Float)) {
            throw new IntException("Eingabe ist kein Float.");
        }
    }

    public static void validateShort(Object obj) throws IntException {
        if (!(obj instanceof Short)) {
            throw new IntException("Eingabe ist kein Short.");
        }
    }

    public static void validateByte(Object obj) throws IntException {
        if (!(obj instanceof Byte)) {
            throw new IntException("Eingabe ist kein Byte.");
        }
    }

    public static void validateCharacter(Object obj) throws StringException {
        if (!(obj instanceof Character)) {
            throw new StringException("Eingabe ist kein Character.");
        }
    }

    public static void validateBigInteger(Object obj) throws IntException {
        if (!(obj instanceof BigInteger)) {
            throw new IntException("Eingabe ist keine BigInteger-Zahl.");
        }
    }

    public static void validateBigDecimal(Object obj) throws IntException {
        if (!(obj instanceof BigDecimal)) {
            throw new IntException("Eingabe ist keine BigDecimal-Zahl.");
        }
    }
}
