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

package no.simule.bean;

import no.simule.dataobjects.Document;
import no.simule.dataobjects.cd.*;
import no.simule.dataobjects.enm.EnumStructure;
import no.simule.utils.Keywords;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "selectionPanel")
@SessionScoped
public class SelectionPanel implements Serializable {
    public static final String Empty = "";
    public static final String Space = " ";
    public static final String Dot = ".";
    public static final String Arrow = "->";
    public static final String Comma = ",";
    public static final String Apostrophe = "'";
    private transient static final Logger logger = Logger.getLogger(SelectionPanel.class);
    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{bean}")
    private QueryListener bean;
    @ManagedProperty(value = "#{panel}")
    private PanelBean panel;
    @ManagedProperty(value = "#{property}")
    private PropertyBean property;
    @ManagedProperty(value = "#{contextPanel}")
    private ContextPanel contextPanel;
    @ManagedProperty(value = "#{propertySelectPanel}")
    private PropertySelectPanel propertySelectPanel;
    @ManagedProperty(value = "#{classOperationPanel}")
    private ClassOperationPanel classOperationPanel;

    @ManagedProperty(value = "#{modelExplorer}")
    private ModelExplorerPanel modelExplorer;


    private transient TreeNode selectedNode;
    private transient ClassStructure selectClassStructure;


    private transient ClassStructure classInstance;
    private transient String instanceName;


    public void onNodeContext() {
        logger.info("onNodeContext");
        logger.info(selectedNode);

        if (selectedNode != null) {
            selectClassStructure = bean.classObject(selectedNode.toString());

            if (selectClassStructure == null) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                        selectedNode + " is not a Class");
                FacesContext.getCurrentInstance().addMessage(null, message);

            }

