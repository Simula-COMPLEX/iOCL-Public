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

import no.simule.models.cd.*;
import no.simule.models.enm.EnumStructure;
import no.simule.utils.Keywords;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.internal.impl.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class ClassDiagramReader read UML Model ClassDiagram and Transform
 * into {@link no.simule.models.cd.ClassDiagram} dataStructure.
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-04-15
 */


public class ModelReader implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Global  ClassDiagram attribute contains  classes,
     * instances , enumerations and relationships
     */
    private ClassDiagram classDiagram;

    private Map<String, List<String>> superClassMap;
    private Map<String, ClassStructure> classMap;
    private Map<String, List<ClassInstance>> classInstanceMap;


    /**
     * This is the public method which take UML Package {@link org.eclipse.uml2.uml.Package} as input and read
     * classes, instances , enumerations and relationships of Classes and return transformed dataStructure .
     *
     * @param _package of type {@link org.eclipse.uml2.uml.Package}.
     * @return {@link no.simule.models.cd.ClassDiagram}.
     */

    public ClassDiagram readUMLPackage(Package _package) {

        classDiagram = new ClassDiagram();
        superClassMap = new HashMap<>();
        classMap = new HashMap<>();
        classInstanceMap = new HashMap<>();

        if (_package != null) {
            EList<PackageableElement> packageableElements = _package.getPackagedElements();
            String packageName = _package.getName() != null ? _package.getName() : "";
            readPackage(packageableElements, packageName);
        } else {
            System.err.println("Package is null");
        }


        for (ClassStructure cs :
                classMap.values()) {
            if (classInstanceMap
                    .containsKey(cs.getName())) {
                List<ClassInstance> classInstances = classInstanceMap.get(cs.getName());
                cs.getInstances().addAll(classInstances);
            }


            if (superClassMap
                    .containsKey(cs.getName())) {
                List<String> superClasses = superClassMap.get(cs.getName());
                for (String superClass : superClasses) {
                    cs.addSuperClass(classMap.get(superClass));

                }
            }

            classDiagram.addClass(cs);
        }

        return classDiagram;
    }


    /**
     * This is the method transform uml package elements and set into class diagram data structure   .
     *
     * @param packageableElements elements in package type {@link org.eclipse.uml2.uml.PackageableElement}.
     * @param supperPackage       supper package name.
     * @return Nothing.
     */


    private void readPackage(EList<PackageableElement> packageableElements, String supperPackage) {

        for (PackageableElement element : packageableElements) {

            if (element.eClass() == UMLPackage.Literals.CLASS) {
                Class _class = (Class) element;
                ClassStructure cs = readClass(_class, supperPackage);
                classMap.put(cs.getName(), cs);
            } else if (element.eClass() == UMLPackage.Literals.STEREOTYPE) {
                Stereotype stereotype = (Stereotype) element;
                ClassStructure cs = readClass(stereotype, supperPackage);
                classMap.put(cs.getName(), cs);
            } else if (element.eClass() == UMLPackage.Literals.ENUMERATION) {
                Enumeration enumeration = (Enumeration) element;
                classDiagram.addEnumeration(readEnumeration(enumeration, supperPackage));
            } else if (element.eClass() == UMLPackage.eINSTANCE.getInstanceSpecification()) {
                InstanceSpecification instance = (InstanceSpecification) element;
                readInstance(instance, supperPackage);
            } else if (element.eClass() == UMLPackage.Literals.PACKAGE) {
                Package _package = (Package) element;
                String newPackageName;

                if (supperPackage.equals("")) {
                    newPackageName = _package.getName() != null
                            ? _package.getName()
                            : supperPackage;
                } else {
                    newPackageName = _package.getName() != null
                            ? supperPackage + "." + _package.getName()
                            : supperPackage;
                }
                readPackage(_package.getPackagedElements(), newPackageName);

            }
        }
    }

    /**
     * This is the method transform uml Class instance.
     *
     * @param instance    of type {@link org.eclipse.uml2.uml.InstanceSpecification}.
     * @param packageName supper package name.
     * @return Nothing.
     */

    private void readInstance(InstanceSpecification instance, String packageName) {

        if (instance.getName() != null && !instance.getName().isEmpty()) {
            ClassInstance classInstance = new ClassInstance();
            classInstance.setName(instance.getName());
            classInstance.set_package(packageName);

            for (Slot slot : instance.getSlots()) {

                StructuralFeature feature = slot.getDefiningFeature();

                InstanceAttribute attribute = new InstanceAttribute();
                attribute.setName(feature.getName());
                attribute.setType(feature.getType().getName());

                List<Object> values = new ArrayList<>();
                for (ValueSpecification valueSpecification : slot.getValues()) {

                    if (valueSpecification instanceof InstanceValue) {

                        attribute.setClass(true);
                        InstanceValue instanceValue = (InstanceValue) valueSpecification;

                        InstanceSpecification valueInstanceSpecification = instanceValue.getInstance();

                        if (valueInstanceSpecification != null) {
                            values.add(valueInstanceSpecification.getName());
                        }


                    } else if (valueSpecification instanceof LiteralSpecification) {

                        LiteralSpecification literalSpecification = (LiteralSpecification) valueSpecification;


                        if (literalSpecification instanceof LiteralString) {
                            LiteralString literal = (LiteralString) literalSpecification;
                            values.add(literal.getValue());

                        } else if (literalSpecification instanceof LiteralInteger) {
                            LiteralInteger literal = (LiteralInteger) literalSpecification;
                            values.add(literal.getValue());


                        } else if (literalSpecification instanceof LiteralBoolean) {
                            LiteralBoolean literal = (LiteralBoolean) literalSpecification;
                            values.add(literal.isValue());

                        } else if (literalSpecification instanceof LiteralReal) {
                            LiteralReal literal = (LiteralReal) literalSpecification;
                            values.add(literal.getValue());

                        } else if (literalSpecification instanceof LiteralUnlimitedNatural) {
                            LiteralUnlimitedNatural literal = (LiteralUnlimitedNatural) literalSpecification;
                            values.add(literal.getValue());

                        }

                    }


                }

                attribute.setValues(values.toArray());
                classInstance.addAttribute(attribute);


            }


            for (Classifier classifier : instance.getClassifiers()) {
                if (instance.getName() != null && classifier.getName() != null) {
                    if (classInstanceMap.containsKey(classifier.getName())) {
                        List<ClassInstance> intances = classInstanceMap.get(classifier.getName());
                        intances.add(classInstance);
                        classInstanceMap.put(classifier.getName(), intances);
                    } else {
                        List<ClassInstance> intances = new ArrayList<>();
                        intances.add(classInstance);
                        classInstanceMap.put(classifier.getName(), intances);
                    }
                }
            }
        }


    }

    /**
     * This is the method transform uml Enumeration.
     *
     * @param enumeration of type {@link org.eclipse.uml2.uml.Enumeration}.
     * @param packageName supper package name.
     * @return Nothing.
     */

    private EnumStructure readEnumeration(Enumeration enumeration, String packageName) {

        EnumStructure structure = new EnumStructure();

        structure.setName(enumeration.getName());
        structure.setPackage(packageName);
        for (EnumerationLiteral literal : enumeration.getOwnedLiterals()) {
            structure.addLiteral(literal.getName());
        }

        return structure;
    }


    /**
     * This is the method transform uml Class and read class detail including class attribute, operations ,visibility
     * and relationships.
     *
     * @param _class      of type {@link org.eclipse.uml2.uml.Class}.
     * @param packageName supper package name.
     * @return Nothing.
     */

    private ClassStructure readClass(Class _class, String packageName) {
        ClassStructure structure = new ClassStructure();

        List<String> rules = new ArrayList<>();
        //System.out.println(_class.getName());


        for (Constraint constraint : _class.getOwnedRules()) {
            if (constraint.getSpecification() instanceof OpaqueExpressionImpl) {
                OpaqueExpressionImpl expressionImpl = (OpaqueExpressionImpl) constraint.getSpecification();
                for (String body : expressionImpl.getBodies()) {
                    rules.add(body);
                }
            }

        }


        for (Class superClass : _class.getSuperClasses()) {

            if (superClassMap.containsKey(_class.getName())) {
                List<String> values = superClassMap.get(_class.getName());
                values.add(superClass.getName());
                superClassMap.put(_class.getName(), values);

            } else {
                List<String> values = new ArrayList<>();
                values.add(superClass.getName());
                superClassMap.put(_class.getName(), values);
            }

        }


        //System.out.println("\n  -------- \n");

        structure.setPackage(packageName);
        structure.setVisibility(_class.getVisibility().toString());
        structure.setRules(rules);
        structure.setAbstract(_class.isAbstract());
        structure.setFinal(_class.isLeaf());
        structure.setName(_class.getName());
        structure.setAttributes(readAttribute(_class));
        structure.setOperations(readClassOperations(_class));
        structure.setRelationships(readClassRelations(_class));


        for (NamedElement inheritedElement : _class.getInheritedMembers()) {

            if (inheritedElement instanceof Property) {
                Property property = (Property) inheritedElement;
                ClassAttribute attribute = readAttribute(property);
                if (attribute != null && attribute.getName() != null) {
                    structure.addAttribute(attribute);
                }
            } else if (inheritedElement instanceof Operation) {
                Operation operation = (Operation) inheritedElement;
                ClassOperation classOperation = readClassOperation(operation);
                if (classOperation != null) {
                    structure.addOperation(classOperation);
                }
            }

        }


        return structure;
    }


    /**
     * This method take  UML Class {@link org.eclipse.uml2.uml.Class} as arguments
     * and read class operations details like return type ,viability and parameters .
     *
     * @param _class of type {@link org.eclipse.uml2.uml.Class}.
     * @return List<ClassOperation> list to class attribute as form of {@link no.simule.models.cd.ClassOperation}.
     */


    private ArrayList<ClassOperation> readClassOperations(Class _class) {
        ArrayList<ClassOperation> operations = new ArrayList<ClassOperation>();
        List<Operation> ownedOperations = _class.getOwnedOperations();
        if (!ownedOperations.isEmpty()) {
            for (Operation operation : ownedOperations) {

                ClassOperation classOperation = readClassOperation(operation);
                if (classOperation != null) {
                    operations.add(classOperation);
                }

            }
        }
        return operations;
    }

    private ClassOperation readClassOperation(Operation operation) {

        ClassOperation classOperation = new ClassOperation();
        classOperation.setName(operation.getName());
        classOperation.setVisibility(operation.getVisibility().toString());
        classOperation.setReturnType(new OperationReturn());

        EList<Parameter> parameters = operation.getOwnedParameters();
        if (!parameters.isEmpty()) {
            for (Parameter parameter : parameters) {

                boolean returnType = false;


                ParameterDirectionKind direction = (ParameterDirectionKind) parameter.getDirection();
                if (direction != null && direction.getValue() == 3) {
                    returnType = true;
                }

                OperationParameter operationParameter = new OperationParameter();

                if (parameter
                        .getType() instanceof PrimitiveTypeImpl) {
                    PrimitiveTypeImpl primitiveType = (PrimitiveTypeImpl) (parameter.getType());
                    if (returnType) {
                        if (primitiveType.getName() == null || primitiveType.getName().equals("")) {
                            OperationReturn methodReturn = new OperationReturn();
                            methodReturn.setType(primitiveType.eProxyURI().fragment());
                            if (parameter.getUpper() == -1) {
                                methodReturn.setCollection(true);
                            }
                            classOperation.setReturnType(methodReturn);
                        } else {
                            OperationReturn methodReturn = new OperationReturn();
                            methodReturn.setType(primitiveType.getName());
                            if (parameter.getUpper() == -1) {
                                methodReturn.setCollection(true);
                            }
                            classOperation.setReturnType(methodReturn);

                        }

                    } else {
                        operationParameter.setName(parameter.getName());

                        if (primitiveType.getName() == null || primitiveType.getName().equals("")) {
                            operationParameter.setType(primitiveType.eProxyURI().fragment());
                            if (parameter.getUpper() == -1) {
                                operationParameter.setCollection(true);
                            }
                        } else {
                            if (parameter.getUpper() == -1) {
                                operationParameter.setCollection(true);
                            }
                            operationParameter.setType(primitiveType.getName());
                        }
                        classOperation.getParameters().add(operationParameter);
                    }
                } else if (parameter
                        .getType() instanceof Enumeration) {

                    Enumeration enumeration = (Enumeration) (parameter.getType());

                    if (returnType) {
                        OperationReturn methodReturn = new OperationReturn();
                        methodReturn.setType(enumeration.getName());
                        if (parameter.getUpper() == -1) {
                            methodReturn.setCollection(true);
                        }
                        classOperation.setReturnType(methodReturn);

                    } else {

                        operationParameter.setName(parameter.getName());
                        operationParameter.setVisibility(parameter.getVisibility().toString());
                        operationParameter.setType(enumeration.getName());
                        if (parameter.getUpper() == -1) {
                            operationParameter.setCollection(true);
                        }
                        classOperation.getParameters().add(operationParameter);
                    }

                } else if (parameter
                        .getType() instanceof Class) {
                    Class _class = (Class) (parameter.getType());
                    if (returnType) {
                        OperationReturn methodReturn = new OperationReturn();
                        methodReturn.setType(_class.getName());
                        methodReturn.setClass(true);
                        if (parameter.getUpper() == -1) {
                            methodReturn.setCollection(true);
                        }
                        classOperation.setReturnType(methodReturn);

                    } else {

                        operationParameter.setName(parameter.getName());
                        operationParameter.setVisibility(parameter.getVisibility().toString());
                        operationParameter.setType(_class.getName());
                        operationParameter.setClass(true);

                        if (parameter.getUpper() == -1) {
                            operationParameter.setCollection(true);
                        }
                        classOperation.getParameters().add(operationParameter);
                    }
                } else if (parameter
                        .getType() instanceof org.eclipse.uml2.uml.internal.impl.InterfaceImpl) {

                    InterfaceImpl prim = (InterfaceImpl) (parameter.getType());
                    URI proxy = prim.eProxyURI();
                    String proxyFragment = proxy.fragment();
                    String arrtibuteType = attributeInterface(proxyFragment);
                    operationParameter.setType(arrtibuteType);

                    if (returnType) {
                        OperationReturn methodReturn = new OperationReturn();
                        methodReturn.setType(arrtibuteType);
                        if (operationParameter.isCollection()) {
                            methodReturn.setCollection(true);
                        }
                        classOperation.setReturnType(methodReturn);

                    } else {
                        operationParameter.setType(arrtibuteType);
                        operationParameter.setName(parameter.getName().toString());
                        operationParameter.setVisibility(parameter.getVisibility().toString());
                        classOperation.getParameters().add(operationParameter);
                    }
                }

            }
        }


        return classOperation;
    }


    /**
     * This  method take UML Class {@link org.eclipse.uml2.uml.Class} as arguments
     * and read class attribute details like type ,viability and multiplicity .
     *
     * @param _class of type {@link org.eclipse.uml2.uml.Class}.
     * @return List<ClassAttribute> list to class attribute as form of {@link no.simule.models.cd.ClassAttribute}.
     */

    private List<ClassAttribute> readAttribute(Class _class) {
        ArrayList<ClassAttribute> attributes = new ArrayList<ClassAttribute>();
        EList<Property> ownedAttributes = _class.getOwnedAttributes();
        if (!ownedAttributes.isEmpty()) {
            for (Property property : ownedAttributes) {
                ClassAttribute attribute = readAttribute(property);
                if (attribute != null && attribute.getName() != null) {
                    attributes.add(attribute);
                }
            }
        }
        return attributes;
    }

    private ClassAttribute readAttribute(Property property) {

        ClassAttribute attribute = new ClassAttribute();
        if (property.getType() instanceof org.eclipse.uml2.uml.internal.impl.PrimitiveTypeImpl) {
            attribute.setName(property.getName().toString());
            attribute.setVisibility(property.getVisibility().toString());
            PrimitiveTypeImpl prim = (PrimitiveTypeImpl) (property.getType());
            if (prim.getName() == null || prim.getName().equals("")) {
                attribute.setType(prim.eProxyURI().fragment());
            } else {
                attribute.setType(prim.getName());
            }

            if (property.getUpper() == -1) {
                attribute.setCollection(true);
            }

        } else if (property
                .getType() instanceof org.eclipse.uml2.uml.internal.impl.EnumerationImpl) {

            EnumerationImpl impl = (EnumerationImpl) (property.getType());
            attribute.setName(property.getName().toString());
            attribute.setVisibility(property.getVisibility().toString());
            attribute.setType(impl.getName());
            attribute.setEnum(true);

            if (property.getUpper() == -1) {
                attribute.setCollection(true);
            }

        } else if (property.getType() instanceof org.eclipse.uml2.uml.internal.impl.ClassImpl) {

            ClassImpl impl = (ClassImpl) (property.getType());
            attribute.setName(property.getName().toString());
            attribute.setVisibility(property.getVisibility().toString());
            attribute.setType(impl.getName());
            attribute.setClass(true);

            if (property.getUpper() == -1) {
                attribute.setCollection(true);
            }

        } else if (property
                .getType() instanceof org.eclipse.uml2.uml.internal.impl.InterfaceImpl) {

            InterfaceImpl prim = (InterfaceImpl) (property.getType());
            URI proxy = prim.eProxyURI();
            String proxyFragment = proxy.fragment();
            attribute.setCollection(attributeInterfaceCollection(proxyFragment));
            String arrtibuteType = attributeInterface(proxyFragment);
            attribute.setType(arrtibuteType);
            attribute.setName(property.getName().toString());
            attribute.setVisibility(property.getVisibility().toString());

        }
        return attribute;
    }


    /**
     * This  method take  UML Class {@link org.eclipse.uml2.uml.Class} as arguments
     * and read  both type of class relationships .
     *
     * @param _class of type {@link org.eclipse.uml2.uml.Class}.
     * @return List<ClassRelation> list to class attribute as form of {@link no.simule.models.cd.ClassRelation}.
     */

    private ArrayList<ClassRelation> readClassRelations(Class _class) {
        ArrayList<ClassRelation> list = new ArrayList<ClassRelation>();
        EList<Relationship> classRelationships = _class.getRelationships();
        for (Relationship relationship : classRelationships) {
            if (relationship.eClass() == UMLPackage.Literals.ASSOCIATION) {
                Association association = (Association) relationship;
                list.add(readAssociation(association));
            } else if (relationship.eClass() == UMLPackage.Literals.GENERALIZATION) {
                Generalization generalization = (Generalization) relationship;
                list.add(readGeneralization(generalization));
            }

        }
        return list;
    }


    private ClassRelation readGeneralization(Generalization generalization) {

        ClassRelation relation = new ClassRelation();
        relation.setType(Keywords.Generalization);
        boolean first = true;
        for (Element elements : generalization.getRelatedElements()) {
            if (elements instanceof Class) {
                ClassImpl relationClass = (ClassImpl) elements;
                if (first) {
                    first = false;
                    relation.setClass_1(relationClass.getName());
                } else {
                    relation.setClass_2(relationClass.getName());
                }

            }
        }
        classDiagram.addRelationship(relation);
        return relation;
    }


    private ClassRelation readAssociation(Association association) {


        ClassRelation relation = new ClassRelation();
        relation.setType(Keywords.Association);
        boolean first = true;
        for (Property end : association.getMemberEnds()) {
            if (end.getType() instanceof org.eclipse.uml2.uml.Class) {
                if (first) {
                    first = false;
                    relation.setVisibility(end.getVisibility().toString());

                    relation.setClass_1(end.getType().getName());
                    if (end.getName() != null && !end.getName().isEmpty()) {
                        relation.setRole_Name_1(end.getName());
                    } else {
                        relation.setRole_Name_1("");
                    }
                    relation.setNavigable_1(end.isNavigable());


                    org.eclipse.uml2.uml.LiteralUnlimitedNatural upperValue =
                            (org.eclipse.uml2.uml.LiteralUnlimitedNatural) end.getUpperValue();
                    if (upperValue != null) {
                        relation.setMultipcity_Uper_1(upperValue.getValue());
                    }

                    if (end instanceof org.eclipse.uml2.uml.LiteralUnlimitedNatural) {

                        org.eclipse.uml2.uml.LiteralUnlimitedNatural lowerValue =
                                (org.eclipse.uml2.uml.LiteralUnlimitedNatural) end.getLowerValue();
                        relation.setMultipcity_Lower_1(lowerValue.getValue());

                    }


                } else {

                    relation.setClass_2(end.getType().getName());
                    if (end.getName() != null && !end.getName().isEmpty()) {
                        relation.setRole_Name_2(end.getName());
                    } else {
                        relation.setRole_Name_2("");
                    }
                    relation.setNavigable_2(end.isNavigable());

                    org.eclipse.uml2.uml.LiteralUnlimitedNatural upperValue =
                            (org.eclipse.uml2.uml.LiteralUnlimitedNatural) end.getUpperValue();
                    if (upperValue != null) {
                        relation.setMultipcity_Uper_2(upperValue.getValue());
                    }

                    if (end instanceof org.eclipse.uml2.uml.LiteralUnlimitedNatural) {

                        org.eclipse.uml2.uml.LiteralUnlimitedNatural lowerValue =
                                (org.eclipse.uml2.uml.LiteralUnlimitedNatural) end.getLowerValue();
                        relation.setMultipcity_Lower_2(lowerValue.getValue());
                    }

                    return relation;
                }

            }

        }

        classDiagram.addRelationship(relation);
        return relation;
    }


    private boolean attributeInterfaceCollection(String proxyFragment) {
        boolean isCollection = false;
        if (proxyFragment != null && !proxyFragment.isEmpty()) {
            if (proxyFragment.contains("java.util.List") || proxyFragment.contains("java.util.ArrayList")) {
                isCollection = true;
            }
        }
        return isCollection;
    }

    private String attributeInterface(String proxyFragment) {
        String arrtibuteType = "";
        boolean isCollection = false;
        if (proxyFragment != null && !proxyFragment.isEmpty()) {

            String collectionType = "";
            if (proxyFragment.contains("java.util.List")) {
                isCollection = true;
                collectionType = "java.util.List";
            } else if (proxyFragment.contains("java.util.ArrayList")) {
                isCollection = true;
                collectionType = "java.util.ArrayList";
            }


            if (isCollection) {
                int startIndex = proxyFragment.indexOf(collectionType + "[project^id=");
                startIndex += (collectionType + "[project^id=").length();
                String temp = arrtibuteType =
                        proxyFragment.substring(startIndex, proxyFragment.indexOf("]$uml.Interface"));
                arrtibuteType = collectionType + "<" + temp + ">";

            } else {

                int startIndex = proxyFragment.indexOf(collectionType + "[project^id=");
                startIndex += ("[project^id=").length();

                arrtibuteType =
                        proxyFragment.substring(startIndex, proxyFragment.indexOf("]$uml.Interface"));
            }


        }

        return arrtibuteType;
    }

}
