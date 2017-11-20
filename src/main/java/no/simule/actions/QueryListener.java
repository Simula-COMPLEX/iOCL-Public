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


package no.simule.actions;


import no.simule.models.ModelFile;
import no.simule.models.cd.*;
import no.simule.models.enm.EnumStructure;
import no.simule.reader.ModelReader;
import no.simule.reader.ModelLoader;
import no.simule.utils.Mappings;
import no.simule.utils.iOCLTypes;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.eclipse.uml2.uml.Package;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.Part;
import java.io.*;
import java.util.*;

/**
 * The Class QueryListener is a action class for contains multiple method for reading uml models, changing the states,
 * and states variable which contain states of constraint and model information which need to determine next states.
 * ocl constraint.
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-04-15
 */
@ManagedBean(name = "bean", eager = true)
@SessionScoped
public class QueryListener extends ActionListener {

    public transient static final String BEAN_NAME = "bean";
    private transient static final Logger logger = Logger.getLogger(QueryListener.class);
    private transient InputStream model;

    private int depthlevel = 3;


    private transient long startTime;
    private transient int count = 0;
    private transient ModelLoader umlModel = new ModelLoader();
    private Stack<String> conditionStack = new Stack<String>();
    private Stack<String> priorityStack = new Stack<String>();
    private Stack<String> propertyStack = new Stack<String>();
    private Stack<String> prameterStack = new Stack<String>();
    private List<String> contextStack = new ArrayList<String>();


    private boolean rightSide = false;

    private transient Stack<byte[]> beanStack = new Stack<byte[]>();
    private transient Part diagram;
    private transient ClassDiagram cd;

    private HashMap<String, String> operationsMap = new HashMap<String, String>();
    private String result;

    @ManagedProperty(value = "#{panel}")
    private PanelBean panel;
    @ManagedProperty(value = "#{property}")
    private PropertyBean property;
    @ManagedProperty(value = "#{statics}")
    private StaticsBean statics;

    @ManagedProperty(value = "#{modelExplorer}")
    private ModelExplorerPanel modelExplorer;

    public QueryListener() {
        Mappings.initialize();
    }


    @PostConstruct
    public void postConstruct() {

    }


