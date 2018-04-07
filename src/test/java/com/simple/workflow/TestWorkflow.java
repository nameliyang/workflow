package com.simple.workflow;

import com.simple.workflow.config.ConfigParser;
import com.simple.workflow.impl.*;

public class TestWorkflow {
	
	    private static final String CONFIG_FILE = "/test-flow.xml";

	    private ConfigParser parser;
	    private Workflow workflow;

	    public TestWorkflow() {
	        parser = new ConfigParser();
	    }

	    public Workflow getWorkflow() throws Exception {
	        if (workflow == null) {
	            parser.parse(
	                    this.getClass().getResource(CONFIG_FILE));

	        }
	        workflow = WorkflowFactoryBase.getInstance().getWorkflow();
	        return workflow;
	    }

	    public static void main(String[] args) throws Exception {
	    	TestWorkflow test = new TestWorkflow();
	        Workflow workflow = test.getWorkflow();
	        Task task = workflow.getTask("SampleFlow");
	        MyContext ctx = new MyContext();
	        task.execute(ctx);
	    }

}
