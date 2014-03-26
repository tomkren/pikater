package pikater;


import java.io.IOException;
import java.io.Serializable;

import pikater.agents.PikaterAgent;
import pikater.agents.management.ManagerAgentCommunicator;
import pikater.ontology.description.ComputationDescription;
import pikater.ontology.description.ComputingAgent;
import pikater.ontology.description.DataSourceDescription;
import pikater.ontology.description.FileDataProvider;
import pikater.ontology.description.FileVisualizer;
import pikater.ontology.description.IComputationElement;
import pikater.ontology.description.IDataProvider;
import pikater.ontology.messages.Data;
import pikater.ontology.messages.EvaluationMethod;
import pikater.ontology.messages.Execute;
import pikater.ontology.messages.ExecuteExperiment;
import pikater.ontology.messages.Id;
import pikater.ontology.messages.Interval;
import pikater.ontology.messages.MessagesOntology;
import pikater.ontology.messages.Method;
import pikater.ontology.messages.Option;
import pikater.ontology.messages.Problem;
import pikater.ontology.messages.SendEmail;
import pikater.ontology.messages.Solve;
import pikater.ontology.messages.Task;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import jade.util.leap.List;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;


public class Agent_ComputingManager extends PikaterAgent {
	
	private static final long serialVersionUID = 7116837600070411675L;
	
	@Override
	protected void setup() {
		
		System.out.println("Agent: " +getLocalName() + " starts.");
	  	
		initDefault();
		registerWithDF("ComputingManager");

        addBehaviour(new ComputingManagerBehaviour(this));
        
	  	// Make this agent terminate
	  	//doDelete();
	}
		
	@Override
	protected String getAgentType(){
		return "ComputingManager";
	}
	
}


class ComputingManagerBehaviour extends AchieveREResponder {

    private Agent agent;

