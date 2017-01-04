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
import no.simule.dataobjects.cd.ClassOperation;
import no.simule.dataobjects.cd.ClassStructure;
import no.simule.dataobjects.cd.OperationParameter;
import no.simule.utils.Keywords;
import no.simule.utils.Mappings;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

@ManagedBean(name = "scopePanel")
@SessionScoped
public class ScopePanel implements Serializable {
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

    public ScopePanel() {

        FacesContext context = FacesContext.getCurrentInstance();

        bean = context.getApplication().evaluateExpressionGet(context, "#{bean}", QueryListener.class);

        panel = context.getApplication().evaluateExpressionGet(context, "#{panel}", PanelBean.class);

        property =
                context.getApplication().evaluateExpressionGet(context, "#{property}", PropertyBean.class);
    }


    public void backScopeListener(AjaxBehaviorEvent event) {
        bean.selection();
        // logger.debug("back Scope Listener");
        QueryListener queryListener = bean.beanFromStack();
        if (queryListener != null) {
            bean.replace(queryListener);
            property.setConstraintType(Empty);
            property.setContextOperation(Empty);
            property.setGlobalValueType(Empty);
            property.setProperty(Empty);
            property.setValueType(Empty);
        }
    }

    /*
     * Ajax Listener when Property Scope Radio Button value changed
     */
    public void scopeListener(AjaxBehaviorEvent event) {
        // logger.debug("scope Listener");
        if (property.getScope() == null || property.getScope().isEmpty()) {
            return;
        }
        bean.beanInStack();
        bean.selection();
        property.getProperties().removeAll(property.getProperties());
        ClassStructure cs = bean.classObject(property.getContext());

        if (property.getScope().equals(Keywords.Local_Scope)) {

            classAttributes(cs);
        } else if (property.getScope().equals(Keywords.Operation_Scope)) {
            classOperations(cs);
            property.setScope(Empty);
        } else if (property.getScope().equals(Keywords.Assosiation_Scope)) {
            classAssosiation(cs);

        } else if (property.getScope().equals(Keywords.Predefined_Scope)) {
            panel.showPredefinedOperationPanel();
            property.setScope(Empty);
        } else if (property.getScope().equals(Keywords.Compare_Scope)) {


            if (property.getGlobalValueType().equals(Empty) || property.getGlobalValueType().equals("value")) {
                if (property.getGlobalProperty().equals(Empty)) {
                    property.setConstraint(property.getConstraint() + property.getRef());
                } else {
                    String constraint = property.getConstraint().trim();
                    if (constraint.endsWith(Dot)) {
                        constraint = constraint.substring(0, constraint.length() - 1);
                    }
                    property.setConstraint(constraint);
                }

                property.setGlobalProperty(Empty);

            } else {

                if (property.getGlobalProperty().equals(Empty)) {
                    property.setConstraint(property.getConstraint() + property.getRef());
                } else {

                    String constraint = property.getConstraint().trim();
                    if (constraint.endsWith(Dot)) {
                        constraint = constraint.substring(0, constraint.length() - 1);
                    }
                    property.setConstraint(constraint);
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


            bean.setOperationsMap(Mappings.getObjectOperations());
            property.setOperations(new ArrayList<>());
            property.getOperations().addAll(bean.getOperationsMap().keySet());


            property.setGlobalScpoe(Keywords.Local_Scope);


            if (property.getGlobalValueType().equals(Empty) || property.getGlobalValueType().equals("value")) {
                panel.showOperationPanel();
            } else {
                panel.showConditionPanel();
            }


            property.setScope(Empty);
        }


    }

    private void classAttributes(ClassStructure cs) {
        if (cs != null) {
            for (ClassAttribute ca : cs.getAttributes()) {
                if (!ca.isClass()) {
                    property.getProperties().add(ca.getName());
                }
            }
        }

        Collections.sort(property.getProperties());
        property.setGlobalScpoe(property.getScope());
        property.setScope(Empty);
        panel.showPropertySelectPanel();
    }

    private void classAssosiation(ClassStructure cs) {
        if (cs != null) {
            for (ClassAttribute ca : cs.getAttributes()) {
                if (ca.isClass()) {
                    property.getProperties().add(ca.getName());
                }
            }
        }

        Collections.sort(property.getProperties());
        property.setGlobalScpoe(property.getScope());
        property.setScope(Empty);
        panel.showPropertySelectPanel();

    }

    private void classOperations(ClassStructure cs) {
        property.setClassOperations(new ArrayList<>());
        for (ClassOperation cm : cs.getOperations()) {
            StringBuilder parameters = new StringBuilder("(");
            Iterator<OperationParameter> iterator = cm.getParameters().iterator();
            while (iterator.hasNext()) {
                OperationParameter ca = (OperationParameter) iterator.next();
                if (ca.isCollection()) {
                    parameters.append("Set");
                    parameters.append("(");
                    parameters.append(ca.getType());
                    parameters.append(")");
                } else {
                    parameters.append(ca.getType());
                }
                parameters.append(Space);
                if (iterator.hasNext()) {
                    parameters.append(",");
                    parameters.append(Space);
                }
            }
            parameters.append(")");
            property.getClassOperations().add(cm.getName() + parameters);
        }
        Collections.sort(property.getClassOperations());
        panel.showClassOperationSelectPanel();

    }


}
