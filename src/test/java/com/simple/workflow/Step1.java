package com.simple.workflow;


public class Step1 implements Task {

    public boolean execute(Context context) throws Exception {
        MyContext myContext = (MyContext)context;
        myContext.put("task_name", "Step 1");
        myContext.setTaskName("Step1");
        System.out.println("Step1");
        return false;  
    }

   
}
