package com.simple.workflow;


public class Step3 implements Task {

    public boolean execute(Context context) throws Exception {
        System.out.println("Step 3");
        return false;
    }
}
