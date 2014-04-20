package org.pikater.core.agents.system.computationDescriptionParser;

import jade.util.leap.ArrayList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.pikater.core.agents.system.Agent_ComputationDescriptionParser;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ProblemGraph;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ProblemItem;
import org.pikater.core.ontology.description.CARecSearchComplex;
import org.pikater.core.ontology.description.ComputationDescription;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.ontology.description.DataSourceDescription;
import org.pikater.core.ontology.description.FileDataProvider;
import org.pikater.core.ontology.description.FileDataSaver;
import org.pikater.core.ontology.description.IComputationElement;
import org.pikater.core.ontology.description.IComputingAgent;
import org.pikater.core.ontology.description.IDataProvider;
import org.pikater.core.ontology.description.IDataSaver;
import org.pikater.core.ontology.description.IErrorProvider;
import org.pikater.core.ontology.description.Recommen;
import org.pikater.core.ontology.description.Search;
import org.pikater.core.ontology.messages.Data;
import org.pikater.core.ontology.messages.EvaluationMethod;
import org.pikater.core.ontology.messages.Option;
import org.pikater.core.ontology.messages.Problem;

public class Parser {
	
	private ProblemGraph graph = null;
	private Agent_ComputationDescriptionParser agent = null;

	public Parser(Agent_ComputationDescriptionParser agent_) {
		this.agent = agent_;
		this.graph = new ProblemGraph();
	}

	public ProblemGraph getProblemGraph() {
		return this.graph;
	}
	
	public ProblemItem process(IComputationElement element) {

		agent.log("Ontology Parser - IComputationElement");

		IDataSaver dataSaver = (IDataSaver) element;
		return process(dataSaver);
	}
	
	public ProblemItem process(IDataSaver dataSaver) {

		agent.log("Ontology Parser - IVisualizer");

		if (dataSaver instanceof FileDataSaver) {

			agent.log("Ontology Matched - FileDataSaver");
			
			FileDataSaver fileDataSaver = (FileDataSaver) dataSaver;
			DataSourceDescription dataSource = fileDataSaver.getDataSource();
			
			return this.process(dataSource);
			
		} else {

			agent.logError("Ontology Parser - Error unknown IDataSaver");
			return null;
		}

    }
	
    public ProblemItem process (IDataProvider dataProvider) {

    	agent.log("Ontology Parser - IDataProvider");

    	if (dataProvider instanceof FileDataProvider) {

    		agent.log("Ontology Matched - FileDataProvider");
			
			FileDataProvider fileData =
					(FileDataProvider) dataProvider;
			
			return this.process (fileData);

    	
    	} else if (dataProvider instanceof CARecSearchComplex) {

    		agent.log("Ontology Matched - CARecSearchComplex");

			CARecSearchComplex comlex =
					(CARecSearchComplex) dataProvider;

			return this.process (comlex);

		} else if (dataProvider instanceof ComputingAgent) {

			agent.log("Ontology Matched - ComputingAgent");

			ComputingAgent computingAgent =
					(ComputingAgent) dataProvider;

			return null;
//			return this.process (computingAgent);

		} else {

			agent.log("Ontology Matched - Error unknown IDataProvider");

			return null;
		}

    }

    public void process (IErrorProvider errorProvider) {

    	agent.log("Ontology Parser - IErrorProvider");

		if (errorProvider instanceof ComputingAgent) {

			agent.log("Ontology Matched - ComputingAgent");

			ComputingAgent computingAgent =
					(ComputingAgent) errorProvider;

			return;
//			this.process (computingAgent);

		} else {

			agent.log("Ontology Matched - Error unknown IErrorProvider");
		}
		
    }
    public ProblemItem process (IComputingAgent iAgent) {

    	agent.log("Ontology Parser - IComputingAgent");

		if (iAgent instanceof CARecSearchComplex) {

			agent.log("Ontology Matched - CARecSearchComplex");

			CARecSearchComplex complex =
					(CARecSearchComplex) iAgent;

			this.process(complex);
			return null;

		} else if (iAgent instanceof ComputingAgent) {

			agent.log("Ontology Matched - ComputingAgent");

			ComputingAgent agent =
					(ComputingAgent) iAgent;

			return null;
//			ProblemItem problemWrapper = this.process(agent);
//			return problemWrapper;

		} else {

			agent.logError("Ontology Matched - Error unknown IComputingAgent");
			return null;
		}

	}
    
