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

import java.io.Serializable;

/**
 * Abstract actions class contains static keywords use to write ocl constraint.
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-04-15
 */

public abstract class ActionListener implements Serializable {
    protected transient static final String Empty = "";
    protected transient static final String Space = " ";
    protected transient static final String Dot = ".";
    protected transient static final String Arrow = "->";
    protected transient static final String Comma = ",";
    protected transient static final String Apostrophe = "'";
    protected transient static final String Colon = ":";
    protected transient static final String LeftBracket = "(";
    protected transient static final String RightBracket = ")";
    protected transient static final String Bracket = "()";
    protected transient static final String Equal = "=";
    private static final long serialVersionUID = 1L;

}
