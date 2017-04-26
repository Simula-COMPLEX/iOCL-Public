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

/**
 * JSF action contain state of active view and helps to determine view which need to b active  .
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-04-15
 */
@ManagedBean(name = "panel")
@SessionScoped
public class PanelBean extends ActionListener {
    public transient static final String BEAN_NAME = "panel";

    public static final String True = "true";
    public static final String False = "false";
    public static final int inputPanel = 1;
    public static final int informationPanel = 2;
    public static final int optionsPanel = 3;
    public static final int contextPanel = 4;
    public static final int addPriorityPanel = 5;
    public static final int closePriorityPanel = 6;
    public static final int constraintTypePanel = 7;
    public static final int contextOperationPanel = 8;
    public static final int operationNamePanel = 9;
    public static final int operationParameterPanel = 10;
    public static final int scopePanel = 11;
    public static final int propertyPanel = 12;
    public static final int predefinedOperationPanel = 13;
    public static final int predefinedOperationParameterPanel = 14;
    public static final int classOperationSelectPanel = 15;
    public static final int textValuePanel = 16;
    public static final int operationTypePanel = 17;
    public static final int operationPanel = 18;
    public static final int collectionOperationPanel = 19;
    public static final int comparisonTypePanel = 20;
    public static final int valuePanel = 21;

    public static final int enumPanel = 22;
    public static final int enumLiteralPanel = 23;
    public static final int parameterPanel = 24;
    public static final int conditionPanel = 25;


    private static final long serialVersionUID = 1L;
    private int activePanel = -1;
    private String disableInputPanel = False;
    private String disableInformationPanel = True;
    private String disableOptionsPanelPanel = True;
    private String disableContextPanel = True;
    private String disableAddPriorityPanel = True;
    private String disableClosePriorityPanel = True;
    private String disableConstraintTypePanel = True;
    private String disableContextOperationPanel = True;
    private String disableOperationNamePanel = True;
    private String disableOperationParameterPanel = True;
    private String disableScopePanel = True;
    private String disablePropertyPanel = True;
    private String disablePredefinedOperationPanel = True;
    private String disablePredefinedOperationParameterPanel = True;
    private String disableClassOperationSelectPanel = True;
    private String disableTextValuePanel = True;
    private String disableOperationTypePanel = True;
    private String disableOperationPanel = True;
    private String disableCollectionOperationPanel = True;
    private String disableComparisonTypePanel = True;
    private String disableValuePanel = True;
    private String disableEnumPanel = True;
    private String disableEnumLiteralPanel = True;
    private String disableParameterPanel = True;
    private String disableConditionPanel = True;

    private String collapseModelPanel = True;
    private String collapseUpdatesPanel = False;


    public void activatePanel(int panelCode) {

        switch (panelCode) {
            case contextPanel:
                showContextPanel();
                break;
            case addPriorityPanel:
                showAddPriorityOperationPanel();
                break;
            case constraintTypePanel:
                showConstraintTypePanel();
                break;
            case contextOperationPanel:
                showContextOperationPanel();
                break;
            case operationNamePanel:
                showOperationNamePanel();
                break;
            case operationParameterPanel:
                showOperationParameterPanel();
                break;
            case scopePanel:
                showScopePanel();
                break;
            case propertyPanel:
                showPropertySelectPanel();
                break;
            case predefinedOperationPanel:
                showPredefinedOperationPanel();
                break;
            case predefinedOperationParameterPanel:
                showPredefinedOperationParameterPanel();
                break;
            case classOperationSelectPanel:
                showClassOperationSelectPanel();
                break;
            case operationTypePanel:
                showOperationTypePanel();
                break;
            case operationPanel:
                showOperationPanel();
                break;
            case collectionOperationPanel:
                showCollectionOperationPanel();
                break;
            case comparisonTypePanel:
                showComparisonTypePanel();
                break;
            case valuePanel:
                showValuePanel();
                break;
            case textValuePanel:
                showTextValuePanel();
                break;
            case conditionPanel:
                showConditionPanel();
                break;
            case enumPanel:
                showEnumPanel();
                break;
            case enumLiteralPanel:
                showEnumLiteralPanel();
                break;
            case parameterPanel:
                showParameterPanel();
                break;
            default:
                break;
        }

    }


