package org.pikater.core.agents.system;

import java.util.ArrayList;
import java.util.Date;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.description.CARecSearchComplex;
import org.pikater.core.ontology.description.ComputationDescription;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.ontology.description.DataSourceDescription;
import org.pikater.core.ontology.description.DescriptionOntology;
import org.pikater.core.ontology.description.DifferenceVisualizer;
import org.pikater.core.ontology.description.FileDataProvider;
import org.pikater.core.ontology.description.FileVisualizer;
import org.pikater.core.ontology.description.IComputationElement;
import org.pikater.core.ontology.description.IComputingAgent;
import org.pikater.core.ontology.description.IDataProvider;
import org.pikater.core.ontology.description.IErrorProvider;
import org.pikater.core.ontology.description.IVisualizer;
import org.pikater.core.ontology.description.Parameter;
import org.pikater.core.ontology.description.Recommender;
import org.pikater.core.ontology.description.Search;
import org.pikater.core.ontology.messages.Data;
import org.pikater.core.ontology.messages.EvaluationMethod;
import org.pikater.core.ontology.messages.ExecuteExperiment;
import org.pikater.core.ontology.messages.Interval;
import org.pikater.core.ontology.messages.Option;
import org.pikater.core.ontology.messages.Problem;
import org.pikater.core.ontology.messages.Solve;

import jade.content.Concept;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;


public class Agent_ComputingManager extends PikaterAgent {
	
	private static final long serialVersionUID = 7116837600070411675L;
	
	@Override
	protected void setup() {
		
		System.out.println("Agent: " +getLocalName() + " starts.");
	  	
		initDefault();
		registerWithDF("ComputingManager");

		this.getContentManager().registerOntology(getOntology());
		this.getContentManager().registerOntology(DescriptionOntology.getInstance());

		ComputingManagerBehaviour compBehaviour =
				new ComputingManagerBehaviour(this, getCodec(), getOntology());
        addBehaviour(compBehaviour);

	}
	
	@Override
	protected String getAgentType(){
		return "ComputingManager";
	}
		
}


class ComputingManagerBehaviour extends AchieveREResponder {

    private Agent agent;
	private Codec codec = null;
	private Ontology ontology = null;