    public void preRenderView() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            // resetAll();
        }
    }


    public void doNothing(AjaxBehaviorEvent event) {
        logger.debug("doNothing");
    }

    public void contextChangeListener(AjaxBehaviorEvent event) {

        if (property.getRefContext() == null && property.getRefContext().isEmpty()) {
            return;
        }

        property.setTempContext(property.getContext());
        property.setTempRef(property.getRef());

        String tokens[] = property.getRefContext().split(":");
        String ref = tokens[0];
        String context = tokens[1];
        property.setRef(ref);
        property.setContext(context);
        property.setRefContext(Empty);
    }


    public void backContextOperationListener(AjaxBehaviorEvent event) {
        selection();
        logger.debug("back Context Operation Listener");
        QueryListener queryListener = beanFromStack();
        if (queryListener != null) {
            replace(queryListener);
            property.setConstraintType(Empty);

        }
    }


    public void contextOperationListener(AjaxBehaviorEvent event) {
        logger.debug("context Operation Listener");
        if (property.getContextOperation() == null || property.getContextOperation().isEmpty()) {
            return;
        }
        beanInStack();
        selection();
        property.setConstraint(property.getConstraint() + "::" + property.getContextOperation() + Space
                + property.getConstraintType() + ": ");


        property.setGlobalContextOperation(property.getContextOperation());
        property.setContextOperation(Empty);
        property.setConstraintType(Empty);
        panel.showAddPriorityOperationPanel();
        panel.showScopePanel();
    }


    public void panelSwitch(AjaxBehaviorEvent event) {
        beanInStack();
        selection();
        logger.debug("panelSwitch");
        String data = (String) event.getComponent().getAttributes().get("data");
        logger.debug("active Panel " + data);
        switch (data) {
            case "scope":
                panel.activatePanel(PanelBean.scopePanel);
                break;
            case "operation":
                panel.activatePanel(PanelBean.operationTypePanel);
                break;
            case "comparison":
                panel.activatePanel(PanelBean.comparisonTypePanel);
                break;
            case "condition":
                panel.activatePanel(PanelBean.conditionPanel);
                break;
            case "context":
                panel.activatePanel(PanelBean.contextPanel);
                break;
            default:
                break;
        }

    }

    void selection() {
        statics.addCost(startTime);
        statics.addSelection();
    }

    void basicValue() {
        statics.addCost(startTime);
        statics.addBasicInput();
    }

    void textValue() {
        statics.addCost(startTime);
        statics.addTextInput();
    }


    ClassStructure classObject(String className) {
        if (cd != null) {
            for (ClassStructure cs : cd.getClasses()) {

                if (cs.getName().equalsIgnoreCase(className)) {
                    return cs;
                }
            }
        }
        return null;
    }


    EnumStructure enumObject(String className) {
        if (cd != null) {
            for (EnumStructure es : cd.getEnumerations()) {

                if (es.getName().equalsIgnoreCase(className)) {
                    return es;
                }
            }
        }
        return null;
    }

    List<ClassOperation> propertyOperation(String className, String operation) {
        List<ClassOperation> classmethods = new ArrayList<>();
        if (classObject(className) == null) {
            return null;
        }
        for (ClassOperation cm : classObject(className).getOperations()) {
            if (cm.getName().equalsIgnoreCase(operation)) {
                classmethods.add(cm);
            }
        }
        return classmethods;
    }


    ClassOperation operationObject(String className, String operation) {
        ClassStructure cs = classObject(className);
        if (cs == null) {
            return null;
        }
        if (operation == null || operation.isEmpty()) {
            return null;
        }

        Map<String, String> operationParms = new HashMap<>();
        String operationName = operation.substring(0, operation.indexOf("(")).trim();
        String parmsString = operation.substring(operation.indexOf("(") + 1, operation.indexOf(")")).trim();
        String operationReturnString = operation.substring(operation.indexOf(")") + 1).trim();
        parmsString = parmsString.replace(" ", "");
        if (!parmsString.isEmpty()) {
            String parms[] = parmsString.split(",");
            //String parms[] = parmsString.split("[|]");
            for (String parm : parms) {

                String tokens[] = parm.split(":");
                String name = tokens[0];
                String type = tokens[1];
                operationParms.put(name, type);
            }
        }
        for (ClassOperation classOperation : cs.getOperations()) {
            boolean isOperation = false;
            if (classOperation.getName().equalsIgnoreCase(operationName)) {
                for (OperationParameter parameter :
                        classOperation.getParameters()) {
                    if (operationParms.containsKey(parameter.getName())) {
                        String type = operationParms.get(parameter.getName());

                        if (type.contains("Set")) {

                            if (parameter.isCollection() || type.contains(parameter.getType())) {
                                isOperation = true;

                            }
                        } else {
                            if (parameter.getType().equals(type)) {
                                isOperation = true;
                            }
                        }
                    } else {
                        isOperation = false;
                        break;
                    }

                }
            }

            if (isOperation) {
                return classOperation;
            }
        }


        ClassOperation classOperation = new ClassOperation();
        classOperation.setName(operationName);
        for (String name : operationParms.keySet()) {
            OperationParameter parameter = new OperationParameter();
            parameter.setName(name);
            String type = operationParms.get(parameter.getName());
            if (type.contains("Set")) {
                parameter.setCollection(true);
                type = type.substring(type.indexOf("(") + 1, type.indexOf(")"));
            }
            parameter.setType(type);
            parameter.setClass(isClass(type));

            classOperation.addParameter(parameter);
        }
        if (operationReturnString != null && !operationReturnString.isEmpty()) {
            OperationReturn operationReturn = new OperationReturn();
            if (operationReturnString.contains("Set")) {
                operationReturn.setCollection(true);
                operationReturnString = operationReturnString.substring(operationReturnString.indexOf("(") + 1, operationReturnString.indexOf(")"));
            }
            operationReturn.setType(operationReturnString);
            operationReturn.setClass(isClass(operationReturnString));

            classOperation.setReturnType(operationReturn);
        }

        return classOperation;
    }

    private boolean isClass(String attributeType) {
        if (attributeType.equals(iOCLTypes.EBooleanObject) || attributeType.equals(iOCLTypes.EString) || attributeType.equals(iOCLTypes.EIntegerObject)
                || attributeType.equals(iOCLTypes.Boolean) || attributeType.equals(iOCLTypes.String) || attributeType.equals(iOCLTypes.Integer) ||
                attributeType.equals(iOCLTypes.Real) || attributeType.equals(iOCLTypes.Double)) {
            return false;
        }
        return true;
    }

    /***
     * This method is used to find attribute object from class and superClass
     *
     * @param className name Of Class
     * @param attribute name of Attribute
     * @return ClassAttribute this method find the attribute object
     */
    ClassAttribute attributeObject(String className, String attribute) {
        ClassStructure cs = classObject(className);
        if (cs == null) {
            return null;
        }
        for (ClassAttribute ca : cs.getAttributes()) {
            if (ca.getName().equalsIgnoreCase(attribute)) {
                return ca;
            }
        }

        return null;
    }


    void beanInStack() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();

            beanStack.push(byteData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public QueryListener beanFromStack() {
        if (!beanStack.empty()) {

            try {

                byte[] byteData = beanStack.pop();
                ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
                return (QueryListener) new ObjectInputStream(bais).readObject();

            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void replace(QueryListener queryListener) {
        this.priorityStack = queryListener.priorityStack;
        this.propertyStack = queryListener.propertyStack;
        this.conditionStack = queryListener.conditionStack;
        this.contextStack = queryListener.contextStack;
        this.prameterStack = queryListener.prameterStack;


        this.operationsMap = queryListener.operationsMap;

        property.replace(queryListener.getProperty());
        this.panel.activatePanel(queryListener.panel.getActivePanel());
    }

    void reset() {
        property.reset();

        this.priorityStack = new Stack<>();
        this.propertyStack = new Stack<>();
        this.conditionStack = new Stack<>();
        this.contextStack = new ArrayList<>();
        this.prameterStack = new Stack<>();

        beanStack = new Stack<>();
        operationsMap = new HashMap<>();
        result = Empty;
        count = 0;
        startTime = 0;
        depthlevel = 1;
        panel.reset();
        statics.reset();
    }

    public void resetAll() {
        umlModel = new ModelLoader();
        setCd(null);
        modelExplorer.setRoot(null);
        modelExplorer.setSelectedNode(null);
        reset();
        panel.resetAll();
        statics.reset();
        property.setContexts(new ArrayList<>());
    }


    public void royalModel() {

        resourceModel("RoyalAndLoyal.uml");

    }


    public void umlModel() {
        resourceModel("UML.uml");


    }


    private void resourceModel(String name) {
        if (cd == null) {
            try {
                model = getClass().getClassLoader().getResourceAsStream(name);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = model.read(buffer)) > -1) {
                    baos.write(buffer, 0, len);
                }
                baos.flush();

                model = new BufferedInputStream(new ByteArrayInputStream(
                        baos.toByteArray()));

                InputStream model2 = new BufferedInputStream(new ByteArrayInputStream(
                        baos.toByteArray()));

                load(model2, name);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void loadModel() {
        resetAll();
        try {
            if (diagram != null) {
                model = diagram.getInputStream();


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = model.read(buffer)) > -1) {
                    baos.write(buffer, 0, len);
                }
                baos.flush();


                model = new BufferedInputStream(new ByteArrayInputStream(
                        baos.toByteArray()));


                InputStream model2 = new BufferedInputStream(new ByteArrayInputStream(
                        baos.toByteArray()));


                load(model2, diagram != null ? diagram.getSubmittedFileName() : "");
                // CodeGenration code = new CodeGenration();
                // code.genrateClass(cd.getClasses());

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Model Not Selected", ""));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void load(InputStream stream, String name) throws Exception {
        property.setContexts(new ArrayList<>());


        ModelReader read = new ModelReader();
        if (stream != null) {

            String ext = FilenameUtils.getExtension(name);
            if (ext.equals("uml") || ext.equals("xmi") || ext.equals("ecore")) {


                Package _package = umlModel.loadModel(new ModelFile(stream));
                cd = read.readUMLPackage(_package);
                Collections.sort(cd.getClasses(), new Comparator<ClassStructure>() {
                    @Override
                    public int compare(ClassStructure o1, ClassStructure o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });


                for (ClassStructure cs : cd.getClasses()) {
                    property.getContexts().add(cs.getName());
                }
                Collections.sort(property.getContexts());
                modelExplorer.loadtree(cd);

                panel.showContextPanel();
                panel.collapseUpdatesPanel();
                panel.uncollapseModelPanel();

                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Model Upload"));
                logger.info("Model Upload");

            } else {
                logger.info("Update UML Model with .uml extension");
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                        "Update UML Model with .uml extension"));

            }
        } else {
            logger.info("UML Model Is null");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "UML Model Is null"));

        }


    }


    @PreDestroy
    public void distroy() {

    }


    public int getDepthlevel() {
        return depthlevel;
    }

    public void setDepthlevel(int depthlevel) {
        this.depthlevel = depthlevel;
    }


    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ModelLoader getUmlModel() {
        return umlModel;
    }

    public void setUmlModel(ModelLoader umlModel) {
        this.umlModel = umlModel;
    }

    public Stack<String> getConditionStack() {
        return conditionStack;
    }

    public void setConditionStack(Stack<String> conditionStack) {
        this.conditionStack = conditionStack;
    }

    public Stack<String> getPriorityStack() {
        return priorityStack;
    }

    public void setPriorityStack(Stack<String> priorityStack) {
        this.priorityStack = priorityStack;
    }

    public Stack<String> getPropertyStack() {
        return propertyStack;
    }

    public void setPropertyStack(Stack<String> propertyStack) {
        this.propertyStack = propertyStack;
    }

    public Stack<String> getPrameterStack() {
        return prameterStack;
    }

    public void setPrameterStack(Stack<String> prameterStack) {
        this.prameterStack = prameterStack;
    }

    public List<String> getContextStack() {
        return contextStack;
    }

    public void setContextStack(List<String> contextStack) {
        this.contextStack = contextStack;
    }

    public boolean isRightSide() {
        return rightSide;
    }

    public void setRightSide(boolean rightSide) {
        this.rightSide = rightSide;
    }

    public Stack<byte[]> getBeanStack() {
        return beanStack;
    }

    public void setBeanStack(Stack<byte[]> beanStack) {
        this.beanStack = beanStack;
    }

    public Part getDiagram() {
        return diagram;
    }

    public void setDiagram(Part diagram) {
        this.diagram = diagram;
    }

    public ClassDiagram getCd() {
        return cd;
    }

    public void setCd(ClassDiagram cd) {
        this.cd = cd;
    }

    public HashMap<String, String> getOperationsMap() {
        return operationsMap;
    }

    public void setOperationsMap(HashMap<String, String> operationsMap) {
        this.operationsMap = operationsMap;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public PanelBean getPanel() {
        return panel;
    }

    public void setPanel(PanelBean panel) {
        this.panel = panel;
    }

    public PropertyBean getProperty() {
        return property;
    }

    public void setProperty(PropertyBean property) {
        this.property = property;
    }

    public StaticsBean getStatics() {
        return statics;
    }

    public void setStatics(StaticsBean statics) {
        this.statics = statics;
    }

    public ModelExplorerPanel getModelExplorer() {
        return modelExplorer;
    }

    public void setModelExplorer(ModelExplorerPanel modelExplorer) {
        this.modelExplorer = modelExplorer;
    }

    public InputStream getModel() {
        return model;
    }

    public void setModel(InputStream model) {
        this.model = model;
    }
}
