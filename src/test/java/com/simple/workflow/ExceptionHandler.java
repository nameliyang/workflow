package com.simple.workflow;

public class ExceptionHandler implements Handler {

    public boolean postprocess(Context context, Exception exception) {
        MyContext myContext = (MyContext) context;

        if (exception != null) {
            System.out.println("task name: " + myContext.getTaskName() + " has error");
            System.out.println("exception message: " + exception.getMessage());
        }

        // Clean up

        // Put the elapsed time here

        return true;
    }

    public boolean execute(Context context) throws Exception {
        System.out.println("Exception handler execute");
        return false;
    }
}
