
package com.simple.workflow;



public interface Flow extends Task {


    void addTask(Task task);


    boolean execute(Context context) throws Exception;


}
