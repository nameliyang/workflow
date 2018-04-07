package com.simple.workflow.config;

import com.simple.workflow.Workflow;
import com.simple.workflow.WorkflowFactory;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;


class ConfigWorkflowRule extends Rule {


    // ----------------------------------------------------------- Constructors



    public ConfigWorkflowRule(String nameAttribute, String workflowClass) {
        super();
        this.nameAttribute = nameAttribute;
        this.workflowClass = workflowClass;
    }


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The fully qualified class name of a {@link com.simple.workflow.Workflow} class to use for
     * instantiating new instances.</p>
     */
    private String workflowClass = null;


    /**
     * <p>The name of the attribute under which we can retrieve the name
     * this workflow should be registered with (if any).</p>
     */
    private String nameAttribute = null;


    // --------------------------------------------------------- Public Methods


    public void begin(String namespace, String name, Attributes attributes)
        throws Exception {

        // Retrieve any current Workflow with the specified name
        Workflow workflow = null;
        WorkflowFactory factory = WorkflowFactory.getInstance();
        String nameValue = attributes.getValue(nameAttribute);
        if (nameValue == null) {
            workflow = factory.getWorkflow();
        } else {
            workflow = factory.getWorkflow(nameValue);
        }

        // Create and register a new Workflow instance if necessary
        if (workflow == null) {
            Class clazz = digester.getClassLoader().loadClass(workflowClass);
            workflow = (Workflow) clazz.newInstance();
            if (nameValue == null) {
                factory.setWorkflow(workflow);
            } else {
                factory.addWorkflow(nameValue, workflow);
            }            
        }

        // Push this Workflow onto the top of the stack
        digester.push(workflow);

    }


}
