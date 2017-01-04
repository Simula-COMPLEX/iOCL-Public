package no.simule.loader.extend;

import java.util.HashMap;
import java.util.Map;

public final class OCLTranlateLibraryUtil
/*      */ {

    /* 77 */ private static final Map<String, Integer> operationCodes = new HashMap();

    /* 82 */ static {
        operationCodes.put("+", Integer.valueOf(1));
    /* 83 */
        operationCodes.put("-", Integer.valueOf(2));
    /* 84 */
        operationCodes.put("*", Integer.valueOf(3));
    /* 85 */
        operationCodes.put("/", Integer.valueOf(4));
    /* 86 */
        operationCodes.put("&&", Integer.valueOf(10));
    /* 87 */
        operationCodes.put("!", Integer.valueOf(11));
    /* 88 */
        operationCodes.put("||", Integer.valueOf(12));
    /* 89 */
        operationCodes.put("implies", Integer.valueOf(13));
    /* 90 */
        operationCodes.put("abs", Integer.valueOf(15));
    /* 91 */
        operationCodes.put("div", Integer.valueOf(16));
    /* 92 */
        operationCodes.put("mod", Integer.valueOf(17));
    /* 93 */
        operationCodes.put("max", Integer.valueOf(18));
    /* 94 */
        operationCodes.put("min", Integer.valueOf(19));
    /* 95 */
        operationCodes.put("size", Integer.valueOf(20));
    /* 96 */
        operationCodes.put("concat", Integer.valueOf(21));
    /* 97 */
        operationCodes.put("substring", Integer.valueOf(22));
    /* 98 */
        operationCodes.put("toInteger", Integer.valueOf(23));
    /* 99 */
        operationCodes.put("toReal", Integer.valueOf(24));
    /* 100 */
        operationCodes.put("^", Integer.valueOf(25));
    /* 101 */
        operationCodes.put("floor", Integer.valueOf(26));
    /* 102 */
        operationCodes.put("round", Integer.valueOf(27));
    /* 103 */
        operationCodes.put("toLower", Integer.valueOf(28));
    /* 104 */
        operationCodes.put("toUpper", Integer.valueOf(29));
    /* 105 */
        operationCodes.put("allInstances", Integer.valueOf(40));
    /* 106 */
        operationCodes.put("==", Integer.valueOf(60));
    /* 107 */
        operationCodes.put("!=", Integer.valueOf(61));
    /* 108 */
        operationCodes.put("oclAsType", Integer.valueOf(62));
    /* 109 */
        operationCodes.put("oclIsKindOf", Integer.valueOf(63));
    /* 110 */
        operationCodes.put("oclIsTypeOf", Integer.valueOf(64));
    /* 111 */
        operationCodes.put("oclIsUndefined", Integer.valueOf(65));
    /* 112 */
        operationCodes.put("oclIsInvalid", Integer.valueOf(66));
    /* 113 */
        operationCodes.put("<", Integer.valueOf(67));
    /* 114 */
        operationCodes.put(">", Integer.valueOf(68));
    /* 115 */
        operationCodes.put("<=", Integer.valueOf(69));
    /* 116 */
        operationCodes.put(">=", Integer.valueOf(70));
    /* 117 */
        operationCodes.put("oclIsNew", Integer.valueOf(71));
    /* 118 */
        operationCodes.put("oclIsInState", Integer.valueOf(72));
    /* 119 */
        operationCodes.put("hasReturned", Integer.valueOf(100));
    /* 120 */
        operationCodes.put("result", Integer.valueOf(101));
    /* 121 */
        operationCodes.put("isSignalSent", Integer.valueOf(102));
    /* 122 */
        operationCodes.put("isOperationCall", Integer.valueOf(103));
    /* 123 */
        operationCodes.put("count", Integer.valueOf(140));
    /* 124 */
        operationCodes.put("excludes", Integer.valueOf(141));
    /* 125 */
        operationCodes.put("excludesAll", Integer.valueOf(142));
    /* 126 */
        operationCodes.put("includes", Integer.valueOf(143));
    /* 127 */
        operationCodes.put("includesAll", Integer.valueOf(144));
    /* 128 */
        operationCodes.put("isEmpty", Integer.valueOf(145));
    /* 129 */
        operationCodes.put("notEmpty", Integer.valueOf(146));
    /* 130 */
        operationCodes.put("product", Integer.valueOf(147));
    /* 131 */
        operationCodes.put("sum", Integer.valueOf(148));
    /* 132 */
        operationCodes.put("asBag", Integer.valueOf(149));
    /* 133 */
        operationCodes.put("asOrderedSet", Integer.valueOf(150));
    /* 134 */
        operationCodes.put("asSequence", Integer.valueOf(151));
    /* 135 */
        operationCodes.put("asSet", Integer.valueOf(152));
    /* 136 */
        operationCodes.put("excluding", Integer.valueOf(153));
    /* 137 */
        operationCodes.put("flatten", Integer.valueOf(154));
    /* 138 */
        operationCodes.put("including", Integer.valueOf(155));
    /* 139 */
        operationCodes.put("intersection", Integer.valueOf(156));
    /* 140 */
        operationCodes.put("union", Integer.valueOf(157));
    /* 141 */
        operationCodes.put("at", Integer.valueOf(158));
    /* 142 */
        operationCodes.put("first", Integer.valueOf(159));
    /* 143 */
        operationCodes.put("indexOf", Integer.valueOf(160));
    /* 144 */
        operationCodes.put("insertAt", Integer.valueOf(161));
    /* 145 */
        operationCodes.put("last", Integer.valueOf(162));
    /* 146 */
        operationCodes.put("prepend", Integer.valueOf(163));
    /* 147 */
        operationCodes.put("subSequence", Integer.valueOf(164));
    /* 148 */
        operationCodes.put("append", Integer.valueOf(165));
    /* 149 */
        operationCodes.put("subOrderedSet", Integer.valueOf(166));
    /* 150 */
        operationCodes.put("symmetricDifference", Integer.valueOf(167));
    /* 151 */
        operationCodes.put("exists", Integer.valueOf(201));
    /* 152 */
        operationCodes.put("forAll", Integer.valueOf(202));
    /* 153 */
        operationCodes.put("isUnique", Integer.valueOf(203));
    /* 154 */
        operationCodes.put("one", Integer.valueOf(204));
    /* 155 */
        operationCodes.put("any", Integer.valueOf(205));
    /* 156 */
        operationCodes.put("collect", Integer.valueOf(206));
    /* 157 */
        operationCodes.put("collectNested", Integer.valueOf(207));
    /* 158 */
        operationCodes.put("closure", Integer.valueOf(208));
    /* 159 */
        operationCodes.put("select", Integer.valueOf(209));
    /* 160 */
        operationCodes.put("reject", Integer.valueOf(210));
    /* 161 */
        operationCodes.put("sortedBy", Integer.valueOf(211));
    /* 162 */
        operationCodes.put("toBoolean", Integer.valueOf(212));
    /* 163 */
        operationCodes.put("toString", Integer.valueOf(213));
    /* 164 */
        operationCodes.put("characters", Integer.valueOf(214));
    /* 165 */
        operationCodes.put("endsWith", Integer.valueOf(215));
    /* 166 */
        operationCodes.put("equalsIgnoreCase", Integer.valueOf(216));
    /* 167 */
        operationCodes.put("lastIndexOf", Integer.valueOf(217));
    /* 168 */
        operationCodes.put("matches", Integer.valueOf(218));
    /* 169 */
        operationCodes.put("replaceAll", Integer.valueOf(219));
    /* 170 */
        operationCodes.put("replaceFirst", Integer.valueOf(220));
    /* 171 */
        operationCodes.put("startsWith", Integer.valueOf(221));
    /* 172 */
        operationCodes.put("substituteAll", Integer.valueOf(222));
    /* 173 */
        operationCodes.put("substituteFirst", Integer.valueOf(223));
    /* 174 */
        operationCodes.put("tokenize", Integer.valueOf(224));
    /* 175 */
        operationCodes.put("trim", Integer.valueOf(225));
    /* 176 */
        operationCodes.put("toLowerCase", Integer.valueOf(226));
    /* 177 */
        operationCodes.put("toUpperCase", Integer.valueOf(227));
    /* 178 */
        operationCodes.put("selectByKind", Integer.valueOf(228));
    /* 179 */
        operationCodes.put("selectByType", Integer.valueOf(229));
    /* 180 */
        operationCodes.put("oclAsSet", Integer.valueOf(230));


    /*      */
    }


    public static Map<String, Integer> getOperationcodes() {
        return operationCodes;
    }

    public static String getOperationName(int opcode)
  /*      */ {
  /*  251 */
        switch (opcode) {
  /*      */
            case 1:
  /*  253 */
                return "+";
  /*      */
            case 2:
  /*  255 */
                return "-";
  /*      */
            case 3:
  /*  257 */
                return "*";
  /*      */
            case 4:
  /*  259 */
                return "/";
  /*      */
            case 10:
  /*  261 */
                return "&&";
  /*      */
            case 11:
  /*  263 */
                return "!";
  /*      */
            case 12:
  /*  265 */
                return "||";
  /*      */
            case 13:
  /*  267 */
                return "implies";
  /*      */
            case 15:
  /*  269 */
                return "abs";
  /*      */
            case 16:
  /*  271 */
                return "div";
  /*      */
            case 17:
  /*  273 */
                return "mod";
  /*      */
            case 18:
  /*  275 */
                return "max";
  /*      */
            case 19:
  /*  277 */
                return "min";
  /*      */
            case 20:
  /*  279 */
                return "size";
  /*      */
            case 21:
  /*  281 */
                return "concat";
  /*      */
            case 22:
  /*  283 */
                return "substring";
  /*      */
            case 23:
  /*  285 */
                return "toInteger";
  /*      */
            case 24:
  /*  287 */
                return "toReal";
  /*      */
            case 25:
  /*  289 */
                return "^";
  /*      */
            case 26:
  /*  291 */
                return "floor";
  /*      */
            case 27:
  /*  293 */
                return "round";
  /*      */
            case 28:
  /*  295 */
                return "toLower";
  /*      */
            case 29:
  /*  297 */
                return "toUpper";
  /*      */
            case 40:
  /*  299 */
                return "allInstances";
  /*      */
            case 60:
  /*  301 */
                return "==";
  /*      */
            case 61:
  /*  303 */
                return "!=";
  /*      */
            case 62:
  /*  305 */
                return "oclAsType";
  /*      */
            case 63:
  /*  307 */
                return "oclIsKindOf";
  /*      */
            case 64:
  /*  309 */
                return "oclIsTypeOf";
  /*      */
            case 65:
  /*  311 */
                return "oclIsUndefined";
  /*      */
            case 66:
  /*  313 */
                return "oclIsInvalid";
  /*      */
            case 67:
  /*  315 */
                return "<";
  /*      */
            case 68:
  /*  317 */
                return ">";
  /*      */
            case 69:
  /*  319 */
                return "<=";
  /*      */
            case 70:
  /*  321 */
                return ">=";
  /*      */
            case 71:
  /*  323 */
                return "oclIsNew";
  /*      */
            case 72:
  /*  325 */
                return "oclIsInState";
  /*      */
            case 100:
  /*  327 */
                return "hasReturned";
  /*      */
            case 101:
  /*  329 */
                return "result";
  /*      */
            case 102:
  /*  331 */
                return "isSignalSent";
  /*      */
            case 103:
  /*  333 */
                return "isOperationCall";
  /*      */
            case 140:
  /*  335 */
                return "count";
  /*      */
            case 141:
  /*  337 */
                return "excludes";
  /*      */
            case 142:
  /*  339 */
                return "excludesAll";
  /*      */
            case 143:
  /*  341 */
                return "includes";
  /*      */
            case 144:
  /*  343 */
                return "includesAll";
  /*      */
            case 145:
  /*  345 */
                return "isEmpty";
  /*      */
            case 146:
  /*  347 */
                return "notEmpty";
  /*      */
            case 147:
  /*  349 */
                return "product";
  /*      */
            case 148:
  /*  351 */
                return "sum";
  /*      */
            case 149:
  /*  353 */
                return "asBag";
  /*      */
            case 150:
  /*  355 */
                return "asOrderedSet";
  /*      */
            case 151:
  /*  357 */
                return "asSequence";
  /*      */
            case 152:
  /*  359 */
                return "asSet";
  /*      */
            case 153:
  /*  361 */
                return "excluding";
  /*      */
            case 154:
  /*  363 */
                return "flatten";
  /*      */
            case 155:
  /*  365 */
                return "including";
  /*      */
            case 156:
  /*  367 */
                return "intersection";
  /*      */
            case 157:
  /*  369 */
                return "union";
  /*      */
            case 158:
  /*  371 */
                return "at";
  /*      */
            case 159:
  /*  373 */
                return "first";
  /*      */
            case 160:
  /*  375 */
                return "indexOf";
  /*      */
            case 161:
  /*  377 */
                return "insertAt";
  /*      */
            case 162:
  /*  379 */
                return "last";
  /*      */
            case 163:
  /*  381 */
                return "prepend";
  /*      */
            case 164:
  /*  383 */
                return "subSequence";
  /*      */
            case 165:
  /*  385 */
                return "append";
  /*      */
            case 166:
  /*  387 */
                return "subOrderedSet";
  /*      */
            case 167:
  /*  389 */
                return "symmetricDifference";
  /*      */
            case 201:
  /*  391 */
                return "exists";
  /*      */
            case 202:
  /*  393 */
                return "forAll";
  /*      */
            case 203:
  /*  395 */
                return "isUnique";
  /*      */
            case 204:
  /*  397 */
                return "one";
  /*      */
            case 205:
  /*  399 */
                return "any";
  /*      */
            case 206:
  /*  401 */
                return "collect";
  /*      */
            case 207:
  /*  403 */
                return "collectNested";
  /*      */
            case 208:
  /*  405 */
                return "closure";
  /*      */
            case 209:
  /*  407 */
                return "select";
  /*      */
            case 210:
  /*  409 */
                return "reject";
  /*      */
            case 211:
  /*  411 */
                return "sortedBy";
  /*      */
            case 212:
  /*  413 */
                return "toBoolean";
  /*      */
            case 213:
  /*  415 */
                return "toString";
  /*      */
            case 214:
  /*  417 */
                return "characters";
  /*      */
            case 215:
  /*  419 */
                return "endsWith";
  /*      */
            case 216:
  /*  421 */
                return "equalsIgnoreCase";
  /*      */
            case 217:
  /*  423 */
                return "lastIndexOf";
  /*      */
            case 218:
  /*  425 */
                return "matches";
  /*      */
            case 219:
  /*  427 */
                return "replaceAll";
  /*      */
            case 220:
  /*  429 */
                return "replaceFirst";
  /*      */
            case 221:
  /*  431 */
                return "startsWith";
  /*      */
            case 222:
  /*  433 */
                return "substituteAll";
  /*      */
            case 223:
  /*  435 */
                return "substituteFirst";
  /*      */
            case 224:
  /*  437 */
                return "tokenize";
  /*      */
            case 225:
  /*  439 */
                return "trim";
  /*      */
            case 226:
  /*  441 */
                return "toLowerCase";
  /*      */
            case 227:
  /*  443 */
                return "toUpperCase";
  /*      */
            case 228:
  /*  445 */
                return "selectByKind";
  /*      */
            case 229:
  /*  447 */
                return "selectByType";
  /*      */
            case 230:
  /*  449 */
                return "oclAsSet";
  /*      */
        }
  /*  451 */
        return "";
  /*      */
    }
  /*      */


}
