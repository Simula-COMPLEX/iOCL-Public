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
import no.simule.models.cd.OperationParameter;
import no.simule.utils.Keywords;
import no.simule.utils.Mappings;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.List;

/**
 * The Class ClassOperationPanel is a action class which handle context Operation panel and determine next
 * states after selection.
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-04-15
 */
@ManagedBean(name = "classOperationPanel")
@SessionScoped
public class ClassOperationPanel extends ActionListener {

    private QueryListener bean;
    private PanelBean panel;
    private PropertyBean property;

    public ClassOperationPanel() {

        FacesContext context = FacesContext.getCurrentInstance();

        bean = context.getApplication().evaluateExpressionGet(context, "#{bean}", QueryListener.class);

        panel = context.getApplication().evaluateExpressionGet(context, "#{panel}", PanelBean.class);

        property =
                context.getApplication().evaluateExpressionGet(context, "#{property}", PropertyBean.class);
    }

    /*
     * Ajax Listener when back button press from context operation input panel
     */

    public void backClassOperationsListener(AjaxBehaviorEvent event) {
        bean.selection();
        QueryListener queryListener = bean.beanFromStack();
        if (queryListener != null) {
            bean.replace(queryListener);
            property.setScope(Empty);
            property.setClassOperation(Empty);

        }
    }

    /*
    * Ajax Listener called when class opertion select from dropdownlist
    */
    public void classOperationsListener(AjaxBehaviorEvent event) {
        // logger.debug("class Operations Listener");
        if (property.getClassOperation() == null || property.getClassOperation().isEmpty()) {
            return;
        }
        bean.beanInStack();


        String methodName =
                property.getClassOperation().substring(0, property.getClassOperation().indexOf("(")).trim();
        List<ClassOperation> cms = bean.propertyOperation(property.getContext(), methodName);


        if (cms != null && !cms.isEmpty()) {
            ClassOperation cm = cms.get(0);
            if (!cm.getParameters().isEmpty()) {
                if (property.getConstraint().trim().endsWith(Dot)) {
                    property.setConstraint(property.getConstraint() + methodName + "(" + Space);
                } else {
                    property.setConstraint(property.getConstraint() + Space + property.getRef() + Dot + methodName + "(" + Space);
                }

                for (OperationParameter ca : cm.getParameters()) {
                    bean.getPropertyStack().push(ca.getType());
                }
                panel.showComparisonTypePanel();


            } else {

                if (cm.getReturnType() != null && cm.getReturnType().isClass()) {
                    property.setGlobalScpoe(Keywords.Assosiation_Scope);
                    if (property.getConstraint().trim().endsWith(Dot)) {
                        property.setConstraint(property.getConstraint() + Space + methodName + "()" + Dot);
                    } else {
                        property.setConstraint(
                                property.getConstraint() + Space + property.getRef() + Dot + methodName + "()" + Dot);
                    }

                    if (cm.getReturnType().isCollection()) {

                        bean.setOperationsMap(Mappings.getCollectionOperations());
                        if (property.getGlobalValueType().equals(Empty) || property.getGlobalValueType().equals("value")) {
                            property.getOperations().addAll(bean.getOperationsMap().keySet());
                        } else {
                            //right sife
                            bean.setRightSide(true);

                            for (String opp : bean.getOperationsMap().keySet()) {
                                if (Mappings.getCollectionNotItreate().get(opp) != null && Mappings.getCollectionNotItreate().get(opp)) {
                                    property.getOperations().add(opp);
                                }
                            }
                        }

                        panel.showCollectionOperationPanel();
                        if (property.getConstraint().trim().endsWith(Dot)) {

                            String constraint = property.getConstraint().trim();
                            property.setConstraint(constraint.substring(0, constraint.length() - 1));

                        }


                    } else {

                        panel.showScopePanel();
                    }

                 /*   if (!bean.getContextStack().contains(property.getRef() + ":" + property.getContext())) {
                        bean.getContextStack().add(property.getRef() + ":" + property.getContext());
                    }*/

                    property.setContext(cm.getReturnType().getType());
                    property.setPropertyType(cm.getReturnType().getType());
                } else {
                    property.setGlobalScpoe(Keywords.Local_Scope);
                    if (property.getConstraint().trim().endsWith(Dot)) {
                        property.setConstraint(property.getConstraint() + methodName + "()" + Space);
                    } else {
                        property.setConstraint(property.getConstraint() + Space + property.getRef() + Dot
                                + methodName + "()" + Space);
                    }
                    property.setPropertyType(cm.getReturnType().getType());
                    panel.showOperationTypePanel();
                }

            }

            property.setGlobalClassOperation(property.getClassOperation());
            property.setClassOperation(Empty);


            //need to test
            if (!property.getNavigationContext().equals(Empty)) {
                property.setContext(property.getNavigationContext());
                property.setNavigationContext(Empty);
            }


        }


    }


}
