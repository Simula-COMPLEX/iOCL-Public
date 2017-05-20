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
import no.simule.models.enm.EnumStructure;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Class ComparisonTypePanel is a action class which handle context Value Type panel and determine next panel
 * which need to provide the value for comparision.
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-04-15
 */
@ManagedBean(name = "comparisonType")
@SessionScoped
public class ComparisonTypePanel extends ActionListener {
    private QueryListener bean;
    private PanelBean panel;
    private PropertyBean property;

    public ComparisonTypePanel() {

        FacesContext context = FacesContext.getCurrentInstance();

        bean = context.getApplication().evaluateExpressionGet(context, "#{bean}", QueryListener.class);

        panel = context.getApplication().evaluateExpressionGet(context, "#{panel}", PanelBean.class);

        property =
                context.getApplication().evaluateExpressionGet(context, "#{property}", PropertyBean.class);
    }


    public void backComparisonTypeListener(AjaxBehaviorEvent event) {
        bean.selection();
        QueryListener queryListener = bean.beanFromStack();
        if (queryListener != null) {
            bean.replace(queryListener);
            property.setValueType(Empty);

        }
    }

    public void comparisonTypeListener(AjaxBehaviorEvent event) {
        bean.beanInStack();
        bean.selection();
        if (property.getValueType().equalsIgnoreCase("value")) {
            inputBox();
        } else if (property.getValueType().equalsIgnoreCase("class")) {

            classValue();
        } else if (property.getValueType().equalsIgnoreCase("enum")) {
            enumValue();
        } else if (property.getValueType().equalsIgnoreCase("paremeter")) {
            paremeterValue();
        }
        property.setGlobalValueType(property.getValueType());
        property.setValueType(Empty);
    }


    private void inputBox() {
        panel.showValuePanel();
    }

    private void classValue() {
        panel.showScopePanel();
    }

    private void enumValue() {
        property.setEnumerations(new ArrayList<>());
        for (EnumStructure structure : bean.getCd().getEnumerations()) {
            property.getEnumerations().add(structure.getName());
        }
        panel.showEnumPanel();
    }

    private void paremeterValue() {
        ClassOperation co = null;
        if (property.getGlobalContextOperation() != null && !property.getGlobalContextOperation().isEmpty()) {
            co = bean.operationObject(property.getGlobalContext(), property.getGlobalContextOperation());
        } else if (property.getOperationSignature() != null && !property.getOperationSignature().isEmpty()) {
            co = bean.operationObject(property.getGlobalContext(), property.getOperationSignature());
        }

        if (co != null) {
            property.setParameters(new ArrayList<>());
            for (OperationParameter op : co.getParameters()) {
                property.getParameters().add(op.getName() + ":" + op.getType());
            }
        }
        panel.showParameterPanel();
    }


}
