package com.simple.workflow.plugin;

import com.simple.workflow.Task;
import com.simple.workflow.Context;


public class ConditionalTask implements Task {

    public boolean execute(Context context) throws Exception {
        return false;  
    }
}
