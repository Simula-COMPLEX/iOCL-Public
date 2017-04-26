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

import no.simule.utils.Keywords;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * JSF action contain states of views and method to reset and replace with last step.
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-04-15
 */

@ManagedBean(name = "property")
@SessionScoped
public class PropertyBean extends ActionListener {


    private String ref = Keywords.SELF;
    private String tempRef = Empty;
    private String navigationRef = Empty;
    private String constraint = Empty;

    private List<String> contexts = new ArrayList<>();
    private String context = Empty;
    private String navigationContext = Empty;
    private String tempContext = Empty;
    private String globalContext = Empty;

    private String refContext = Empty;

    private String constraintType = Empty;
    private String globalConstraintType = Empty;

    private String contextOperation = Empty;
    private List<String> contextOperations = new ArrayList<>();
    private String globalContextOperation = Empty;

    private List<String> allTypes = new ArrayList<>();
    private String operationSignature = Empty;
    private String operationSignatureName = Empty;
    private String operationSignatureReturnType = Empty;
    private boolean operationSignatureSetReturnType = false;
    private String operationSignatureParameter = Empty;
    private String operationSignatureParameterCount = Empty;

    private String scope = Empty;
    private String globalScpoe = Empty;

    private List<String> classOperations = new ArrayList<>();
    private String classOperation = Empty;
    private String globalClassOperation = Empty;

    private List<String> properties = new ArrayList<>();
    private String property = Empty;
    private String globalProperty = Empty;

    private String predefinedOperation = Empty;
    private List<String> predefinedOperationParameters = new ArrayList<>();
    private String predefinedOperationParameter = Empty;


    private String operationType = Empty;
    private String globalOperationType = Empty;

    private List<String> operations = new ArrayList<>();

    private String operation = Empty;
    private String globalOperation = Empty;

    private boolean twoVariable = false;


    private String globalValueType = Empty;
    private String valueType = Empty;

    private String value = Empty;
    private String propertyType = Empty;

    private String suggestion = Empty;
    private List<String> suggestions = new ArrayList<>();

    private String inputValue = Empty;

    private List<String> enumerations = new ArrayList<>();
    private String enumeration = Empty;

    private List<String> enumLiterals = new ArrayList<>();
    private String enumLiteral = Empty;


    private List<String> parameters = new ArrayList<>();
    private String parameter = Empty;

    private String condition = Empty;

    private String priorityValue = Empty;

    public static String getEmpty() {
        return Empty;
    }


    /**
     * This method replace all states variable with other stat provided by main Action .
     *
     * @param bean object of this class
     */

    public void replace(PropertyBean bean) {
        setRef(bean.ref);
        setTempRef(bean.tempRef);
        setNavigationRef(bean.navigationRef);
        setConstraint(bean.constraint);
        setContexts(bean.contexts);
        setContext(bean.context);
        setRefContext(bean.refContext);
        setTempContext(bean.tempContext);
        setNavigationContext(bean.navigationContext);
        setGlobalContext(bean.globalContext);
        setConstraintType(bean.constraintType);
        setGlobalConstraintType(bean.globalConstraintType);
        setContextOperation(bean.contextOperation);
        setContextOperations(bean.contextOperations);
        setGlobalContextOperation(bean.globalContextOperation);
        setOperationSignature(bean.operationSignature);
        setOperationSignatureName(bean.operationSignatureName);
        setOperationSignatureReturnType(bean.operationSignatureReturnType);
        setOperationSignatureSetReturnType(bean.operationSignatureSetReturnType);
        setOperationSignatureParameter(bean.operationSignatureParameter);
        setOperationSignatureParameterCount(bean.operationSignatureParameterCount);
        setScope(bean.scope);
        setGlobalScpoe(bean.globalScpoe);
        setAllTypes(bean.allTypes);
        setProperties(bean.properties);
        setProperty(bean.property);
        setGlobalProperty(bean.globalProperty);
        setClassOperation(bean.classOperation);
        setGlobalClassOperation(bean.globalClassOperation);
        setClassOperations(bean.classOperations);
        setPredefinedOperation(bean.predefinedOperation);
        setPredefinedOperationParameters(bean.predefinedOperationParameters);
        setPredefinedOperationParameter(bean.predefinedOperationParameter);
        setOperationType(bean.operationType);
        setOperations(bean.operations);
        setTwoVariable(bean.twoVariable);
        setOperation(bean.operation);
        setGlobalOperation(bean.globalOperation);
        setValueType(bean.valueType);
        setGlobalValueType(bean.globalValueType);
        setSuggestion(bean.suggestion);
        setSuggestions(bean.suggestions);
        setInputValue(bean.inputValue);
        setValue(bean.value);
        setPropertyType(bean.propertyType);
        setEnumerations(bean.enumerations);
        setEnumeration(bean.enumeration);
        setEnumLiterals(bean.enumLiterals);
        setEnumLiteral(bean.enumLiteral);
        setParameters(bean.parameters);
        setParameter(bean.parameter);
        setPriorityValue(bean.priorityValue);
        setCondition(bean.condition);
    }

