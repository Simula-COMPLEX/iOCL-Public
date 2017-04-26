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

import no.simule.models.cd.ClassAttribute;
import no.simule.models.cd.ClassOperation;
import no.simule.models.cd.ClassStructure;
import no.simule.models.cd.OperationParameter;
import no.simule.utils.Keywords;
import no.simule.utils.Mappings;
import no.simule.utils.iOCLTypes;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ManagedBean(name = "operationPanel")
@SessionScoped
public class OperationPanel implements Serializable {
    public static final String Empty = "";
    public static final String Space = " ";
    public static final String Dot = ".";
    public static final String Arrow = "->";
    public static final String Comma = ",";
    public static final String Apostrophe = "'";
    private static final long serialVersionUID = 1L;
    private QueryListener bean;
    private PanelBean panel;
    private PropertyBean property;

    public OperationPanel() {

        FacesContext context = FacesContext.getCurrentInstance();
        bean = context.getApplication().evaluateExpressionGet(context, "#{bean}", QueryListener.class);

        panel = context.getApplication().evaluateExpressionGet(context, "#{panel}", PanelBean.class);
        property =
                context.getApplication().evaluateExpressionGet(context, "#{property}", PropertyBean.class);


    }

    public void backOperationListener(AjaxBehaviorEvent event) {
        bean.selection();
        QueryListener queryListener = bean.beanFromStack();
        if (queryListener != null) {
            bean.replace(queryListener);
            property.setProperty(Empty);
            property.setOperation(Empty);
        }
    }


    public void operationListener(AjaxBehaviorEvent event) {
        if (property.getOperation() == null || property.getOperation().isEmpty()) {
            return;
        }

        bean.beanInStack();
        bean.selection();
        if (!property.getGlobalOperationType().equals(Empty)) {

            refranceOperation();

        } else {

            newOperation();
        }

        Collections.sort(property.getOperations());
        property.setGlobalOperation(property.getOperation());
        property.setOperation(Empty);
    }

    private void refranceOperation() {

        if (Mappings.getArithmeticOperationsType().get(property.getOperation()) != null) {
            if (Mappings.getArithmeticOperationsType().get(property.getOperation())
                    .equals(iOCLTypes.primitiveType)) {

                primitveFuntion();

            } else if (Mappings.getArithmeticOperationsType().get(property.getOperation())
                    .equals(iOCLTypes.objectType)) {
                chainOperation();
            }
        }
    }

    private void primitveFuntion() {

        if (Mappings.getArithmeticOperationsArguments().get(property.getOperation()).size() > 0) {

            property.setConstraint(property.getConstraint() + Space
                    + bean.getOperationsMap().get(property.getOperation()) + Space);

            for (int i = 0; i < Mappings.getArithmeticOperationsArguments().get(property.getOperation())
                    .size(); i++) {

                bean.getPropertyStack()
                        .push(Mappings.getArithmeticOperationsArguments().get(property.getOperation()).get(i));
            }

            panel.showComparisonTypePanel();

        }
    }

    private void chainOperation() {

        if (Mappings.getArithmeticOperationsArguments().get(property.getOperation()).size() == 0) {
            property.setConstraint(property.getConstraint() + Dot
                    + bean.getOperationsMap().get(property.getOperation()) + "()" + Space);

            property
                    .setPropertyType(Mappings.getArithmeticOperationsReturn().get(property.getOperation()));
            panel.showOperationTypePanel();
        } else {
            property.setConstraint(property.getConstraint() + Dot
                    + bean.getOperationsMap().get(property.getOperation()) + "(" + Space);


            for (int i = 0; i < Mappings.getArithmeticOperationsArguments().get(property.getOperation())
                    .size(); i++) {
                bean.getPropertyStack()
                        .push(Mappings.getArithmeticOperationsArguments().get(property.getOperation()).get(i));
            }


            panel.showComparisonTypePanel();

        }
    }

    private void newOperation() {

        if (property.getGlobalScpoe().equals(Keywords.Local_Scope)) {
            newLocalOperation();
        } else if (property.getGlobalScpoe().equals(Keywords.Assosiation_Scope)) {

            newAssosiationOperation();

        }
    }

