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

import no.simule.utils.QueryUtil;
import no.simule.utils.iOCLTypes;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;

@ManagedBean(name = "signature")
@SessionScoped
public class OperationSignaturePanel implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient static final Logger logger = Logger.getLogger(OperationSignaturePanel.class);
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

    public OperationSignaturePanel() {

        FacesContext context = FacesContext.getCurrentInstance();

        bean = context.getApplication().evaluateExpressionGet(context, "#{bean}", QueryListener.class);

        panel = context.getApplication().evaluateExpressionGet(context, "#{panel}", PanelBean.class);

        property =
                context.getApplication().evaluateExpressionGet(context, "#{property}", PropertyBean.class);
    }

    public void backOperationSignatureListener(AjaxBehaviorEvent event) {
        bean.selection();
        logger.debug("back Operation Signature Listener");
        QueryListener queryListener = bean.beanFromStack();
        if (queryListener != null) {
            bean.replace(queryListener);
            property.setConstraintType(Empty);
            property.setOperationSignature(Empty);
            property.setOperationSignatureName(Empty);
            property.setOperationSignatureSetReturnType(false);
            property.setOperationSignatureReturnType(Empty);
            property.setOperationSignatureParameterCount(Empty);
            property.setOperationSignatureParameter(Empty);
        }
    }


    public void operationSignatureListener(AjaxBehaviorEvent event) {
        logger.debug("Operation Signature Listener");
        if ((property.getOperationSignatureName() == null || property.getOperationSignatureName().isEmpty()) &&
                (property.getOperationSignatureParameterCount() == null || property.getOperationSignatureParameterCount().isEmpty()) &&
                property.getOperationSignatureReturnType() == null || property.getOperationSignatureReturnType().isEmpty()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Please Fill all Option");
            FacesContext.getCurrentInstance().addMessage(null, message);
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            return;
        }

        bean.beanInStack();
        bean.textValue();
        bean.textValue();
        bean.selection();

        Integer param;
        try {
            param = Integer.parseInt(property.getOperationSignatureParameterCount());
        } catch (NumberFormatException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Parameter must be Integer");
            FacesContext.getCurrentInstance().addMessage(null, message);
            RequestContext.getCurrentInstance().showMessageInDialog(message);

            return;
        }

        property.setConstraint(property.getConstraint() + property.getConstraintType() + Colon);
        property.setConstraint(property.getConstraint() + Space + property.getOperationSignatureName());


        if (param == 0) {
            property.setConstraint(property.getConstraint() + Bracket + Space);
            property.setOperationSignature(property.getOperationSignatureName() + Bracket + Space);
            if (!property.getOperationSignatureReturnType().equals(iOCLTypes.Void)) {
                String returnType = QueryUtil.attributeType(property.getOperationSignatureReturnType(), property.isOperationSignatureSetReturnType());
                property.setConstraint(property.getConstraint() + Colon + Space + returnType + Space);
                property.setOperationSignature(property.getOperationSignature() + Colon + Space + returnType + Space);
            }


            property.setConstraint(property.getConstraint() + Equal + Space);

            property.setOperationSignatureSetReturnType(false);
            property.setOperationSignatureReturnType(Empty);
            panel.showAddPriorityOperationPanel();

            panel.showScopePanel();
        } else {

            for (int i = 1; i <= param; i++) {
                bean.getPrameterStack().push("operationSignature");
            }
            property.setConstraint(property.getConstraint() + LeftBracket);
            property.setOperationSignature(property.getOperationSignatureName() + LeftBracket);

            panel.showOperationParameterPanel();
        }

        property.setConstraintType(Empty);
        property.setOperationSignatureName(Empty);
        property.setOperationSignatureParameter(Empty);
        property.setOperationSignatureParameterCount(Empty);

    }

    public void backOperationSignatureParameterListener(AjaxBehaviorEvent event) {
        bean.selection();
        logger.debug("back Operation Signature Parameter Listener");
        QueryListener queryListener = bean.beanFromStack();
        if (queryListener != null) {
            bean.replace(queryListener);
//            property.setOperationSignatureSetReturnType(false);
//            property.setOperationSignatureReturnType(Empty);
//            property.setOperationSignatureParameter(Empty);
        }
    }

    public void operationSignatureParameterListener(AjaxBehaviorEvent event) {
        logger.debug("Operation Signature Parameter Listener");
        if (property.getOperationSignatureParameter() == null || property.getOperationSignatureParameter().isEmpty()) {
            return;
        }
        bean.selection();
        bean.beanInStack();
        bean.getPrameterStack().pop();
        property.setConstraint(property.getConstraint() + property.getOperationSignatureParameter().substring(0, 3).toLowerCase() + Space + Colon + Space + property.getOperationSignatureParameter());
        property.setOperationSignature(property.getOperationSignature() + property.getOperationSignatureParameter().substring(0, 3).toLowerCase() + Space + Colon + Space + property.getOperationSignatureParameter());

        if (bean.getPrameterStack().isEmpty()) {
            if (!property.getOperationSignatureReturnType().equals(iOCLTypes.Void)) {
                String returnType = QueryUtil.attributeType(property.getOperationSignatureReturnType(), property.isOperationSignatureSetReturnType());
                property.setConstraint(property.getConstraint() + RightBracket + Space + Colon + Space + returnType + Space);
                property.setOperationSignature(property.getOperationSignature() + RightBracket + Space + Colon + Space + returnType + Space);

            } else {
                property.setConstraint(property.getConstraint() + RightBracket + Space);
                property.setOperationSignature(property.getOperationSignature() + RightBracket + Space);
            }
            property.setConstraint(property.getConstraint() + Equal + Space);


            property.setOperationSignatureSetReturnType(false);
            property.setOperationSignatureReturnType(Empty);
            panel.showAddPriorityOperationPanel();
            panel.showScopePanel();
        } else {
            property.setConstraint(property.getConstraint() + Comma + Space);
            property.setOperationSignature(property.getOperationSignature() + Comma + Space);
            panel.showOperationParameterPanel();
        }
        property.setOperationSignatureParameter(Empty);
    }


}
