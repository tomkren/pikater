package org.pikater.core.agents.system;

import java.util.ArrayList;
import java.util.List;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.Ontology;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.proto.ContractNetInitiator;

import java.util.Date;

import org.pikater.core.CoreAgents;
import org.pikater.core.CoreConstant;
import org.pikater.core.agents.experiment.computing.Agent_WekaLinearRegression;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.manager.ManagerAgentService;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.DurationOntology;
import org.pikater.core.ontology.FilenameTranslationOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.batchdescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.batchdescription.evaluationmethod.Standard;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.data.types.DataTypes;
import org.pikater.core.ontology.subtrees.duration.Duration;
import org.pikater.core.ontology.subtrees.duration.GetDuration;
import org.pikater.core.ontology.subtrees.duration.SetDuration;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.task.Eval;
import org.pikater.core.ontology.subtrees.task.Evaluation;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task;

/**
 * 
 * Agent used for performance measuring
 *
 */
public class Agent_Duration extends PikaterAgent {

	private static final long serialVersionUID = -5555820420884978956L;
    
    private static final String LOG_LR_DURATIONS_NAME = "log_LR_durations";
	
    List<Duration> durations = new ArrayList<Duration>();
    
    int timeMs = 10000;
    AID aid = null;
    
    Duration lastDuration;
    
    boolean logDurations = false;
    
	private String durationDatasetName;
	private String durationDatasetHash;
    
    @Override
    protected String getAgentType(){
    	return CoreAgents.DURATION.getName();
    }
    
	/**
	 * Get ontologies which is using this agent
	 */
    @Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(AgentManagementOntology.getInstance());
		ontologies.add(TaskOntology.getInstance());
		ontologies.add(DurationOntology.getInstance());
		ontologies.add(FilenameTranslationOntology.getInstance());
		
