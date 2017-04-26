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
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean(name = "statics")
@SessionScoped
public class StaticsBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private long cost = 0;
    private int basicInput = 0;
    private int textInput = 0;
    private int selection = 0;

    public void reset() {
        cost = 0;
        basicInput = 0;
        textInput = 0;
        selection = 0;
    }

    public void setStatics(int cost, int basicInput, int textInput, int selection) {
        this.cost = cost;
        this.basicInput = basicInput;
        this.textInput = textInput;
        this.selection = selection;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public void addCost(long starttime) {
        this.cost = (System.currentTimeMillis() - starttime) / 1000;
    }

    public int getBasicInput() {
        return basicInput;
    }

    public void setBasicInput(int basicInput) {
        this.basicInput = basicInput;
    }

    public void addBasicInput() {
        this.basicInput++;
    }

    public int getTextInput() {
        return textInput;
    }

    public void setTextInput(int textInput) {
        this.textInput = textInput;
    }

    public void addTextInput() {
        this.textInput++;
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public void addSelection() {
        this.selection++;
    }

}
