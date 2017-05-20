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
import no.simule.models.cd.ClassStructure;
import no.simule.models.enm.EnumStructure;
import no.simule.utils.Keywords;
import no.simule.utils.QueryUtil;
import no.simule.utils.iOCLTypes;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;

@ManagedBean(name = "constraint")
@SessionScoped
public class ConstraintTypePanel extends ActionListener {

    private transient static final Logger logger = Logger.getLogger(ConstraintTypePanel.class);
    private QueryListener bean;
    private PanelBean panel;
    private PropertyBean property;

    public ConstraintTypePanel() {

        FacesContext context = FacesContext.getCurrentInstance();

        bean = context.getApplication().evaluateExpressionGet(context, "#{bean}", QueryListener.class);

        panel = context.getApplication().evaluateExpressionGet(context, "#{panel}", PanelBean.class);

        property =
                context.getApplication().evaluateExpressionGet(context, "#{property}", PropertyBean.class);
    }


    public void backConstraintTypeListener(AjaxBehaviorEvent event) {
        bean.selection();
        logger.debug("back Constraint Type Listener");
        QueryListener queryListener = bean.beanFromStack();
        if (queryListener != null) {
            bean.replace(queryListener);
            property.setContext(Empty);
            property.setConstraint(Empty);

        }
    }

  /*
   * Ajax Listener when Constraint Type Radio Button value changed (INV,PRE,POST)
   */

    public void constraintTypeListener(AjaxBehaviorEvent event) {
        logger.debug("constraint Type Listener");
        if (property.getConstraintType() == null || property.getConstraintType().isEmpty()) {
            return;
        }
        bean.beanInStack();
        bean.selection();
        property.setGlobalConstraintType(property.getConstraintType());

        if (property.getConstraintType().equals(Keywords.Query_Constraint)) {
            queryConstraint();
        } else if (property.getConstraintType().equals(Keywords.INVARIANT_Constraint)) {
            invConstraint();
        } else if (property.getConstraintType().equals(Keywords.PRE_Condition_Constraint)
                || property.getConstraintType().equals(Keywords.POST_Condition_Constraint)
                || property.getConstraintType().equals(Keywords.Operation_Body)) {
            prePostBodyConstraint();
        } else if (property.getConstraintType().equals(Keywords.Def_Operation)) {
            defConstraint();
        }


    }


    private void queryConstraint() {
        logger.debug("Query Type");
        property.setConstraint("");
        panel.showScopePanel();
        panel.showAddPriorityOperationPanel();
        property.setConstraintType(Empty);
    }

    private void invConstraint() {
        logger.debug("INVARIANT Type");
        property.setConstraint(property.getConstraint() + property.getConstraintType() + Colon);
        panel.showScopePanel();
        panel.showAddPriorityOperationPanel();
        property.setConstraintType(Empty);
    }

    private void defConstraint() {
        logger.debug("Def Type");
        property.setAllTypes(new ArrayList<>());
        for (ClassStructure cs : bean.getCd().getClasses()) {
            property.getAllTypes().add(cs.getName());
        }
        for (EnumStructure es : bean.getCd().getEnumerations()) {
            property.getAllTypes().add(es.getName());
        }
        property.getAllTypes().add(iOCLTypes.String);
        property.getAllTypes().add(iOCLTypes.Integer);
        property.getAllTypes().add(iOCLTypes.Boolean);
        property.getAllTypes().add(iOCLTypes.Real);
        property.getAllTypes().add(iOCLTypes.OclVoid);
        property.getAllTypes().add(iOCLTypes.OclInvalid);
        property.getAllTypes().add(iOCLTypes.Void);
        panel.showOperationNamePanel();
    }


    private void prePostBodyConstraint() {
        logger.debug("PRE or POST Operation Body Type");
        for (ClassOperation cm : bean.classObject(property.getGlobalContext()).getOperations()) {
            String name = cm.getName();
            String parameters = QueryUtil.operationPrametersName(cm.getParameters());
            name = name + parameters;

            if (cm.getReturnType() != null && !cm.getReturnType().getType().equals(Empty)) {
                name = name + " : " + QueryUtil.attributeType(cm.getReturnType().getType(), cm.getReturnType().isCollection());
            }
            property.getContextOperations().add(name);
        }
        panel.showContextOperationPanel();
    }

}
