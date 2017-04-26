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

import no.simule.models.Prop;
import no.simule.utils.Mappings;
import org.apache.log4j.Logger;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

@ManagedBean(name = "transMap")
@SessionScoped
public class MappingsListener implements Serializable {
    public transient static final Logger logger = Logger.getLogger(MappingsListener.class);
    private static final long serialVersionUID = 1L;
    private List<Prop> mappings = new ArrayList<>();

    public void preRenderView() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            initialize();
        }
    }

    public void initialize() {
        mappings = new ArrayList<>();
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = getClass().getClassLoader().getResourceAsStream("mappings.properties");
            prop.load(input);
            Enumeration keys = prop.propertyNames();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = prop.getProperty(key);
                mappings.add(new Prop(key, value, !value.equals("none")));
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (NullPointerException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    public void onRowEdit(RowEditEvent event) {

        Prop entry = (Prop) event.getObject();
        update(entry);
        FacesMessage msg =
                new FacesMessage(entry.getKey() + " Edited", "" + entry.getValue());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }


    private void update(Prop entry) {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = getClass().getClassLoader().getResourceAsStream("mappings.properties");
            prop.load(input);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (NullPointerException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        OutputStream output = null;
        try {
            output = new FileOutputStream(getClass().getClassLoader().getResource("mappings.properties").getPath());
            logger.debug("Mapping " + entry.getKey() + " change to " + (entry.getEnable() ? entry.getValue() : "none"));
            prop.setProperty(entry.getKey(), (entry.getEnable() ? entry.getValue() : "none"));
            prop.store(output, null);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }

        }
        Mappings.initialize();
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg =
                new FacesMessage("Edit Cancelled", ((Prop) event.getObject()).getKey().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed",
                    "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public List<Prop> getMappings() {
        return mappings;
    }

    public void setMappings(List<Prop> mappings) {
        this.mappings = mappings;
    }
}
