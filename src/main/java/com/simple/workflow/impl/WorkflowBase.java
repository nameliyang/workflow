package com.simple.workflow.impl;


import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import com.simple.workflow.Workflow;
import com.simple.workflow.Task;


public class WorkflowBase implements Workflow {


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The map of named {@link com.simple.workflow.Task}s, keyed by name.
     */
    protected Map tasks = Collections.synchronizedMap(new HashMap());


    // --------------------------------------------------------- Constructors

    /**
     * Create an empty workflow.
     */
    public WorkflowBase() { }

    /**
     * <p>Create a workflow whose tasks are those specified in the given <code>Map</code>.
     * All Map keys should be <code>String</code> and all values should be <code>Task</code>.</p>
     *
     * @param tasks Map of Tasks.
     *
     */
    public WorkflowBase( Map tasks) {
        this.tasks = Collections.synchronizedMap(tasks);
    }

    // --------------------------------------------------------- Public Methods


    /**
     * <p>Add a new name and associated {@link com.simple.workflow.Task}
     * to the set of named tasks known to this {@link com.simple.workflow.Workflow},
     * replacing any previous task for that name.
     *
     * @param name Name of the new task
     * @param task {@link com.simple.workflow.Task} to be returned
     *  for later lookups on this name
     */
    public void addTask(String name, Task task) {

        tasks.put(name, task);

    }

    /**
     * <p>Return the {@link com.simple.workflow.Task} associated with the
     * specified name, if any; otherwise, return <code>null</code>.</p>
     *
     * @param name Name for which a {@link com.simple.workflow.Task}
     *  should be retrieved
     * @return The Task associated with the specified name.
     */
    public Task getTask(String name) {

        return ((Task) tasks.get(name));

    }


    /**
     * <p>Return an <code>Iterator</code> over the set of named tasks
     * known to this {@link com.simple.workflow.Workflow}.  If there are no known tasks,
     * an empty Iterator is returned.</p>
     * @return An iterator of the names in this Workflow.
     */
    public Iterator getNames() {

        return (tasks.keySet().iterator());

    }

    /**
     * Converts this Workflow to a String.  Useful for debugging purposes.
     * @return a representation of this workflow as a String
     */
    public String toString() {

        Iterator names = getNames();
        StringBuffer str =
            new StringBuffer("[" + this.getClass().getName() + ": ");

        while (names.hasNext()) {
            str.append(names.next());
            if (names.hasNext()) {
            str.append(", ");
            }
        }
        str.append("]");

        return str.toString();

    }
}