    public void showContextPanel() {
        activePanel = contextPanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = False;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableClassOperationSelectPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = True;

    }


    public void showConstraintTypePanel() {
        activePanel = constraintTypePanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = False;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableClassOperationSelectPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableTextValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = True;

    }


    public void showContextOperationPanel() {
        activePanel = contextOperationPanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = False;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableClassOperationSelectPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableTextValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = True;
    }

    public void showScopePanel() {
        activePanel = scopePanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = False;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableClassOperationSelectPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableTextValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = True;
    }

    public void showPropertySelectPanel() {
        activePanel = propertyPanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = False;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableClassOperationSelectPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableTextValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = True;
    }

    public void showPredefinedOperationPanel() {
        activePanel = predefinedOperationPanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = False;
        disablePredefinedOperationParameterPanel = True;
        disableClassOperationSelectPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableTextValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = True;
    }

    public void showPredefinedOperationParameterPanel() {
        activePanel = predefinedOperationParameterPanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = False;
        disableClassOperationSelectPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableTextValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = True;
    }

    public void showClassOperationSelectPanel() {
        activePanel = classOperationSelectPanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableClassOperationSelectPanel = False;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableTextValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = True;
    }

    public void showOperationTypePanel() {
        activePanel = operationTypePanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableClassOperationSelectPanel = True;
        disableOperationTypePanel = False;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableTextValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = True;
    }

    public void showOperationPanel() {
        activePanel = operationPanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableClassOperationSelectPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = False;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableTextValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = True;
    }


    public void showCollectionOperationPanel() {
        activePanel = collectionOperationPanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableClassOperationSelectPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = False;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableTextValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = True;

    }

    public void showComparisonTypePanel() {
        activePanel = comparisonTypePanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableClassOperationSelectPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = False;
        disableValuePanel = True;
        disableTextValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = True;
    }

    public void showValuePanel() {
        activePanel = valuePanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = True;
        disableValuePanel = False;
        disableTextValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = True;
    }

    public void showTextValuePanel() {
        activePanel = textValuePanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableTextValuePanel = False;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = True;
    }

    public void showEnumPanel() {
        activePanel = enumPanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableTextValuePanel = True;
        disableEnumPanel = False;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = True;

    }

    public void showEnumLiteralPanel() {
        activePanel = enumLiteralPanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableTextValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = False;
        disableParameterPanel = True;
        disableConditionPanel = True;

    }

    public void showConditionPanel() {
        activePanel = conditionPanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableTextValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = False;
    }

    public void showOperationNamePanel() {
        activePanel = operationNamePanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = False;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableTextValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = True;
    }

    public void showOperationParameterPanel() {
        activePanel = operationParameterPanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = False;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableTextValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableConditionPanel = True;
    }


    public void showParameterPanel() {
        activePanel = parameterPanel;

        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableComparisonTypePanel = True;
        disableValuePanel = True;
        disableTextValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = False;
        disableConditionPanel = True;
    }


    public void showAddPriorityOperationPanel() {
        disableAddPriorityPanel = False;
    }

    public void hideAddPriorityOperationPanel() {
        disableAddPriorityPanel = True;
    }

    public void showClosePriorityOperationPanel() {
        disableClosePriorityPanel = False;
    }

    public void hideClosePriorityOperationPanel() {
        disableClosePriorityPanel = True;
    }


    public void collapseUpdatesPanel() {
        collapseUpdatesPanel = True;
    }

