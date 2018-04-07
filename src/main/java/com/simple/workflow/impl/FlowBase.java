package com.simple.workflow.impl;


import java.util.Collection;
import java.util.Iterator;

import com.simple.workflow.Flow;
import com.simple.workflow.Task;
import com.simple.workflow.Context;
import com.simple.workflow.Handler;


public class FlowBase implements Flow {

    // ----------------------------------------------------------- Constructors


    public FlowBase() {

    }


    public FlowBase(Task task) {

        addTask(task);

    }


    public FlowBase(Task[] tasks) {

        if (tasks == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < tasks.length; i++) {
            addTask(tasks[i]);
        }

    }


    public FlowBase(Collection tasks) {

        if (tasks == null) {
            throw new IllegalArgumentException();
        }
        Iterator elements = tasks.iterator();
        while (elements.hasNext()) {
            addTask((Task) elements.next());
        }

    }

    // ----------------------------------------------------- Instance Variables


    protected Task[] tasks = new Task[0];


    protected boolean frozen = false;

    // ---------------------------------------------------------- Flow Methods


    public void addTask(Task task) {

        if (task == null) {
            throw new IllegalArgumentException();
        }
        if (frozen) {
            throw new IllegalStateException();
        }
        Task[] results = new Task[tasks.length + 1];
        System.arraycopy(tasks, 0, results, 0, tasks.length);
        results[tasks.length] = task;
        tasks = results;

    }


    public boolean execute(Context context) throws Exception {

        // Verify our parameters
        if (context == null) {
            throw new IllegalArgumentException();
        }

        // Freeze the configuration of the task list
        frozen = true;

        // Execute the tasks in this list until one returns true
        // or throws an exception
        boolean saveResult = false;
        Exception saveException = null;
        int i = 0;
        int n = tasks.length;
        for (i = 0; i < n; i++) {
            try {
                //if (!(tasks[i] instanceof Handler)) {
                    saveResult = tasks[i].execute(context);
                    if (saveResult) {
                        break;
                    }
                //}
            } catch (Exception e) {
                saveException = e;
                break;
            }
        }

        // Call postprocess methods on Filters in reverse order
        if (i >= n) { // Fell off the end of the workflow
            i--;
        }
        boolean handled = false;
        boolean result = false;
        for (int j = i; j >= 0; j--) {
            if (tasks[j] instanceof Handler) {
                try {
                    result =
                            ((Handler) tasks[j]).postprocess(context,
                                    saveException);
                    if (result) {
                        handled = true;
                    }
                } catch (Exception e) {
                    // Silently ignore
                }
            }
        }

        // Return the exception or result state from the last execute()
        if ((saveException != null) && !handled) {
            throw saveException;
        } else {
            return (saveResult);
        }

    }

    // -------------------------------------------------------- Package Methods


    Task[] getTasks() {

        return (tasks);

    }


}