    /**
     * This method reset all state variables to start new constraint.
     */

    public void reset() {
        setRef(Keywords.SELF);
        setTempRef(Keywords.SELF);
        setNavigationRef(Empty);
        setConstraint(Empty);
        setContext(Empty);
        setRefContext(Empty);
        setTempContext(Empty);
        setNavigationContext(Empty);
        setGlobalContext(Empty);
        setConstraintType(Empty);
        setGlobalConstraintType(Empty);
        setContextOperation(Empty);
        setContextOperations(new ArrayList<String>());
        setGlobalContextOperation(Empty);
        setOperationSignature(Empty);
        setOperationSignatureName(Empty);
        setOperationSignatureReturnType(Empty);
        setOperationSignatureSetReturnType(false);
        setOperationSignatureParameter(Empty);
        setOperationSignatureParameterCount(Empty);
        setScope(Empty);
        setGlobalScpoe(Empty);
        setAllTypes(new ArrayList<String>());
        setProperties(new ArrayList<String>());
        setProperty(Empty);
        setGlobalProperty(Empty);
        setClassOperation(Empty);
        setGlobalClassOperation(Empty);
        setClassOperations(new ArrayList<>());
        setPredefinedOperation(Empty);
        setPredefinedOperationParameters(new ArrayList<String>());
        setPredefinedOperationParameter(Empty);
        setTwoVariable(false);
        setOperationType(Empty);
        setOperations(new ArrayList<String>());
        setOperation(Empty);
        setGlobalOperation(Empty);
        setValueType(Empty);
        setGlobalValueType(Empty);
        setSuggestion(Empty);
        setSuggestions(new ArrayList<>());
        setInputValue(Empty);
        setValue(Empty);
        setPropertyType(Empty);
        setEnumerations(new ArrayList<>());
        setEnumeration(Empty);
        setEnumLiterals(new ArrayList<>());
        setEnumLiteral(Empty);
        setPriorityValue(Empty);
        setCondition(Empty);
    }


    /*getter and setter*/

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public List<String> getContexts() {
        return contexts;
    }

    public void setContexts(List<String> contexts) {
        this.contexts = contexts;
    }

    public String getTempContext() {
        return tempContext;
    }

    public void setTempContext(String tempContext) {
        this.tempContext = tempContext;
    }

    public String getGlobalContext() {
        return globalContext;
    }

    public void setGlobalContext(String globalContext) {
        this.globalContext = globalContext;
    }

    public String getRefContext() {
        return refContext;
    }

    public void setRefContext(String refContext) {
        this.refContext = refContext;
    }

    public String getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(String constraintType) {
        this.constraintType = constraintType;
    }

    public String getGlobalConstraintType() {
        return globalConstraintType;
    }

    public void setGlobalConstraintType(String globalConstraintType) {
        this.globalConstraintType = globalConstraintType;
    }

    public String getContextOperation() {
        return contextOperation;
    }

    public void setContextOperation(String contextOperation) {
        this.contextOperation = contextOperation;
    }

    public List<String> getContextOperations() {
        return contextOperations;
    }

    public void setContextOperations(List<String> contextOperations) {
        this.contextOperations = contextOperations;
    }

    public String getGlobalContextOperation() {
        return globalContextOperation;
    }

    public void setGlobalContextOperation(String globalContextOperation) {
        this.globalContextOperation = globalContextOperation;
    }

    public List<String> getAllTypes() {
        return allTypes;
    }

    public void setAllTypes(List<String> allTypes) {
        this.allTypes = allTypes;
    }

    public String getOperationSignatureName() {
        return operationSignatureName;
    }

    public void setOperationSignatureName(String operationSignatureName) {
        this.operationSignatureName = operationSignatureName;
    }

