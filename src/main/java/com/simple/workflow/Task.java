package com.simple.workflow;


public interface Task {

    
    public static final boolean CONTINUE_PROCESSING = false;

    public static final boolean PROCESSING_COMPLETE = true;

    boolean execute(Context context) throws Exception;


}
