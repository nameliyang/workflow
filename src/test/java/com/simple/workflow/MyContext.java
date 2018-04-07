package com.simple.workflow;

import com.simple.workflow.impl.ContextBase;


public class MyContext extends ContextBase {
    private String taskName;


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

}