    public void uncollapseModelPanel() {
        collapseModelPanel = False;
    }


    public void reset() {
        activePanel = 4;
        disableInputPanel = False;
        disableInformationPanel = False;
        disableOptionsPanelPanel = False;
        disableContextPanel = False;
        disableAddPriorityPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableClosePriorityPanel = True;
        disableConditionPanel = True;
    }

    public void resetAll() {
        activePanel = -1;
        disableInputPanel = False;
        disableInformationPanel = True;
        disableOptionsPanelPanel = True;
        disableContextPanel = True;
        disableAddPriorityPanel = True;
        disableConstraintTypePanel = True;
        disableContextOperationPanel = True;
        disableOperationNamePanel = True;
        disableOperationParameterPanel = True;
        disableScopePanel = True;
        disablePropertyPanel = True;
        disablePredefinedOperationPanel = True;
        disablePredefinedOperationParameterPanel = True;
        disableOperationTypePanel = True;
        disableOperationPanel = True;
        disableCollectionOperationPanel = True;
        disableValuePanel = True;
        disableEnumPanel = True;
        disableEnumLiteralPanel = True;
        disableParameterPanel = True;
        disableClosePriorityPanel = True;
        disableConditionPanel = True;

        collapseUpdatesPanel = False;
        collapseModelPanel = True;
    }

    public int getActivePanel() {
        return activePanel;
    }

    public void setActivePanel(int activePanel) {
        this.activePanel = activePanel;
    }

    public String getDisableInputPanel() {
        return disableInputPanel;
    }

    public void setDisableInputPanel(String disableInputPanel) {
        this.disableInputPanel = disableInputPanel;
    }

    public String getDisableInformationPanel() {
        return disableInformationPanel;
    }

    public void setDisableInformationPanel(String disableInformationPanel) {
        this.disableInformationPanel = disableInformationPanel;
    }

    public String getDisableOptionsPanelPanel() {
        return disableOptionsPanelPanel;
    }

    public void setDisableOptionsPanelPanel(String disableOptionsPanelPanel) {
        this.disableOptionsPanelPanel = disableOptionsPanelPanel;
    }

    public String getDisableContextPanel() {
        return disableContextPanel;
    }

    public void setDisableContextPanel(String disableContextPanel) {
        this.disableContextPanel = disableContextPanel;
    }

    public String getDisableAddPriorityPanel() {
        return disableAddPriorityPanel;
    }

    public void setDisableAddPriorityPanel(String disableAddPriorityPanel) {
        this.disableAddPriorityPanel = disableAddPriorityPanel;
    }

    public String getDisableConstraintTypePanel() {
        return disableConstraintTypePanel;
    }

    public void setDisableConstraintTypePanel(String disableConstraintTypePanel) {
        this.disableConstraintTypePanel = disableConstraintTypePanel;
    }

    public String getDisableContextOperationPanel() {
        return disableContextOperationPanel;
    }

    public void setDisableContextOperationPanel(String disableContextOperationPanel) {
        this.disableContextOperationPanel = disableContextOperationPanel;
    }

    public String getDisableScopePanel() {
        return disableScopePanel;
    }

    public void setDisableScopePanel(String disableScopePanel) {
        this.disableScopePanel = disableScopePanel;
    }

    public String getDisablePropertyPanel() {
        return disablePropertyPanel;
    }

    public void setDisablePropertyPanel(String disablePropertyPanel) {
        this.disablePropertyPanel = disablePropertyPanel;
    }

    public String getDisablePredefinedOperationPanel() {
        return disablePredefinedOperationPanel;
    }

    public void setDisablePredefinedOperationPanel(String disablePredefinedOperationPanel) {
        this.disablePredefinedOperationPanel = disablePredefinedOperationPanel;
    }

    public String getDisablePredefinedOperationParameterPanel() {
        return disablePredefinedOperationParameterPanel;
    }

