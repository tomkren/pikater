package org.pikater.core.ontology.subtrees.task;

import jade.content.Concept;

import org.pikater.core.ontology.subtrees.task.TaskOutput;
import org.pikater.core.ontology.subtrees.batchDescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.ExpectedDuration;
import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.management.Agent;

import java.util.ArrayList;

public class Task implements Concept {

	private static final long serialVersionUID = -8242598855481511427L;

	public enum InOutType {
		TRAIN, TEST, ERRORS, VALIDATION, AGENT, DATA
	}

	// administrative:
	private int nodeID;
	private int computationID;

	private int userID;
	private int batchID;
	private int experimentID;

	// setting the task:
	private Agent agent;
	private Datas datas;
	private EvaluationMethod evaluationMethod; // e.g. cross validation

	// duration
	private int priority;
	private ExpectedDuration expectedDuration;
	private String start;
	private String finish;

	private String saveMode = null;
	private boolean saveResults;
	private String guiAgent;

	// results are filled in after they are computed
	private Evaluation result;
	private ArrayList<TaskOutput> output; // list of outputs

	private String note;

	public int getNodeID() {
		return nodeID;
	}
	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}

	public int getComputationID() {
		return computationID;
	}
	public void setComputationID(int computationID) {
		this.computationID = computationID;
	}

	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public int getBatchID() {
		return batchID;
	}
	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}
	
	public int getExperimentID() {
		return experimentID;
	}
	public void setExperimentID(int experimentID) {
		this.experimentID = experimentID;
	}

	public Agent getAgent() {
		return agent;
	}
	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public Datas getDatas() {
		return datas;
	}
	public void setDatas(Datas datas) {
		this.datas = datas;
	}

	public EvaluationMethod getEvaluationMethod() {
		return evaluationMethod;
	}
	public void setEvaluationMethod(EvaluationMethod evaluationMethod) {
		this.evaluationMethod = evaluationMethod;
	}

	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}

	public ExpectedDuration getExpectedDuration() {
		return expectedDuration;
	}
	public void setExpectedDuration(ExpectedDuration expectedDuration) {
		this.expectedDuration = expectedDuration;
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

	public String getSaveMode() {
		return saveMode;
	}
	public void setSaveMode(String saveMode) {
		this.saveMode = saveMode;
	}

	public boolean getSaveResults() {
		return saveResults;
	}
	public void setSaveResults(boolean saveResults) {
		this.saveResults = saveResults;
	}

	public String getGuiAgent() {
		return guiAgent;
	}
	public void setGuiAgent(String guiAgent) {
		this.guiAgent = guiAgent;
	}

	public Evaluation getResult() {
		return result;
	}
	public void setResult(Evaluation result) {
		this.result = result;
	}

	public ArrayList<TaskOutput> getOutput() {
		return output;
	}
	public void setOutput(ArrayList<TaskOutput> output) {
		this.output = output;
	}

	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

    public TaskOutput getOutputByType(InOutType name) {
        for (TaskOutput to : this.output) {
            if (to.getType().name().equals(name.toString())) {
                return to;
            }
        }
        return null;
    }

	public boolean equalsTask(Task task) {
		boolean equals =
				getBatchID() == task.getBatchID() &&
				getNodeID() == task.getNodeID() &&
				getComputationID() == task.getComputationID();
		return equals;
	}
	
}
