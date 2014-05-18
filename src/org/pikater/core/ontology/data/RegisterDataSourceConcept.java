package org.pikater.core.ontology.data;

import jade.content.Concept;

/**
 * User: Kuba
 * Date: 2.5.2014
 * Time: 17:24
 */
public class RegisterDataSourceConcept implements Concept {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4747799712901200743L;

	private String taskId;
    private String[] dataTypes;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String[] getDataTypes() {
        return dataTypes;
    }

    public void setDataTypes(String[] dataTypes) {
        this.dataTypes = dataTypes;
    }
}
