//package OUTDATED;
//
//import Exception.StringException;
//import Validator.BaseValidator;
//import Exception.IntException;
//import Exception.LongException;
//import Exception.ShortException;
//import Exception.BasicTypeException;
//import Exception.BigDecimalException;
//import Exception.BigIntegerException;
//import Exception.BooleanException;
//import Exception.ByteException;
//import Exception.DataIsNullException;
//import Exception.DoubleException;
//import Exception.FloatException;
//
//import java.math.BigInteger;
//import java.math.BigDecimal;
//
//public class OUT_BasicTypeValidator extends BaseValidator<Object> {
//
//    @Override
//    public void validate(Object obj) throws Exception, BasicTypeException {
//        errors.clear();
//        if (obj == null) {
//            errors.add("Eingabe ist null");
//            throw new DataIsNullException("Eingabe ist null");
//        }
//
//        if (obj instanceof String) {
//            validateString(obj);
//        } else if (obj instanceof Integer) {
//            validateInt(obj);
//        } else if (obj instanceof Boolean) {
//            validateBoolean(obj);
//        } else if (obj instanceof Long) {
//            validateLong(obj);
//        } else if (obj instanceof Double) {
//            validateDouble(obj);
//        } else if (obj instanceof Float) {
//            validateFloat(obj);
//        } else if (obj instanceof Short) {
//            validateShort(obj);
//        } else if (obj instanceof Byte) {
//            validateByte(obj);
//        } else if (obj instanceof Character) {
//            validateCharacter(obj);
//        } else if (obj instanceof BigInteger) {
//            validateBigInteger(obj);
//        } else if (obj instanceof BigDecimal) {
//            validateBigDecimal(obj);
//        } else {
//            String msg = "Unbekannter Typ: " + obj.getClass().getName();
//            errors.add(msg);
//            throw new Exception(msg);
//        }
//    }
//
//    public static void validateString(Object obj) throws StringException {
//        if (!(obj instanceof String)) {
//            throw new StringException("Eingabe ist kein String.");
//        }
//    }
//
//    public static void validateInt(Object obj) throws IntException {
//        if (!(obj instanceof Integer)) {
//            throw new IntException("Eingabe ist keine ganze Zahl.");
//        }
//    }
//
//    public static void validateBoolean(Object obj) throws BooleanException {
//        if (!(obj instanceof Boolean)) {
//            throw new BooleanException("Eingabe ist kein Boolean (true/false).");
//        }
//    }
//
//    public static void validateLong(Object obj) throws LongException {
//        if (!(obj instanceof Long)) {
//            throw new LongException("Eingabe ist kein Long.");
//        }
//    }
//
//    public static void validateDouble(Object obj) throws DoubleException {
//        if (!(obj instanceof Double)) {
//            throw new DoubleException("Eingabe ist kein Double.");
//        }
//    }
//
//    public static void validateFloat(Object obj) throws FloatException {
//        if (!(obj instanceof Float)) {
//            throw new FloatException("Eingabe ist kein Float.");
//        }
//    }
//
//    public static void validateShort(Object obj) throws ShortException {
//        if (!(obj instanceof Short)) {
//            throw new ShortException("Eingabe ist kein Short.");
//        }
//    }
//
//    public static void validateByte(Object obj) throws ByteException {
//        if (!(obj instanceof Byte)) {
//            throw new ByteException("Eingabe ist kein Byte.");
//        }
//    }
//
//    public static void validateCharacter(Object obj) throws OUT_CharException  {
//        if (!(obj instanceof Character)) {
//            throw new OUT_CharException ("Eingabe ist kein Character.");
//        }
//    }
//
//    public static void validateBigInteger(Object obj) throws BigIntegerException {
//        if (!(obj instanceof BigInteger)) {
//            throw new BigIntegerException("Eingabe ist keine BigInteger-Zahl.");
//        }
//    }
//
//    public static void validateBigDecimal(Object obj) throws BigDecimalException {
//        if (!(obj instanceof BigDecimal)) {
//            throw new BigDecimalException("Eingabe ist keine BigDecimal-Zahl.");
//        }
//    }
//}