		return ontologies;
	}
	
	/**
	 * Agent setup
	 */
    @Override
    protected void setup() {

		initDefault();
		
		registerWithDF(CoreAgents.DURATION.getName());
    	
		if (containsArgument(LOG_LR_DURATIONS_NAME) &&
				isArgumentValueTrue(LOG_LR_DURATIONS_NAME)) {
			logDurations = true;
		}
		
        // create linear regression agent
        // send message to AgentManager to create an agent
        aid = ManagerAgentService.createAgent(
        		this,
        		Agent_WekaDurationLinearRegression.class.getName(),
        		CoreAgents.DURATION_SERVICE.getName(),
        		null);
        
		this.durationDatasetName = CoreConstant.DURATION_DATASET_NAME;
		this.durationDatasetHash = DataManagerService.
				translateExternalFilename(this, -1, durationDatasetName);

		// compute one LR (as the first one is usually longer)
        ACLMessage durationMsg = createCFPmessage(
        		aid, this, durationDatasetName, durationDatasetHash);
        
		addBehaviour(new ExecuteTaskInitiator(this, durationMsg));
		
		doWait(2000);
		
        addBehaviour(new TestBehaviour(this, timeMs));			  
        
        Ontology ontology = DurationOntology.getInstance();
        MessageTemplate memplate = MessageTemplate.and(
        		MessageTemplate.MatchOntology(
        				ontology.getName()),
        				MessageTemplate.MatchPerformative(
        						ACLMessage.REQUEST));

        addBehaviour(new AchieveREResponder(this, memplate) {

            private static final long serialVersionUID = 1L;

            @Override
            protected ACLMessage handleRequest(ACLMessage request)
                    throws NotUnderstoodException, RefuseException {

                try {
                    Action action = (Action)
                    		getContentManager().extractContent(request);

                    if (action.getAction() instanceof SetDuration) {
                        SetDuration sd = (SetDuration) action.getAction();
                        
                        ACLMessage reply = request.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        
                        Duration duration = sd.getDuration();
                        durations.add(duration);
 
                        duration.setdurationLR(
                        		countDuration(
                        				duration.getStart(),
                        				duration.getDurationMiliseconds()));
                        
                        lastDuration = duration;
                        
                        Result result = new Result(sd, duration);                        
						getContentManager().fillContent(reply, result);												

                        return reply;
                    }
                    
                    if (action.getAction() instanceof GetDuration) {
                    	GetDuration gd = (GetDuration) action.getAction();
                        
                    	gd.setDuration(lastDuration);
                    	
                        ACLMessage reply = request.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        
                        Result r = new Result(gd,lastDuration);                        
						getContentManager().fillContent(reply, r);												

                        return reply;
                    }
                } catch (OntologyException e) {
                	Agent_Duration.this.logException(e.getMessage(), e);
                } catch (CodecException e) {
                	Agent_Duration.this.logException(e.getMessage(), e);
                }

                ACLMessage failure = request.createReply();
                failure.setPerformative(ACLMessage.FAILURE);
                return failure;
            }
        });
    }
    
    /**
     * Counts a duration
     * 
     */
    private float countDuration(Date startDate, long duration) {
    	
    	if (duration >= Integer.MAX_VALUE) {
    		return Integer.MAX_VALUE;
    	}    	
    	
    	float numberOfLRs = 0; 
    	long start = startDate.getTime();
    	
    	// find the duration right before the start
    	int id = durations.size() -1;
    	
    	while (start < (durations.get(id)).getStart().getTime()) {    		
    		id--;
    	}
    	
    	int i = 0;
		
    	long t1 = -1;
		long t2 = -1;
		long durationMs = duration;
		
    	while (durationMs > 0) {   		    	
    		try {
    			
    			Duration durationI = durations.get(id + i);
	    		t1 = durationI.getStart().getTime();
	    		
	    		if (id + i + 1 > durations.size()-1){ 
	    			// after last LR, expected time
	        		t2 = t1 + timeMs;
	    		
	    		} else {
	    			Duration durationIplus = durations.get(id + i + 1);
	    			t2 = durationIplus.getStart().getTime();
	    		}
	    		long timeBetweenLRs = t2 - t1;
	    		
	    		// handles the first part
	    		long duration1;
	    		if (duration < timeBetweenLRs) {
	    			duration1 = duration;
	    		} else {
	    			duration1 = Math.min(t2 - start, timeBetweenLRs);
	    		}
	    		
	    		long durationIMs = durationI.getDurationMiliseconds();
	    		numberOfLRs += (float)durationMs / (float)durationIMs;
	    		durationMs -= duration1;
	    		
	    		i++;
    		
	    	}
	    	catch (Exception e){
	    		logException("Unexpected error occured:", e);
	    	}

    	}
    	    	
    	return numberOfLRs;
    }
  
    /**
     * 
     * Behavior for the one test
     *
     */
    protected class TestBehaviour extends TickerBehaviour {

		private static final long serialVersionUID = -2200601967185243650L;
		private PikaterAgent agent;


		/**
		 * Constructor
		 * 
		 */
		public TestBehaviour(PikaterAgent agent, long period) {
			super(agent, period);
			this.agent = agent;
		}

		/**
		 * Computes linear regression on random (but the same) Data-Set
		 */
		protected void onTick() {
					
	        ACLMessage durationMsg = createCFPmessage(aid, agent,
	        		durationDatasetName, durationDatasetHash);
			addBehaviour(new ExecuteTaskInitiator(agent, durationMsg));			  
		} 
    }
    
    /**
     * 
     * Behavior which executes duration Task
     *
     */
	protected class ExecuteTaskInitiator extends ContractNetInitiator{

		private static final long serialVersionUID = -4895199062239049907L;
				
		
		private PikaterAgent agent;
		
		public ExecuteTaskInitiator(PikaterAgent agent, ACLMessage cfp) {
			super(agent, cfp);
			this.agent = agent;
		}
		
		protected void handleRefuse(ACLMessage refuse) {
			logSevere("Agent "+refuse.getSender().getName() + " refused.");
		}
		
		protected void handleFailure(ACLMessage failure) {
			if (failure.getSender().equals(myAgent.getAMS())) {
				// FAILURE notification from the JADE runtime: the receiver
				// does not exist
                logSevere("Responder " +
                		failure.getSender().getName() +
                		" does not exist");
			}
			else {
                logSevere("Agent " +
                		failure.getSender().getName() +
                		" failed");
			}
		}
				
		protected void handleInform(ACLMessage inform) {
            logSevere("Agent " +
            		inform.getSender().getName() +
            		" successfully performed the requested action");
																			
			ContentElement content;
			try {
				content = getContentManager().extractContent(inform);
				if (content instanceof Result) {
					Result result = (Result) content;					
					jade.util.leap.List tasks =
							(jade.util.leap.List)result.getValue();
					Task task = (Task) tasks.get(0);
					
					// over 270 hours
					if (durations.size() > 1000000) {
						durations.remove(0);
					}
					
					// save the duration of the computation to the list
					Evaluation evaluation = (Evaluation)task.getResult();
					List<Eval> evaluations = evaluation.getEvaluations();
					
					Duration duration = new Duration();
					for (Eval evalI : evaluations) {
						if(evalI.getName().equals(CoreConstant.DURATION)) {
							int evalMs = (int)evalI.getValue();
							duration.setDurationMiliseconds(evalMs);
						}
					}
					duration.setStart(evaluation.getStart());
					durations.add(duration);
					
					logSevere(duration.getStart() +
							" - " + duration.getDurationMiliseconds());
					if (logDurations){
						// write duration into a file:
						logInfo(duration.getStart() +
								" - " + duration.getDurationMiliseconds());
					}											
					
				}				
			} catch (UngroundedException e) {
				agent.logException(e.getMessage(), e);
			} catch (CodecException e) {
				agent.logException(e.getMessage(), e);
			} catch (OntologyException e) {
				agent.logException(e.getMessage(), e);
			}			
		}			
	}
	

    /**
     * Prepares a message for the duration service
     * 
     * @param aid - receiver identification
     * @param agent - agent for login exceptions
     */
    protected ACLMessage createCFPmessage(AID aid, PikaterAgent agent,
    		String durationDatasetName, String durationDatasetHash) {

        Ontology ontology = TaskOntology.getInstance();
    	
		// create CFP message for Linear Regression Computing Agent							  		
		ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
		cfp.setLanguage(codec.getName());
		cfp.setOntology(ontology.getName());
		cfp.addReceiver(aid);
		cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

		// We want to receive a reply in 10 secs
		org.pikater.core.ontology.subtrees.management.Agent ag =
				new org.pikater.core.ontology.subtrees.management.Agent();
		ag.setType(Agent_WekaLinearRegression.class.getName());
		ag.setOptions(new ArrayList<NewOption>());
		
		Datas datas = new Datas();
		datas.addData(
				new Data(durationDatasetName,
						durationDatasetHash,
						DataTypes.TRAIN_DATA));
		datas.addData(new Data("xxx", "xxx", DataTypes.TEST_DATA));
		datas.setMode(CoreConstant.Mode.TRAIN_ONLY.name());
		
		Task task = new Task();		
		task.setAgent(ag);
		task.setDatas(datas);
		
		EvaluationMethod evaluationMethod = new EvaluationMethod();
		evaluationMethod.setAgentType(Standard.class.getName());
		
		task.setEvaluationMethod(evaluationMethod);
		
		task.setSaveResults(false);

		ExecuteTask executeTask = new ExecuteTask();
		executeTask.setTask(task);
		
		try {
			Action action = new Action();
			action.setAction(executeTask);
			action.setActor(this.getAID());
										
			getContentManager().fillContent(cfp, action);

		} catch (CodecException e) {
			agent.logException(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logException(e.getMessage(), e);
		}
				
		return cfp;

	}
}