    public String getOperationSignatureReturnType() {
        return operationSignatureReturnType;
    }

    public void setOperationSignatureReturnType(String operationSignatureReturnType) {
        this.operationSignatureReturnType = operationSignatureReturnType;
    }

    public boolean isOperationSignatureSetReturnType() {
        return operationSignatureSetReturnType;
    }

    public void setOperationSignatureSetReturnType(boolean operationSignatureSetReturnType) {
        this.operationSignatureSetReturnType = operationSignatureSetReturnType;
    }

    public String getOperationSignatureParameter() {
        return operationSignatureParameter;
    }

    public void setOperationSignatureParameter(String operationSignatureParameter) {
        this.operationSignatureParameter = operationSignatureParameter;
    }

    public String getOperationSignatureParameterCount() {
        return operationSignatureParameterCount;
    }

    public void setOperationSignatureParameterCount(String operationSignatureParameterCount) {
        this.operationSignatureParameterCount = operationSignatureParameterCount;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getGlobalScpoe() {
        return globalScpoe;
    }

    public void setGlobalScpoe(String globalScpoe) {
        this.globalScpoe = globalScpoe;
    }

    public List<String> getClassOperations() {
        return classOperations;
    }

    public void setClassOperations(List<String> classOperations) {
        this.classOperations = classOperations;
    }

    public String getClassOperation() {
        return classOperation;
    }

    public void setClassOperation(String classOperation) {
        this.classOperation = classOperation;
    }

    public String getGlobalClassOperation() {
        return globalClassOperation;
    }

    public void setGlobalClassOperation(String globalClassOperation) {
        this.globalClassOperation = globalClassOperation;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getGlobalProperty() {
        return globalProperty;
    }

    public void setGlobalProperty(String globalProperty) {
        this.globalProperty = globalProperty;
    }

    public String getPredefinedOperation() {
        return predefinedOperation;
    }

    public void setPredefinedOperation(String predefinedOperation) {
        this.predefinedOperation = predefinedOperation;
    }

    public List<String> getPredefinedOperationParameters() {
        return predefinedOperationParameters;
    }

    public void setPredefinedOperationParameters(List<String> predefinedOperationParameters) {
        this.predefinedOperationParameters = predefinedOperationParameters;
    }

    public String getPredefinedOperationParameter() {
        return predefinedOperationParameter;
    }

    public void setPredefinedOperationParameter(String predefinedOperationParameter) {
        this.predefinedOperationParameter = predefinedOperationParameter;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getGlobalOperationType() {
        return globalOperationType;
    }

    public void setGlobalOperationType(String globalOperationType) {
        this.globalOperationType = globalOperationType;
    }

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getGlobalOperation() {
        return globalOperation;
    }

    public void setGlobalOperation(String globalOperation) {
        this.globalOperation = globalOperation;
    }

    public String getGlobalValueType() {
        return globalValueType;
    }

    public void setGlobalValueType(String globalValueType) {
        this.globalValueType = globalValueType;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public List<String> getEnumerations() {
        return enumerations;
    }

    public void setEnumerations(List<String> enumerations) {
        this.enumerations = enumerations;
    }

    public String getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    public List<String> getEnumLiterals() {
        return enumLiterals;
    }

    public void setEnumLiterals(List<String> enumLiterals) {
        this.enumLiterals = enumLiterals;
    }

    public String getEnumLiteral() {
        return enumLiteral;
    }

    public void setEnumLiteral(String enumLiteral) {
        this.enumLiteral = enumLiteral;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getPriorityValue() {
        return priorityValue;
    }

    public void setPriorityValue(String priorityValue) {
        this.priorityValue = priorityValue;
    }

    public String getOperationSignature() {
        return operationSignature;
    }

    public void setOperationSignature(String operationSignature) {
        this.operationSignature = operationSignature;
    }

    public String getTempRef() {
        return tempRef;
    }

    public void setTempRef(String tempRef) {
        this.tempRef = tempRef;
    }

    public String getNavigationContext() {
        return navigationContext;
    }

    public void setNavigationContext(String navigationContext) {
        this.navigationContext = navigationContext;
    }

    public boolean isTwoVariable() {
        return twoVariable;
    }

    public void setTwoVariable(boolean twoVariable) {
        this.twoVariable = twoVariable;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public String getNavigationRef() {
        return navigationRef;
    }

    public void setNavigationRef(String navigationRef) {
        this.navigationRef = navigationRef;
    }
}
