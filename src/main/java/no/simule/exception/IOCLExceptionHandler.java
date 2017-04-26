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


package no.simule.exception;

import no.simule.actions.QueryListener;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;

public class IOCLExceptionHandler extends ExceptionHandlerWrapper {

    private transient static final Logger logger = Logger.getLogger(IOCLExceptionHandler.class);
    private final ExceptionHandler wrapped;

    public IOCLExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;

    }

    public void handle() throws FacesException {
        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();

        while (i.hasNext()) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context =
                    (ExceptionQueuedEventContext) event.getSource();

            Throwable t = context.getException();
            logger.error(t.getMessage(), t);


            if (t.getCause() != null && t.getCause().getCause() instanceof IOCLViewException) {

                FacesContext context1 = FacesContext.getCurrentInstance();
                QueryListener queryListener = context1.getApplication().evaluateExpressionGet(context1, "#{bean}", QueryListener.class);
                QueryListener beanStack = queryListener.beanFromStack();
                queryListener.replace(beanStack);

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                        "Unable to Perform this Step");
                FacesContext.getCurrentInstance().addMessage(null, message);
                RequestContext.getCurrentInstance().showMessageInDialog(message);


            }



        /*  final FacesContext fc = FacesContext.getCurrentInstance();
            final ExternalContext externalContext = fc.getExternalContext();
            final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
            final ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
            requestMap.put("exceptionMessage", t.getMessage());
            nav.performNavigation("/TestPRoject/error.xhtml");
            fc.renderResponse();*/


           /* String viewId = "/error.xhtml";
            ViewHandler viewHandler = context.getApplication().getViewHandler();
            context.setViewRoot(viewHandler.createView(context, viewId));
            context.getPartialViewContext().setRenderAll(true);
            context.renderResponse();*/

            i.remove();

        }
        getWrapped().handle();
    }

}