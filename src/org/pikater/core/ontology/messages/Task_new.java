package org.pikater.core.ontology.messages;

import java.util.List;

import jade.content.Concept;

public class Task_new implements Concept, Cloneable {

	private static final long serialVersionUID = -8242598855481511427L;

	// administrative:
	private Id _id;
	private List<String> output; // list of queues where to put output

	// setting the task:
	private Agent _agent;
	private Data _data;	
	private EvaluationMethod _evaluation_method; // e.g. cross validation

	// further settings
	private String _save_mode = null;  // if not null -> save the agent
								//    message (agent is sent in the message with the results)
								//    file (agent is stored in the file by agentManager)
	private boolean _save_results;
	
	
	// results are filled in after they are computed
	private Evaluation _result;	 

	private String _note;

	
	public void setAgent(Agent agent) {
		_agent=agent;
	}	
	public Agent getAgent() {
		return _agent;
	}	
	public void setData(Data data) {
		_data=data;
	}
	public Data getData() {
		return _data;
	}
	public void setId(Id id) {
		_id=id;
	}
	public Id getId() {
		return _id;
	}
	public void setResult(Evaluation result) {
		_result = result;
	}
	public Evaluation getResult() {
		return _result;
	}	
	public void setSave_mode(String _save_mode) {
		this._save_mode = _save_mode;
	}
	public String getSave_mode() {
		return _save_mode;
	}
	public void setSave_results(boolean _save_results) {
		this._save_results = _save_results;
	}	
	public boolean getSave_results() {
		return _save_results;
	}	
	public void setNote(String _note) {
		this._note = _note;
	}
	public String getNote() {
		return _note;
	}	
	public void setEvaluation_method(EvaluationMethod _evaluation_method) {
		this._evaluation_method = _evaluation_method;
	}	
	public EvaluationMethod getEvaluation_method() {
		return _evaluation_method;
	}
	public List<String> getOutput() {
		return output;
	}
	public void setOutput(List<String> output) {
		this.output = output;
	}

    public Object clone() {
        
        Task_new task = new Task_new();
    	task.setId(this._id);
    	task.setOutput(this.output);
    	task.setResult(this._result);
    	task.setAgent(this._agent);
    	task.setData(this._data);    	
    	task.setSave_mode(this._save_mode);
    	task.setSave_results(this._save_results);
    	task.setEvaluation_method(this._evaluation_method);

        return task;
    }
}
