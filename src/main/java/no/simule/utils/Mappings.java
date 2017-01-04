/*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package no.simule.utils;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

public class Mappings implements Serializable {
    public static final long serialVersionUID = 1L;
    public transient static final Logger logger = Logger.getLogger(Mappings.class);

    public static String add = "add";
    public static String mul = "mul";
    public static String sub = "sub";
    public static String devide = "devide";

    public static String mod = "mod";
    public static String div = "div";
    public static String max = "max";
    public static String min = "min";

    public static String floor = "floor";
    public static String round = "round";
    public static String abs = "abs";
    public static String toString = "toString";

    public static String concat = "concat";
    public static String subString = "substring";
    public static String toLower = "toLower";
    public static String toUpper = "toUpper";
    public static String size = "size";
    public static String greater = "greater than";
    public static String less = "less than";
    public static String equal = "equal to";
    public static String notEqual = "notEqual";
    public static String greaterEqual = "greaterEqual";
    public static String lessEqual = "lessEqual";
    public static String forAll = "forAll";
    public static String select = "select";
    public static String reject = "reject";
    public static String isUnique = "isUnique";
    public static String collect = "collect";
    public static String exists = "exists";
    public static String emptyList = "isEmpty";
    public static String notEmptyList = "notEmpty";
    public static String listSize = "size";
    public static String setFirstItem = "first in OrderSet ";
    public static String setLastItem = "last in OrderSet";
    public static String sequenceFirstItem = "first in Sequence";
    public static String sequenceLastItem = "last in sequence";
    public static String setConversion = "asSet";
    public static String orderSetConversion = "asOrderSet";
    public static String sequenceConversion = "asSequence";
    public static String bagConversion = "asBag";


    public static String firstItem = "first";
    public static String lastItem = "last";

    public static String reverseOrderSet = "reverse OrderSet";
    public static String reverseSequence = "reverse Sequence";

    public static String flattenConversion = "asflatten";
    public static String union = "union";
    public static String intersection = "Intersection";
    public static String including = "intersection";
    public static String excluding = "excluding";
    public static String includes = "includes";
    public static String excludes = "excludes";
    public static String includesAll = "includesAll";
    public static String excludesAll = "excludesAll";
    public static String closure = "closure";
    public static String toInteger = "toInteger";
    public static String toReal = "toReal";
    public static String toBoolean = "toBoolean";
    public static String indexOf = "indexOf";
    public static String equalsIgnoreCase = "equalsIgnoreCase";
    public static String at = "at";
    public static String listCount = "count";
    public static String any = "any";
    public static String sumOfCollection = "sum";
    public static String minofCollection = "min";
    public static String maxOfCollection = "max";

    private static Mappings mappings = null;

    public static void initialize() {
        if (mappings == null) {
            mappings = new Mappings();
        }
        Properties prop = new Properties();
        Properties prop2 = new Properties();
        InputStream input = null;
        InputStream input2 = null;
        try {
            input = mappings.getClass().getClassLoader().getResourceAsStream("mappings.properties");
            prop.load(input);
            input2 = mappings.getClass().getClassLoader().getResourceAsStream("defaultMappings.properties");
            prop2.load(input2);

            Enumeration keys = prop.propertyNames();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = prop.getProperty(key);

                if (value.equals("none")) {
                    value = prop2.getProperty(key);
                }

                try {
                    Field field = mappings.getClass().getField(key);
                    field.set(null, value);
                    logger.debug("Mapping for " + key + " is " + value);
                } catch (NoSuchFieldException e) {
                    logger.error(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    logger.error(e.getMessage(), e);
                }
            }


        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (NullPointerException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }


    public static HashMap<String, String> getIntegerOperations() {
        HashMap<String, String> operations = new HashMap<>();
        operations.put(greater, ">");
        operations.put(less, "<");
        operations.put(equal, "=");
        operations.put(notEqual, "<>");
        operations.put(greaterEqual, ">=");
        operations.put(lessEqual, "<=");
        return operations;
    }

    public static HashMap<String, String> getIntegerArithmeticOperations() {
        HashMap<String, String> operations = new HashMap<>();

        operations.put(mod, "mod");
        operations.put(div, "div");
        operations.put(max, "max");
        operations.put(min, "min");

        operations.put(add, "+");
        operations.put(mul, "*");
        operations.put(sub, "-");
        operations.put(devide, "/");

        operations.put(floor, "floor");
        operations.put(round, "round");
        operations.put(abs, "abs");
        operations.put(toString, "toString");
        return operations;
    }

    public static HashMap<String, String> getRealOperations() {
        HashMap<String, String> operations = new HashMap<>();
        operations.put(greater, ">");
        operations.put(less, "<");
        operations.put(equal, "=");
        operations.put(notEqual, "<>");
        operations.put(greaterEqual, ">=");
        operations.put(lessEqual, "<=");
        return operations;
    }

    public static HashMap<String, String> getRealArithmeticOperations() {
        HashMap<String, String> operations = new HashMap<>();
        operations.put(max, "max");
        operations.put(min, "min");

        operations.put(add, "+");
        operations.put(mul, "*");
        operations.put(sub, "-");
        operations.put(devide, "/");

        operations.put(floor, "floor");
        operations.put(round, "round");
        operations.put(abs, "abs");

        operations.put(toString, "toString");
        return operations;
    }

    public static HashMap<String, String> getUnlimitedNaturalOperations() {
        HashMap<String, String> operations = new HashMap<>();
        operations.put(greater, ">");
        operations.put(less, "<");
        operations.put(equal, "=");
        operations.put(notEqual, "<>");
        operations.put(greaterEqual, ">=");
        operations.put(lessEqual, "<=");
        return operations;
    }

    public static HashMap<String, String> getUnlimitedNaturalArithmeticOperations() {
        HashMap<String, String> operations = new HashMap<>();
        operations.put(mod, "mod");
        operations.put(div, "div");
        operations.put(max, "max");
        operations.put(min, "min");

        operations.put(add, "+");
        operations.put(mul, "*");
        operations.put(sub, "-");
        operations.put(devide, "/");

        operations.put(floor, "floor");
        operations.put(round, "round");
        operations.put(abs, "abs");
        operations.put(toString, "toString");
        operations.put(toInteger, "toInteger");
        return operations;
    }

    public static HashMap<String, String> getOclInvalidOperations() {
        HashMap<String, String> operations = new HashMap<>();
        operations.put(equal, "=");
        operations.put(notEqual, "<>");
        return operations;
    }

    public static HashMap<String, String> getOclVoidOperations() {
        HashMap<String, String> operations = new HashMap<>();
        operations.put(equal, "=");
        operations.put(notEqual, "<>");
        return operations;
    }


    public static HashMap<String, String> getObjectOperations() {
        HashMap<String, String> operations = new HashMap<>();
        operations.put(equal, "=");
        operations.put(notEqual, "<>");
        return operations;
    }

    public static HashMap<String, String> getEnumOperations() {
        HashMap<String, String> operations = new HashMap<>();
        operations.put(equal, "=");
        operations.put(notEqual, "<>");
        return operations;
    }

    public static HashMap<String, String> getStringOperations() {
        HashMap<String, String> operations = new HashMap<>();
        operations.put(equal, "=");
        operations.put(notEqual, "<>");
        operations.put(greater, ">");
        operations.put(less, "<");
        return operations;
    }


    public static HashMap<String, String> getStringArithmeticOperations() {
        HashMap<String, String> operations = new HashMap<>();
        operations.put(concat, "concat");
        operations.put(subString, "subString");
        operations.put(toLower, "toLower");
        operations.put(toUpper, "toUpper");
        operations.put(toInteger, "toInteger");
        operations.put(toReal, "toReal");
        operations.put(toBoolean, "toBoolean");
        operations.put(indexOf, "indexOf");
        operations.put(equalsIgnoreCase, "equalsIgnoreCase");
        operations.put(at, "at");
        operations.put(size, "size");
        return operations;
    }

    public static HashMap<String, String> getBooleanOperations() {
        HashMap<String, String> operations = new HashMap<>();
        operations.put(equal, "=");
        operations.put(notEqual, "<>");
        operations.put("and", "and");
        operations.put("or", "or");
        operations.put("not", "not");
        operations.put("xor", "xor");
        operations.put("implies", "implies");

        return operations;
    }

    public static HashMap<String, String> getBooleanArithmeticOperations() {
        HashMap<String, String> operations = new HashMap<>();
        operations.put(toString, "toString");
        return operations;
    }

    public static HashMap<String, String> getArithmeticOperationsType() {
        HashMap<String, String> operations = new HashMap<>();

        operations.put(mod, iOCLTypes.objectType);
        operations.put(div, iOCLTypes.objectType);
        operations.put(max, iOCLTypes.objectType);
        operations.put(min, iOCLTypes.objectType);

        operations.put(add, iOCLTypes.primitiveType);
        operations.put(mul, iOCLTypes.primitiveType);
        operations.put(sub, iOCLTypes.primitiveType);
        operations.put(devide, iOCLTypes.primitiveType);

        operations.put(floor, iOCLTypes.objectType);
        operations.put(round, iOCLTypes.objectType);
        operations.put(abs, iOCLTypes.objectType);
        operations.put(toString, iOCLTypes.objectType);

        operations.put(concat, iOCLTypes.objectType);
        operations.put(subString, iOCLTypes.objectType);
        operations.put(toInteger, iOCLTypes.objectType);
        operations.put(toReal, iOCLTypes.objectType);
        operations.put(toBoolean, iOCLTypes.objectType);
        operations.put(indexOf, iOCLTypes.objectType);
        operations.put(equalsIgnoreCase, iOCLTypes.objectType);
        operations.put(at, iOCLTypes.objectType);
        operations.put(size, iOCLTypes.objectType);
        return operations;
    }


    public static HashMap<String, List<String>> getArithmeticOperationsArguments() {

        HashMap<String, List<String>> operations = new HashMap<>();
        operations.put(mod, new ArrayList<String>(Arrays.asList(new String[]{iOCLTypes.Integer})));
        operations.put(div, new ArrayList<String>(Arrays.asList(new String[]{iOCLTypes.Integer})));
        operations.put(max, new ArrayList<String>(Arrays.asList(new String[]{iOCLTypes.Integer})));
        operations.put(min, new ArrayList<String>(Arrays.asList(new String[]{iOCLTypes.Integer})));

        operations.put(add, new ArrayList<String>(Arrays.asList(new String[]{iOCLTypes.Integer})));
        operations.put(mul, new ArrayList<String>(Arrays.asList(new String[]{iOCLTypes.Integer})));
        operations.put(sub, new ArrayList<String>(Arrays.asList(new String[]{iOCLTypes.Integer})));
        operations.put(devide, new ArrayList<String>(Arrays.asList(new String[]{iOCLTypes.Integer})));

        operations.put(floor, new ArrayList<String>());
        operations.put(round, new ArrayList<String>());
        operations.put(abs, new ArrayList<String>());
        operations.put(toString, new ArrayList<String>());

        operations.put(toLower, new ArrayList<String>());
        operations.put(toUpper, new ArrayList<String>());
        operations.put(toInteger, new ArrayList<String>());
        operations.put(toReal, new ArrayList<String>());
        operations.put(toBoolean, new ArrayList<String>());
        operations.put(indexOf, new ArrayList<String>(Arrays.asList(new String[]{iOCLTypes.String})));
        operations.put(equalsIgnoreCase,
                new ArrayList<String>(Arrays.asList(new String[]{iOCLTypes.String})));
        operations.put(at, new ArrayList<String>(Arrays.asList(new String[]{iOCLTypes.Integer})));
        operations.put(concat, new ArrayList<String>(Arrays.asList(new String[]{iOCLTypes.String})));
        operations.put(subString,
                new ArrayList<String>(Arrays.asList(new String[]{iOCLTypes.Integer, iOCLTypes.Integer})));

        operations.put(size, new ArrayList<String>());
        return operations;

    }

    public static HashMap<String, String> getArithmeticOperationsReturn() {

        HashMap<String, String> operations = new HashMap<>();

        operations.put(mod, iOCLTypes.Integer);
        operations.put(div, iOCLTypes.Integer);
        operations.put(max, iOCLTypes.Integer);
        operations.put(min, iOCLTypes.Integer);

        operations.put(add, iOCLTypes.Integer);
        operations.put(mul, iOCLTypes.Integer);
        operations.put(sub, iOCLTypes.Integer);
        operations.put(devide, iOCLTypes.Integer);

        operations.put(floor, iOCLTypes.Integer);
        operations.put(round, iOCLTypes.Integer);
        operations.put(abs, iOCLTypes.Integer);
        operations.put(toString, iOCLTypes.String);


        operations.put(toLower, iOCLTypes.String);
        operations.put(toUpper, iOCLTypes.String);
        operations.put(toInteger, iOCLTypes.Integer);
        operations.put(toReal, iOCLTypes.Real);
        operations.put(toBoolean, iOCLTypes.Boolean);
        operations.put(indexOf, iOCLTypes.Integer);
        operations.put(equalsIgnoreCase, iOCLTypes.Boolean);
        operations.put(at, Keywords.SELF);
        operations.put(concat, iOCLTypes.String);
        operations.put(subString, iOCLTypes.String);
        operations.put(size, iOCLTypes.Integer);


        return operations;

    }


    public static HashMap<String, String> getCollectionOperations() {
        HashMap<String, String> operations = new HashMap<>();
        operations.put(forAll, "forAll");
        operations.put(select, "select");
        operations.put(reject, "reject");
        operations.put(exists, "exists");
        operations.put(isUnique, "isUnique");

        operations.put(closure, "closure");

        operations.put(includes, "includes");
        operations.put(excludes, "excludes");
        operations.put(includesAll, "includesAll");
        operations.put(excludesAll, "excludesAll");

        operations.put(union, "union");
        operations.put(intersection, "intersection");
        operations.put(including, "including");
        operations.put(excluding, "excluding");

        operations.put(collect, "collect");


        operations.put(setConversion, "asSet");
        operations.put(orderSetConversion, "asOrderedSet");
        operations.put(sequenceConversion, "asSequence");
        operations.put(bagConversion, "asBag");
        operations.put(flattenConversion, "flatten");

        operations.put(any, "any");

        operations.put(listSize, "size");
        operations.put(listCount, "count");

        operations.put(emptyList, "isEmpty");
        operations.put(notEmptyList, "notEmpty");


        operations.put(sequenceFirstItem, "asSequence()->first");
        operations.put(sequenceLastItem, "asSequence()->last");

        operations.put(setFirstItem, "asOrderedSet()->first");
        operations.put(setLastItem, "asOrderedSet()->last");


        operations.put(firstItem, "first");
        operations.put(lastItem, "last");

        operations.put(reverseOrderSet, "asOrderedSet()->reverse");
        operations.put(reverseSequence, "asSequence()->reverse");


        operations.put(sumOfCollection, "sum");
        operations.put(minofCollection, "min");
        operations.put(maxOfCollection, "max");

        return operations;
    }

    //    reverse sequence
//    sortedBy
    public static HashMap<String, String> getCollectionItreateReturn() {
        HashMap<String, String> operations = new HashMap<>();
        operations.put(forAll, "close");
        operations.put(exists, "close");
        operations.put(isUnique, "close");
        operations.put(select, "return");
        operations.put(reject, "return");
        operations.put(any, "return");
        operations.put(collect, "newList");
        return operations;
    }


    public static HashMap<String, Boolean> getCollectionSingleProperty() {
        HashMap<String, Boolean> operations = new HashMap<>();
        operations.put(collect, true);
        return operations;
    }

    public static HashMap<String, Boolean> getCollectionInput() {
        HashMap<String, Boolean> operations = new HashMap<>();

        operations.put(closure, true);

        operations.put(includes, true);
        operations.put(excludes, true);
        operations.put(includesAll, true);
        operations.put(excludesAll, true);

        operations.put(union, true);
        operations.put(intersection, true);
        operations.put(including, true);
        operations.put(excluding, true);


        return operations;
    }


    public static HashMap<String, String> getCollectionInputReturn() {
        HashMap<String, String> operations = new HashMap<>();
        operations.put(union, iOCLTypes.List);
        operations.put(intersection, iOCLTypes.List);
        operations.put(including, iOCLTypes.List);
        operations.put(excluding, iOCLTypes.List);
        return operations;
    }

    public static HashMap<String, Boolean> getCollectionNotItreate() {
        HashMap<String, Boolean> operations = new HashMap<>();
        operations.put(setConversion, true);
        operations.put(orderSetConversion, true);
        operations.put(sequenceConversion, true);
        operations.put(bagConversion, true);

        operations.put(flattenConversion, true);
        operations.put(listSize, true);
        operations.put(listCount, true);
        operations.put(emptyList, true);
        operations.put(notEmptyList, true);
        operations.put(setFirstItem, true);
        operations.put(setLastItem, true);
        operations.put(sequenceFirstItem, true);
        operations.put(sequenceLastItem, true);

        operations.put(firstItem, true);
        operations.put(lastItem, true);

        operations.put(reverseOrderSet, true);
        operations.put(reverseSequence, true);

        operations.put(sumOfCollection, true);
        operations.put(minofCollection, true);
        operations.put(maxOfCollection, true);

        return operations;
    }

    public static HashMap<String, String> getCollectionNotItreateType() {
        HashMap<String, String> operations = new HashMap<>();
        operations.put(listSize, iOCLTypes.Integer);
        operations.put(listCount, iOCLTypes.Integer);
        operations.put(emptyList, iOCLTypes.Boolean);
        operations.put(notEmptyList, iOCLTypes.Boolean);

        operations.put(setFirstItem, iOCLTypes.Any);
        operations.put(setLastItem, iOCLTypes.Any);

        operations.put(sequenceFirstItem, iOCLTypes.Any);
        operations.put(sequenceLastItem, iOCLTypes.Any);

        operations.put(firstItem, iOCLTypes.Any);
        operations.put(lastItem, iOCLTypes.Any);

        operations.put(reverseOrderSet, iOCLTypes.List);
        operations.put(reverseSequence, iOCLTypes.List);

        operations.put(setConversion, iOCLTypes.List);
        operations.put(orderSetConversion, iOCLTypes.List);
        operations.put(sequenceConversion, iOCLTypes.List);
        operations.put(bagConversion, iOCLTypes.List);
        operations.put(flattenConversion, iOCLTypes.List);

        operations.put(sumOfCollection, iOCLTypes.Integer);
        operations.put(minofCollection, iOCLTypes.Integer);
        operations.put(maxOfCollection, iOCLTypes.Integer);

        return operations;
    }


}
