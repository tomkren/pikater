package org.pikater.core.ontology.subtrees.task;

import jade.content.Concept;

import org.pikater.core.ontology.subtrees.task.TaskOutput;
import org.pikater.core.ontology.subtrees.batchDescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.IExpectedDuration;
import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.management.Agent;

import java.util.ArrayList;

public class Task implements Concept {

    private static final long serialVersionUID = -8242598855481511427L;
    private int experimentID;
    
    private String start;
    private String finish;
    private String saveMode;
    private String getResults;
    private String guiAgent;
    private boolean saveResults;
    private int priority;
    
    private int cpuCoreID;
    private IExpectedDuration expectedDuration;

    public int getExperimentID() {
		return experimentID;
	}
	public void setExperimentID(int experimentID) {
		this.experimentID = experimentID;
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

    public void setSaveResults(boolean saveResults) {
        this.saveResults = saveResults;
    }
    public boolean isSaveResults() {
        return saveResults;
    }

    public int getCpuCoreID() {
		return cpuCoreID;
	}
	public void setCpuCoreID(int cpuCoreID) {
		this.cpuCoreID = cpuCoreID;
	}
	
    public IExpectedDuration getExpectedDuration() {
		return expectedDuration;
	}
	public void setExpectedDuration(IExpectedDuration expectedDuration) {
		this.expectedDuration = expectedDuration;
	}

	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}


	public enum InOutType {TRAIN, TEST, ERRORS, VALIDATION, AGENT, DATA};

    // administrative:
    private int graphId;
    private int nodeId;
    private int computationId;

    private ArrayList<TaskOutput> output; // list of outputs

    // setting the task:
    private Agent agent;
    private Datas datas;
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
    public Datas getDatas() {
        return datas;
    }
    public void setDatas(Datas datas) {
        this.datas = datas;
    }
    public EvaluationMethod getEvaluationMethod() {
        return evaluation_method;
    }
    public void setEvaluationMethod(EvaluationMethod evaluation_method) {
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

	public int getComputationId() {
		return computationId;
	}

	public void setComputationId(int computationId) {
		this.computationId = computationId;
	}

}
