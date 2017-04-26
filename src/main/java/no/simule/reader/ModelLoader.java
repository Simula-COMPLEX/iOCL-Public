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

import no.simule.models.ModelFile;
import no.simule.models.cd.ClassStructure;
import no.simule.exception.ValidationParseException;
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
import org.eclipse.ocl.OCL;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.OCLHelper;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.util.UMLUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * The Class ModelLoader Util class witch provide methods for reading uml models, creation of uml instances,
 * validation and evaluation of uml constraint .
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-04-15
 */
public class ModelLoader {

    private transient static final Logger logger = Logger.getLogger(ModelLoader.class);
    public static Queue<String> expresstionStack = new LinkedList<>();
    private String constraint = null;
    private InstanceGenerator instanceGenerator = new InstanceGenerator();
    private ResourceSet RESOURCE_SET = null;
    private Resource resource = null;
    private Package _package = null;
    private OCL ocl = null;

    public ModelLoader() {
        RESOURCE_SET = new ResourceSetImpl();
        EMFProperties.registerPackages(getResourceSet());
        EMFProperties.registerResourceFactories();
    }

    /**
     * This method read both UML and Ecore Model and Trasform into UML package.
     *
     * @param file of type ModelFile witch contain UML model as input String.
     * @return {@link org.eclipse.uml2.uml.Package}.
     */

    public Package loadModel(ModelFile file) throws Exception {

        URL url = getClass().getClassLoader().getResource("org.eclipse.uml2.uml_5.1.0.v20150906-1225.jar");

        if (url != null) {
            URI uri = URI.createURI(url.getPath());

            URIConverter.URI_MAP.put(URI.createURI("platform:/plugin/org.eclipse.uml2.uml/"), uri);
            EMFProperties.registerPathmaps(uri);
        }


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
                        UMLUtil.convertFromEcore(_package2, UML2EcoreConverterProperties.optionsToProcess());

                if (ecorePackages != null && !ecorePackages.isEmpty()) {
                    _package = (Package) ecorePackages.toArray()[0];
                }

            }
        }

        return _package;


    }

    /**
     * This method convert EMF package into {@link java.io.OutputStream} so user can download model.
     *
     * @return {@link java.io.ByteArrayOutputStream}.
     */
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


    /**
     * This method validation and evaluate ocl constraint with help of eclipse ocl.
     *
     * @param structure      of type ClassStructure contain context package and instances.
     * @param constraintType type of ocl constraint inv, body , pre ,post.
     * @param expression     is ocl constraint .
     * @param evaluate       flag to determinate need to evaluate or not.
     * @return Object true in case of validation and message .
     * @throws ParserException          if constraint not parsed and contains errors
     * @throws ValidationParseException if constraint type not defied or extracted
     */


    public Object validateConstraint(ClassStructure structure, String constraintType, String expression, boolean evaluate)
            throws ValidationParseException, ParserException, NullPointerException {
        expression = expression.replace("  ", " ");
        System.out.println(expression);


        if (_package != null) {

            Package contextPackage = getPackage(structure);

            Class context = (Class) contextPackage.getOwnedType(structure.getName());

            List<InstanceSpecification> instanceSpecifications = getInstanceSpecifications(_package.getPackagedElements(), context);


            OCLHelper helper = ocl.createOCLHelper();

            helper.setContext(context);
            helper.setValidating(true);


            if (constraintType.equals(Keywords.PRE_Condition_Constraint)
                    || constraintType.equals(Keywords.POST_Condition_Constraint)) {
                setContextOperation(expression, context, helper);
            }


            OCLExpressionSolver oclExpressionSolver = new OCLExpressionSolver(constraint);
            OCLExpression query = oclExpressionSolver.parseConstraint(constraintType, expression, helper);

            if (constraintType.equals(Keywords.INVARIANT_Constraint)) {
                Constraint contextOwnedRule = context.createOwnedRule(constraint);
                context.getOwnedRules().add(contextOwnedRule);
            }


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

                    }
                    return message;
                }
                return null;
            }
            return true;
        } else {
            logger.error("UML Package is null");
        }
        return null;
    }


    /**
     * This method create a new instance of class and add into instances package.
     *
     * @param instanceName name of new instance
     * @param structure    detail about class of instance
     * @return {@link java.io.ByteArrayOutputStream}.
     */

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


    /**
     * This method Uml packages and fetch instances of context constraint context class.
     *
     * @param packageableElements name of new instance
     * @param context             detail about class of instance
     * @return List<InstanceSpecification>list of Uml Instances.
     */

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

    /**
     * this method set ocl method into context class
     *
     * @param expression ocl method body
     * @param context    context of ocl constraint
     * @param helper     ocl helper
     * @return Nothing.
     */
    private void setContextOperation(String expression, Class context, OCLHelper helper) {
        String oppname =
                expression.substring(expression.indexOf("::") + 2, expression.indexOf("(")).trim();
        System.out.println("Opperation Name : " + oppname);
        for (Operation operation : context.getOperations()) {
            if (operation.getName().equals(oppname)) {
                helper.setOperationContext(context, operation);
            }
        }
    }


    private ResourceSet getResourceSet() {
        if (RESOURCE_SET == null) {
            RESOURCE_SET = new ResourceSetImpl();
            EMFProperties.registerPackages(getResourceSet());
            EMFProperties.registerResourceFactories();
        }
        return RESOURCE_SET;
    }


}
