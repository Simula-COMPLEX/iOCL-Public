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

package no.simule.reader;

import org.eclipse.uml2.uml.util.UMLUtil;
import org.eclipse.uml2.uml.util.UMLUtil.UML2EcoreConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * This class util class constrains properties use to transform Ecore Package in UML Package
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-04-15
 */

public class UML2EcoreConverterProperties {
    public static Map<String, String> optionsToProcess() {
        final Map<String, String> options = new HashMap<String, String>();


        options.put(UML2EcoreConverter.OPTION__UNION_PROPERTIES,
                UMLUtil.OPTION__PROCESS);

        options.put(UML2EcoreConverter.OPTION__REDEFINING_OPERATIONS,
                UMLUtil.OPTION__PROCESS);

        options.put(UML2EcoreConverter.OPTION__SUBSETTING_PROPERTIES,
                UMLUtil.OPTION__PROCESS);

        options.put(UML2EcoreConverter.OPTION__DUPLICATE_FEATURE_INHERITANCE,
                UMLUtil.OPTION__PROCESS);

        options.put(UML2EcoreConverter.OPTION__DUPLICATE_FEATURES,
                UMLUtil.OPTION__PROCESS);

        options.put(UML2EcoreConverter.OPTION__DUPLICATE_OPERATION_INHERITANCE,
                UMLUtil.OPTION__PROCESS);

        options.put(UML2EcoreConverter.OPTION__DUPLICATE_OPERATIONS,
                UMLUtil.OPTION__PROCESS);

        options.put(UML2EcoreConverter.OPTION__CAMEL_CASE_NAMES,
                UMLUtil.OPTION__PROCESS);

        options.put(UML2EcoreConverter.OPTION__SUPER_CLASS_ORDER,
                UMLUtil.OPTION__PROCESS);

        return options;
    }

}
