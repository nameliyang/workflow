package com.simple.workflow.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.simple.workflow.Workflow;
import com.simple.workflow.WorkflowFactory;


public class WorkflowFactoryBase extends WorkflowFactory {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct an empty instance of {@link WorkflowFactoryBase}.  This
     * constructor is intended solely for use by {@link com.simple.workflow.WorkflowFactory}.</p>
     */
    public WorkflowFactoryBase() { }


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The default {@link com.simple.workflow.Workflow} for this {@link com.simple.workflow.WorkflowFactory}.</p>
     */
    private Workflow workflow = null;


    /**
     * <p>Map of named {@link com.simple.workflow.Workflow}s, keyed by workflow name.</p>
     */
    private Map workflows = new HashMap();


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Gets the default instance of Workflow associated with the factory
     * (if any); otherwise, return <code>null</code>.</p>
     *
     * @return the default Workflow instance
     */
    public Workflow getWorkflow() {

        return workflow;

    }


    /**
     * <p>Sets the default instance of Workflow associated with the factory.</p>
     *
     * @param workflow the default Workflow instance
     */
    public void setWorkflow(Workflow workflow) {

        this.workflow = workflow;

    }


    /**
     * <p>Retrieves a Workflow instance by name (if any); otherwise
     * return <code>null</code>.</p>
     *
     * @param name the name of the Workflow to retrieve
     * @return the specified Workflow
     */
    public Workflow getWorkflow(String name) {

        synchronized (workflows) {
            return (Workflow) workflows.get(name);
        }

    }


    /**
     * <p>Adds a named instance of Workflow to the factory (for subsequent
     * retrieval later).</p>
     *
     * @param name the name of the Workflow to add
     * @param workflow the Workflow to add
     */
    public void addWorkflow(String name, Workflow workflow) {

        synchronized (workflows) {
            workflows.put(name, workflow);
        }

    }



    public Iterator getNames() {

        synchronized (workflows) {
            return workflows.keySet().iterator();
        }

    }


}
