package tests.pikater.core.agents.system;

import java.util.ArrayList;
import java.util.List;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.task.EvaluationMethod;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task;
import org.pikater.core.options.RBFNetwork_CABox;

public class PlannerTester extends PikaterAgent {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6420194571183822412L;

	@Override
	public List<Ontology> getOntologies() {
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(TaskOntology.getInstance());
		ontologies.add(AgentManagementOntology.getInstance());
		return ontologies;
	}

	@Override
    protected void setup() {
        initDefault();

        sendTask();
        
        log("PlannerTester ending");
        doDelete();
	}
	
    protected void sendTask() {
    	
        AID receiver = new AID(AgentNames.PLANNER, false);
    	
    	Agent agent = new Agent();
    	agent.setName(RBFNetwork_CABox.class.getSimpleName());
    	agent.setType(RBFNetwork_CABox.class.getName());
    	//agent.setOptions(options); //S,M
    	
    	Data data = new Data();
    	data.setExternal_test_file_name("weather.arff");
    	data.setTestFileName("28c7b9febbecff6ce207bcde29fc0eb8");
    	data.setExternal_train_file_name("weather.arff");
    	data.setTrainFileName("28c7b9febbecff6ce207bcde29fc0eb8");
    	data.setMode("train_test");
    	data.setOutput("evaluation_only");
    	
    	EvaluationMethod method = new EvaluationMethod();
    	method.setType("CrossValidation");

    	Task task = new Task();
    	task.setAgent(agent);
    	task.setData(data);
    	task.setEvaluationMethod(method);
    	
        ExecuteTask executeTask = new ExecuteTask();
        executeTask.setTask(task);
        
        try {
        	
            ACLMessage request = makeActionRequest(receiver, executeTask);
            log("Sending test request to Planner");
            ACLMessage reply = FIPAService.doFipaRequestClient(this, request, 10000);
            if (reply == null)
                logError("Reply not received.");
            else
                log("Reply received: "+ACLMessage.getPerformative(reply.getPerformative())+" "+reply.getContent());
        } catch (CodecException | OntologyException e) {
            logError("Ontology/codec error occurred: "+e.getMessage(), e);
            e.printStackTrace();
        } catch (FIPAException e) {
            logError("FIPA error occurred: "+e.getMessage(), e);
            e.printStackTrace();
        }

    }

	private ACLMessage makeActionRequest(AID receiver, ExecuteTask executeTask) throws CodecException, OntologyException {
		
    	Ontology ontology = TaskOntology.getInstance();
    	
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(receiver);
        msg.setLanguage(getCodec().getName());
        msg.setOntology(ontology.getName());
        getContentManager().fillContent(msg, new Action(receiver, executeTask));
        return msg;
	}

}
