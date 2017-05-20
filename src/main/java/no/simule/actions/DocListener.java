package no.simule.actions;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.*;

/**
 * The Class DocListener is a action class for contains method that convert and return output stream of user manual
 * and case study to jsf which use to download items.
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-04-15
 */

@ManagedBean(name = "docs")
@SessionScoped
public class DocListener implements Serializable {

    private static final long serialVersionUID = 1L;

    public void downloadFile() {


        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        File file = new File(getClass().getClassLoader().getResource("iOCL_User_Manual.pdf").getFile());
        ec.responseReset();
        ec.setResponseContentType("application/pdf");
        ec.setResponseContentLength((int) file.length());
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        OutputStream output = null;

        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            output = ec.getResponseOutputStream();
            byte[] buffer = new byte[1024];

            int i = 0;
            while ((i = input.read(buffer)) != -1) {
                output.write(buffer);
                output.flush();
            }

        } catch (IOException err) {
            err.printStackTrace();


        }
        fc.responseComplete();


    }

    public void downloadModel() {


        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        File file = new File(getClass().getClassLoader().getResource("RoyalAndLoyal.uml").getFile());
        ec.responseReset();
        ec.setResponseContentType("application/download");
        ec.setResponseContentLength((int) file.length());
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        OutputStream output = null;

        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            output = ec.getResponseOutputStream();
            byte[] buffer = new byte[1024];

            int i = 0;
            while ((i = input.read(buffer)) != -1) {
                output.write(buffer);
                output.flush();
            }

        } catch (IOException err) {
            err.printStackTrace();


        }
        fc.responseComplete();


    }
}