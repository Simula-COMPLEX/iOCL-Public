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

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.*;

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
        File file = new File(getClass().getClassLoader().getResource("RoyalAndLoyal.ecore").getFile());
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
