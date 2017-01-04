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

import no.simule.dataobjects.cd.OperationParameter;

import java.util.Iterator;
import java.util.List;


public class QueryUtil {

    private transient static final String Empty = "";
    private transient static final String Space = " ";
    private transient static final String Dot = ".";
    private transient static final String Arrow = "->";
    private transient static final String Comma = ",";
    private transient static final String Apostrophe = "'";
    private transient static final String Colon = ":";
    private transient static final String LeftBracket = "(";
    private transient static final String RightBracket = ")";
    private transient static final String Bracket = "()";
    private transient static final String Equal = "=";

    public static String operationPrametersName(List<OperationParameter> parameters) {
        StringBuilder parameter = new StringBuilder(LeftBracket);
        Iterator<OperationParameter> iterator = parameters.iterator();
        while (iterator.hasNext()) {
            OperationParameter ca = (OperationParameter) iterator.next();
            parameter.append(ca.getName());
            parameter.append(Space);
            parameter.append(":");
            parameter.append(Space);
            parameter.append(attributeType(ca.getType(), ca.isCollection()));
            parameter.append(Space);
            if (iterator.hasNext()) {
                parameter.append(",");
                parameter.append(Space);
            }
        }
        parameter.append(RightBracket);

        return parameter.toString();
    }

    public static String operationPrameters(List<OperationParameter> parameters) {
        StringBuilder parameter = new StringBuilder(LeftBracket);
        Iterator<OperationParameter> iterator = parameters.iterator();
        while (iterator.hasNext()) {
            OperationParameter ca = (OperationParameter) iterator.next();
            parameter.append(attributeType(ca.getType(), ca.isCollection()));
            parameter.append(Space);
            if (iterator.hasNext()) {
                parameter.append(",");
                parameter.append(Space);
            }
        }
        parameter.append(RightBracket);
        return parameter.toString();
    }

    public static String attributeType(String type, Boolean Collection) {
        StringBuilder newType = new StringBuilder(Empty);
        if (Collection) {
            newType.append("Set");
            newType.append(LeftBracket);
            newType.append(type);
            newType.append(RightBracket);
        } else {
            newType.append(type);
        }
        return newType.toString();
    }

}
