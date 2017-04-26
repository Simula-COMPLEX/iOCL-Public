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

import no.simule.models.cd.ClassAttribute;
import no.simule.models.cd.ClassOperation;
import no.simule.models.cd.OperationReturn;
import no.simule.utils.Keywords;
import no.simule.utils.Mappings;
import no.simule.utils.iOCLTypes;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ManagedBean(name = "propertySelectPanel")
@SessionScoped
public class PropertySelectPanel implements Serializable {
    public static final String Empty = "";
    public static final String Space = " ";
    public static final String Dot = ".";
    public static final String Arrow = "->";
    public static final String Comma = ",";
    public static final String Apostrophe = "'";
    private static final long serialVersionUID = 1L;
    private QueryListener bean;
    private PanelBean panel;
    private PropertyBean property;

    public PropertySelectPanel() {

        FacesContext context = FacesContext.getCurrentInstance();
        bean = context.getApplication().evaluateExpressionGet(context, "#{bean}", QueryListener.class);

        panel = context.getApplication().evaluateExpressionGet(context, "#{panel}", PanelBean.class);

        property =
                context.getApplication().evaluateExpressionGet(context, "#{property}", PropertyBean.class);


    }


    public void backPropertySelectListener(AjaxBehaviorEvent event) {
        bean.selection();
        QueryListener queryListener = bean.beanFromStack();
        if (queryListener != null) {
            bean.replace(queryListener);
            property.setScope(Empty);

        }
    }

    /**
     * Ajax Listener when Property select from dropdown
     **/
    public void propertyListener(AjaxBehaviorEvent event) {
        if (property.getProperty() == null || property.getProperty().isEmpty()) {
            return;
        }

        bean.beanInStack();
        bean.selection();
        if (!bean.getPriorityStack().empty()) {
            panel.showClosePriorityOperationPanel();
        } else {
            panel.hideClosePriorityOperationPanel();
        }

        if (bean.getPropertyStack().size() < 1) {
            compersionsProperty(); //property use for compersions
        } else {
            argumentProperty(); //property use for arguments of function
        }


        Collections.sort(property.getOperations());
        panel.hideAddPriorityOperationPanel();
        property.setGlobalProperty(property.getProperty());
        property.setProperty(Empty);
    }


    /**
     * function handle the property  which is use for compersions
     **/
    private void compersionsProperty() {
        boolean firstTime = false;
        if (property.getConstraint().trim().endsWith(Dot)) {
            navigatingAttribute(); //condition  navigation class attribute
        } else {
            startAttribute();  //condition  class  attribute
            firstTime = true; //first time do navigation
        }

        if (property.getGlobalValueType().equals(Empty) || property.getGlobalValueType().equals("value")) {
            leftSideProperty(firstTime); //constraint leftSide
        } else {
            rightSideProperty(firstTime); //constraint right Side
        }


    }


    /**
     * when attribute come with dot at end
     */

    private void navigatingAttribute() {
        property.setConstraint(property.getConstraint() + property.getProperty());
    }

    /**
     * when attribute come with condition at end (new constraint)
     */

    private void startAttribute() {

        property.setConstraint(
                property.getConstraint() + Space + property.getRef() + Dot + property.getProperty());


    }

    /**
     * handle property before condtion (left)
     */

    private void leftSideProperty(boolean firstTime) {
        property.getOperations().removeAll(property.getOperations());
        ClassAttribute ca = bean.attributeObject(property.getContext(), property.getProperty());

        if (property.getGlobalScpoe().equals(Keywords.Local_Scope)) {

            if (ca != null) {


                if (!property.getTempContext().equals(Empty)) {
                    property.setContext(property.getTempContext());
                    property.setRef(property.getTempRef());
                    property.setTempRef(Empty);
                    property.setTempContext(Empty);
                }

                if (!property.getNavigationContext().equals(Empty)) {
                    property.setContext(property.getNavigationContext());
                    property.setNavigationContext(Empty);
                }


                property.setPropertyType(ca.getType());
            }
            panel.showOperationTypePanel();
        } else if (property.getGlobalScpoe().equals(Keywords.Assosiation_Scope)) {
            if (ca.isClass()) {
                property.setPropertyType(ca.getType());
            }
            if (ca.isCollection()) {
                bean.setOperationsMap(Mappings.getCollectionOperations());
                property.getOperations().addAll(bean.getOperationsMap().keySet());
                panel.showCollectionOperationPanel();
            } else {
                property.setConstraint(property.getConstraint().trim() + Dot);
                panel.showScopePanel();
                //&& !property.getRef().equals(Keywords.SELF)
                if (firstTime) {
                    property.setNavigationRef(property.getRef());
                    property.setNavigationContext(property.getContext());
                }
                property.setContext(property.getPropertyType());
            }
        }
    }

