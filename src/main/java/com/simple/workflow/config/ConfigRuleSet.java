package com.simple.workflow.config;


import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;


public class ConfigRuleSet extends RuleSetBase {

    // ----------------------------------------------------- Instance Variables


    private String workflowClass = "com.simple.workflow.impl.WorkflowBase";
    private String workflowElement = "workflow";
    private String flowClass = "com.simple.workflow.impl.FlowBase";
    private String flowElement = "flow";
    private String classAttribute = "className";
    private String taskElement = "task";
    private String defineElement = "define";
    private String nameAttribute = "name";


    // ------------------------------------------------------------- Properties

    public String getWorkflowClass() {
        return (this.workflowClass);
    }


    public void setWorkflowClass(String workflowClass) {
        this.workflowClass = workflowClass;
    }


    public String getWorkflowElement() {
        return (this.workflowElement);
    }


    public void setWorkflowElement(String workflowElement) {
        this.workflowElement = workflowElement;
    }


    public String getFlowClass() {
        return (this.flowClass);
    }



    public void setFlowClass(String flowClass) {
        this.flowClass = flowClass;
    }


    public String getFlowElement() {
        return (this.flowElement);
    }


    public void setFlowElement(String flowElement) {
        this.flowElement = flowElement;
    }


    public String getClassAttribute() {
        return (this.classAttribute);
    }


    public void setClassAttribute(String classAttribute) {
        this.classAttribute = classAttribute;
    }


    public String getTaskElement() {
        return (this.taskElement);
    }


    public void setTaskElement(String taskElement) {
        this.taskElement = taskElement;
    }


    public String getDefineElement() {
        return (this.defineElement);
    }


    public void setDefineElement(String defineElement) {
        this.defineElement = defineElement;
    }


    public String getNameAttribute() {
        return (this.nameAttribute);
    }


    public void setNameAttribute(String nameAttribute) {
        this.nameAttribute = nameAttribute;
    }


    // --------------------------------------------------------- Public Methods


    public void addRuleInstances(Digester digester) {

        // Add rules for a workflow element
        digester.addRule("*/" + getWorkflowElement(),
                         new ConfigWorkflowRule(nameAttribute, workflowClass));
        digester.addSetProperties("*/" + getWorkflowElement());

        // Add rules for a workflow element
        digester.addObjectCreate("*/" + getFlowElement(),
                                 getFlowClass(),
                                 getClassAttribute());
        digester.addSetProperties("*/" + getFlowElement());
        digester.addRule("*/" + getFlowElement(),
                         new ConfigRegisterRule(nameAttribute));

        // Add rules for a task element
        digester.addObjectCreate("*/" + getTaskElement(),
                                 null,
                                 getClassAttribute());
        digester.addSetProperties("*/" + getTaskElement());
        digester.addRule("*/" + getTaskElement(),
                         new ConfigRegisterRule(nameAttribute));

        // Add rules for a define element
        digester.addRule("*/" + getDefineElement(),
                         new ConfigDefineRule(getNameAttribute(),
                                              getClassAttribute()));

    }

}