    public ComputingManagerBehaviour(Agent agent) {
    	super(agent, MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
    	
    	this.agent = agent;
    }
    
    @Override
    protected ACLMessage handleRequest(final ACLMessage request) throws NotUnderstoodException, RefuseException {
        try {
        	//Serializable object = request.getContentObject();
            Concept object = ((Action)(agent.getContentManager().extractContent(request))).getAction();

            ACLMessage reply = request.createReply();
            
        	if (object instanceof ExecuteExperiment) {
        		
        		ExecuteExperiment executeExperiment =
        				(ExecuteExperiment) object;
        		process(executeExperiment);
        		
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent("OK");

        	}
   
            
            return reply;

        } catch (UngroundedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return null;
    }


    public void process (ExecuteExperiment executeExperiment) {
    	
		ComputationDescription comDescription =
				executeExperiment.getDescription();
		
		IComputationElement element =
				comDescription.getRootElement();

		if (element instanceof FileVisualizer) {
			
			FileVisualizer fileVisualizer = (FileVisualizer) element;
			DataSourceDescription dataSource = fileVisualizer.getDataSource();
			
			this.process(dataSource);
		}

    }
    
    public void process (DataSourceDescription dataSource) {
    	
		IDataProvider dataProvider = dataSource.getDataProvider();

		if (dataProvider instanceof ComputingAgent) {
			
			System.out.println("Schedule - ComputingAgent");
			
			ComputingAgent computingAgent = (ComputingAgent) dataProvider;
			System.out.println("    " + computingAgent.getModelClass());
			
			ContainerController cc = agent.getContainerController();
			AgentController ac = null;
			try {
				ac = cc.createNewAgent("WekaCA", computingAgent.getModelClass(), null);
				ac.start();
				

				Interval inRangeB = new Interval();
				inRangeB.setMin(Float.parseFloat("2.0"));
				inRangeB.setMax(Float.parseFloat("1000.0"));
				
				Interval inArgsB = new Interval();
				inArgsB.setMin(Float.parseFloat("1.0"));
				inArgsB.setMax(Float.parseFloat("1.0"));

				Option optionB = new Option();
				optionB.setMutable(false);
				optionB.setRange(inRangeB);
				optionB.setIs_a_set(false);
				optionB.setNumber_of_args(inArgsB);
				optionB.setData_type("INT");
				optionB.setName("B");
				optionB.setNumber_of_values_to_try(0);
				optionB.setNumberOfOptions(0);
				
				Interval inRangeR = new Interval();
				inRangeR.setMin(Float.parseFloat("1.0E-9"));
				inRangeR.setMax(Float.parseFloat("10.0"));

				Interval inArgsR = new Interval();
				inArgsR.setMin(Float.parseFloat("1.0"));
				inArgsR.setMax(Float.parseFloat("1.0"));

				Option optionR = new Option();
				optionR.setMutable(false);
				optionR.setRange(inRangeR);
				optionR.setIs_a_set(false);
				optionR.setNumber_of_args(inArgsR);
				optionR.setData_type("FLOAT");
				optionR.setName("R");
				optionR.setNumber_of_values_to_try(0);
				optionR.setNumberOfOptions(0);
			
				Interval inRangeS = new Interval();
				inRangeS.setMin(Float.parseFloat("0.0"));
				inRangeS.setMax(Float.parseFloat("2.14748365E9"));

				Interval inArgsS = new Interval();
				inArgsS.setMin(Float.parseFloat("1.0"));
				inArgsS.setMax(Float.parseFloat("1.0"));

				Option optionS = new Option();
				optionS.setMutable(false);
				optionS.setRange(inRangeS);
				optionS.setIs_a_set(false);
				optionS.setNumber_of_args(inArgsS);
				optionS.setData_type("INT");
				optionS.setName("S");
				optionS.setValue("0");
				optionS.setNumber_of_values_to_try(0);
				optionS.setNumberOfOptions(0);


				Interval inRangeM = new Interval();
				inRangeM.setMin(Float.parseFloat("1.0"));
				inRangeM.setMax(Float.parseFloat("50.0"));

				Interval inArgsM = new Interval();
				inArgsM.setMin(Float.parseFloat("1.0"));
				inArgsM.setMax(Float.parseFloat("1.0"));

				Option optionM = new Option();
				optionM.setMutable(false);
				optionM.setRange(inRangeM);
				optionM.setIs_a_set(false);
				optionM.setNumber_of_args(inArgsM);
				optionM.setData_type("INT");
				optionM.setName("M");
				optionM.setValue("0.2");
				optionM.setDefault_value("0.2");
				optionM.setNumber_of_values_to_try(0);
				optionM.setNumberOfOptions(0);

				jade.util.leap.ArrayList optionsAgent = new jade.util.leap.ArrayList();
				optionsAgent.add(optionB);
				optionsAgent.add(optionR);
				optionsAgent.add(optionS);
				optionsAgent.add(optionM);
				
				pikater.ontology.messages.Agent agent_ = new pikater.ontology.messages.Agent();
				agent_.setType("RBFNetwork");
				agent_.setOptions(optionsAgent);
				
				Data data = new Data();
				data.setTrain_file_name("data/files/772c551b8486b932aed784a582b9c1b1");
				data.setExternal_train_file_name("weather.arff");
				data.setTest_file_name("data/files/772c551b8486b932aed784a582b9c1b1");
				data.setExternal_test_file_name("weather.arff");
				data.setOutput("evaluation_only");
				data.setMode("train_test");
				data.setGui_id(0);
				

				Option optionF = new Option();
				optionF.setMutable(false);
				optionF.setIs_a_set(false);
				optionF.setName("F");
				optionF.setValue("10");
				optionF.setUser_value("10");
				optionF.setNumber_of_values_to_try(0);
				optionF.setNumberOfOptions(0);
				
				jade.util.leap.ArrayList optionsMethod = new jade.util.leap.ArrayList();
				optionsMethod.add(optionF);
				
				EvaluationMethod evaluation_method = new EvaluationMethod();
				evaluation_method.setName("CrossValidation");
				evaluation_method.setOptions(optionsMethod);

				Task task = new Task();
				task.setId(new Id("0"));
				task.setProblem_id(new Id("0"));
				task.setAgent(agent_);
				task.setData(data);
				task.setGet_results("after_each_computation");
				task.setGui_agent("UI");
				task.setSave_results(true);
				task.setUserID(0);
				task.setStart("2014-03-25 21:38:55.000756");
				task.setProblem_name("test");
				task.setEvaluation_method(evaluation_method);
				
				
				Interval inRangeN = new Interval();
				inRangeN.setMin(Float.parseFloat("1.0"));
				inRangeN.setMax(Float.parseFloat("2000.0"));

				Interval inArgsN = new Interval();
				inArgsN.setMin(Float.parseFloat("1.0"));
				inArgsN.setMax(Float.parseFloat("1.0"));

				Option optionN = new Option();
				optionN.setName("neco");
				optionN.setMutable(false);
				optionN.setRange(inRangeN);
				optionN.setIs_a_set(false);
				optionN.setNumber_of_args(inArgsN);
				optionN.setData_type("INT");
				optionN.setDescription("Default number of values to try for each option");
				optionN.setSynopsis("number_of_values_to_try");
				optionN.setValue("5");
				optionN.setDefault_value("5");
				optionN.setNumber_of_values_to_try(0);

				jade.util.leap.ArrayList optionsAgentMethod = new jade.util.leap.ArrayList();
				optionsAgentMethod.add(optionN);

				pikater.ontology.messages.Agent method = new pikater.ontology.messages.Agent();
				method.setName("ChooseXValues");
				method.setOptions(optionsAgentMethod);
				method.setType("neco");
				
				Execute execute = new Execute();
				execute.setTask(task);
				execute.setMethod(method);

/*				
				// Find ComputingAgent
		        ServiceDescription compManagerDescription = new ServiceDescription();
		        compManagerDescription.setType("ComputingAgent");
		        
		        DFAgentDescription compManagerTemplate = new DFAgentDescription();
		        compManagerTemplate.addServices(compManagerDescription);
		        
		        DFAgentDescription[] compAgentsDF = null;
		        try {
		        	compAgentsDF = DFService.search(agent, compManagerTemplate);
		        } catch (FIPAException fe) {
		                fe.printStackTrace();
		        }

		        System.out.println("Found the following ComputingAgent agents:");
		        AID[] compAgentsAID = new AID[compAgentsDF.length];
		        for (int i = 0; i < compAgentsDF.length; ++i) {
		        	    compAgentsAID[i] = compAgentsDF[i].getName();
		                System.out.println(compAgentsAID[i].getName());
		        }
*/
				
		        AID receiverCA = new AID("WekaCA", false);		

		        ACLMessage msgCompAgent = new ACLMessage(ACLMessage.REQUEST);
		        msgCompAgent.addReceiver(receiverCA);
		        msgCompAgent.setLanguage( ((PikaterAgent) agent).getCodec().getName());
		        msgCompAgent.setOntology( ((PikaterAgent) agent).getOntology().getName());
		 
		        agent.getContentManager().fillContent(msgCompAgent, new Action(receiverCA, execute));
				//agent.send(msgCompAgent);
				
			} catch (StaleProxyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	  	

		}
		
    }
    
    
    
    
    
}