    /**
     * handle property after condtion (right)
     */
    private void rightSideProperty(boolean firstTime) {
        property.setValueType(Empty);
        panel.showClosePriorityOperationPanel();

        ClassAttribute ca = bean.attributeObject(property.getContext(), property.getProperty());
        if (property.getGlobalScpoe().equals(Keywords.Assosiation_Scope)) {
            if (ca.isClass()) {
                property.setPropertyType(ca.getType());
            }
            if (ca.isCollection()) {
                bean.setOperationsMap(Mappings.getCollectionOperations());
                for (String opp : bean.getOperationsMap().keySet()) {
                    if (Mappings.getCollectionNotItreate().get(opp) != null && Mappings.getCollectionNotItreate().get(opp)) {
                        property.getOperations().add(opp);
                    }
                }

                bean.setRightSide(true);
                panel.showCollectionOperationPanel();

            } else {
                property.setConstraint(property.getConstraint().trim() + Dot);
                panel.showScopePanel();
                if (firstTime) {
                    property.setNavigationContext(property.getContext());
                }
                property.setContext(property.getPropertyType());
            }
        } else {
            //local scope


            if (!property.getTempContext().equals(Empty)) {
                property.setContext(property.getTempContext());
                property.setRef(property.getTempRef());
                property.setTempRef(Empty);
                property.setTempContext(Empty);
            }

            //need to test

            if (!property.getNavigationContext().equals(Empty)) {
                property.setContext(property.getNavigationContext());
                property.setNavigationContext(Empty);
            }

            panel.showConditionPanel();
        }
    }


    /**
     * function handle the property  hich for argument of class operation or arithmetic Operation
     **/

    private void argumentProperty() {

        if (bean.getPropertyStack().size() == 1) {
            lastArgument();
        } else if (bean.getPropertyStack().size() > 1) {

            String ref = property.getRef();
            if (property.getContext().equals(property.getGlobalContext())) {
                ref = Keywords.SELF;
            }

            property.setConstraint(property.getConstraint() + ref + Dot + property.getProperty());

            multipleArguments();
        }
    }

    /**
     * function handle last argument property
     **/

    private void lastArgument() {
        if (property.getGlobalClassOperation().equals(Empty)) {
            arithmeticOperationArgumentLastArgument();
        } else {
            classOperationArgumentLastArgument();
        }
    }

    /**
     * function handle last argument of Arithmetic Operation
     **/
    private void arithmeticOperationArgumentLastArgument() {
        if (property.getGlobalScpoe().equals(Keywords.Local_Scope)) {
            String ref = property.getRef();
            if (property.getContext().equals(property.getGlobalContext())) {
                ref = Keywords.SELF;
            }
            if (Mappings.getArithmeticOperationsType().get(property.getGlobalOperation())
                    .equals(iOCLTypes.objectType)) {
                property.setConstraint(property.getConstraint() + ref + Dot + property.getProperty() + Space + ")" + Space);
            } else {
                property.setConstraint(property.getConstraint() + ref + Dot + property.getProperty() + Space);
            }
            bean.getPropertyStack().pop();
            property.setPropertyType(
                    Mappings.getArithmeticOperationsReturn().get(property.getGlobalOperation()));
            panel.showOperationTypePanel();
        } else if (property.getGlobalScpoe().equals(Keywords.Assosiation_Scope)) {
            argumentsNavigation();


        }

    }

    /**
     * function handle last argument of Class Operation
     **/

