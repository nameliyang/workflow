package com.simple.workflow;


public class Step2 implements Task {

    public boolean execute(Context context) throws Exception {
        System.out.println("Step 2");
        MyContext myContext = (MyContext) context;
        System.out.println("task name: " + myContext.getTaskName());
        return false;
    }


}