    private void newLocalOperation() {
        property.setConstraint(property.getConstraint() + Space
                + bean.getOperationsMap().get(property.getOperation()) + Space);
        panel.showComparisonTypePanel();

    }

    private void newAssosiationOperation() {

        if (Mappings.getCollectionNotItreate().get(property.getOperation()) != null
                && (Mappings.getCollectionNotItreate().get(property.getOperation()))) {

            newAssosiationNotItreateOperation();
        } else if (Mappings.getCollectionInput().get(property.getOperation()) != null
                && (Mappings.getCollectionInput().get(property.getOperation()))) {

            newAssosiationInput();
        } else {
            newAssosiationItreateOperation();
        }
    }

    private void newAssosiationInput() {
/*        property.setRef(property.getGlobalProperty().substring(0, 2));*/
        property.setConstraint(property.getConstraint() + Arrow
                + bean.getOperationsMap().get(property.getOperation()) + "(" + Space);

        boolean collection = false;
        if (property.getOperation().equals(Mappings.includesAll)
                || property.getOperation().equals(Mappings.excludesAll)
                || property.getOperation().equals(Mappings.union)
                || property.getOperation().equals(Mappings.intersection)) {
            collection = true;
        }

        if (property.getPropertyType().equals(Empty)) {
            if (!property.getGlobalClassOperation().equals(Empty)) {

                String operation = property.getGlobalClassOperation();
                String methodName = operation
                        .substring(0, operation.indexOf("(")).trim();

                List<ClassOperation> cms = bean.propertyOperation(property.getContext(), methodName);
                ClassOperation cm = null;
                if (!cms.isEmpty()) {
                    cm = cms.get(0);
                }
                if (cm != null) {
                    property.setPropertyType(cm.getReturnType().getType());
                }
                property.setGlobalClassOperation(Empty);
            }
        }

        if (property.getOperation().equals(Mappings.closure)) {
            collection = false;
        }

        // check with globel context like self and its properties
        if (!property.getContext().equals(property.getGlobalContext())) {
            suggestions(property.getGlobalContext(), Keywords.SELF, collection, bean.getDepthlevel());
        }

        // check with current context
        suggestions(property.getContext(), property.getRef(), collection, bean.getDepthlevel());

        //check with temp context
        suggestions(property.getTempContext(), property.getTempRef(), collection, bean.getDepthlevel());


        for (String row : bean.getContextStack()) {

            String tokens[] = row.split(":");
            String ref = tokens[0];
            String context = tokens[1];

            if ((ref.equals(Keywords.SELF) && context.equals(property.getGlobalContext())) || (ref.equals(property.getRef()) && context.equals(property.getContext()))) {
                continue;
            }

            suggestions(context, ref, collection, bean.getDepthlevel());

        }


        //test with Context operation in case of pre post body type constraint
        if (!property.getGlobalContextOperation().equals(Empty)) {
            ClassOperation cm = bean.operationObject(property.getGlobalContext(), property.getGlobalContextOperation());
            suggestions(collection);

        }

        collection = false;
        String suggestion = property.getSuggestion().trim();

        property.setSuggestion(suggestion.length() > 0
                ? property.getSuggestion().trim().substring(0, suggestion.length() - 1) : suggestion);

        bean.getPriorityStack().push("input");
        panel.showTextValuePanel();
    }