    public ComputingManagerBehaviour(Agent agent, Codec codec, Ontology ontology) {
    	super(agent, MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
    	
		this.agent = agent;
		this.codec = codec;
		this.ontology = ontology;
    }
    
    @Override
    protected ACLMessage handleRequest(final ACLMessage request) throws NotUnderstoodException, RefuseException {
   
    	Concept object = null;
 
    	try {
        	//Serializable object = request.getContentObject();
            object = ((Action)(agent.getContentManager().extractContent(request))).getAction();
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
            
  
        ACLMessage reply = request.createReply();
        
    	if (object instanceof ExecuteExperiment) {
    		
    		ExecuteExperiment executeExperiment =
    				(ExecuteExperiment) object;
    		ComputationDescription comDescription =
					executeExperiment.getDescription();
    		
    		ComputingDescriptionParser parser =
    				new ComputingDescriptionParser(agent, codec, ontology);
    		parser.process(comDescription);
    		
    		
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("OK");
        }
   
        return reply;

    }
    
}



class ComputingDescriptionParser {
	
	private Agent agent = null;
	private Codec codec = null;
	private Ontology ontology = null;
	
	public ComputingDescriptionParser(Agent agent, Codec codec, Ontology ontology) {
		
		this.agent = agent;
		this.codec = codec;
		this.ontology = ontology;
	}


	public void process(IComputationElement element) {

		System.out.println("Ontology Parser - IComputationElement");

		IVisualizer visualizer = (IVisualizer) element;
		process(visualizer);
	}
	
	public void process(IVisualizer visualizer) {

		System.out.println("Ontology Parser - IVisualizer");

		if (visualizer instanceof FileVisualizer) {

			System.out.println("Ontology Matched - FileVisualizer");
			
			FileVisualizer fileVisualizer = (FileVisualizer) visualizer;
			DataSourceDescription dataSource = fileVisualizer.getDataSource();
			
			this.process(dataSource);
			
		} else if (visualizer instanceof DifferenceVisualizer ) {

			System.out.println("Ontology Matched - DifferenceVisualizer");

			DifferenceVisualizer diffVisualizer =
					(DifferenceVisualizer) visualizer;

			DataSourceDescription dataSrcDataMod =
					diffVisualizer.getModelData();
			DataSourceDescription dataSrcTarData =
					diffVisualizer.getTargetData();

			this.process(dataSrcDataMod);
			this.process(dataSrcTarData);
			
		} else {

			System.out.println("Ontology Parser - Error unknown IVisualizer");
		}

    }
	
    public String process (IDataProvider dataProvider) {

    	System.out.println("Ontology Parser - IDataProvider");

    	if (dataProvider instanceof FileDataProvider) {

			System.out.println("Ontology Matched - FileDataProvider");
			
			FileDataProvider fileData =
					(FileDataProvider) dataProvider;
			
			return this.process (fileData);

    	
    	} else if (dataProvider instanceof CARecSearchComplex) {

			System.out.println("Ontology Matched - CARecSearchComplex");

			CARecSearchComplex comlex =
					(CARecSearchComplex) dataProvider;

			this.process (comlex);

		} else if (dataProvider instanceof ComputingAgent) {

			System.out.println("Ontology Matched - ComputingAgent");

			ComputingAgent computingAgent =
					(ComputingAgent) dataProvider;

			this.process (computingAgent);

		} else {

			System.out.println("Ontology Matched - Error unknown IDataProvider");
		}
		
    	return "NOT IMPLEMENTED";
    }

    public void process (IErrorProvider errorProvider) {

    	System.out.println("Ontology Parser - IErrorProvider");

		if (errorProvider instanceof ComputingAgent) {

			System.out.println("Ontology Matched - ComputingAgent");

			ComputingAgent computingAgent =
					(ComputingAgent) errorProvider;

			this.process (computingAgent);

		} else {

			System.out.println("Ontology Matched - Error unknown IErrorProvider");
		}
		
    }
    public void process (IComputingAgent iAgent) {

    	System.out.println("Ontology Parser - IComputingAgent");

		if (iAgent instanceof CARecSearchComplex) {

			System.out.println("Ontology Matched - CARecSearchComplex");

			CARecSearchComplex complex =
					(CARecSearchComplex) iAgent;

			this.process(complex);

		} else if (iAgent instanceof ComputingAgent) {

			System.out.println("Ontology Matched - ComputingAgent");

			ComputingAgent agent =
					(ComputingAgent) iAgent;

			this.process(agent);

		} else {

			System.out.println("Ontology Matched - Error unknown IComputingAgent");
		}

	}
    
	public void process(ComputationDescription comDescription) {

		System.out.println("Ontology Parser - ComputationDescription");
		
		IComputationElement element =
				comDescription.getRootElement();
		
		this.process(element);
	}
	
    public String process (DataSourceDescription dataSource) {

    	System.out.println("Ontology Parser - DataSourceDescription");

    	IDataProvider dataProvider = dataSource.getDataProvider();
    	
    	return this.process(dataProvider);
    }

    public String process (FileDataProvider file) {

    	System.out.println("Ontology Parser - FileDataProvider");
    	return file.getFileURI();
    }
    
    public void process (CARecSearchComplex complex) {

    	System.out.println("Ontology Parser - CARecSearchComplex");

    	IComputingAgent agent = complex.getComputingAgent();
    	this.process(agent);
    	
    	Recommender recommender = complex.getRecommender();
    	this.process(recommender);
	}
    
    public void process (Search search) {
 
    	System.out.println("Ontology Parser - Search");
    }
    
    public org.pikater.core.ontology.messages.Agent process (Recommender recommender) {

    	String recommenderClass =
    			recommender.getRecommenderClass();
    	
    	ArrayList<Parameter> parameters = recommender.getParameters();
    	
		org.pikater.core.ontology.messages.Agent method =
				new org.pikater.core.ontology.messages.Agent();
		method.setName(recommenderClass);
		method.setType(recommenderClass);
		//TODO: Konverze parametru neni hotova
		//method.setOptions(parameters);
		
		return method;
    }
    
    public void process (ComputingAgent computingAgent) {

    	System.out.println("Ontology Parser - ComputingAgent");
    	
    	DataSourceDescription rainingDataSource =
    			computingAgent.getTrainingData();
    	DataSourceDescription testingDataSource =
    			computingAgent.getTestingData();
    	DataSourceDescription validationDataSource =
    			computingAgent.getValidationData();
    	
    	//process(rainingDataSource);
    	
			Solve solve = new Solve();

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
			
			org.pikater.core.ontology.messages.Agent agent_ = new org.pikater.core.ontology.messages.Agent();
			agent_.setType("RBFNetwork");
			agent_.setGui_id("0");
			agent_.setOptions(optionsAgent);

			jade.util.leap.ArrayList agents = new jade.util.leap.ArrayList();
			agents.add(agent_);


			Data data = new Data();
			data.setTrain_file_name("772c551b8486b932aed784a582b9c1b1");
			data.setExternal_train_file_name("weather.arff");
			data.setTest_file_name("772c551b8486b932aed784a582b9c1b1");
			data.setExternal_test_file_name("weather.arff");
			data.setOutput("evaluation_only");
			data.setMode("train_test");
			data.setGui_id(0);

			jade.util.leap.ArrayList datas = new jade.util.leap.ArrayList();
			datas.add(data);
			
			
			Interval inRangeN = new Interval();
			inRangeN.setMin(Float.parseFloat("1.0"));
			inRangeN.setMax(Float.parseFloat("2000.0"));

			Interval inArgsN = new Interval();
			inArgsN.setMin(Float.parseFloat("1.0"));
			inArgsN.setMax(Float.parseFloat("1.0"));

			Option optionN = new Option();
			optionN.setName("N");
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

			org.pikater.core.ontology.messages.Agent method = new org.pikater.core.ontology.messages.Agent();
			method.setName("ChooseXValues");
			method.setType("ChooseXValues");				
			method.setOptions(optionsAgentMethod);


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

			
			Problem problem = new Problem();
			problem.setGui_id("0");
			problem.setStatus("new");			
			problem.setAgents(agents);
			problem.setData(datas);				
			problem.setTimeout(30000);
			problem.setStart("2014-04-03 18:55:56.000978");
			problem.setGet_results("after_each_computation");
			problem.setSave_results(true);
			problem.setGui_agent("UI");
			problem.setName("test");
			problem.setMethod(method);
			problem.setEvaluation_method(evaluation_method);
					
			solve.setProblem(problem);



			System.out.println("Sending SOLVE");
			
			// create a request message with SendProblem content
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setSender(agent.getAID());
			msg.addReceiver(new AID("manager", false));
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

			msg.setLanguage(codec.getName());
			msg.setOntology(ontology.getName());
			// We want to receive a reply in 30 secs
			msg.setReplyByDate(new Date(System.currentTimeMillis() + 30000));
			msg.setConversationId(problem.getGui_id() + agent.getLocalName());

			Action a = new Action();
			a.setAction(solve);
			a.setActor(agent.getAID());

			try {
				// Let JADE convert from Java objects to string
				agent.getContentManager().fillContent(msg, a);

			} catch (CodecException ce) {
				ce.printStackTrace();
			} catch (OntologyException oe) {
				oe.printStackTrace();
			}

			agent.addBehaviour(new SendProblemToManager(agent, msg));
			
	}

}




class SendProblemToManager extends AchieveREInitiator {

	private static final long serialVersionUID = 8923548223375000884L;

	PikaterAgent agent;
	
	public SendProblemToManager(Agent agent, ACLMessage msg) {
		super(agent, msg);
		this.agent = (PikaterAgent) agent;
	}

	protected void handleAgree(ACLMessage agree) {
		System.out.println(agent.getLocalName() + ": Agent "
				+ agree.getSender().getName() + " agreed.");
	}

	protected void handleInform(ACLMessage inform) {
		System.out.println(agent.getLocalName() + ": Agent "
				+ inform.getSender().getName() + " replied.");
	}

	protected void handleRefuse(ACLMessage refuse) {
		System.out.println(agent.getLocalName() + ": Agent "
				+ refuse.getSender().getName()
				+ " refused to perform the requested action");
	}

	protected void handleFailure(ACLMessage failure) {
		if (failure.getSender().equals(myAgent.getAMS())) {
			// FAILURE notification from the JADE runtime: the receiver
			// does not exist
			System.out.println(agent.getLocalName() + ": Responder does not exist");
		} else {
			System.out.println(agent.getLocalName() + ": Agent " + failure.getSender().getName()
					+ " failed to perform the requested action");
		}
	}

}




