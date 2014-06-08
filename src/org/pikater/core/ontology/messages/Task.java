package org.pikater.core.ontology.messages;

import java.util.ArrayList;

import jade.content.Concept;

public class Task implements Concept {

	private static final long serialVersionUID = -8242598855481511427L;

	public enum InOutType {TRAIN, TEST, ERRORS, VALIDATION, AGENT};
	
	// administrative:
	private int graphId;
	private int nodeId;
	
	private ArrayList<TaskOutput> output; // list of outputs

	// setting the task:
	private Agent agent;
	private Data data;	
	private EvaluationMethod evaluation_method; // e.g. cross validation

	// further settings
	private String save_mode = null;  // if not null -> save the agent
								//    message (agent is sent in the message with the results)
								//    file (agent is stored in the file by agentManager)
	private boolean save_results;
	
	
	// results are filled in after they are computed
	private Evaluation result;	 

	private String note;

	
	public int getGraphId() {
		return graphId;
	}
	public void setGraphId(int graphId) {
		this.graphId = graphId;
	}
	public Agent getAgent() {
		return agent;
	}
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	public EvaluationMethod getEvaluation_method() {
		return evaluation_method;
	}
	public void setEvaluation_method(EvaluationMethod evaluation_method) {
		this.evaluation_method = evaluation_method;
	}
	public String getSave_mode() {
		return save_mode;
	}
	public void setSave_mode(String save_mode) {
		this.save_mode = save_mode;
	}
	public boolean isSave_results() {
		return save_results;
	}
	public void setSave_results(boolean save_results) {
		this.save_results = save_results;
	}
	public Evaluation getResult() {
		return result;
	}
	public void setResult(Evaluation result) {
		this.result = result;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getNodeId() {
		return nodeId;
	}
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
	public ArrayList<TaskOutput> getOutput() {
		return output;
	}
	public void setOutput(ArrayList<TaskOutput> output) {
		this.output = output;
	}
	
	
	public Object getOutputByName(InOutType name) {
		for (TaskOutput to: this.output){
			if (to.getName().equals(name)){
				return to.getValue();
			}
		}
		return null;
	}
		
}