    public void setDisablePredefinedOperationParameterPanel(String disablePredefinedOperationParameterPanel) {
        this.disablePredefinedOperationParameterPanel = disablePredefinedOperationParameterPanel;
    }

    public String getDisableClassOperationSelectPanel() {
        return disableClassOperationSelectPanel;
    }

    public void setDisableClassOperationSelectPanel(String disableClassOperationSelectPanel) {
        this.disableClassOperationSelectPanel = disableClassOperationSelectPanel;
    }

    public String getDisableTextValuePanel() {
        return disableTextValuePanel;
    }

    public void setDisableTextValuePanel(String disableTextValuePanel) {
        this.disableTextValuePanel = disableTextValuePanel;
    }

    public String getDisableOperationTypePanel() {
        return disableOperationTypePanel;
    }

    public void setDisableOperationTypePanel(String disableOperationTypePanel) {
        this.disableOperationTypePanel = disableOperationTypePanel;
    }

    public String getDisableOperationPanel() {
        return disableOperationPanel;
    }

    public void setDisableOperationPanel(String disableOperationPanel) {
        this.disableOperationPanel = disableOperationPanel;
    }

    public String getDisableCollectionOperationPanel() {
        return disableCollectionOperationPanel;
    }

    public void setDisableCollectionOperationPanel(String disableCollectionOperationPanel) {
        this.disableCollectionOperationPanel = disableCollectionOperationPanel;
    }

    public String getDisableComparisonTypePanel() {
        return disableComparisonTypePanel;
    }

    public void setDisableComparisonTypePanel(String disableComparisonTypePanel) {
        this.disableComparisonTypePanel = disableComparisonTypePanel;
    }

    public String getDisableValuePanel() {
        return disableValuePanel;
    }

    public void setDisableValuePanel(String disableValuePanel) {
        this.disableValuePanel = disableValuePanel;
    }

    public String getDisableConditionPanel() {
        return disableConditionPanel;
    }

    public void setDisableConditionPanel(String disableConditionPanel) {
        this.disableConditionPanel = disableConditionPanel;
    }

    public String getDisableClosePriorityPanel() {
        return disableClosePriorityPanel;
    }

    public void setDisableClosePriorityPanel(String disableClosePriorityPanel) {
        this.disableClosePriorityPanel = disableClosePriorityPanel;
    }

    public String getDisableEnumPanel() {
        return disableEnumPanel;
    }

    public void setDisableEnumPanel(String disableEnumPanel) {
        this.disableEnumPanel = disableEnumPanel;
    }

    public String getDisableEnumLiteralPanel() {
        return disableEnumLiteralPanel;
    }

    public void setDisableEnumLiteralPanel(String disableEnumLiteralPanel) {
        this.disableEnumLiteralPanel = disableEnumLiteralPanel;
    }

    public String getCollapseModelPanel() {
        return collapseModelPanel;
    }

    public void setCollapseModelPanel(String collapseModelPanel) {
        this.collapseModelPanel = collapseModelPanel;
    }

    public String getCollapseUpdatesPanel() {
        return collapseUpdatesPanel;
    }

    public void setCollapseUpdatesPanel(String collapseUpdatesPanel) {
        this.collapseUpdatesPanel = collapseUpdatesPanel;
    }

    public String getDisableOperationNamePanel() {
        return disableOperationNamePanel;
    }

    public void setDisableOperationNamePanel(String disableOperationNamePanel) {
        this.disableOperationNamePanel = disableOperationNamePanel;
    }

    public String getDisableOperationParameterPanel() {
        return disableOperationParameterPanel;
    }

    public void setDisableOperationParameterPanel(String disableOperationParameterPanel) {
        this.disableOperationParameterPanel = disableOperationParameterPanel;
    }

    public String getDisableParameterPanel() {
        return disableParameterPanel;
    }

    public void setDisableParameterPanel(String disableParameterPanel) {
        this.disableParameterPanel = disableParameterPanel;
    }
}