	public void process(ComputationDescription comDescription) {

		agent.log("Ontology Parser - ComputationDescription");
		
		IComputationElement element =
				comDescription.getRootElement();
		
		ProblemItem problem = this.process(element);
		this.graph.addRootProblem(problem);

	}
	
    public ProblemItem process (DataSourceDescription dataSource) {

    	agent.log("Ontology Parser - DataSourceDescription");

    	if (dataSource == null) {
    		return null;
    	}
 
    	IDataProvider dataProvider = dataSource.getDataProvider();
    	
    	return this.process(dataProvider);
    }

    public ProblemItem process (FileDataProvider file) {

    	agent.log("Ontology Parser - FileDataProvider");

    	ProblemItem problem = new ProblemItem();
    	problem.setOutputFile(file.getFileURI());
 
    	return problem;
    }
    
    public ProblemItem process (CARecSearchComplex complex) {

    	agent.log("Ontology Parser - CARecSearchComplex");

    	// Generate ID of this problem
    	int problemID = this.graph.getNumOfProblems();

    	
    	// Option parsing
		String evaluationMethod = null;
		String output = null;
		
		Option optionF = null;
		
		ArrayList options = complex.getOptions();
		for (int i = 0; i < options.size(); i++) {
			
			Option optionI = (Option) options.get(i);
			
			if (optionI.getName().equals("evaluation_method")) {
				evaluationMethod = optionI.getValue();

			} else if (optionI.getName().equals("output")) {
				output = optionI.getValue();
				
			} else if (optionI.getName().equals("F")) {
				optionF = optionI;
			}
		}

    	IComputingAgent iComputingAgent = complex.getComputingAgent();
    	ComputingAgent computingAgentO = (ComputingAgent) iComputingAgent;
    	org.pikater.core.ontology.messages.Agent computingAgent = 
    			processAgent(computingAgentO, problemID);
    	
    	Search searchAgentO = complex.getSearch();
    	org.pikater.core.ontology.messages.Agent searchAgent =
    			processSearch(searchAgentO);

    	Recommen recommenderO = complex.getRecommender();
    	org.pikater.core.ontology.messages.Agent recommendeAgent =
    			processRecommender(recommenderO);

    	Data data = processData(computingAgentO, output);
    	

    	
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		Date date = new Date();
		String startDate = dateFormat.format(date);

		ArrayList agents = new ArrayList();
		agents.add(computingAgent);

		ArrayList datas = new ArrayList();
		datas.add(data);

		ArrayList optionsMethod = new ArrayList();
		if (optionF != null) {
			optionsMethod.add(optionF);
		}
		
		EvaluationMethod evaluation_method = new EvaluationMethod();
		evaluation_method.setName(evaluationMethod);
		evaluation_method.setOptions(optionsMethod);


		Problem problem = new Problem();
		problem.setGui_id(String.valueOf(problemID));
		problem.setStatus("new");			
		problem.setAgents(agents);
		problem.setData(datas);				
		problem.setTimeout(30000);
		problem.setStart(startDate);
		problem.setGet_results("after_each_computation");
		problem.setSave_results(true);
		problem.setGui_agent("UI");
		problem.setName("test");
		problem.setMethod(searchAgent);
		problem.setRecommender(recommendeAgent);
		problem.setEvaluation_method(evaluation_method);



    	ProblemItem item = new ProblemItem();
    	item.setProblem(problem);
    	
    	return item;
	}
    
