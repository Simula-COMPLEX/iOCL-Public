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

package no.simule.loader;

import no.simule.dataobjects.ModelFile;
import no.simule.dataobjects.cd.ClassStructure;
import no.simule.exception.ValidationParseException;
import no.simule.loader.extend.HelperUtilImpl;
import no.simule.loader.extend.OCLHelperImpl;
import no.simule.utils.Keywords;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.emf.mapping.ecore2xml.Ecore2XMLPackage;
import org.eclipse.emf.mapping.ecore2xml.util.Ecore2XMLResource;
import org.eclipse.ocl.OCL;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.uml.ExpressionInOCL;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.resource.UML212UMLResource;
import org.eclipse.uml2.uml.resource.UML22UMLResource;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.util.UMLUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ModelLoader {

    private transient static final Logger logger = Logger.getLogger(ModelLoader.class);
    public static Queue<String> expresstionStack = new LinkedList();
    String constraint = null;
    InstanceGenerator instanceGenerator = new InstanceGenerator();
    private ResourceSet RESOURCE_SET = null;
    private Resource resource = null;
    private Package _package = null;
    private OCL ocl = null;

    public ModelLoader() {
        RESOURCE_SET = new ResourceSetImpl();
        registerPackages(getResourceSet());
        registerResourceFactories();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void registerResourceFactories() {
        Map extensionFactoryMap = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
        extensionFactoryMap.put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);

        extensionFactoryMap.put(Ecore2XMLResource.FILE_EXTENSION, Ecore2XMLResource.Factory.INSTANCE);
        extensionFactoryMap.put("ecore", new EcoreResourceFactoryImpl());

        extensionFactoryMap.put(UML22UMLResource.FILE_EXTENSION, UML22UMLResource.Factory.INSTANCE);
        extensionFactoryMap.put(UMLResource.FILE_EXTENSION, UML22UMLResource.Factory.INSTANCE);
        extensionFactoryMap.put(UMLResource.FILE_EXTENSION, UML22UMLResource.Factory.INSTANCE);

        extensionFactoryMap.put("xml", UMLResource.Factory.INSTANCE);

        extensionFactoryMap.put("xml", new XMLResourceFactoryImpl());

        extensionFactoryMap.put("xmi", UMLResource.Factory.INSTANCE);
        extensionFactoryMap.put("xmi", new XMIResourceFactoryImpl());

        extensionFactoryMap.put("genmodel", new XMIResourceFactoryImpl());

    }

  /*
   * This function inputs uri, load model and return that model.
   */

    private static void registerPackages(ResourceSet resourceSet) {
        Map<String, Object> packageRegistry = resourceSet.getPackageRegistry();
        packageRegistry.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
        packageRegistry.put(Ecore2XMLPackage.eNS_URI, Ecore2XMLPackage.eINSTANCE);
        packageRegistry.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
        packageRegistry.put(UML212UMLResource.UML_METAMODEL_NS_URI, UMLPackage.eINSTANCE);

        packageRegistry.put("http://www.eclipse.org/uml2/1.0.0/UML", UMLPackage.eINSTANCE);
        packageRegistry.put("http://www.eclipse.org/uml2/2.0.0/UML", UMLPackage.eINSTANCE);
        packageRegistry.put("http://www.eclipse.org/uml2/3.0.0/UML", UMLPackage.eINSTANCE);
        packageRegistry.put("http://www.eclipse.org/uml2/4.0.0/UML", UMLPackage.eINSTANCE);
        packageRegistry.put("http://www.eclipse.org/uml2/5.0.0/UML", UMLPackage.eINSTANCE);

        packageRegistry.put("http://www.omg.org/spec/UML/20061001", UMLPackage.eINSTANCE);
        packageRegistry.put("http://www.omg.org/spec/UML/20080501", UMLPackage.eINSTANCE);
        packageRegistry.put("http://www.omg.org/spec/UML/20101101", UMLPackage.eINSTANCE);
        packageRegistry.put("http://www.omg.org/spec/UML/20110701", UMLPackage.eINSTANCE);
        packageRegistry.put("http://www.omg.org/spec/UML/20131001", UMLPackage.eINSTANCE);

    }

    /*
     * This function converts input path to uri and return uri.
     */
    public String getFileURI(String path) throws Exception {
        File f = new File(path);
        String uri = f.toURI().toString();
        return uri;
    }

    /**
     * @param file
     * @return
     */

    public Package loadModel(ModelFile file) throws Exception {

        String relPath = null;
        try {
            relPath = new File(".").getCanonicalPath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

      /*  URI uri = URI.createURI(
                "jar:file:" + relPath + "jars/uml/org.eclipse.uml2.uml_5.1.0.v20150906-1225.jar!/");
*/
        URI uri = URI.createURI(getClass().getClassLoader().getResource("org.eclipse.uml2.uml_5.1.0.v20150906-1225.jar").getPath());

        URIConverter.URI_MAP.put(URI.createURI("platform:/plugin/org.eclipse.uml2.uml/"), uri);
        // registerPathmaps(uri);

        org.eclipse.ocl.uml.OCL.initialize(getResourceSet());
        ocl = org.eclipse.ocl.uml.OCL.newInstance(getResourceSet());
        ocl.setParseTracingEnabled(true);
        Map<String, Object> options = new HashMap<String, Object>();
        options.put(XMLResource.OPTION_EXTENDED_META_DATA, Boolean.TRUE);
        options.put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
        options.put(XMLResource.OPTION_ENCODING, "UTF-8");


        resource =
                getResourceSet().createResource(URI.createFileURI(System.currentTimeMillis() + ".xmi"));
        resource.load(file.getModel(), options);

        _package = (Model) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.MODEL);

        if (_package == null) {
            _package =
                    (Package) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE);
        }
        if (_package == null) {


            EPackage _package2 = (EPackage) EcoreUtil.getObjectByType(resource.getContents(),
                    EcorePackage.Literals.EPACKAGE);
            if (_package2 != null) {
                Collection<Package> ecorePackages =
                        UMLUtil.convertFromEcore(_package2, ConvertProperties.optionsToProcess());

                if (ecorePackages != null && !ecorePackages.isEmpty()) {
                    _package = (Package) ecorePackages.toArray()[0];
                }

            }
        }

        return _package;


    }

    public ByteArrayOutputStream exportModel() {
        try {

            String _packageUri = _package.getURI();
            logger.info(_packageUri);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            resource = getResourceSet().createResource(_package.eResource().getURI());
            resource.getContents().add(_package);
            resource.save(outputStream, null);

            return outputStream;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean createInstace(String instanceName, ClassStructure structure) {


        if (_package != null) {

            Package contextPackage = getPackage(structure);

            Class context = (Class) contextPackage.getOwnedType(structure.getName());

            InstanceSpecification instanceSpecifications = instanceGenerator.createInstanceSpecification(instanceName, contextPackage, context, structure);

            contextPackage.getPackagedElements().add(instanceSpecifications);

            return true;
        }
        return false;
    }


    public Object validateConstraint(ClassStructure structure, String constraintType, String expression, boolean evaluate)
            throws ValidationParseException, ParserException, NullPointerException {
        expression = expression.replace("  ", " ");
        System.out.println(expression);


        if (_package != null) {

            Package contextPackage = getPackage(structure);

            Class context = (Class) contextPackage.getOwnedType(structure.getName());

            List<InstanceSpecification> instanceSpecifications = getInstanceSpecifications(context);


            // OCLHelper helper = ocl.createOCLHelper();


            OCLHelperImpl helper = HelperUtilImpl.createOCLHelper(ocl);
            helper.setContext(context);
            helper.setValidating(true);


            if (constraintType.equals(Keywords.PRE_Condition_Constraint)
                    || constraintType.equals(Keywords.POST_Condition_Constraint)) {
                setContextOperation(expression, context, helper);
            }


            OCLExpression query = parseConstraint(constraintType, expression, helper);


            if (constraintType.equals(Keywords.INVARIANT_Constraint)) {
                Constraint contextOwnedRule = context.createOwnedRule(constraint);
                context.getOwnedRules().add(contextOwnedRule);
            }

           /* if (constraintType.equals(Keywords.Def_Operation)) {
                // hammad(bur : Burning, cus : Customer) : Set(Burning)

                String operationExpress = expression.substring(expression.indexOf(":") + 1, expression.indexOf("=")).trim();
                String operationName = operationExpress.substring(0, operationExpress.indexOf("("));
                String returnTypeExpress = operationExpress.substring(operationExpress.indexOf(")") + 1, operationExpress.length()).trim();
                String argumentExpress = operationExpress.substring(operationExpress.indexOf("(") + 1, operationExpress.indexOf(")")).trim();

                EList<String> names = new BasicEList<>();
                EList<Type> types = new BasicEList<>();

                if (!argumentExpress.isEmpty()) {



                    for (String argument : argumentExpress.split(",")) {
                        String tokens[] = argument.split(":");
                        String name = tokens[0].trim();
                        String type = tokens[1].trim();

                        boolean collection = false;
                        if (returnTypeExpress.contains("Set")) {
                            collection = true;
                            type = returnTypeExpress.substring(type.indexOf("(") + 1, type.indexOf(")")).trim();

                        }

                        if (type.equals(iOCLTypes.Integer)) {
                            names.add(name);
                           // PrimitiveType primitiveType= UMLFactory.eINSTANCE.createPrimitiveType();
                           UMLFactory exception= (UMLFactory) EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/uml2/5.0.0/UML");
                            PrimitiveType primitiveType=exception.createPrimitiveType();
                            primitiveType.setName(iOCLTypes.Integer);
                            types.add(primitiveType);
                        }


                    7}
                }

                String returnType=returnTypeExpress.trim();
                boolean returnCollection=false;
                if(returnTypeExpress.contains("Set")){
                    returnCollection=true;
                     returnType=returnTypeExpress.substring(returnTypeExpress.indexOf("(")+1,returnTypeExpress.indexOf(")")).trim();
                }


               Operation operation=context.createOwnedOperation(operationName,names,types );
                context.getOwnedOperations().add(operation);

            }*/

            if (evaluate && query != null) {
                if (!instanceSpecifications.isEmpty()) {

                    StringBuilder message = new StringBuilder();

                    for (InstanceSpecification instanceSpecification : instanceSpecifications) {


                        Object result = ocl.evaluate(instanceSpecification, query);
                        System.out.print(result);

                        message.append("Instance Name: ");
                        message.append(instanceSpecification.getName());
                        message.append(" , ");
                        message.append("Result: ");
                        message.append(result);
                        message.append("    ");
                        //                       return result;
                    }

                    return message;

                }
                return null;
            }

            // code(helper,constraint);
            return true;


        } else {

            logger.error("UML Package is null");
        }


        return null;
    }


    private OCLExpression parseConstraint(String constraintType, String expression, OCLHelperImpl helper) throws ValidationParseException, ParserException, NullPointerException {


        OCLExpression query = null;


        if (constraintType.equals(Keywords.Query_Constraint)) {

            query = helper.createQuery(expression);

        } else if (constraintType.equals(Keywords.INVARIANT_Constraint)) {

            query = invarinat(constraintType, expression, helper);

        } else if (constraintType.equals(Keywords.PRE_Condition_Constraint)) {
            query = pre(constraintType, expression, helper);

        } else if (constraintType.equals(Keywords.POST_Condition_Constraint)) {

            query = post(constraintType, expression, helper);
        } else if (constraintType.equals(Keywords.Operation_Body)) {

            query = body(constraintType, expression, helper);

        } else if (constraintType.equals(Keywords.Def_Operation)) {

            query = def(constraintType, expression, helper);


        } else {
            throw new ValidationParseException("not processed ");
        }


        return query;
    }


    private OCLExpression invarinat(String constraintType, String expression, OCLHelperImpl helper) throws ValidationParseException, ParserException, NullPointerException {
        int index = constraintIndex(expression, Keywords.INVARIANT_Constraint);
        if (index > 0) {
            constraint = expression.substring(index).trim();
            System.out.println(constraint);
            Constraint oclConstraint = (Constraint) helper.createInvariant(constraint);

            OCLExpression query = null;
            if (oclConstraint.getSpecification() instanceof ExpressionInOCL) {
                ExpressionInOCL expressionInOCL = (ExpressionInOCL) oclConstraint.getSpecification();
                query = expressionInOCL.getBodyExpression();
            }

            if (query == null) {
                query = helper.createQuery(constraint);
            }

            return query;

        } else {
            throw new ValidationParseException("\"inv:\" OR \"inv :\" not found");
        }
    }


    private OCLExpression pre(String constraintType, String expression, OCLHelperImpl helper) throws ValidationParseException, ParserException, NullPointerException {
        int index = constraintIndex(expression, Keywords.PRE_Condition_Constraint);
        if (index > 0) {
            constraint = expression.substring(index).trim();
            System.out.println(constraint);


            Constraint oclConstraint = (Constraint) helper.createPrecondition(constraint);

            OCLExpression query = null;
            if (oclConstraint.getSpecification() instanceof ExpressionInOCL) {
                ExpressionInOCL expressionInOCL = (ExpressionInOCL) oclConstraint.getSpecification();
                query = expressionInOCL.getBodyExpression();
            }

            if (query == null) {
                query = helper.createQuery(constraint);
            }

            return query;

        } else {
            throw new ValidationParseException("\"pre:\" OR \"pre :\" not found");
        }
    }

    private OCLExpression post(String constraintType, String expression, OCLHelperImpl helper) throws ValidationParseException, ParserException, NullPointerException {
        int index = constraintIndex(expression, Keywords.POST_Condition_Constraint);
        if (index > 0) {
            constraint = expression.substring(index).trim();
            System.out.println(constraint);


            Constraint oclConstraint = (Constraint) helper.createPostcondition(constraint);

            OCLExpression query = null;
            if (oclConstraint.getSpecification() instanceof ExpressionInOCL) {
                ExpressionInOCL expressionInOCL = (ExpressionInOCL) oclConstraint.getSpecification();
                query = expressionInOCL.getBodyExpression();
            }

            if (query == null) {
                query = helper.createQuery(constraint);
            }

            return query;


        } else {
            throw new ValidationParseException("\"post:\" OR \"post :\" not found");
        }
    }

    private OCLExpression body(String constraintType, String expression, OCLHelperImpl helper) throws ValidationParseException, ParserException, NullPointerException {
        int index = constraintIndex(expression, Keywords.Operation_Body);
        if (index > 0) {
            constraint = expression.substring(index).trim();
            System.out.println(constraint);
            Constraint oclConstraint = (Constraint) helper.createBodyCondition(constraint);
            OCLExpression query = null;
            if (oclConstraint.getSpecification() instanceof ExpressionInOCL) {
                ExpressionInOCL expressionInOCL = (ExpressionInOCL) oclConstraint.getSpecification();
                query = expressionInOCL.getBodyExpression();
            }
            if (query == null) {
                query = helper.createQuery(constraint);
            }
            return query;

        } else {
            throw new ValidationParseException("\"body:\" OR \"body :\" not found");
        }
    }


    private OCLExpression def(String constraintType, String expression, OCLHelperImpl helper) throws ValidationParseException, ParserException, NullPointerException {
        int index = constraintIndex(expression, Keywords.Def_Operation);
        if (index > 0) {
            constraint = expression.substring(index).trim();
            helper.defineOperation(constraint);
            OCLExpression query = null;
            query = helper.createQuery(constraint);
            return query;
        } else {
            throw new ValidationParseException("\"def:\" OR \"def :\" not found");
        }
    }


    private int constraintIndex(String expression, String type) {
        int index = 0;
        if (expression.contains(type + ":")) {
            index = expression.indexOf(type + ":") + type.length() + 1;
        } else if (expression.contains(type + " :")) {
            index = expression.indexOf(type + " :") + type.length() + 2;
        } else if (expression.contains(type)) {
            index = expression.indexOf(type) + type.length();
            String temp = expression.substring(index).trim();
            if (temp.contains(":")) {
                index = index + temp.indexOf(":") + 1;
            } else {
                index = 0;
            }
        }
        return index;
    }


    @SuppressWarnings("unused")
    private void registerPathmaps(URI uri) {
        URIConverter.URI_MAP.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP),
                uri.appendSegment("libraries").appendSegment(""));
        URIConverter.URI_MAP.put(URI.createURI(UMLResource.METAMODELS_PATHMAP),
                uri.appendSegment("metamodels").appendSegment(""));
        URIConverter.URI_MAP.put(URI.createURI(UMLResource.PROFILES_PATHMAP),
                uri.appendSegment("profiles").appendSegment(""));

        URIConverter.URI_MAP.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP),
                uri.appendSegment("libraries").appendSegment(""));

        URIConverter.URI_MAP.put(URI.createURI(UMLResource.METAMODELS_PATHMAP),
                uri.appendSegment("metamodels").appendSegment(""));

        URIConverter.URI_MAP.put(URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI),
                uri.appendSegment("libraries").appendSegment("UMLPrimitiveTypes.library.uml"));

        URIConverter.URI_MAP.put(URI.createURI(UMLResource.UML_METAMODEL_URI),
                uri.appendSegment("metamodels").appendSegment("UML.metamodel.uml"));

        URIConverter.URI_MAP.put(URI.createURI(UMLResource.UML2_PROFILE_URI),
                uri.appendSegment("profiles").appendSegment("UML2.profile.uml"));

    }

    public ResourceSet getResourceSet() {
        if (RESOURCE_SET == null) {
            RESOURCE_SET = new ResourceSetImpl();
            registerPackages(getResourceSet());
            registerResourceFactories();
        }
        return RESOURCE_SET;
    }


    private Package getPackage(ClassStructure cs) {
        EList<PackageableElement> packageableElements = _package.getPackagedElements();
        String packageName = _package.getName() != null ? _package.getName() : "";
        Package contextPackage = null;

        if (packageName.equalsIgnoreCase(cs.getPackage())) {

            Class _class = (Class) _package.getOwnedType(cs.getName());
            if (_class != null && _class.getName() != null) {
                contextPackage = _package;
            } else {
                contextPackage =
                        getPackage(packageableElements, packageName, cs.getPackage());
            }

        } else {
            contextPackage = getPackage(packageableElements, packageName, cs.getPackage());
        }
        return contextPackage;
    }

    private Package getPackage(EList<PackageableElement> packageableElements, String packageName,
                               String contextPackage) {

        for (PackageableElement element : packageableElements) {

            if (element.eClass() == UMLPackage.Literals.PACKAGE) {
                Package _package = (Package) element;

                String newPackageName = _package.getName() != null
                        ? (packageName.equals("") ? _package.getName() : packageName + "." + _package.getName())
                        : packageName;

                if (newPackageName.equalsIgnoreCase(contextPackage)) {
                    return _package;
                }

                getPackage(_package.getPackagedElements(), newPackageName, contextPackage);

            }
        }

        return null;
    }


    private List<InstanceSpecification> getInstanceSpecifications(Class context) {
        List<InstanceSpecification> instanceSpecifications = getInstanceSpecifications(_package.getPackagedElements(), context);
        return instanceSpecifications;
    }


    private List<InstanceSpecification> getInstanceSpecifications(EList<PackageableElement> packageableElements, Class context) {
        List<InstanceSpecification> instanceSpecifications = new ArrayList<>();
        for (PackageableElement element : packageableElements) {
            if (element.eClass() == UMLPackage.eINSTANCE.getInstanceSpecification()) {
                InstanceSpecification instance = (InstanceSpecification) element;
                for (Classifier classifier : instance.getClassifiers()) {

                    if (classifier != null) {
                        if (classifier.eClass() == UMLPackage.Literals.CLASS) {
                            if (classifier.getName().equals(context.getName())) {
                                //System.out.println(instance.getName());
                                instanceSpecifications.add(instance);
                            }
                        }
                    }

                }
            } else if (element.eClass() == UMLPackage.Literals.PACKAGE) {
                Package _nustedPackage = (Package) element;
                instanceSpecifications.addAll(getInstanceSpecifications(_nustedPackage.getPackagedElements(), context));
            }
        }
        return instanceSpecifications;
    }

    private void setContextOperation(String expression, Class context, OCLHelperImpl helper) {
        String oppname =
                expression.substring(expression.indexOf("::") + 2, expression.indexOf("(")).trim();
        System.out.println("Opperation Name : " + oppname);
        for (Operation operation : context.getOperations()) {
            if (operation.getName().equals(oppname)) {
                helper.setOperationContext(context, operation);
            }
        }
    }


}