    private void suggestions(String context, String ref, boolean collection, int dept) {
        if (context.isEmpty() || dept < 1) {
            return;
        }

        --dept;

        if (context.equals(property.getPropertyType()) && !ref.contains(Dot)) {

            if (collection) {
                if (!property.getSuggestions().contains(ref + Arrow + "asSet()")) {
                    property.getSuggestions().add(ref + Arrow + "asSet()");
                    property.setSuggestion(property.getSuggestion() + ref + Arrow + "asSet()" + Comma + Space);
                }


            } else {
                if (!property.getSuggestions().contains(ref)) {
                    property.getSuggestions().add(ref);
                    property.setSuggestion(property.getSuggestion() + ref + Comma + Space);
                }
            }
        }

        ClassStructure cs = bean.classObject(context);
        for (ClassAttribute ca : cs.getAttributes()) {
            if (ca.getType().equals(property.getPropertyType())) {

                if (collection) {

                    if (ca.isCollection()) {


                        if (!property.getSuggestions().contains(ref + Dot + ca.getName())) {
                            property.getSuggestions().add(ref + Dot + ca.getName());
                            property
                                    .setSuggestion(property.getSuggestion() + ref + Dot + ca.getName() + Space + Comma + Space);
                        }

                    } else {

                        if (!property.getSuggestions().contains(ref + Dot + ca.getName() + "->asSet()")) {
                            property.getSuggestions().add(ref + Dot + ca.getName() + "->asSet()");
                            property.setSuggestion(
                                    property.getSuggestion() + ref + Dot + ca.getName() + "->asSet()" + Space + Comma + Space);
                        }
                    }

                } else {

                    if (!property.getSuggestions().contains(ref + Dot + ca.getName())) {
                        property.getSuggestions().add(ref + Dot + ca.getName());
                        property.setSuggestion(property.getSuggestion() + ref + Dot + ca.getName() + Space + Comma + Space);
                    }
                }

            }

            if (ca.isClass()) {
                if (dept > 1) {
                    suggestions(ca.getType(), ref + Dot + ca.getName(), collection, dept);
                }
            }


        }
    }


    private void suggestions(boolean collection) {
        String methodName = property.getGlobalContextOperation()
                .substring(0, property.getGlobalContextOperation().indexOf("(")).trim();

        List<ClassOperation> cms = bean.propertyOperation(property.getGlobalContext(), methodName);
        ClassOperation cm = null;
        if (!cms.isEmpty()) {
            cm = cms.get(0);
        }

        if (cm != null) {
            for (OperationParameter ca : cm.getParameters()) {

                if (ca.getType().equals(property.getPropertyType())) {
                    if (collection) {

                        if (ca.isCollection()) {
                            property
                                    .setSuggestion(property.getSuggestion() + ca.getName() + Space + ", " + Space);
                        } else {
                            property.setSuggestion(
                                    property.getSuggestion() + ca.getName() + "->asSet()" + Space + ", " + Space);
                        }

                    } else {
                        property.setSuggestion(property.getSuggestion() + ca.getName() + Space + ", " + Space);
                    }
                }
            }
        }
    }

    private void newAssosiationItreateOperation() {


        if (property.getRef().equals(Keywords.SELF)) {

            if (!bean.getContextStack().contains(property.getRef() + ":" + property.getGlobalContext())) {
                bean.getContextStack().add(property.getRef() + ":" + property.getGlobalContext());
            }
            property.setNavigationContext(Empty);

        } else {

// need to test
            if (!property.getNavigationContext().equals(Empty) && !property.getNavigationRef().equals(Keywords.SELF)) {

                if (!bean.getContextStack().contains(property.getRef() + ":" + property.getNavigationContext())) {
                    bean.getContextStack().add(property.getRef() + ":" + property.getNavigationContext());
                }
                property.setNavigationContext(Empty);
                property.setNavigationRef(Empty);
            } else {
                if (!bean.getContextStack().contains(property.getRef() + ":" + property.getContext())) {
                    bean.getContextStack().add(property.getRef() + ":" + property.getContext());
                }
            }


        }

        if (!property.getGlobalProperty().equals(Empty)) {
            property.setRef(property.getGlobalProperty().substring(0, 2));
        } else if (!property.getGlobalClassOperation().equals(Empty)) {
            property.setRef(property.getGlobalClassOperation().substring(0, 2));
        } else {
            property.setRef(property.getContext().toLowerCase().substring(0, 2));
        }

        if (property.isTwoVariable()) {
            property.setConstraint(property.getConstraint() + Arrow
                    + bean.getOperationsMap().get(property.getOperation()) + "(" + property.getRef() + Comma + Space + property.getRef() + "2" + " |");


            if (!bean.getContextStack().contains(property.getRef() + "2" + ":" + property.getPropertyType())) {
                bean.getContextStack().add(property.getRef() + "2" + ":" + property.getPropertyType());
            }

            property.setTwoVariable(false);

        } else {
            property.setConstraint(property.getConstraint() + Arrow
                    + bean.getOperationsMap().get(property.getOperation()) + "(" + property.getRef() + " |");
        }

        if (!property.getPropertyType().equals(Empty)) {
            property.setContext(property.getPropertyType());
        }

//        if (property.getOperation().equals(Operation.forAll)
//                || property.getOperation().equals(Operation.exists)) {
//            bean.getPriorityStack().push("close");
//        } else if (property.getOperation().equals(Operation.select)
//                || property.getOperation().equals(Operation.reject)) {
//            bean.getPriorityStack().push("return");
//        } else if (property.getOperation().equals(Operation.collect)) {
//            bean.getPriorityStack().push("newList");
//        }


        bean.getPriorityStack().push(Mappings.getCollectionItreateReturn().get(property.getOperation()));

        panel.showScopePanel();
        panel.showAddPriorityOperationPanel();
        if (!bean.getPriorityStack().empty()) {
            panel.showClosePriorityOperationPanel();
        } else {
            panel.hideClosePriorityOperationPanel();
        }


        //need to b test
        property.setGlobalClassOperation(Empty);
        //need to b test
        property.setGlobalProperty(Empty);

    }

