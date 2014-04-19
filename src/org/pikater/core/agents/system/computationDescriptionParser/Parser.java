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
import org.pikater.core.ontology.description.Recommender;
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

			return this.process (computingAgent);

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

			this.process (computingAgent);

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

			ProblemItem problemWrapper = this.process(agent);
			return problemWrapper;

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

    	IComputingAgent agent = complex.getComputingAgent();
    	ProblemItem problemWrapper = this.process(agent);
    	Problem problem = problemWrapper.getProblem();
    	
    	Recommender recommender = complex.getRecommender();
    	org.pikater.core.ontology.messages.Agent recommendeAgent =
    			this.process(recommender);
    	
    	problem.setRecommender(recommendeAgent);
    	
    	return problemWrapper;
	}
    
    public void process (Search search) {
 
    	agent.log("Ontology Parser - Search");
    }
    
    public org.pikater.core.ontology.messages.Agent process (Recommender recommender) {

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
    
    public ProblemItem process (ComputingAgent computingAgent) {

    	agent.log("Ontology Parser - ComputingAgent");
    	
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
    	
    	
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		Date date = new Date();

		String startDate = dateFormat.format(date);
   
		
		Option evaluationMethodOprion = null;
		Option computingMethodOprion = null;
		Option searchMethodOprion = null;
		Option outputOption = null;
		
		ArrayList options = computingAgent.getOptions();
		for (int i = 0; i < options.size(); i++) {
			
			Option optionI = (Option) options.get(i);
			
			if (optionI.getName().equals("evaluation_method")) {
				evaluationMethodOprion = optionI;
			
			} else if (optionI.getName().equals("search_method")) {
				searchMethodOprion = optionI;

			} else if (optionI.getName().equals("computing_method")) {
				computingMethodOprion = optionI;
				
			} else if (optionI.getName().equals("output")) {
				outputOption = optionI;
			}
		}
		options.remove(evaluationMethodOprion);
		options.remove(computingMethodOprion);
		options.remove(searchMethodOprion);
		options.remove(outputOption);


		String evaluationMethod = evaluationMethodOprion.getValue();
		String searchMethod = searchMethodOprion.getValue();
		String computingMethod = computingMethodOprion.getValue();
		String output = outputOption.getValue();

		agent.log("evaluation_method:  " + evaluationMethod);
		agent.log("search_method:  " + searchMethod);
		agent.log("computing_method:  " + computingMethod);
		agent.log("output:  " + output);
		
		
 			ArrayList optionsAgent = computingAgent.getOptions();

			org.pikater.core.ontology.messages.Agent agent_ = new org.pikater.core.ontology.messages.Agent();
			agent_.setType(computingMethod);
			agent_.setGui_id(String.valueOf(problemID));
			agent_.setOptions(optionsAgent);

			ArrayList agents = new ArrayList();
			agents.add(agent_);


			Data data = new Data();
			data.setTrain_file_name(trainDataFileHash);
			data.setExternal_train_file_name(trainDataFileName);
			data.setTest_file_name(testingDataFileHash);
			data.setExternal_test_file_name(testingDataFileName);
			data.setOutput(output);
			data.setMode("train_test");
			data.setGui_id(problemID);

			ArrayList datas = new ArrayList();
			datas.add(data);


			Option optionN = new Option();
			optionN.setName("N");
			optionN.setData_type("INT");
			optionN.setSynopsis("number_of_values_to_try");
			optionN.setValue("5");

			ArrayList optionsAgentMethod = new ArrayList();
			optionsAgentMethod.add(optionN);

			org.pikater.core.ontology.messages.Agent method = new org.pikater.core.ontology.messages.Agent();
			method.setName(searchMethod);
			method.setType(searchMethod);				
			method.setOptions(optionsAgentMethod);

			Option optionF = new Option();
			optionF.setName("F");
			optionF.setData_type("INT");
			optionF.setValue("10");
			
			
			ArrayList optionsMethod = new ArrayList();
			optionsMethod.add(optionF);
			
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
			problem.setMethod(method);
			problem.setEvaluation_method(evaluation_method);
			
			
			ProblemItem problemItem = new ProblemItem();
			problemItem.setProblem(problem);

		return problemItem;
	}

}