            instanceName = "";
            try {
                classInstance = (ClassStructure) selectClassStructure.clone();
                for (ClassAttribute ca : classInstance.getAttributes()) {
                    ca.setValue(null);
                    ca.setValues(null);
                }

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        }


    }


    public List<String> getTypeAvailableInstance(String type) {
        List<String> instanceNames = new ArrayList<>();
        instanceNames.addAll(instancesNameList(type));
        return instanceNames;
    }


    private List<String> instancesNameList(String className) {
        List<String> instanceNames = new ArrayList<>();
        ClassStructure cs = bean.classObject(className);
        if (cs != null) {
            for (ClassInstance ci : cs.getInstances()) {
                instanceNames.add(ci.getName() + " : " + cs.getName());
            }

            for (ClassRelation classRelation : cs.getRelationships()) {
                if (classRelation.getType().equals(Keywords.Generalization) && classRelation.getClass_2().equals(className)) {
                    instanceNames.addAll(instancesNameList(classRelation.getClass_1()));
                }
            }

        }
        return instanceNames;
    }

    public List<String> getTypeAvailableEnum(String type) {
        List<String> enums = new ArrayList<>();
        EnumStructure es = bean.enumObject(type);
        if (es != null) {
            for (String litral : es.getLiterals()) {
                enums.add(litral);
            }
        }
        return enums;
    }


    public void createInstace() {
        logger.info("createInstace");
        if (instanceName != null && !instanceName.isEmpty()) {

            for (ClassAttribute ca : classInstance.getAttributes()) {

                if (ca.getValues() == null || ca.getValues().length == 0) {
                    if (ca.getValue() != null) {
                        Object[] values = new Object[1];
                        values[0] = ca.getValue();
                        ca.setValues(values);
                    }
                }

            }

            boolean result = bean.getUmlModel().createInstace(instanceName, classInstance);

            if (result) {
                ClassInstance newInstance = new ClassInstance();
                newInstance.setName(instanceName);

                for (ClassAttribute ca : classInstance.getAttributes()) {
                    newInstance.addAttribute(new InstanceAttribute(ca.getName(), ca.getType(), ca.getValues(), ca.isClass(), ca.isEnum(), ca.isCollection()));
                }

                selectClassStructure.getInstances().add(newInstance);

                for (TreeNode className : modelExplorer.getRoot().getChildren()) {

                    if (className.toString().equals(classInstance.getName())) {
                        for (TreeNode item : className.getChildren()) {
                            if (item.toString().equals("instances")) {
                                TreeNode treeInstance = new DefaultTreeNode("instance",
                                        new Document(instanceName, "-", "Folder"), item);
                                break;
                            }
                        }
                        break;
                    }
                }

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error",
                        "Instance Name \"" + instanceName + "\" is created");
                FacesContext.getCurrentInstance().addMessage(null, message);
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Instance Name is Empty");
            FacesContext.getCurrentInstance().addMessage(null, message);
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }


    }


    public void onNodeUnselect(NodeUnselectEvent event) {

    }

    public void onNodeSelect(NodeSelectEvent event) {
        bean.selection();

        if (panel.getActivePanel() == PanelBean.contextPanel) {
            onNodeContextSelect(event.getTreeNode().toString());
        } else if (panel.getActivePanel() == PanelBean.propertyPanel) {
            onNodeAttributeSelect(event.getTreeNode().toString());
        } else if (panel.getActivePanel() == PanelBean.classOperationSelectPanel) {
            onNodeOperationSelect(event.getTreeNode().toString());
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning",
                    "You can't Select context, property or operation at this moment");
            FacesContext.getCurrentInstance().addMessage(null, message);
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }


    }


    private void onNodeOperationSelect(String text) {

        if (property.getClassOperations().contains(text.trim())) {

            property.setClassOperation(text);
            classOperationPanel.classOperationsListener(null);

            FacesMessage message =
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", "Operation Selected " + text);
            FacesContext.getCurrentInstance().addMessage(null, message);

        } else {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Selected Operation " + text + " not in List");
            FacesContext.getCurrentInstance().addMessage(null, message);
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        }

    }

    private void onNodeContextSelect(String text) {

        if (property.getContexts().contains(text)) {

            property.setContext(text);
            contextPanel.contextListener(null);

            FacesMessage message =
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", "Context Selected " + text);
            FacesContext.getCurrentInstance().addMessage(null, message);

        } else {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Selected context " + text + " not in List");
            FacesContext.getCurrentInstance().addMessage(null, message);
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        }
    }

    private void onNodeAttributeSelect(String text) {

        if (property.getProperties().contains(text)) {
            property.setProperty(text);
            propertySelectPanel.propertyListener(null);
            FacesMessage message =
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", "Property Selected " + text);
            FacesContext.getCurrentInstance().addMessage(null, message);

        } else {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Selected Property " + text + " not in List");
            FacesContext.getCurrentInstance().addMessage(null, message);
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        }
    }


    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public ClassStructure getSelectClassStructure() {
        return selectClassStructure;
    }

    public void setSelectClassStructure(ClassStructure selectClassStructure) {
        this.selectClassStructure = selectClassStructure;
    }


    public ClassStructure getClassInstance() {
        return classInstance;
    }

    public void setClassInstance(ClassStructure classInstance) {
        this.classInstance = classInstance;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public ClassOperationPanel getClassOperationPanel() {
        return classOperationPanel;
    }

    public void setClassOperationPanel(ClassOperationPanel classOperationPanel) {
        this.classOperationPanel = classOperationPanel;
    }

    public PropertySelectPanel getPropertySelectPanel() {
        return propertySelectPanel;
    }

    public void setPropertySelectPanel(PropertySelectPanel propertySelectPanel) {
        this.propertySelectPanel = propertySelectPanel;
    }

    public ContextPanel getContextPanel() {
        return contextPanel;
    }

    public void setContextPanel(ContextPanel contextPanel) {
        this.contextPanel = contextPanel;
    }

    public PropertyBean getProperty() {
        return property;
    }

    public void setProperty(PropertyBean property) {
        this.property = property;
    }

    public PanelBean getPanel() {
        return panel;
    }

    public void setPanel(PanelBean panel) {
        this.panel = panel;
    }

    public QueryListener getBean() {
        return bean;
    }

    public void setBean(QueryListener bean) {
        this.bean = bean;
    }

    public ModelExplorerPanel getModelExplorer() {
        return modelExplorer;
    }

    public void setModelExplorer(ModelExplorerPanel modelExplorer) {
        this.modelExplorer = modelExplorer;
    }
}
