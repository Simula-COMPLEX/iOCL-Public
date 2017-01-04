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

import no.simule.dataobjects.cd.ClassOperation;
import no.simule.dataobjects.cd.OperationReturn;
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

@ManagedBean(name = "parameterSelect")
@SessionScoped
public class ParameterSelectPanel implements Serializable {
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

    public ParameterSelectPanel() {

        FacesContext context = FacesContext.getCurrentInstance();
        bean = context.getApplication().evaluateExpressionGet(context, "#{bean}", QueryListener.class);

        panel = context.getApplication().evaluateExpressionGet(context, "#{panel}", PanelBean.class);

        property =
                context.getApplication().evaluateExpressionGet(context, "#{property}", PropertyBean.class);


    }


    public void backParameterListener(AjaxBehaviorEvent event) {
        bean.selection();
        QueryListener queryListener = bean.beanFromStack();
        if (queryListener != null) {
            bean.replace(queryListener);
            property.setValueType(Empty);
            property.setParameter(Empty);
        }
    }

    public void parameterListener(AjaxBehaviorEvent event) {
        if (property.getParameter() == null || property.getParameter().isEmpty()) {
            return;
        }
        bean.beanInStack();
        bean.selection();

        String token[] = property.getParameter().split(":");
        String parameter = token[0];
        String parameterType = token[1];
        boolean collection = parameterType.contains("Set");
        if (collection) {
            parameterType = parameterType.substring(parameterType.indexOf("(") + 1, parameterType.indexOf(")")).trim();
        }

        if (isClass(parameterType)) {
            property.setGlobalScpoe(Keywords.Assosiation_Scope);
        } else {
            property.setGlobalScpoe(Keywords.Local_Scope);
        }

        if (!bean.getPriorityStack().empty()) {
            panel.showClosePriorityOperationPanel();
        } else {
            panel.hideClosePriorityOperationPanel();
        }

        if (bean.getPropertyStack().isEmpty()) {
            compersionsProperty(parameter, parameterType, collection); //property use for compersions
        } else {
            argumentProperty(parameter, parameterType, collection); //property use for arguments of function
        }


        Collections.sort(property.getOperations());
        panel.hideAddPriorityOperationPanel();
        property.setGlobalProperty(property.getParameter());
        property.setParameter(Empty);


    }


    /**
     * function handle the property  which is use for compersions
     **/
    private void compersionsProperty(String parameter, String parameterType, boolean collection) {

        property.setConstraint(
                property.getConstraint() + Space + parameter);

        if (property.getGlobalValueType().equals("paremeter")) {
            rightSideProperty(parameter, parameterType, collection); //constraint right Side
        } else {
            leftSideProperty(parameter, parameterType, collection); //constraint left Side
        }

    }

    /**
     * handle property before condtion (left)
     */

    private void leftSideProperty(String parameter, String parameterType, boolean collection) {
        property.getOperations().removeAll(property.getOperations());

        if (property.getGlobalScpoe().equals(Keywords.Local_Scope)) {

            property.setPropertyType(parameterType);

            panel.showOperationTypePanel();
        } else if (property.getGlobalScpoe().equals(Keywords.Assosiation_Scope)) {
            if (isClass(parameterType)) {
                property.setPropertyType(parameterType);
            }
            if (collection) {
                bean.setOperationsMap(Mappings.getCollectionOperations());
                property.getOperations().addAll(bean.getOperationsMap().keySet());
                panel.showCollectionOperationPanel();
            } else {
                property.setContext(parameterType);
                property.setConstraint(property.getConstraint().trim() + Dot);
                panel.showScopePanel();
            }
        }
    }

    /**
     * handle property after condtion (right)
     */
    private void rightSideProperty(String parameter, String parameterType, boolean collection) {
        property.setValueType(Empty);
        panel.showClosePriorityOperationPanel();

        // ClassAttribute ca = bean.attributeObject(property.getContext(), property.getProperty());

        if (property.getGlobalScpoe().equals(Keywords.Assosiation_Scope)) {
            if (isClass(parameterType)) {
                property.setPropertyType(parameterType);
            }
            if (collection) {
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
                property.setContext(property.getPropertyType());
            }
        } else {
            panel.showConditionPanel();
        }
    }


    /**
     * function handle the property  hich for argument of class operation or arithmetic Operation
     **/

    private void argumentProperty(String parameter, String parameterType, boolean collection) {

        if (bean.getPropertyStack().size() == 1) {
            lastArgument(parameter, parameterType, collection);
        } else if (bean.getPropertyStack().size() > 1) {
            multipleArguments(parameter, parameterType, collection);
        }
    }


    private void multipleArguments(String parameter, String parameterType, boolean collection) {
        if (property.getGlobalScpoe().equals(Keywords.Local_Scope)) {
            arguments();
        } else if (property.getGlobalScpoe().equals(Keywords.Assosiation_Scope)) {
            argumentsNavigation(parameter, parameterType, collection);
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


    private void argumentsNavigation(String parameter, String parameterType, boolean collection) {
        property.setOperations(new ArrayList<>());
        if (isClass(parameterType)) {
            property.setPropertyType(parameterType);
        }
        if (collection) {
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


    /**
     * function handle last argument property
     **/

    private void lastArgument(String parameter, String parameterType, boolean collection) {
        if (property.getGlobalClassOperation().equals(Empty)) {
            arithmeticOperationArgumentLastArgument(parameter, parameterType, collection);
        } else {
            classOperationArgumentLastArgument(parameter, parameterType, collection);
        }
    }

    /**
     * function handle last argument of Arithmetic Operation
     **/
    private void arithmeticOperationArgumentLastArgument(String parameter, String parameterType, boolean collection) {
        if (property.getGlobalScpoe().equals(Keywords.Local_Scope)) {
            if (Mappings.getArithmeticOperationsType().get(property.getGlobalOperation())
                    .equals(iOCLTypes.objectType)) {
                property.setConstraint(property.getConstraint() + parameter + Space + ")" + Space);
            } else {
                property.setConstraint(property.getConstraint() + parameter + Space);
            }
            bean.getPropertyStack().pop();
            property.setPropertyType(
                    Mappings.getArithmeticOperationsReturn().get(property.getGlobalOperation()));
            panel.showOperationTypePanel();
        } else if (property.getGlobalScpoe().equals(Keywords.Assosiation_Scope)) {
            argumentsNavigation(parameter, parameterType, collection);
        }

    }


    /**
     * function handle last argument of Class Operation
     **/

    private void classOperationArgumentLastArgument(String parameter, String parameterType, boolean collection) {
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


                    if (!property.getConstraint().trim().endsWith(Dot)) {
                        property.setConstraint(property.getConstraint() + parameter + Space + ")" + Dot);
                    }


                    String returnAttributeType = cm.getReturnType().getType();
                    boolean returnColelttion = cm.getReturnType().isCollection();
                    computePanel(returnAttributeType, returnColelttion);

                } else {
                    property.setGlobalScpoe(Keywords.Local_Scope);

                    if (!property.getConstraint().trim().endsWith(Dot)) {
                        property.setConstraint(
                                property.getConstraint()
                                        + parameter + Space + ")" + Space);
                    }
                    panel.showOperationTypePanel();
                }
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
