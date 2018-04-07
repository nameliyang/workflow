package com.simple.workflow;


import java.util.Iterator;



public interface Workflow {

    String WORKFLOW_KEY = "com.simple.workflow.WORKFLOW";

    void addTask(String name, Task task);

    Task getTask(String name);

    Iterator getNames();


}

