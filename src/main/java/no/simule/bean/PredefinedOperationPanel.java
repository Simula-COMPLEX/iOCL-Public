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

import no.simule.dataobjects.cd.ClassAttribute;
import no.simule.dataobjects.cd.ClassStructure;
import no.simule.dataobjects.enm.EnumStructure;
import no.simule.utils.Keywords;
import no.simule.utils.Mappings;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;

@ManagedBean(name = "predefined")
@SessionScoped
public class PredefinedOperationPanel implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient static final Logger logger = Logger.getLogger(PredefinedOperationPanel.class);
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

    public PredefinedOperationPanel() {

        FacesContext context = FacesContext.getCurrentInstance();

        bean = context.getApplication().evaluateExpressionGet(context, "#{bean}", QueryListener.class);

        panel = context.getApplication().evaluateExpressionGet(context, "#{panel}", PanelBean.class);

        property =
                context.getApplication().evaluateExpressionGet(context, "#{property}", PropertyBean.class);
    }


    public void backPredefinedOperationListener(AjaxBehaviorEvent event) {
        bean.selection();
        QueryListener queryListener = bean.beanFromStack();
        if (queryListener != null) {
            bean.replace(queryListener);
            property.setPredefinedOperation(Empty);
            property.setPredefinedOperationParameter(Empty);
            property.setScope(Empty);

        }
    }


    public void predefinedOperationListener(AjaxBehaviorEvent event) {
        if (property.getPredefinedOperation() == null || property.getPredefinedOperation().isEmpty()) {
            return;
        }
        bean.beanInStack();
        bean.selection();
        property.setPredefinedOperationParameters(new ArrayList<>());
        String constraint = property.getConstraint().trim();
        if (constraint.endsWith(Dot)) {
            constraint = constraint.substring(0, constraint.lastIndexOf(Dot));
        } else {
            constraint = constraint + Space + property.getRef();
        }


        if (property.getPredefinedOperation().equals(property)) {
            panel.showScopePanel();
        } else if (property.getPredefinedOperation().equals(Keywords.nullCheck)
                || property.getPredefinedOperation().equals(Keywords.notNullCheck)
                || property.getPredefinedOperation().equals(Keywords.emptyCheck)
                || property.getPredefinedOperation().equals(Keywords.notemptyCheck)
                || property.getPredefinedOperation().equals(Keywords.oclIsNew)) {

            predefinedOperation(constraint);

        } else if (property.getPredefinedOperation().equals(Keywords.oclIsTypeOf)
                || property.getPredefinedOperation().equals(Keywords.oclIsKindOf)
                || property.getPredefinedOperation().equals(Keywords.oclIsInState)
                || property.getPredefinedOperation().equals(Keywords.oclAsType)) {

            predefinedOperationParameter(constraint);
        } else if (property.getPredefinedOperation().equals(Keywords.asSet)) {
            constraint = constraint + Arrow + "asSet()" + Space;
            property.setConstraint(constraint);
            property.setPredefinedOperation(Empty);
            panel.showClosePriorityOperationPanel();
            property.setOperations(new ArrayList<>());
            bean.setOperationsMap(Mappings.getCollectionOperations());
            property.getOperations().addAll(bean.getOperationsMap().keySet());
            panel.showCollectionOperationPanel();
        } else if (property.getPredefinedOperation().equals(Mappings.closure)) {
            constraint = constraint + Arrow + Mappings.closure + LeftBracket + Space;
            property.setConstraint(constraint);
            closure();
        }

    }

    private void closure() {

        if (property.getPropertyType().equals(Empty)) {
            property.setPropertyType(property.getGlobalContext());
        }

        property.setSuggestion("");
        if (property.getGlobalContext().equals(property.getPropertyType())) {
            property.setSuggestion("self" + Comma + Space);
        }
        ClassStructure cs = bean.classObject(property.getGlobalContext());
        for (ClassAttribute ca : cs.getAttributes()) {
            if (ca.getType().equals(property.getPropertyType())) {
                property.setSuggestion(property.getSuggestion() + "self." + ca.getName() + Space + Comma + Space);


            }
        }


        if (property.getContext().equals(property.getPropertyType())) {

            property.setSuggestion(property.getRef() + Comma + Space);
        }

        cs = bean.classObject(property.getContext());
        for (ClassAttribute ca : cs.getAttributes()) {
            if (ca.getType().equals(property.getPropertyType())) {

                property.setSuggestion(property.getSuggestion() + property.getRef() + Dot + ca.getName() + Space + Comma + Space);
            }
        }

        String suggestion = property.getSuggestion().trim();

        property.setSuggestion(suggestion.length() > 0
                ? property.getSuggestion().trim().substring(0, suggestion.length() - 1) : suggestion);

        bean.getPriorityStack().push("input");
        panel.showTextValuePanel();
    }

    private void predefinedOperation(String constraint) {
        switch (property.getPredefinedOperation()) {
            case Keywords.nullCheck:
                constraint = constraint + Space + Equal + Space + "null" + Space;
                break;
            case Keywords.notNullCheck:
                constraint = constraint + Space + "<>" + Space + "null" + Space;
                break;
            case Keywords.emptyCheck:
                constraint = constraint + Arrow + "isEmpty()" + Space;
                break;
            case Keywords.notemptyCheck:
                constraint = constraint + Arrow + "notEmpty()" + Space;
                break;
            case Keywords.oclIsNew:
                constraint = constraint + Dot + "oclIsNew()" + Space;
                break;
        }
        property.setConstraint(constraint);
        property.setPredefinedOperation(Empty);
        panel.showClosePriorityOperationPanel();
        panel.showConditionPanel();
    }

    private void predefinedOperationParameter(String constraint) {
        property.setConstraint(constraint + Dot + property.getPredefinedOperation());

        for (ClassStructure structure : bean.getCd().getClasses()) {
            property.getPredefinedOperationParameters().add(structure.getName());
        }
        if (!property.getPredefinedOperation().equals(Keywords.oclAsType)) {

            for (EnumStructure structure : bean.getCd().getEnumerations()) {
                property.getPredefinedOperationParameters().add(structure.getName());
            }
        }

//need to test
        if (!property.getTempContext().equals(Empty)) {
            property.setRef(property.getTempRef());
            property.setContext(property.getTempContext());
            property.setTempRef(Empty);
            property.setTempContext(Empty);
        }

        if (!property.getNavigationContext().equals(Empty)) {
            property.setContext(property.getNavigationContext());
            property.setNavigationContext(Empty);
        }

        panel.showPredefinedOperationParameterPanel();
    }

    public void backPredefinedOperationParameterListener(AjaxBehaviorEvent event) {
        bean.selection();
        QueryListener queryListener = bean.beanFromStack();
        if (queryListener != null) {
            bean.replace(queryListener);
            property.setPredefinedOperation(Empty);
            property.setPredefinedOperationParameter(Empty);

        }
    }


    public void predefinedOperationParameterListener(AjaxBehaviorEvent event) {
        if (!property.getPredefinedOperationParameter().isEmpty()) {
            bean.beanInStack();
            bean.selection();
            if (property.getPredefinedOperation().equals(Keywords.oclIsTypeOf)
                    || property.getPredefinedOperation().equals(Keywords.oclIsKindOf)
                    || property.getPredefinedOperation().equals(Keywords.oclIsInState)) {
                property.setConstraint(
                        property.getConstraint().trim() + LeftBracket + property.getPredefinedOperationParameter() + RightBracket + Space);
                panel.showClosePriorityOperationPanel();
                panel.showConditionPanel();
            } else if (property.getPredefinedOperation().equals(Keywords.oclAsType)) {
                property.setConstraint(
                        property.getConstraint().trim() + LeftBracket + property.getPredefinedOperationParameter() + RightBracket + Dot);

                if (!bean.getContextStack().contains(property.getRef() + ":" + property.getContext())) {
                    bean.getContextStack().add(property.getRef() + ":" + property.getContext());
                }
                property.setContext(property.getPredefinedOperationParameter());
                panel.showScopePanel();
            }

            property.setPredefinedOperation(Empty);
            property.setPredefinedOperationParameter(Empty);
        }

    }
}
