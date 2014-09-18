package tests.pikater.core.agents.system.planner;

import java.util.ArrayList;
import java.util.List;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import org.pikater.core.AgentNames;
import org.pikater.core.CoreConstants;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.experiment.computing.Agent_WekaJ48;
import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.batchDescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.ExpectedDuration;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.ExpectedDuration.DurationType;
import org.pikater.core.ontology.subtrees.batchDescription.evaluationMethod.CrossValidation;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.data.types.DataTypes;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
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
		
		for (int i = 0; i < 50; i++) { 
	        sendTask();
	        sendTask2();
	        sendTask3();
		}
        
        logInfo("PlannerTester ending");
        doDelete();
	}
	
    protected void sendTask() {
    	
        AID receiver = new AID(AgentNames.PLANNER, false);
    	
		NewOption optionB = new NewOption("B", new IntegerValue(3));
		List<NewOption> options = new ArrayList<NewOption>();
		options.add(optionB);
		
    	Agent agent = new Agent();
    	agent.setName(Agent_WekaRBFNetworkCA.class.getSimpleName());
    	agent.setType(Agent_WekaRBFNetworkCA.class.getName());
        agent.setOptions(options); 
    	//agent.setOptions(options); //S,M
    	
    	Datas datas = new Datas();
    	datas.addData(
    			new Data("weather.arff",
    					"28c7b9febbecff6ce207bcde29fc0eb8",
    					DataTypes.TEST_DATA) );
    	datas.addData(
    			new Data("weather.arff",
    					"28c7b9febbecff6ce207bcde29fc0eb8",
    					DataTypes.TRAIN_DATA) );
    	datas.setMode(CoreConstants.MODE_TRAIN_TEST);
    	datas.setOutput(CoreConstants.OUTPUT_EVALUATION_ONLY);
    	
        NewOption optionF = new NewOption("F", 8);
    	
    	EvaluationMethod method = new EvaluationMethod();
    	method.setAgentType(CrossValidation.class.getName());
    	method.addOption(optionF);

    	ExpectedDuration expectedDuration = new ExpectedDuration();
    	expectedDuration.setDurationType(DurationType.SECONDS.name());
    	
    	Task task = new Task();
    	task.setAgent(agent);
    	task.setDatas(datas);
    	task.setEvaluationMethod(method);
    	task.setExpectedDuration(expectedDuration);
    	
        ExecuteTask executeTask = new ExecuteTask();
        executeTask.setTask(task);
        
        try {
        	
            ACLMessage request = makeActionRequest(receiver, executeTask);
            //send(request);
            logInfo("Sending test request to Planner");
            send(request);
/*            
            ACLMessage reply = FIPAService.doFipaRequestClient(this, request, 100000);
            if (reply == null)
                logError("Reply not received.");
            else
                log("Reply received: "+ACLMessage.getPerformative(reply.getPerformative())+" "+reply.getContent());
*/
        } catch (CodecException | OntologyException e) {
            logException("Ontology/codec error occurred: "+e.getMessage(), e);
        }

    }

    protected void sendTask2() {
    	
        AID receiver = new AID(AgentNames.PLANNER, false);
    	
		NewOption optionC = new NewOption("C", new FloatValue(0.25f));
		List<NewOption> options = new ArrayList<NewOption>();
		options.add(optionC);
		
    	Agent agent = new Agent();
    	agent.setName(Agent_WekaJ48.class.getSimpleName());
    	agent.setType(Agent_WekaJ48.class.getName());
        agent.setOptions(options); 
    	//agent.setOptions(options); //S,M
    	
    	Datas datas = new Datas();
    	datas.addData(
    			new Data("weather.arff",
    					"28c7b9febbecff6ce207bcde29fc0eb8",
    					DataTypes.TEST_DATA) );
    	datas.addData(
    			new Data("weather.arff",
    					"28c7b9febbecff6ce207bcde29fc0eb8",
    					DataTypes.TRAIN_DATA) );
    	datas.setMode(CoreConstants.MODE_TRAIN_TEST);
    	datas.setOutput(CoreConstants.OUTPUT_EVALUATION_ONLY);
    	
        NewOption optionF = new NewOption("F", 8);
        
    	EvaluationMethod method = new EvaluationMethod();
    	method.setAgentType(CrossValidation.class.getName());
    	method.addOption(optionF);
    	
    	ExpectedDuration expectedDuration = new ExpectedDuration();
    	expectedDuration.setDurationType(DurationType.MINUTES.name());
    	
    	Task task = new Task();
    	task.setAgent(agent);
    	task.setDatas(datas);
    	task.setEvaluationMethod(method);
    	task.setExpectedDuration(expectedDuration);
    	
        ExecuteTask executeTask = new ExecuteTask();
        executeTask.setTask(task);
        
        try {
        	
            ACLMessage request = makeActionRequest(receiver, executeTask);
            //send(request);
            logInfo("Sending test request to Planner");
            send(request);
/* 
            ACLMessage reply = FIPAService.doFipaRequestClient(this, request, 100000);
            if (reply == null)
                logError("Reply not received.");
            else
                log("Reply received: "+ACLMessage.getPerformative(reply.getPerformative())+" "+reply.getContent());
*/
        } catch (CodecException | OntologyException e) {
            logException("Ontology/codec error occurred: "+e.getMessage(), e);
        }

    }
    
    protected void sendTask3() {
    	
        AID receiver = new AID(AgentNames.PLANNER, false);
    	
		NewOption optionC = new NewOption("C", new FloatValue(0.25f));
		List<NewOption> options = new ArrayList<NewOption>();
		options.add(optionC);
		
    	Agent agent = new Agent();
    	agent.setName(Agent_WekaJ48.class.getSimpleName());
    	agent.setType(Agent_WekaJ48.class.getName());
        agent.setOptions(options); 
    	//agent.setOptions(options); //S,M
    	
    	Datas datas = new Datas();
    	datas.addData(
    			new Data("weather.arff",
    					"28c7b9febbecff6ce207bcde29fc0eb8",
    					DataTypes.TEST_DATA) );
    	datas.addData(
    			new Data("weather.arff",
    					"28c7b9febbecff6ce207bcde29fc0eb8",
    					DataTypes.TRAIN_DATA) );
    	datas.setMode(CoreConstants.MODE_TRAIN_TEST);
    	datas.setOutput(CoreConstants.OUTPUT_EVALUATION_ONLY);
    	
        NewOption optionF = new NewOption("F", 8);
        
    	EvaluationMethod method = new EvaluationMethod();
    	method.setAgentType(CrossValidation.class.getName());
    	method.addOption(optionF);
    	
    	ExpectedDuration expectedDuration = new ExpectedDuration();
    	expectedDuration.setDurationType(DurationType.SECONDS.name());
    	
    	Task task = new Task();
    	task.setAgent(agent);
    	task.setDatas(datas);
    	task.setEvaluationMethod(method);
    	task.setExpectedDuration(expectedDuration);
    	
        ExecuteTask executeTask = new ExecuteTask();
        executeTask.setTask(task);
        
        try {
        	
            ACLMessage request = makeActionRequest(receiver, executeTask);
            //send(request);
            logInfo("Sending test request to Planner");
            send(request);
/* 
            ACLMessage reply = FIPAService.doFipaRequestClient(this, request, 100000);
            if (reply == null)
                logError("Reply not received.");
            else
                log("Reply received: "+ACLMessage.getPerformative(reply.getPerformative())+" "+reply.getContent());
*/
        } catch (CodecException | OntologyException e) {
            logException("Ontology/codec error occurred: "+e.getMessage(), e);
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
