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

import no.simule.models.enm.EnumStructure;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

@ManagedBean(name = "enumPanel")
@SessionScoped
public class EnumPanel extends ActionListener {

    private transient static final Logger logger = Logger.getLogger(EnumPanel.class);

    private QueryListener bean;
    private PanelBean panel;
    private PropertyBean property;

    public EnumPanel() {

        FacesContext context = FacesContext.getCurrentInstance();


        bean = context.getApplication().evaluateExpressionGet(context, QueryListener.BEAN_NAME, QueryListener.class);

        panel = context.getApplication().evaluateExpressionGet(context, PanelBean.BEAN_NAME, PanelBean.class);

        property =
                context.getApplication().evaluateExpressionGet(context, "#{property}", PropertyBean.class);
    }


    public void backEnumListener(AjaxBehaviorEvent event) {
        bean.selection();
        QueryListener queryListener = bean.beanFromStack();
        if (queryListener != null) {
            bean.replace(queryListener);
            property.setEnumeration(Empty);
            property.setValueType(Empty);
            property.setEnumLiteral(Empty);

        }
    }

    public void enumListener(AjaxBehaviorEvent event) {
        bean.beanInStack();
        bean.selection();
        for (EnumStructure structure : bean.getCd().getEnumerations()) {
            if (structure.getName().equals(property.getEnumeration())) {
                property.setEnumLiterals(structure.getLiterals());
                break;
            }

        }

        panel.showEnumLiteralPanel();
    }

    public void backEnumLitratlListener(AjaxBehaviorEvent event) {
        bean.selection();
        QueryListener queryListener = bean.beanFromStack();
        if (queryListener != null) {
            bean.replace(queryListener);
            property.setEnumLiteral(Empty);
            property.setValueType(Empty);
            property.setEnumeration(Empty);
        }
    }

    public void enumLitratlListener(AjaxBehaviorEvent event) {
        bean.beanInStack();
        bean.selection();
        property.setConstraint(property.getConstraint() + Space + property.getEnumeration() + "::"
                + property.getEnumLiteral() + Space);
        property.setEnumeration(Empty);
        property.setEnumLiteral(Empty);
        panel.showClosePriorityOperationPanel();
        panel.showConditionPanel();
    }
}
