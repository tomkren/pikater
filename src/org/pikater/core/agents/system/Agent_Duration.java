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
import org.pikater.core.ontology.subtrees.batchDescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.batchDescription.evaluationMethod.Standard;
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
import org.pikater.core.ontology.subtrees.task.Id;
import org.pikater.core.ontology.subtrees.task.Task;

public class Agent_Duration extends PikaterAgent {

	private static final long serialVersionUID = -5555820420884978956L;
    
    private static final String LOG_LR_DURATIONS_NAME="log_LR_durations";
	
    List<Duration> durations = new ArrayList<Duration>();
    
    int t = 10000; //ms
    AID aid = null;
    int id = 0;
    
    Duration lastDuration;
    
    boolean log_LR_durations = false;
    
	private String durationDatasetName;
	private String durationDatasetHash;
    
    @Override
    protected String getAgentType(){
    	return CoreAgents.DURATION.getName();
    }
    
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(AgentManagementOntology.getInstance());
		ontologies.add(TaskOntology.getInstance());
		ontologies.add(DurationOntology.getInstance());
		ontologies.add(FilenameTranslationOntology.getInstance());
		
		return ontologies;
	}
	
    @Override
    protected void setup() {

		initDefault();
		
		registerWithDF(CoreAgents.DURATION.getName());
    	
		if (containsArgument(LOG_LR_DURATIONS_NAME) && isArgumentValueTrue(LOG_LR_DURATIONS_NAME))
		{
			log_LR_durations = true;
		}
		
        // create linear regression agent
        // send message to AgentManager to create an agent
        aid = ManagerAgentService.createAgent(
        		this,
        		Agent_WekaDurationLinearRegression.class.getName(),
        		CoreAgents.DURATION_SERVICE.getName(),
        		null);
        
		this.durationDatasetName = CoreConstant.Misc.DURATION_DATASET_NAME.get();
		this.durationDatasetHash = DataManagerService.
				translateExternalFilename(this, -1, durationDatasetName);

		// compute one LR (as the first one is usually longer)
        ACLMessage durationMsg = createCFPmessage(
        		aid, this, durationDatasetName, durationDatasetHash);
        
		addBehaviour(new ExecuteTaskInitiator(this, durationMsg));
		
		doWait(2000);
		
        addBehaviour(new TestBehaviour(this, t));			  
        
        Ontology ontology = DurationOntology.getInstance();
        MessageTemplate memplate = MessageTemplate.and(
        		MessageTemplate.MatchOntology(
        				ontology.getName()),
        				MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

        addBehaviour(new AchieveREResponder(this, memplate) {

            private static final long serialVersionUID = 1L;

            @Override
            protected ACLMessage handleRequest(ACLMessage request)
                    throws NotUnderstoodException, RefuseException {

                try {
                    Action a = (Action) getContentManager().extractContent(request);

                    if (a.getAction() instanceof SetDuration) {
                        SetDuration sd = (SetDuration) a.getAction();
                        
                        ACLMessage reply = request.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        
                        Duration duration = sd.getDuration();
                        durations.add(duration);
 
                        duration.setLR_duration(
                        		countDuration(duration.getStart(), duration.getDurationMiliseconds()));
                        
                        lastDuration=duration;
                        
                        Result r = new Result(sd, duration);                        
						getContentManager().fillContent(reply, r);												

                        return reply;
                    }
                    
                    if (a.getAction() instanceof GetDuration) {
                    	GetDuration gd = (GetDuration) a.getAction();
                        
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
    
    private float countDuration(Date _start, long duration){    	
    	if (duration >= Integer.MAX_VALUE){
    		return Integer.MAX_VALUE;
    	}    	
    	
    	float number_of_LRs = 0; 
    	long start = _start.getTime();
    	
    	// find the duration right before the start
    	int i_d = durations.size()-1;
    	while (start < (durations.get(i_d)).getStart().getTime()){    		
    		i_d--;
    	}
    	
    	int i = 0;
		
    	long t1 = -1;
		long t2 = -1;
		long d = duration;
		
    	while (d > 0){   		    	
    		try {
	    		t1 = ((Duration)durations.get(i_d + i)).getStart().getTime();	    		
	    		if (i_d + i + 1 > durations.size()-1){ 
	    			// after last LR
	        		t2 = t1 + t; // expected time    		
	    		}
	    		else {
	    			t2 = ((Duration)durations.get(i_d + i + 1)).getStart().getTime();
	    		}
	    		long time_between_LRs = t2 - t1;
	    		
	    		final long duration1 = duration < time_between_LRs ? duration : Math.min(t2 - start, time_between_LRs); // osetreni prvniho useku
	    		
	    		// System.out.println("d: " + d + " LR dur: " + ((Duration)durations.get(i_d + i)).getDuration());
	    		number_of_LRs += (float)d / (float)(durations.get(i_d + i)).getDurationMiliseconds();
	    		d -= duration1;
	    		
	    		i++;
    		
	    	}
	    	catch (Exception e){
	    		logException("Unexpected error occured:", e);
	    	}

    	}
    	    	
    	return number_of_LRs;
    }
  
    protected class TestBehaviour extends TickerBehaviour {

		private static final long serialVersionUID = -2200601967185243650L;
		private PikaterAgent agent;


		public TestBehaviour(PikaterAgent agent, long period) {
			super(agent, period);
			this.agent = agent;
		}

		protected void onTick() {
			// compute linear regression on random (but the same) dataset
					
	        ACLMessage durationMsg = createCFPmessage(aid, agent,
	        		durationDatasetName, durationDatasetHash);
			addBehaviour(new ExecuteTaskInitiator(agent, durationMsg));			  
		} 
    }
    
	protected class ExecuteTaskInitiator extends ContractNetInitiator{

		private static final long serialVersionUID = -4895199062239049907L;
				
		
		private PikaterAgent agent;
		
		public ExecuteTaskInitiator(PikaterAgent agent, ACLMessage cfp) {
			super(agent, cfp);
			this.agent = agent;
		}
		
		protected void handleRefuse(ACLMessage refuse) {
			logSevere("Agent "+refuse.getSender().getName()+" refused.");
		}
		
		protected void handleFailure(ACLMessage failure) {
			if (failure.getSender().equals(myAgent.getAMS())) {
				// FAILURE notification from the JADE runtime: the receiver
				// does not exist
                logSevere("Responder " + failure.getSender().getName() + " does not exist");
			}
			else {
                logSevere("Agent "+failure.getSender().getName()+" failed");
			}
		}
				
		protected void handleInform(ACLMessage inform) {
            logSevere("Agent "+inform.getSender().getName() + " successfully performed the requested action");
																			
			ContentElement content;
			try {
				content = getContentManager().extractContent(inform);
				if (content instanceof Result) {
					Result result = (Result) content;					
					jade.util.leap.List tasks = (jade.util.leap.List)result.getValue();
					Task task = (Task) tasks.get(0);
					
					if (durations.size() > 1000000) { // over 270 hours
						durations.remove(0);
					}
					
					// save the duration of the computation to the list
					Evaluation evaluation = (Evaluation)task.getResult();
					List<Eval> ev = evaluation.getEvaluations();
					
					Duration d = new Duration();
					for (Eval eval : ev) {
						if(eval.getName().equals(CoreConstant.Misc.DURATION.get())){
							d.setDurationMiliseconds((int)eval.getValue());
						}
					}
					d.setStart(evaluation.getStart());
					durations.add(d);
					logSevere(d.getStart() + " - " + d.getDurationMiliseconds());
					if (log_LR_durations){
						// write duration into a file:
						logInfo(d.getStart() + " - " + d.getDurationMiliseconds());
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
	} // end of call for proposal bahavior

        
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
		//cfp.setReplyByDate(new java.util.Date(System.currentTimeMillis() + 10000));

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
		datas.setMode(CoreConstant.Mode.TRAIN_ONLY.get());
		
		Task task = new Task();
		Id _id = new Id();
		_id.setIdentificator(Integer.toString(id));
		id++;
		
		task.setAgent(ag);
		task.setDatas(datas);
		
		EvaluationMethod evaluationMethod = new EvaluationMethod();
		evaluationMethod.setAgentType(Standard.class.getName());
		
		task.setEvaluationMethod(evaluationMethod);
		
		task.setSaveResults(false);

		ExecuteTask executeTask = new ExecuteTask();
		executeTask.setTask(task);
		
		try {
			Action a = new Action();
			a.setAction(executeTask);
			a.setActor(this.getAID());
										
			getContentManager().fillContent(cfp, a);

		} catch (CodecException e) {
			agent.logException(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logException(e.getMessage(), e);
		}
				
		return cfp;

	} // end createCFPmessage()
}
