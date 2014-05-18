package org.pikater.core.ontology.messages;

import java.util.Date;

import jade.content.Concept;
import jade.util.leap.LinkedList;
import jade.util.leap.List;

public class Evaluation implements Concept {

	private static final long serialVersionUID = 1319671908304254420L;

	private java.util.List<Eval> evaluations;
	private Date start;
	private String status;

	private String object_filename;
	
	private List labeledData = new LinkedList(); // List of DataInstances

    private DataInstances data_table;

	private byte [] object;  // saved agent

    public void setStatus(String status) {
            this.status = status;
        }

    public String getStatus() {
            return status;
        }

    public DataInstances getData_table() {
		return data_table;
	}

	public void setData_table(DataInstances dataTable) {
		data_table = dataTable;
	}

	public List getLabeled_data() {
		return labeledData;
	}

	public void setLabeled_data(List labeledData) {
		this.labeledData = labeledData;
	}
	
	public void setObject_filename(String object_filename) {
		this.object_filename = object_filename;
	}

	public String getObject_filename() {
		return object_filename;
	}
	
	public void setObject(byte [] object) {
		this.object = object;
	}
	public byte [] getObject() {
		return object;
	}

	public java.util.List<Eval> getEvaluations() {
		return evaluations;
	}
	public void setEvaluations(java.util.List<Eval> evaluations) {
		this.evaluations = evaluations;
	}
	
    public void setStart(Date start) {
        this.start = start;
    }

    public Date getStart() {
        return start;
    }

}