    private void newAssosiationNotItreateOperation() {
        if (Mappings.getCollectionNotItreateType().get(property.getOperation())
                .equalsIgnoreCase(iOCLTypes.List)) {
            property.setConstraint(property.getConstraint() + Arrow
                    + bean.getOperationsMap().get(property.getOperation()) + "()" + Space);

            property.setOperations(new ArrayList<>());
            property.setGlobalScpoe(Keywords.Assosiation_Scope);
            bean.setOperationsMap(Mappings.getCollectionOperations());
            property.getOperations().addAll(bean.getOperationsMap().keySet());

            property.setRef(Keywords.SELF);
            if (!bean.getPriorityStack().empty()) {
                panel.showClosePriorityOperationPanel();

            } else {
                panel.hideClosePriorityOperationPanel();
            }
            Collections.sort(property.getOperations());

            panel.hideClosePriorityOperationPanel();
            if (bean.isRightSide()) {
                panel.showConditionPanel();
                bean.setRightSide(false);
            } else {
                panel.showCollectionOperationPanel();
            }

        } else if (Mappings.getCollectionNotItreateType().get(property.getOperation())
                .equalsIgnoreCase(iOCLTypes.Any)) {
            property.setConstraint(property.getConstraint() + Arrow
                    + bean.getOperationsMap().get(property.getOperation()) + "()" + Dot);
            property.setGlobalScpoe(Keywords.Assosiation_Scope);
            if (!bean.getContextStack().contains(property.getRef() + ":" + property.getContext())) {
                bean.getContextStack().add(property.getRef() + ":" + property.getContext());
            }

            if (!property.getPropertyType().equals(Empty)) {
                property.setContext(property.getPropertyType());
            }

            if (bean.isRightSide()) {
                panel.showConditionPanel();
                bean.setRightSide(false);
            } else {
                panel.showScopePanel();
            }

        } else {
            property.setConstraint(property.getConstraint() + Arrow
                    + bean.getOperationsMap().get(property.getOperation()) + "()" + Space);
            property.setGlobalScpoe(Keywords.Local_Scope);
            if (Mappings.getCollectionNotItreateType().get(property.getOperation())
                    .equalsIgnoreCase(iOCLTypes.Integer)) {
                bean.setOperationsMap(Mappings.getIntegerOperations());
                property.setPropertyType(iOCLTypes.Integer);
            } else if (Mappings.getCollectionNotItreateType().get(property.getOperation())
                    .equalsIgnoreCase(iOCLTypes.String)) {
                bean.setOperationsMap(Mappings.getStringOperations());
                property.setPropertyType(iOCLTypes.String);
            } else if (Mappings.getCollectionNotItreateType().get(property.getOperation())
                    .equalsIgnoreCase(iOCLTypes.Boolean)) {
                bean.setOperationsMap(Mappings.getBooleanOperations());
                property.setPropertyType(iOCLTypes.Boolean);
            } else if (Mappings.getCollectionNotItreateType().get(property.getOperation())
                    .equalsIgnoreCase(iOCLTypes.UnlimitedNatural)) {
                bean.setOperationsMap(Mappings.getUnlimitedNaturalOperations());
                property.setPropertyType(iOCLTypes.UnlimitedNatural);
            } else if (Mappings.getCollectionNotItreateType().get(property.getOperation())
                    .equalsIgnoreCase(iOCLTypes.Real)) {
                bean.setOperationsMap(Mappings.getRealOperations());
                property.setPropertyType(iOCLTypes.Real);
            } else if (Mappings.getCollectionNotItreateType().get(property.getOperation())
                    .equalsIgnoreCase(iOCLTypes.OclVoid)) {
                bean.setOperationsMap(Mappings.getOclVoidOperations());
                property.setPropertyType(iOCLTypes.OclVoid);
            } else if (Mappings.getCollectionNotItreateType().get(property.getOperation())
                    .equalsIgnoreCase(iOCLTypes.OclInvalid)) {
                bean.setOperationsMap(Mappings.getOclInvalidOperations());
                property.setPropertyType(iOCLTypes.OclInvalid);
            }

            property.getOperations().removeAll(property.getOperations());
            property.getOperations().addAll(bean.getOperationsMap().keySet());


            if (bean.isRightSide()) {
                panel.showConditionPanel();
                bean.setRightSide(false);
            } else {
                panel.showOperationTypePanel();
            }

        }
    }

