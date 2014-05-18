package org.pikater.core.agents.system.computationDescriptionParser;

import jade.util.leap.ArrayList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.pikater.core.agents.system.Agent_ComputationDescriptionParser;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ProblemGraph;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ProblemItem;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.description.CARecSearchComplex;
import org.pikater.core.ontology.subtrees.description.ComputationDescription;
import org.pikater.core.ontology.subtrees.description.ComputingAgent;
import org.pikater.core.ontology.subtrees.description.DataProcessing;
import org.pikater.core.ontology.subtrees.description.DataSourceDescription;
import org.pikater.core.ontology.subtrees.description.FileDataProvider;
import org.pikater.core.ontology.subtrees.description.FileDataSaver;
import org.pikater.core.ontology.subtrees.description.IComputationElement;
import org.pikater.core.ontology.subtrees.description.IComputingAgent;
import org.pikater.core.ontology.subtrees.description.IDataProvider;
import org.pikater.core.ontology.subtrees.description.IDataSaver;
import org.pikater.core.ontology.subtrees.description.IErrorProvider;
import org.pikater.core.ontology.subtrees.description.Recommend;
import org.pikater.core.ontology.subtrees.description.Search;
import org.pikater.core.ontology.subtrees.messages.Problem;
import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.core.ontology.subtrees.task.EvaluationMethod;

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

			// Small hack - this parser is able to parse only CARecSearchComplex
			CARecSearchComplex complex =
					new CARecSearchComplex();
			complex.setComputingAgent(computingAgent);

			return this.process(complex);

		} else if (dataProvider instanceof DataProcessing) {

			agent.log("Ontology Matched - DataProcessing");
			
			DataProcessing postprocessing =
					(DataProcessing) dataProvider;
			
			//TODO:  process(postprocessing);
			return null;

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

			// Small hack - this parser is able to parse only CARecSearchComplex
			CARecSearchComplex complex =
					new CARecSearchComplex();
			complex.setComputingAgent(computingAgent);
			
			this.process(complex);

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

			return this.process(complex);

		} else if (iAgent instanceof ComputingAgent) {

			agent.log("Ontology Matched - ComputingAgent");

			ComputingAgent computingAgent =
					(ComputingAgent) iAgent;

			// Small hack - this parser is able to parse only CARecSearchComplex
			CARecSearchComplex complex =
					new CARecSearchComplex();
			complex.setComputingAgent(computingAgent);
			
			return this.process(complex);

		} else {

			agent.logError("Ontology Matched - Error unknown IComputingAgent");
			return null;
		}

	}
    
	public void process(ComputationDescription comDescription) {

		agent.log("Ontology Parser - ComputationDescription");
		
		List<FileDataSaver> elements =
				comDescription.getRootElements();

		// TODO: Vyresit aby se sdilene podulohy rootElementu pocitely pouze jednou
		for (int i = 0; i < elements.size(); i++) {

			IComputationElement fileSaverI = (IComputationElement) elements.get(i);
			ProblemItem problem = this.process(fileSaverI);
			this.graph.addRootProblem(problem);
		}

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

    	// TODO: Generate ID of this problem
    	int problemID = 123; //this.graph.getNumOfProblems();


    	// Option parsing
		String evaluationMethod = null;
		String output = null;

		Option optionF = null;

		List<Option> options = complex.getOptions();
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
    	org.pikater.core.ontology.subtrees.management.Agent computingAgent = 
    			processAgent(computingAgentO, problemID);

    	Search searchAgentO = complex.getSearch();
    	org.pikater.core.ontology.subtrees.management.Agent searchAgent =
    			processSearch(searchAgentO);

    	Recommend recommenderO = complex.getRecommender();
    	org.pikater.core.ontology.subtrees.management.Agent recommendeAgent =
    			processRecommender(recommenderO);

    	Data data = processData(computingAgentO, output);
    	

    	
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		Date date = new Date();
		String startDate = dateFormat.format(date);

		ArrayList agents = new ArrayList();
		agents.add(computingAgent);

		ArrayList datas = new ArrayList();
		datas.add(data);

		java.util.List<Option> optionsMethod = new java.util.ArrayList<Option>();
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



		// Return sons which are dependent on this Problem
		java.util.ArrayList<ProblemItem> dependentSons =
				getDependentSons(computingAgentO);
		
    	ProblemItem item = new ProblemItem();
    	item.setProblem(problem);
    	item.setDependentSons(dependentSons);
    	item.setStatus(ProblemItem.ProblemStatus.IS_WAITING);

    	return item;
	}
    
    public org.pikater.core.ontology.subtrees.management.Agent processSearch (Search search) {
 
    	agent.log("Ontology Parser - Search");
    	
    	// Option parsing
		Option searchMethodOprion = null;
		Option optionN = null;

		if (search != null) {
			for (Option optionI : search.getOptions()) {
				
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

		java.util.ArrayList<Option> optionsSearchMethod = new java.util.ArrayList<Option>();
		if (optionN != null) {
			optionsSearchMethod.add(optionN);
		}		
		org.pikater.core.ontology.subtrees.management.Agent searchAgent = new org.pikater.core.ontology.subtrees.management.Agent();
		searchAgent.setName(searchMethod);
		searchAgent.setType(searchMethod);
		searchAgent.setOptions(optionsSearchMethod);
		
		return searchAgent;
    }
    
    public org.pikater.core.ontology.subtrees.management.Agent processRecommender (Recommend recommender) {

    	agent.log("Ontology Parser - Recommender");

    	if (recommender == null) {
    		return null;
    	}

    	String recommenderClass =
    			recommender.getRecommenderClass();
    	
    	java.util.List<Option> options = recommender.getOptions();
    	
		org.pikater.core.ontology.subtrees.management.Agent method =
				new org.pikater.core.ontology.subtrees.management.Agent();
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

    public org.pikater.core.ontology.subtrees.management.Agent processAgent (ComputingAgent computingAgent, int problemID) {

    	agent.log("Ontology Parser - ComputingAgent");

		String modelClass = computingAgent.getModelClass();
		agent.log("ModelClass:  " + modelClass);

				
		java.util.List<Option> optionsCompAgent = computingAgent.getOptions();

		org.pikater.core.ontology.subtrees.management.Agent compAgentO = new org.pikater.core.ontology.subtrees.management.Agent();
		compAgentO.setType(computingAgent.getModelClass());
		compAgentO.setGui_id(String.valueOf(problemID));
		compAgentO.setOptions(optionsCompAgent);

		return compAgentO;
	}

    public java.util.ArrayList<ProblemItem> getDependentSons(ComputingAgent compAgent) {
    	
    	if (compAgent == null) {
    		return null;
    	}

    	DataSourceDescription trainingData = compAgent.getTrainingData();
    	DataSourceDescription testingData = compAgent.getTestingData();
    	DataSourceDescription validationData = compAgent.getValidationData();
    	
    	ProblemItem trainingItem = process(trainingData);
    	ProblemItem testingItem = process(testingData);
    	ProblemItem validationItem = process(validationData);
    	
    	java.util.ArrayList<ProblemItem> dependentSons =
    			new java.util.ArrayList<ProblemItem>();
    	
    	if (trainingItem != null) {
    		dependentSons.add(trainingItem);
    	}
    	if (testingItem != null) {
    		dependentSons.add(testingItem);
    	}
    	if (validationItem != null) {
    		dependentSons.add(validationItem);
    	}
    	
    	return dependentSons;
    }
    
}
