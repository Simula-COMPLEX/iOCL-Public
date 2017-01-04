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

import no.simule.dataobjects.enm.EnumStructure;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;

@ManagedBean(name = "enumPanel")
@SessionScoped
public class EnumPanel implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient static final Logger logger = Logger.getLogger(EnumPanel.class);
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

    public EnumPanel() {

        FacesContext context = FacesContext.getCurrentInstance();

        bean = context.getApplication().evaluateExpressionGet(context, "#{bean}", QueryListener.class);

        panel = context.getApplication().evaluateExpressionGet(context, "#{panel}", PanelBean.class);

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
