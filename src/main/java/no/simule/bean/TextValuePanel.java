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

import no.simule.utils.Mappings;
import no.simule.utils.iOCLTypes;
import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "textValue")
@SessionScoped
public class TextValuePanel implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient static final Logger logger = Logger.getLogger(TextValuePanel.class);

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

    public TextValuePanel() {

        FacesContext context = FacesContext.getCurrentInstance();
        bean = context.getApplication().evaluateExpressionGet(context, "#{bean}", QueryListener.class);

        panel = context.getApplication().evaluateExpressionGet(context, "#{panel}", PanelBean.class);
        property =
                context.getApplication().evaluateExpressionGet(context, "#{property}", PropertyBean.class);


    }

    public void backTextValueListener(AjaxBehaviorEvent event) {
        bean.selection();
        logger.debug("Text Value Listener Listener");
        QueryListener queryListener = bean.beanFromStack();
        if (queryListener != null) {
            bean.replace(queryListener);
            property.setInputValue(Empty);

        }
    }

    public List<String> completeText(String query) {
        List<String> results = new ArrayList<>();

        String tokens[] = property.getSuggestion().split(Comma);
        for (String token : tokens) {
            if (!token.trim().isEmpty()) {
                if (token.startsWith(query)) {
                    results.add(token.trim());
                } else if (token.contains(Dot)) {
                    String dotTokens[] = token.split("\\.");
                    if (dotTokens[dotTokens.length - 1].startsWith(query)) {
                        results.add(token.trim());
                    }
                }
            }
        }
        return results;
    }

    public void textValueSelect(SelectEvent event) {
        if (event.getObject().toString() != null) {
            property.setInputValue(event.getObject().toString());
        }
        textValueListener(null);
    }


    public void textValueListener(AjaxBehaviorEvent event) {
        if (property.getInputValue() == null || property.getInputValue().isEmpty()) {
            return;
        }
        bean.beanInStack();
        bean.textValue();
        if (!bean.getPriorityStack().empty() && bean.getPriorityStack().peek().equals("input")) {
            bean.getPriorityStack().pop();
            property.setConstraint(
                    property.getConstraint() + Space + property.getInputValue() + Space + RightBracket + Space);

            if (Mappings.getCollectionInputReturn().get(property.getGlobalOperation()) != null) {

                if (Mappings.getCollectionInputReturn().get(property.getGlobalOperation()).equals(iOCLTypes.Any)) {
                    property.setNavigationContext(property.getContext());
                    property.setContext(property.getPropertyType());
                    panel.showScopePanel();

                } else if (Mappings.getCollectionInputReturn().get(property.getGlobalOperation()).equals(iOCLTypes.List)) {

                    bean.setOperationsMap(Mappings.getCollectionOperations());
                    property.getOperations().addAll(bean.getOperationsMap().keySet());
                    panel.showCollectionOperationPanel();

                }

            } else {
                panel.showClosePriorityOperationPanel();
                panel.showConditionPanel();
            }


        } else {
            property.setConstraint(property.getConstraint() + Space + property.getInputValue() + Space);
            panel.showClosePriorityOperationPanel();
            panel.showConditionPanel();
        }

        property.setSuggestion(Empty);
        property.setSuggestions(new ArrayList<>());
        property.setInputValue(Empty);
    }


}

