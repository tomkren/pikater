package org.pikater.core.ontology.subtrees.task;

import java.util.Date;

import org.pikater.core.ontology.subtrees.dataInstance.DataInstances;

import jade.content.Concept;

import java.util.LinkedList;
import java.util.List;

public class Evaluation implements Concept {

	private static final long serialVersionUID = 1319671908304254420L;

	private List<Eval> evaluations;
	private Date start;
	private String status;

	private String objectFilename;
	
	private List<DataInstances> labeledData = new LinkedList<DataInstances>(); // List of DataInstances

    private DataInstances dataTable;

	private byte [] object;  // saved agent

	public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
            this.status = status;
        }

    public DataInstances getDataTable() {
		return dataTable;
	}
	public void setDataTable(DataInstances dataTable) {
		this.dataTable = dataTable;
	}

	public List<DataInstances> getLabeledData() {
		return labeledData;
	}
	public void setLabeledData(List<DataInstances> labeledData) {
		this.labeledData = labeledData;
	}
	
	public String getObjectFilename() {
		return objectFilename;
	}
	public void setObjectFilename(String object_filename) {
		this.objectFilename = object_filename;
	}
	
	public byte [] getObject() {
		return object;
	}
	public void setObject(byte [] object) {
		this.object = object;
	}

	public List<Eval> getEvaluations() {
		return evaluations;
	}
	public void setEvaluations(java.util.List<Eval> evaluations) {
		this.evaluations = evaluations;
	}

	public Eval exportEvalByName(String evalName) {
		
		for (Eval evalI : getEvaluations()) {
			if (evalI.getName().equals(evalName)) {
				return evalI;
			}
		}
		
		return null;
	}
	
    public Date getStart() {
        return start;
    }
    public void setStart(Date start) {
        this.start = start;
    }

}