    public void returnCollectionListener(AjaxBehaviorEvent event) {
        property.getOperations().removeAll(property.getOperations());
        property.setConstraint(property.getConstraint() + " ) ");
        property.setGlobalScpoe(Keywords.Assosiation_Scope);
        bean.setOperationsMap(Mappings.getCollectionOperations());
        property.getOperations().addAll(bean.getOperationsMap().keySet());
        panel.hideClosePriorityOperationPanel();
        panel.showOperationPanel();
        property.setRef(Keywords.SELF);
        if (!bean.getPriorityStack().empty()) {
            panel.showClosePriorityOperationPanel();

        } else {
            panel.hideClosePriorityOperationPanel();
        }
        bean.getPriorityStack().pop();
        Collections.sort(property.getOperations());

        resetContext();

    }


    public void newListCollectionListener(AjaxBehaviorEvent event) {

        String constraint = property.getConstraint().trim();

        property.setConstraint(
                constraint.endsWith(Dot) ? constraint.substring(0, constraint.length() - 1) : constraint);

        property.getOperations().removeAll(property.getOperations());
        property.setConstraint(property.getConstraint() + " ) ");
        property.setGlobalScpoe(Keywords.Assosiation_Scope);
        bean.setOperationsMap(Mappings.getCollectionOperations());
        property.getOperations().addAll(bean.getOperationsMap().keySet());
        panel.hideClosePriorityOperationPanel();
        panel.showCollectionOperationPanel();
        property.setRef(Keywords.SELF);

        // if (!bean.getContextStack().contains(property.getContext())) {
        // bean.getContextStack().add(property.getContext());
        // }
        property.setContext(property.getPropertyType());

        if (!bean.getPriorityStack().empty()) {
            panel.showClosePriorityOperationPanel();

        } else {
            panel.hideClosePriorityOperationPanel();
        }
        bean.getPriorityStack().pop();
        Collections.sort(property.getOperations());

        resetContext();
    }


    public void closeCollectionListener(AjaxBehaviorEvent event) {
        property.setConstraint(property.getConstraint() + " ) ");
        panel.hideClosePriorityOperationPanel();
        panel.showConditionPanel();
        property.setRef(Keywords.SELF);

        if (!bean.getPriorityStack().empty()) {
            panel.showClosePriorityOperationPanel();

        } else {
            panel.hideClosePriorityOperationPanel();
        }
        bean.getPriorityStack().pop();

        resetContext();
    }


    private void resetContext() {

        if (!bean.getContextStack().isEmpty()) {
            String contextString = bean.getContextStack().remove(bean.getContextStack().size() - 1);
            String tokens[] = contextString.split(":");
            String ref = tokens[0];
            String context = tokens[1];
            if (ref.contains("2")) {
                if (!bean.getContextStack().isEmpty()) {
                    contextString = bean.getContextStack().remove(bean.getContextStack().size() - 1);
                    String tokens2[] = contextString.split(":");
                    ref = tokens2[0];
                    context = tokens2[1];
                }
            }

            property.setContext(context);
            property.setRef(ref);

        }

    }
}
