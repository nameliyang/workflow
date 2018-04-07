
package com.simple.workflow.config;


import com.simple.workflow.Workflow;
import com.simple.workflow.Flow;
import com.simple.workflow.Task;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;


class ConfigRegisterRule extends Rule {


    // ----------------------------------------------------------- Constructors


    public ConfigRegisterRule(String nameAttribute) {
        super();
        this.nameAttribute = nameAttribute;
    }


    // ----------------------------------------------------- Instance Variables


    private String nameAttribute = null;


    // --------------------------------------------------------- Public Methods


    public void begin(String namespace, String name, Attributes attributes)
        throws Exception {

        // Is the top object a Task?
        Object top = digester.peek(0);
        if ((top == null)
            || !(top instanceof Task)) {
            return;
        }
        Task task = (Task) top;

        // Is the next object a Workflow or a Flow?
        Object next = digester.peek(1);
        if (next == null) {
            return;
        }

        // Register the top element appropriately
        if (next instanceof Workflow) {
            String nameValue = attributes.getValue(nameAttribute);
            if (nameValue != null) {
                ((Workflow) next).addTask(nameValue, task);
            }
        } else if (next instanceof Flow) {
            ((Flow) next).addTask(task);
        }

    }


}