    private void classOperationArgumentLastArgument() {
        bean.getPropertyStack().pop();
        String methodName = property.getGlobalClassOperation()
                .substring(0, property.getGlobalClassOperation().indexOf("(")).trim();

        List<ClassOperation> cms = bean.propertyOperation(property.getContext(), methodName);
        if (cms == null || cms.isEmpty()) {
            cms = bean.propertyOperation(property.getTempContext(), methodName);
        }
        if (cms != null && !cms.isEmpty()) {
            ClassOperation cm = cms.get(0);

            if (cm.getReturnType() != null) {
                OperationReturn methodReturn = cm.getReturnType();
                if (methodReturn.isClass() && isClass(methodReturn.getType())) {
                    property.setGlobalScpoe(Keywords.Assosiation_Scope);

                    String ref = property.getRef();
                    if (property.getContext().equals(property.getGlobalContext())) {
                        ref = Keywords.SELF;
                    }
                    if (property.getConstraint().trim().endsWith(Dot)) {
                        property.setConstraint(property.getConstraint()
                                + Dot + property.getProperty() + Space + ")" + Dot);
                    } else {
                        property.setConstraint(property.getConstraint() + ref + Dot + property.getProperty() + Space + ")" + Dot);
                    }


                    String attributeType = cm.getReturnType().getType();
                    boolean collection = cm.getReturnType().isCollection();
                    computePanel(attributeType, collection);

                } else {
                    property.setGlobalScpoe(Keywords.Local_Scope);
                    String ref = property.getRef();
                    if (property.getContext().equals(property.getGlobalContext())) {
                        ref = Keywords.SELF;
                    }
                    if (property.getConstraint().trim().endsWith(Dot)) {
                        property.setConstraint(property.getConstraint()
                                + Dot + property.getProperty() + Space + ")" + Space);
                    } else {
                        property.setConstraint(
                                property.getConstraint()
                                        + ref + Dot + property.getProperty() + Space + ")" + Space);
                    }
                    panel.showOperationTypePanel();
                }
                property.setPropertyType(cm.getReturnType().getType());
            }
        }

    }


    private boolean isClass(String attributeType) {
        if (attributeType.equals(iOCLTypes.EBooleanObject) || attributeType.equals(iOCLTypes.EString) || attributeType.equals(iOCLTypes.EIntegerObject)
                || attributeType.equals(iOCLTypes.Boolean) || attributeType.equals(iOCLTypes.String) || attributeType.equals(iOCLTypes.Integer) ||
                attributeType.equals(iOCLTypes.Real) || attributeType.equals(iOCLTypes.Double)) {
            return false;
        }
        return true;
    }

    private void multipleArguments() {
        if (property.getGlobalScpoe().equals(Keywords.Local_Scope)) {
            arguments();
        } else if (property.getGlobalScpoe().equals(Keywords.Assosiation_Scope)) {
            argumentsNavigation();
        }
    }


    private void arguments() {
        if (Mappings.getArithmeticOperationsType().get(property.getGlobalOperation())
                .equals(iOCLTypes.objectType)) {
            property.setConstraint(property.getConstraint() + Space + Comma + Space);
        }
        bean.getPropertyStack().pop();
        panel.showComparisonTypePanel();
    }

    private void argumentsNavigation() {
        property.setOperations(new ArrayList<>());
        ClassAttribute ca = bean.attributeObject(property.getContext(), property.getProperty());
        if (ca.isClass()) {
            property.setPropertyType(ca.getType());
        }

        if (ca.isCollection()) {
            bean.setOperationsMap(Mappings.getCollectionOperations());
            property.getOperations().addAll(bean.getOperationsMap().keySet());
            panel.showCollectionOperationPanel();
        } else {
            property.setConstraint(property.getConstraint().trim() + Dot);
            panel.showScopePanel();
            if (!bean.getContextStack().contains(property.getRef() + ":" + property.getContext())) {
                bean.getContextStack().add(property.getRef() + ":" + property.getContext());
            }
            property.setContext(property.getPropertyType());
        }

    }

    private void computePanel(String attributeType, boolean collection) {
        property.setContext(attributeType);
        if (collection) {
            bean.setOperationsMap(Mappings.getCollectionOperations());
            property.getOperations().addAll(bean.getOperationsMap().keySet());
            panel.showCollectionOperationPanel();
            if (!bean.getContextStack().contains(property.getRef() + ":" + property.getContext())) {
                bean.getContextStack().add(property.getRef() + ":" + property.getContext());
            }
        } else {
            if (!bean.getContextStack().contains(property.getRef() + ":" + property.getContext())) {
                bean.getContextStack().add(property.getRef() + ":" + property.getContext());
            }

            panel.showScopePanel();
        }
    }


}
