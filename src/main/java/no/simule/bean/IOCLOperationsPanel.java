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

import no.simule.dataobjects.cd.ClassOperation;
import no.simule.dataobjects.cd.ClassStructure;
import no.simule.dataobjects.cd.OperationParameter;
import no.simule.exception.EsOCLException;
import no.simule.exception.ValidationParseException;
import no.simule.utils.Keywords;
import no.simule.utils.Mappings;
import no.simule.utils.api.EsOCLEndPoint;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.eclipse.ocl.ParserException;
import org.primefaces.context.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.*;
import java.util.ArrayList;

@ManagedBean(name = "util")
@SessionScoped
public class IOCLOperationsPanel implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient static final Logger logger = Logger.getLogger(IOCLOperationsPanel.class);
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

    @ManagedProperty(value = "#{bean}")
    private QueryListener bean;
    @ManagedProperty(value = "#{panel}")
    private PanelBean panel;
    @ManagedProperty(value = "#{property}")
    private PropertyBean property;
    @ManagedProperty(value = "#{statics}")
    private StaticsBean statics;

    private String name = "";
    private String email = "";


    public void addPriorityListener(AjaxBehaviorEvent event) {
        property.setConstraint(property.getConstraint() + " ( ");
        bean.getPriorityStack().push("parenthesis");

    }

    public void allInstances(AjaxBehaviorEvent event) {
        property.setConstraint(property.getConstraint() + property.getGlobalContext() + Dot + "allInstances()");
        bean.setOperationsMap(Mappings.getCollectionOperations());
        property.getOperations().addAll(bean.getOperationsMap().keySet());

        property.setPropertyType(property.getGlobalContext());
        property.setGlobalProperty("allInstances");
        property.setGlobalScpoe(Keywords.Assosiation_Scope);

        panel.showCollectionOperationPanel();

    }


    public void closePriorityListener(AjaxBehaviorEvent event) {
        property.setConstraint(property.getConstraint() + " ) ");
        if (!bean.getPriorityStack().empty()) {
            panel.showClosePriorityOperationPanel();

        } else {
            panel.hideClosePriorityOperationPanel();
        }
        bean.getPriorityStack().pop();
    }


    public void addNotPriorityListener(AjaxBehaviorEvent event) {
        property.setConstraint(property.getConstraint() + " not (");
        bean.getPriorityStack().push("parenthesis");
    }


    public void ifListener(AjaxBehaviorEvent event) {
        property.setConstraint(property.getConstraint() + "if" + Space);
        bean.getConditionStack().push("endif");
        bean.getConditionStack().push("else");
        bean.getConditionStack().push("then");
    }


    public void byParameterListener(AjaxBehaviorEvent event) {
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


    public void evaluateConstraint() {
        FacesMessage message = null;

        try {
            Object temp = bean.getUmlModel().validateConstraint(bean.classObject(property.getGlobalContext()), property.getGlobalConstraintType(),
                    property.getConstraint(), true);

            if (temp != null) {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Result", "" + temp);
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Instance not Found");
            }
            bean.setResult("" + temp);
        } catch (NullPointerException e) {
            message =
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Null Pointer Exception", e.getMessage());
            bean.setResult("Exception: " + "");
            e.printStackTrace();
        } catch (ParserException e) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Eclipse OCL Validation Error", e.getMessage());
            e.printStackTrace();
            bean.setResult("Error: " + e.getMessage());
        } catch (ValidationParseException e) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "IOCL Parse Exception", e.getMessage());
            bean.setResult("Error: " + e.getMessage());
        }

        RequestContext.getCurrentInstance().showMessageInDialog(message);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }


    public void newConstraint() {
        bean.reset();
    }

    public void esOCL() {

        EsOCLEndPoint esOCLEndPoint = new EsOCLEndPoint();
        try {

            InputStream is = bean.getModel();

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            bean.setModel(new BufferedInputStream(new ByteArrayInputStream(
                    buffer.toByteArray())));

            FacesMessage message = null;
            try {
                boolean result = esOCLEndPoint.checkConstraint(property.getConstraint(), buffer.toByteArray());

                buffer.close();


                if (result) {
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "ESOCL Result", "No Problem found");
                } else {
                    message = new FacesMessage(FacesMessage.SEVERITY_WARN, "ESOCL Result", "No Instace Satisfied");
                }

            } catch (EsOCLException e) {
                logger.error(e.getMessage(), e);
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Connection Error", "ESOCL Connection Issue");
            }

            RequestContext.getCurrentInstance().showMessageInDialog(message);
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }


    }


    public void validateConstraint() {
        FacesMessage message = null;

        try {
            Object temp = bean.getUmlModel().validateConstraint(bean.classObject(property.getGlobalContext()), property.getGlobalConstraintType(),
                    property.getConstraint(), false);

            if (temp != null) {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Result", "No Syntax Error");
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Result", "Syntax Error");
            }
            bean.setResult("Result: " + temp);
        } catch (NullPointerException e) {
            message =
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Null Pointer Exception", e.getMessage());
            bean.setResult("Exception: " + "");
            e.printStackTrace();
        } catch (ParserException e) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Eclipse OCL Validation Error", e.getMessage());
            e.printStackTrace();
            bean.setResult("Error: " + e.getMessage());
        } catch (ValidationParseException e) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "IOCL Parse Exception", e.getMessage());
            bean.setResult("Error: " + e.getMessage());
        }

        RequestContext.getCurrentInstance().showMessageInDialog(message);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }


    public void exportModel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext ec = facesContext.getExternalContext();


        ec.responseReset();

        ByteArrayOutputStream dataStream = bean.getUmlModel().exportModel();
        byte[] dataArray = dataStream.toByteArray();


        ec.setResponseContentType("application/download");
        ec.setResponseContentLength(dataArray.length);
        String attachmentName =
                "attachment; filename=\"" + "ioclGenratedModel" + ".uml\"";
        ec.setResponseHeader("Content-Disposition", attachmentName);


        try {
            OutputStream output = ec.getResponseOutputStream();
            output.write(dataArray);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            dataStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        facesContext.responseComplete();
    }

    public void exportConstraint() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext ec = facesContext.getExternalContext();


        ec.responseReset();

        String export;
        ClassStructure _class = bean.classObject(property.getGlobalContext());
        if (!_class.getPackage().equals("")) {
            export = "package" + Space + _class.getPackage().replaceAll("\\.", "::") + "\n";
            export = export + property.getConstraint() + "\n";
            export = export + "endpackage \n";
        } else {
            export = property.getConstraint();
        }
        //ec.setResponseContentType("application/download");
        ec.setResponseContentType("application/download");
        ec.setResponseContentLength(export.getBytes().length);
        String attachmentName =
                "attachment; filename=\"" + property.getGlobalContext().toLowerCase() + ".ocl\"";
        ec.setResponseHeader("Content-Disposition", attachmentName);


        try {
            OutputStream output = ec.getResponseOutputStream();
            Streams.copy(new ByteArrayInputStream(export.getBytes()), output, false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        facesContext.responseComplete();
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StaticsBean getStatics() {
        return statics;
    }

    public void setStatics(StaticsBean statics) {
        this.statics = statics;
    }

    public PropertyBean getProperty() {
        return property;
    }

    public void setProperty(PropertyBean property) {
        this.property = property;
    }

    public PanelBean getPanel() {
        return panel;
    }

    public void setPanel(PanelBean panel) {
        this.panel = panel;
    }

    public QueryListener getBean() {
        return bean;
    }

    public void setBean(QueryListener bean) {
        this.bean = bean;
    }
}
