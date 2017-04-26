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

import no.simule.models.cd.ClassOperation;
import no.simule.utils.Keywords;
import no.simule.utils.Mappings;
import no.simule.utils.iOCLTypes;
import org.primefaces.context.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.util.List;

@ManagedBean(name = "basicValuePanel")
@SessionScoped
public class BasicValuePanel extends ActionListener {

    private QueryListener bean;
    private PanelBean panel;
    private PropertyBean property;

    public BasicValuePanel() {

        FacesContext context = FacesContext.getCurrentInstance();
        bean = context.getApplication().evaluateExpressionGet(context, "#{bean}", QueryListener.class);

        panel = context.getApplication().evaluateExpressionGet(context, "#{panel}", PanelBean.class);
        property =
                context.getApplication().evaluateExpressionGet(context, "#{property}", PropertyBean.class);


    }

    public void backValueListener(AjaxBehaviorEvent event) {
        bean.selection();
        QueryListener queryListener = bean.beanFromStack();
        if (queryListener != null) {
            bean.replace(queryListener);
            property.setValueType(Empty);

        }
    }

    public void valueListener(AjaxBehaviorEvent event) {
        bean.beanInStack();
        bean.basicValue();
        if (!bean.getPriorityStack().empty()) {
            panel.showClosePriorityOperationPanel();
        } else {
            panel.hideClosePriorityOperationPanel();
        }

        if (bean.getPropertyStack().size() < 1) {
            valueChange();
        } else {
            valueChangeStack();
        }

        property.setValue(Empty);
    }


    private void valueChange() {
        if (property.getPropertyType().equals(iOCLTypes.String)
                || property.getPropertyType().equalsIgnoreCase(iOCLTypes.EString)) {
            property.setConstraint(
                    property.getConstraint() + Space + Apostrophe + property.getValue() + Apostrophe + Space);
            panel.showClosePriorityOperationPanel();
            panel.showConditionPanel();
        } else if (property.getPropertyType().equals(iOCLTypes.Integer)
                || property.getPropertyType().equalsIgnoreCase(iOCLTypes.EIntegerObject)) {
            try {
                Integer.parseInt(property.getValue());
                property.setConstraint(property.getConstraint() + Space + property.getValue() + Space);
                panel.showClosePriorityOperationPanel();
                panel.showConditionPanel();
            } catch (NumberFormatException e) {

                FacesMessage message =
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Enter Integer Value");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
                FacesContext.getCurrentInstance().addMessage(null, message);
                property.setValue(Empty);
                panel.showValuePanel();
            }

        } else if (property.getPropertyType().equals(iOCLTypes.Boolean)) {

            if ("true".equals(property.getValue()) || "false".equals(property.getValue())) {
                property.setConstraint(property.getConstraint() + Space + property.getValue() + Space);
                panel.showClosePriorityOperationPanel();
                panel.showConditionPanel();
            } else {

                FacesMessage message =
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Enter Boolean Value");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
                FacesContext.getCurrentInstance().addMessage(null, message);
                property.setValue(Empty);
                panel.showValuePanel();
            }


        } else {
            property.setConstraint(property.getConstraint() + Space + property.getValue() + Space);
            panel.showClosePriorityOperationPanel();
            panel.showConditionPanel();
        }
    }


    private void valueChangeStack() {

        if (bean.getPropertyStack().size() == 1) {

            valueStackLast();

        } else if (bean.getPropertyStack().size() > 1) {
            valueStackChain();
        }
    }

    private void valueStackChain() {
        bean.getPropertyStack().pop();

        if (property.getPropertyType().equals(iOCLTypes.String)) {
            property.setConstraint(property.getConstraint() + Space + Apostrophe + property.getValue()
                    + Apostrophe + Comma + Space);
        } else {
            property
                    .setConstraint(property.getConstraint() + Space + property.getValue() + Comma + Space);
        }
        panel.showValuePanel();
    }

    private void valueStackLast() {
        if (property.getGlobalClassOperation().equals(Empty)) {

            arithmeticOperationArgumentStackLast();
        } else {
            classOperationArgumentStackLast();
        }
    }

    private void arithmeticOperationArgumentStackLast() {
        bean.getPropertyStack().pop();
        if (Mappings.getArithmeticOperationsType().get(property.getGlobalOperation())
                .equals(iOCLTypes.primitiveType)) {

            if (property.getPropertyType().equals(iOCLTypes.String)) {
                property.setConstraint(property.getConstraint() + Space + Apostrophe + property.getValue()
                        + Apostrophe + Space);
            } else {
                property.setConstraint(property.getConstraint() + Space + property.getValue() + Space);
            }
        } else {

            if (property.getPropertyType().equals(iOCLTypes.String)) {
                property.setConstraint(property.getConstraint() + Space + Apostrophe + property.getValue()
                        + Apostrophe + Space + ")" + Space);
            } else {
                property.setConstraint(
                        property.getConstraint() + Space + property.getValue() + Space + ")" + Space);
            }

        }

        property.setPropertyType(
                Mappings.getArithmeticOperationsReturn().get(property.getGlobalOperation()));
        panel.showOperationTypePanel();

    }

    private void classOperationArgumentStackLast() {
        bean.getPropertyStack().pop();
        String methodName = property.getGlobalClassOperation()
                .substring(0, property.getGlobalClassOperation().indexOf("(")).trim();
        List<ClassOperation> cms = bean.propertyOperation(property.getContext(), methodName);
        if (cms != null && !cms.isEmpty()) {
            ClassOperation cm = cms.get(0);

            if (cm.getReturnType() != null && cm.getReturnType().isClass()) {
                property.setGlobalScpoe(Keywords.Assosiation_Scope);
                property.setConstraint(property.getConstraint() + Space + property.getValue() + Space + ")" + Dot);

                String attributeType = cm.getReturnType().getType();
                boolean collection = cm.getReturnType().isCollection();
                computePanel(attributeType, collection);

            } else {
                property.setGlobalScpoe(Keywords.Local_Scope);
                property.setConstraint(property.getConstraint()
                        + property.getValue() + Space + ")" + Space);

                panel.showOperationTypePanel();
            }
            property.setPropertyType(cm.getReturnType().getType());
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

