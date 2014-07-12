package tests.pikater.core.agents.system.planner;

import java.util.ArrayList;
import java.util.List;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.experiment.computing.Agent_WekaJ48;
import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.typedValue.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.IntegerValue;
import org.pikater.core.ontology.subtrees.task.EvaluationMethod;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task;

public class Agent_PlannerTester extends PikaterAgent {

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

		registerWithDF("PlannerTester");
		
		// waiting to start ManagerAgent
		doWait(6000);
		
        sendTask();
//        doWait(600);
        sendTask2();
        
        log("PlannerTester ending");
        doDelete();
	}
	
    protected void sendTask() {
    	
        AID receiver = new AID(AgentNames.PLANNER, false);
    	
		NewOption optionB = new NewOption(
				new IntegerValue(3),
				"B" );
		List<NewOption> options = new ArrayList<NewOption>();
		options.add(optionB);
		
    	Agent agent = new Agent();
    	agent.setName(Agent_WekaRBFNetworkCA.class.getSimpleName());
    	agent.setType(Agent_WekaRBFNetworkCA.class.getName());
        agent.setOptions(options); 
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
            //send(request);
            log("Sending test request to Planner");
            send(request);
/*            
            ACLMessage reply = FIPAService.doFipaRequestClient(this, request, 100000);
            if (reply == null)
                logError("Reply not received.");
            else
                log("Reply received: "+ACLMessage.getPerformative(reply.getPerformative())+" "+reply.getContent());
*/
        } catch (CodecException | OntologyException e) {
            logError("Ontology/codec error occurred: "+e.getMessage(), e);
            e.printStackTrace();
        }

    }

    protected void sendTask2() {
    	
        AID receiver = new AID(AgentNames.PLANNER, false);
    	
		NewOption optionC = new NewOption(
				new FloatValue(0.25f),
				"C" );
		List<NewOption> options = new ArrayList<NewOption>();
		options.add(optionC);
		
    	Agent agent = new Agent();
    	agent.setName(Agent_WekaJ48.class.getSimpleName());
    	agent.setType(Agent_WekaJ48.class.getName());
        agent.setOptions(options); 
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
            //send(request);
            log("Sending test request to Planner");
            send(request);
/* 
            ACLMessage reply = FIPAService.doFipaRequestClient(this, request, 100000);
            if (reply == null)
                logError("Reply not received.");
            else
                log("Reply received: "+ACLMessage.getPerformative(reply.getPerformative())+" "+reply.getContent());
*/
        } catch (CodecException | OntologyException e) {
            logError("Ontology/codec error occurred: "+e.getMessage(), e);
            e.printStackTrace();
        }

    }
    
	private ACLMessage makeActionRequest(AID receiver, ExecuteTask executeTask) throws CodecException, OntologyException {
		
    	Ontology ontology = TaskOntology.getInstance();
    	
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(receiver);
        msg.addReplyTo(new AID("PlannerTester", false));
        msg.setLanguage(getCodec().getName());
        msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        msg.setOntology(ontology.getName());
        getContentManager().fillContent(msg, new Action(receiver, executeTask));
        return msg;
	}

}