    public org.pikater.core.ontology.messages.Agent processSearch (Search search) {
 
    	agent.log("Ontology Parser - Search");
    	
    	// Option parsing
		Option searchMethodOprion = null;
		Option optionN = null;

		if (search != null) {
			ArrayList options = search.getOptions();
			for (int i = 0; i < options.size(); i++) {
				
				Option optionI = (Option) options.get(i);
				
				if (optionI.getName().equals("search_method")) {
					searchMethodOprion = optionI;
				}
				if (optionI.getName().equals("N")) {
					optionN = optionI;
				}
			}
		}
		
		String searchMethod =  null;

		if (searchMethodOprion == null) {
			searchMethod = "ChooseXValues";
		} else {
			searchMethod = searchMethodOprion.getValue();
		}

		ArrayList optionsSearchMethod = new ArrayList();
		if (optionN != null) {
			optionsSearchMethod.add(optionN);
		}		
		org.pikater.core.ontology.messages.Agent searchAgent = new org.pikater.core.ontology.messages.Agent();
		searchAgent.setName(searchMethod);
		searchAgent.setType(searchMethod);
		searchAgent.setOptions(optionsSearchMethod);
		
		return searchAgent;
    }
    
    public org.pikater.core.ontology.messages.Agent processRecommender (Recommen recommender) {

    	agent.log("Ontology Parser - Recommender");

    	if (recommender == null) {
    		return null;
    	}

    	String recommenderClass =
    			recommender.getRecommenderClass();
    	
    	ArrayList options = recommender.getOptions();
    	
		org.pikater.core.ontology.messages.Agent method =
				new org.pikater.core.ontology.messages.Agent();
		method.setName(recommenderClass);
		method.setType(recommenderClass);
		method.setOptions(options);
		
		return method;
    }
    
    
    public Data processData(ComputingAgent computingAgent, String output) {

    	DataSourceDescription trainingDataSource =
    			computingAgent.getTrainingData();
    	DataSourceDescription testingDataSource =
    			computingAgent.getTestingData();
    	DataSourceDescription validationDataSource =
    			computingAgent.getValidationData();


    	int problemID = this.graph.getNumOfProblems();
    	String trainDataFileName = null;
    	String testingDataFileName = null;
    	String validationDataFileName = null;


    	ProblemItem trainingProblem = process(trainingDataSource);
    	if (trainingProblem != null) {
    		trainDataFileName = trainingProblem.getOutputFile();
    	}
    	ProblemItem testingProblem = process(testingDataSource);
    	if (testingProblem != null) {
    		testingDataFileName = testingProblem.getOutputFile(); 
    	}
    	ProblemItem validationProblem = process(validationDataSource);
    	if (validationProblem != null) {
    		validationDataFileName = validationProblem.getOutputFile();
    	}

    	String trainDataFileHash = agent.getHashOfFile(trainDataFileName);
    	String testingDataFileHash = agent.getHashOfFile(testingDataFileName);
    	String validationDataFileHash = agent.getHashOfFile(validationDataFileName);
    	
    	
		Data data = new Data();
		data.setTrain_file_name(trainDataFileHash);
		data.setExternal_train_file_name(trainDataFileName);
		data.setTest_file_name(testingDataFileHash);
		data.setExternal_test_file_name(testingDataFileName);
		data.setOutput(output);
		data.setMode("train_test");
		data.setGui_id(problemID);
		
		return data;
    }

    public org.pikater.core.ontology.messages.Agent processAgent (ComputingAgent computingAgent, int problemID) {

    	agent.log("Ontology Parser - ComputingAgent");


		Option computingMethodOprion = null;

		ArrayList options = computingAgent.getOptions();
		for (int i = 0; i < options.size(); i++) {
			
			Option optionI = (Option) options.get(i);
			
			if (optionI.getName().equals("computing_method")) {
				computingMethodOprion = optionI;
				
			}
		}
		options.remove(computingMethodOprion);


		String computingMethod = computingMethodOprion.getValue();
		agent.log("computing_method:  " + computingMethod);

				
		ArrayList optionsCompAgent = computingAgent.getOptions();

		org.pikater.core.ontology.messages.Agent compAgentO = new org.pikater.core.ontology.messages.Agent();
		compAgentO.setType(computingMethod);
		compAgentO.setGui_id(String.valueOf(problemID));
		compAgentO.setOptions(optionsCompAgent);



		return compAgentO;
	}

}