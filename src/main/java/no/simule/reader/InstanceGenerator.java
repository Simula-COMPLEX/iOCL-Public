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

import no.simule.models.cd.ClassAttribute;
import no.simule.models.cd.ClassStructure;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Package;

import java.util.Collection;

/**
 * This class create new UML instance and add update values of new instances
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-04-15
 */

public class InstanceGenerator {

    /**
     * This method loads the Class Diagram, retrieves the desired elements ,
     * creates an instance of the desired class and
     * evaluates the constraint for the Class's instance
     */
    public InstanceSpecification createInstanceSpecification(String name, Package _package, Class context, ClassStructure structure) {
        InstanceSpecification instance = instantiate(_package, context);
        instance.setName(name);

        for (ClassAttribute attribute : structure.getAttributes()) {
            Property property = context.getOwnedAttribute(attribute.getName(), null);
            if (attribute.getValues() != null) {
                for (Object value : attribute.getValues()) {
                    if (value != null) {

                        if (attribute.isEnum()) {
                            EnumerationLiteral literal = getEnumerationLiteral(_package, attribute.getType(), value);
                            setValue(instance, property, literal);
                        } else if (attribute.isClass()) {

                            String type = attribute.getType();
                            if (value instanceof String && !value.toString().isEmpty()) {
                                String tokens[] = value.toString().split(":");
                                value = tokens[0].trim();
                                type = tokens[1].trim();
                            }
                            InstanceSpecification instanceValue = getInstanceSpecifications(_package, type, value);

                            setValue(instance, property, instanceValue);
                        } else {
                            setValue(instance, property, value);
                        }

                    }
                }
            }

        }
        return instance;
    }


    /**
     * This method creates an instance of the class supplied
     *
     * @param pkg        - the package containing class
     * @param classifier - the class whose instance would be created
     * @return
     */
    private InstanceSpecification instantiate(Package pkg, Classifier classifier) {
        InstanceSpecification result = (InstanceSpecification) pkg.createPackagedElement(
                null, UMLPackage.eINSTANCE.getInstanceSpecification());

        if (classifier != null) {
            result.getClassifiers().add(classifier);
        }

        return result;
    }

    /**
     * This method creates an instance of the slot according to the property supplied and fills it with the value supplied
     *
     * @param instance - Instance of the desired class
     * @param property - attribute of the instance
     * @param value    - value to for the attribute
     * @return
     */
    private Slot setValue(
            InstanceSpecification instance,
            Property property,
            Object value) {

        Slot result = null;

        for (Slot slot : instance.getSlots()) {
            if (slot.getDefiningFeature() == property) {
                result = slot;
                //slot.getValues().clear();
                break;
            }
        }

        if (result == null) {
            result = instance.createSlot();
            result.setDefiningFeature(property);
        }

        if (value instanceof Collection<?>) {
            for (Object e : (Collection<?>) value) {
                addValue(result, e);
            }
        } else {
            addValue(result, value);
        }

        return result;
    }

