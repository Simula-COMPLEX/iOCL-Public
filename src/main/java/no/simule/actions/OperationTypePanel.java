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
import no.simule.models.enm.EnumStructure;
import no.simule.utils.Keywords;
import no.simule.utils.Mappings;
import no.simule.utils.iOCLTypes;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ManagedBean(name = "operationType")
@SessionScoped
public class OperationTypePanel implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient static final Logger logger = Logger.getLogger(OperationTypePanel.class);
    private transient static final String Empty = "";
    private transient static final String Space = " ";
    private transient static final String Dot = ".";
    private transient static final String Arrow = "->";
    private transient static final String Comma = ",";
    private transient static final String Apostrophe = "'";
    private transient static final String Colon = ":";
    private transient static final String LeftBracket = "(";
    private transient static final String RightBracket = ")";
    private transient static final String Bracket = "()";
    private transient static final String Equal = "=";
    private QueryListener bean;
    private PanelBean panel;
    private PropertyBean property;

    public OperationTypePanel() {

        FacesContext context = FacesContext.getCurrentInstance();

        bean = context.getApplication().evaluateExpressionGet(context, "#{bean}", QueryListener.class);

        panel = context.getApplication().evaluateExpressionGet(context, "#{panel}", PanelBean.class);

        property =
                context.getApplication().evaluateExpressionGet(context, "#{property}", PropertyBean.class);
    }

    public void backOperationTypeListener(AjaxBehaviorEvent event) {
        bean.selection();
        QueryListener queryListener = bean.beanFromStack();
        if (queryListener != null) {
            bean.replace(queryListener);
            property.setOperationType(Empty);

        }

    }
  /*
   * Ajax Listener when Property Scope is local select from radio button
   */

    public void operationTypeListener(AjaxBehaviorEvent event) {

        bean.beanInStack();
        bean.selection();
        property.setOperations(new ArrayList<>());

        ClassAttribute ca = bean.attributeObject(property.getContext(), property.getGlobalProperty());

        String attributeType = null;


        //this need to test
        if (property.getGlobalClassOperation().equals(Empty) && (!property.getGlobalOperation().equals(Empty) || !property.getGlobalProperty().equals(Empty))) {

            if (property.getPropertyType() != null && !property.getPropertyType().isEmpty()) {
                attributeType = property.getPropertyType();
            } else if (ca != null) {
                attributeType = ca.getType();
            }
        } else {
            String methodName = property.getGlobalClassOperation()
                    .substring(0, property.getGlobalClassOperation().indexOf(LeftBracket)).trim();

            List<ClassOperation> cms = bean.propertyOperation(property.getContext(), methodName);
            if (cms == null || cms.isEmpty()) {
                cms = bean.propertyOperation(property.getTempContext(), methodName);
            }
            if (cms != null && !cms.isEmpty()) {
                ClassOperation cm = cms.get(0);

                if (cm.getReturnType() != null) {
                    attributeType = cm.getReturnType().getType();
                }
                property.setGlobalClassOperation(Empty);
            }

        }


        if (property.getOperationType().equals(Keywords.Comparison_Operation)) {

            comparisonOperation(attributeType);
            Collections.sort(property.getOperations());
        } else if (property.getOperationType().equals(Keywords.Arithmetic_Operation)) {
            arithmeticOperation(attributeType);
            Collections.sort(property.getOperations());
        } else if (property.getOperationType().equals(Keywords.nullCheck)) {
            property.setConstraint(property.getConstraint() + Space + Equal + Space + "null" + Space);
            property.setOperationType(Empty);
            property.setGlobalOperationType(Empty);
            panel.showClosePriorityOperationPanel();
            panel.showConditionPanel();

        } else if (property.getOperationType().equals(Keywords.notNullCheck)) {
            property.setConstraint(property.getConstraint() + Space + "<>" + Space + "null" + Space);
            property.setOperationType(Empty);
            property.setGlobalOperationType(Empty);
            panel.showClosePriorityOperationPanel();
            panel.showConditionPanel();

        } else if (property.getOperationType().equals("@pre")) {
            property.setConstraint(property.getConstraint() + "@pre" + Space);
            property.setOperationType(Empty);
            property.setGlobalOperationType(Empty);
            panel.showOperationTypePanel();
        }


    }

    private void comparisonOperation(String attributeType) {

        if (attributeType.equalsIgnoreCase(iOCLTypes.Integer)
                || attributeType.equalsIgnoreCase(iOCLTypes.EIntegerObject)
                || attributeType.equalsIgnoreCase(iOCLTypes.Double)
                || attributeType.equalsIgnoreCase(iOCLTypes.EDoubleObject)) {
            bean.setOperationsMap(Mappings.getIntegerOperations());
            property.setPropertyType(iOCLTypes.Integer);
        } else if (attributeType.equalsIgnoreCase(iOCLTypes.String)
                || attributeType.equalsIgnoreCase(iOCLTypes.EString)) {
            bean.setOperationsMap(Mappings.getStringOperations());
            property.setPropertyType(iOCLTypes.String);
        } else if (attributeType.equalsIgnoreCase(iOCLTypes.Boolean)
                || attributeType.equalsIgnoreCase(iOCLTypes.EBooleanObject)) {
            bean.setOperationsMap(Mappings.getBooleanOperations());
            property.setPropertyType(iOCLTypes.Boolean);
        } else if (attributeType.equalsIgnoreCase(iOCLTypes.UnlimitedNatural)) {
            bean.setOperationsMap(Mappings.getUnlimitedNaturalOperations());
            property.setPropertyType(iOCLTypes.UnlimitedNatural);
        } else if (attributeType.equalsIgnoreCase(iOCLTypes.Real)) {
            bean.setOperationsMap(Mappings.getRealOperations());
            property.setPropertyType(iOCLTypes.Real);
        } else if (attributeType.equalsIgnoreCase(iOCLTypes.OclVoid)) {
            bean.setOperationsMap(Mappings.getOclVoidOperations());
            property.setPropertyType(iOCLTypes.OclVoid);
        } else if (attributeType.equalsIgnoreCase(iOCLTypes.OclInvalid)) {
            bean.setOperationsMap(Mappings.getOclInvalidOperations());
            property.setPropertyType(iOCLTypes.OclInvalid);
        } else {

            for (EnumStructure structure : bean.getCd().getEnumerations()) {
                if (structure.getName().equals(attributeType)) {
                    bean.setOperationsMap(Mappings.getEnumOperations());
                    property.setPropertyType(attributeType);
                }
            }

        }


        property.getOperations().addAll(bean.getOperationsMap().keySet());
        if (bean.getPriorityStack().empty()
                || (!bean.getPriorityStack().peek().equals("close") && !bean.getPriorityStack().peek().equals("return"))) {
            property.setContext(property.getGlobalContext());
        }
        panel.showOperationPanel();
        property.setOperationType(Empty);
        property.setGlobalOperationType(Empty);
    }

    private void arithmeticOperation(String attributeType) {

        if (attributeType.equalsIgnoreCase(iOCLTypes.Integer)
                || attributeType.equalsIgnoreCase(iOCLTypes.EIntegerObject)
                || attributeType.equalsIgnoreCase(iOCLTypes.Double)
                || attributeType.equalsIgnoreCase(iOCLTypes.EDoubleObject)) {
            bean.setOperationsMap(Mappings.getIntegerArithmeticOperations());
            property.setPropertyType(iOCLTypes.Integer);
        } else if (attributeType.equalsIgnoreCase(iOCLTypes.String)
                || attributeType.equalsIgnoreCase(iOCLTypes.EString)) {
            bean.setOperationsMap(Mappings.getStringArithmeticOperations());
            property.setPropertyType(iOCLTypes.String);
        } else if (attributeType.equalsIgnoreCase(iOCLTypes.Boolean)
                || attributeType.equalsIgnoreCase(iOCLTypes.EBooleanObject)) {
            bean.setOperationsMap(Mappings.getBooleanArithmeticOperations());
            property.setPropertyType(iOCLTypes.Boolean);
        } else if (attributeType.equalsIgnoreCase(iOCLTypes.Real)) {
            bean.setOperationsMap(Mappings.getRealArithmeticOperations());
            property.setPropertyType(iOCLTypes.Real);
        } else if (attributeType.equalsIgnoreCase(iOCLTypes.UnlimitedNatural)) {
            bean.setOperationsMap(Mappings.getUnlimitedNaturalArithmeticOperations());
            property.setPropertyType(iOCLTypes.UnlimitedNatural);
        }


        property.getOperations().addAll(bean.getOperationsMap().keySet());
        panel.showOperationPanel();


        property.setGlobalOperationType(property.getOperationType());
        property.setOperationType(Empty);
    }


}
