package org.pikater.core.ontology.subtrees.datasource;

import jade.content.AgentAction;

/**
 * User: Kuba
 * Date: 2.5.2014
 * Time: 13:21
 */
public class GetDataSourcePath implements AgentAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2808234460031853327L;

	private String taskId;
    private String type;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
