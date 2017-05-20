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

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

/**
 * The Class ContextPanel is a action class which handle context Panel when user select constraint this
 * class set property and enable constraint type panel.
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-04-15
 */
@ManagedBean(name = "contextPanel")
@SessionScoped
public class ContextPanel extends ActionListener {

    @ManagedProperty(value = "#{bean}")
    private QueryListener bean;
    @ManagedProperty(value = "#{panel}")
    private PanelBean panel;
    @ManagedProperty(value = "#{property}")
    private PropertyBean property;
    @ManagedProperty(value = "#{statics}")
    private StaticsBean statics;

    public ContextPanel() {


    }


    /*
     * Ajax Listener when Context Dropdown value changed
     */
    public void contextListener(AjaxBehaviorEvent event) {
        // logger.debug("context Listener");
        if (property.getContext() == null || property.getContext().isEmpty()) {
            return;
        }
        bean.setStartTime(System.currentTimeMillis());
        bean.beanInStack();
        bean.selection();
        if (!property.getContext().equals(Empty)) {
            property.setGlobalContext(property.getContext());


            property.setConstraint("context " + property.getContext() + Space);


            panel.showConstraintTypePanel();
        }

    }


    public QueryListener getBean() {
        return bean;
    }

    public void setBean(QueryListener bean) {
        this.bean = bean;
    }

    public PanelBean getPanel() {
        return panel;
    }

    public void setPanel(PanelBean panel) {
        this.panel = panel;
    }

    public PropertyBean getProperty() {
        return property;
    }

    public void setProperty(PropertyBean property) {
        this.property = property;
    }

    public StaticsBean getStatics() {
        return statics;
    }

    public void setStatics(StaticsBean statics) {
        this.statics = statics;
    }
}
