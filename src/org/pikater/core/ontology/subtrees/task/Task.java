package org.pikater.core.ontology.subtrees.task;

import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.management.Agent;

import jade.content.Concept;

public class Task implements Concept, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8242598855481511427L;
	private Id id;
	private Id problemId;
	private Evaluation result;
	private Agent agent;
	private Data data;
	
	private String saveMode = null;  // if not null -> save the agent
								//    message (agent is sent in the message with the results)
								//    file (agent is stored in the file by agentManager) --> TODO database
	private String getResults;
	private String guiAgent;
	private boolean saveResults;
	
	private int userID;
	private String start;
	private String finish;

	private String problemName;
	private String note;
	private EvaluationMethod evaluationMethod; // e.g. cross validation

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
		this.data=data;
	}

	public Id getId() {
		return id;
	}
	public void setId(Id id) {
		this.id=id;
	}

	public Id getProblem_id() {
		return problemId;
	}
	public void setProblem_id(Id problem_id) {
		this.problemId=problem_id;
	}
	
	public Evaluation getResult() {
		return result;
	}
	public void setResult(Evaluation result) {
		this.result = result;
	}	

	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getSaveMode() {
		return saveMode;
	}
	public void setSave_mode(String save_mode) {
		this.saveMode = save_mode;
	}

	public String getGetResults() {
		return getResults;
	}
	public void setGetResults(String getResults) {
		this.getResults = getResults;
	}
	
	public String getGuiAgent() {
		return guiAgent;
	}
	public void setGuiAgent(String guiAgent) {
		this.guiAgent = guiAgent;
	}

	public boolean getSaveResults() {
		return saveResults;
	}
	public void setSaveResults(boolean saveResults) {
		this.saveResults = saveResults;
	}

	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}

	public String getFinish() {
		return finish;
	}
	public void setFinish(String finish) {
		this.finish = finish;
	}
	
	public String getProblemName() {
		return problemName;
	}
	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	public EvaluationMethod getEvaluation_method() {
		return evaluationMethod;
	}
	public void setEvaluation_method(EvaluationMethod _evaluation_method) {
		this.evaluationMethod = _evaluation_method;
	}
	
    public Object clone() {
        
        Task task = new Task();
    	task.setId(this.id);
    	task.setProblem_id(this.problemId);
    	task.setResult(this.result);
    	task.setAgent(this.agent);
    	task.setData(this.data);    	
    	task.setSave_mode(this.saveMode);
    	task.setGetResults(this.getResults);
    	task.setGuiAgent(this.guiAgent);
    	task.setSaveResults(this.saveResults);
    	task.setUserID(this.userID);
    	task.setStart(this.start);
    	task.setFinish(this.finish);
    	task.setEvaluation_method(this.evaluationMethod);

        return task;
    }

}