    /**
     * This method fills the supplied slot with the supplied value and returns an instance of ValueSpecification
     *
     * @param slot  - Refers to the attribute of Class
     * @param value - Value of the attribute
     * @return
     */
    private ValueSpecification addValue(Slot slot, Object value) {
        ValueSpecification result = null;


//        if (slot.getDefiningFeature().getType() == getUMLUnlimitedNatural()) {
//            LiteralUnlimitedNatural valueSpec =
//                    (LiteralUnlimitedNatural) slot.createValue(
//                            null, null, uml.getLiteralUnlimitedNatural());
//            valueSpec.setValue(((Integer) value).intValue());
//            result = valueSpec;
//        }


        if (value == null) {

            LiteralNull valueSpec = (LiteralNull) slot.createValue(
                    null, null, UMLPackage.eINSTANCE.getLiteralNull());
            result = valueSpec;

        } else if (value instanceof InstanceSpecification) {

            InstanceValue valueSpec = (InstanceValue) slot.createValue(
                    null, null, UMLPackage.eINSTANCE.getInstanceValue());
            valueSpec.setInstance((InstanceSpecification) value);
            result = valueSpec;

        } else if (value instanceof EnumerationLiteral) {

            InstanceValue valueSpec = (InstanceValue) slot.createValue(
                    null, null, UMLPackage.eINSTANCE.getInstanceValue());

            valueSpec.setInstance((InstanceSpecification) value);
            result = valueSpec;

        } else if (value instanceof String) {

            LiteralString valueSpec = (LiteralString) slot.createValue(
                    null, null, UMLPackage.eINSTANCE.getLiteralString());
            valueSpec.setValue((String) value);
            result = valueSpec;

        } else if (value instanceof Integer) {

            LiteralInteger valueSpec = (LiteralInteger) slot.createValue(
                    null, null, UMLPackage.eINSTANCE.getLiteralInteger());
            valueSpec.setValue(((Integer) value).intValue());
            result = valueSpec;

        } else if (value instanceof Boolean) {

            LiteralBoolean valueSpec = (LiteralBoolean) slot.createValue(
                    null, null, UMLPackage.eINSTANCE.getLiteralBoolean());
            valueSpec.setValue(((Boolean) value).booleanValue());
            result = valueSpec;

        } else {
            throw new IllegalArgumentException("Unrecognized slot value: " + value);
        }

        return result;


    }


    private EnumerationLiteral getEnumerationLiteral(Package _package, String type, Object value) {

        return getEnumerationLiteral(_package.getPackagedElements(), type, value);
    }


    private EnumerationLiteral getEnumerationLiteral(EList<PackageableElement> packageableElements, String type, Object value) {

        for (PackageableElement element : packageableElements) {
            if (element.eClass() == UMLPackage.Literals.ENUMERATION) {
                Enumeration enumeration = (Enumeration) element;
                if (enumeration.getName().equals(type)) {
                    for (EnumerationLiteral literal : enumeration.getOwnedLiterals()) {

                        if (literal.getName().equals(value)) {
                            return literal;
                        }
                    }
                }
            } else if (element.eClass() == UMLPackage.Literals.PACKAGE) {
                Package _nustedPackage = (Package) element;
                getEnumerationLiteral(_nustedPackage.getPackagedElements(), type, value);
            }
        }
        return null;
    }

    private InstanceSpecification getInstanceSpecifications(Package _package, String instanceType, Object instanceName) {
        return getInstanceSpecifications(_package.getPackagedElements(), instanceType, instanceName);
    }

    private InstanceSpecification getInstanceSpecifications(EList<PackageableElement> packageableElements, String instanceType, Object instanceName) {

        for (PackageableElement element : packageableElements) {
            if (element.eClass() == UMLPackage.eINSTANCE.getInstanceSpecification()) {
                InstanceSpecification instanceSpecification = getInstanceSpecifications(element, instanceType, instanceName);

                if (instanceSpecification != null) {
                    return instanceSpecification;
                }

            } else if (element.eClass() == UMLPackage.Literals.PACKAGE) {
                Package _nustedPackage = (Package) element;
                InstanceSpecification instanceSpecification = getInstanceSpecifications(_nustedPackage.getPackagedElements(), instanceType, instanceName);

                if (instanceSpecification != null) {
                    return instanceSpecification;
                }

            }
        }
        return null;
    }


    private InstanceSpecification getInstanceSpecifications(PackageableElement element, String instanceType, Object instanceName) {


        if (element.eClass() == UMLPackage.eINSTANCE.getInstanceSpecification()) {
            InstanceSpecification instance = (InstanceSpecification) element;
            if (instance.getName().equals(instanceName.toString())) {
                for (Classifier classifier : instance.getClassifiers()) {
                    if (classifier != null) {
                        if (classifier.eClass() == UMLPackage.Literals.CLASS) {
                            if (classifier.getName().equals(instanceType)) {
                                return instance;
                            }
                        }
                    }

                }
            }
        }

        return null;
    }


}
