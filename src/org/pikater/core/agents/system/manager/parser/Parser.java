package org.pikater.core.agents.system.manager.parser;

import org.pikater.core.CoreConfiguration;
import org.pikater.core.CoreConstant;
import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.manager.graph.ComputationGraph;
import org.pikater.core.agents.system.manager.graph.ComputationNode;
import org.pikater.core.agents.system.manager.graph.DataProcessingComputationNode;
import org.pikater.core.agents.system.manager.graph.ModelComputationNode;
import org.pikater.core.agents.system.manager.graph.RecommenderComputationNode;
import org.pikater.core.agents.system.manager.graph.SearchComputationNode;
import org.pikater.core.agents.system.manager.graph.edges.AgentTypeEdge;
import org.pikater.core.agents.system.manager.graph.edges.DataSourceEdge;
import org.pikater.core.agents.system.manager.graph.edges.EdgeValue;
import org.pikater.core.agents.system.manager.graph.edges.ErrorEdge;
import org.pikater.core.agents.system.manager.graph.edges.OptionEdge;
import org.pikater.core.agents.system.manager.graph.strategies.CAStartComputationStrategy;
import org.pikater.core.agents.system.manager.graph.strategies.DataProcessingStrategy;
import org.pikater.core.agents.system.manager.graph.strategies.RecommenderStartComputationStrategy;
import org.pikater.core.agents.system.manager.graph.strategies.SearchStartComputationStrategy;
import org.pikater.core.agents.system.metadata.MetadataService;
import org.pikater.core.agents.system.openml.OpenMLAgentService;
import org.pikater.core.ontology.subtrees.batchdescription.*;
import org.pikater.core.ontology.subtrees.experiment.Experiment;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.shared.database.jpa.status.JPAExperimentStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for parsing Computation Graph from ontology
 */
public class Parser {
    private ComputationGraph computationGraph =
    		new ComputationGraph();
    private Map<Integer,ComputationNode> alreadyProcessed =
    		new HashMap<>();
    private Agent_Manager agent = null;

    /**
     * Constructor
     * @param agent Owner manager
     */
    public Parser(Agent_Manager agent) {
        this.agent = agent;
    }

    /**
     * Parse roots of ontology
     * @param dataSaver Root dataSaver
     * @param batchID If of the batch
     * @param userID If of the user
     */
    public void parseRoot(IDataSaver dataSaver, int batchID, int userID) {
        agent.logInfo("Ontology Parser - IDataSaver");
        //Ontology root is Leaf in Computation
        parseSaver(dataSaver, batchID, userID);
    }

    /**
     * Parses saver
     * @param dataSaver Ontology dataSaver
     * @param batchID Id of the batch
     * @param userID Id of the owner
     */
    private void parseSaver(IDataSaver dataSaver, int batchID, int userID) {
        if (dataSaver instanceof FileDataSaver) {
            agent.logInfo("Ontology Matched - FileDataSaver");

            FileDataSaver fileDataSaver = (FileDataSaver) dataSaver;
            DataSourceDescription dataSource = fileDataSaver.getDataSource();
            parseDataSourceDescription(dataSource, batchID, userID);
        } else {
            agent.logSevere("Ontology Parser - Error unknown IDataSaver");
        }
    }

    /**
     * Parses dataSource description
     * @param dataSource Datasource in the workflow
     * @param batchID Id of the batch
     * @param userID Id of the owner
     * @param child Child in the computation workflow
     * @param connectionName Name of the buffer
     * @param connectionOutName Name of the output connection
     */
    private void parseDataSourceDescription(DataSourceDescription dataSource,
    		int batchID, int userID, ComputationNode child, String connectionName, 
    		String connectionOutName) {
        agent.logInfo("Ontology Parser - DataSourceDescription");

        IDataProvider dataProvider = dataSource.getDataProvider();
        this.parseDataProvider(dataProvider, batchID, userID, child, 
        		connectionName, connectionOutName);
    }

    /**
     * Parses data source description
     * @param dataSource Datasource in the workflow
     * @param batchID Id of the batch
     * @param userID Id of the owner
     */
    private void parseDataSourceDescription(DataSourceDescription dataSource,
    		int batchID, int userID){
    	parseDataSourceDescription(dataSource, batchID, userID, null,
    			null, null);
    }

    /**
     * Parses data provider
     * @param dataProvider Data provider
     * @param batchID Id of the batch
     * @param userID Id of the owner
     * @param child Child in the computation workflow
     * @param connectionName Name of the buffer
     * @param connectionOutName Name of the output connection
     */
    private void parseDataProvider(IDataProvider dataProvider, int batchID,
    		int userID, ComputationNode child, String connectionName,
    		String connectionOutName) {
        agent.logInfo("Ontology Parser - IDataProvider");
        ComputationNode parent;
        if (dataProvider instanceof FileDataProvider) {
            agent.logInfo("Ontology Matched - FileDataProvider");

            FileDataProvider fileData = (FileDataProvider) dataProvider;
            
            
            
            this.parseFileDataProvider(fileData, child, connectionName, userID);
            return;
        }  else if (dataProvider instanceof ComputingAgent) {
            agent.logInfo("Ontology Matched - ComputingAgent");

            ComputingAgent computingAgent = (ComputingAgent) dataProvider;
            parent = parseComputing(computingAgent, batchID, userID, true);
        } else if (dataProvider instanceof CARecSearchComplex)
        {
            agent.logInfo("Ontology Matched - CARecSearchComplex");

            CARecSearchComplex complex = (CARecSearchComplex) dataProvider;
            parent = parseComplex(complex, batchID, userID);
        } else if (dataProvider instanceof DataProcessing) {
            agent.logInfo("Ontology Matched - DataProcessing");
            
            parseDataProcessing((DataProcessing) dataProvider, child,
            		batchID, userID, connectionName, connectionOutName);
            return;
        } else {
            agent.logInfo("Ontology Matched - Error unknown IDataProvider");
            return;
        }
        
        if (child!=null) {
            //handle parent - set him as file receiver
            ComputationOutputBuffer<EdgeValue> fileBuffer =
                    new StandardBuffer<>(parent, child);
            fileBuffer.setData(true);
            parent.addBufferToOutput(connectionName, fileBuffer);
            child.addInput(connectionName, fileBuffer);
        }
    }

    /**
     * Parses error provider
     * @param errorDescription Error provider
     * @param child Child in the computation workflow
     */
    private void parseErrors(ErrorSourceDescription errorDescription,
    		ComputationNode child) {
    	
        if (errorDescription == null) {
        	return;
        }
        agent.logInfo("Ontology Parser - IErrorProvider");
        ComputingAgent errorProvider =
        		(ComputingAgent)errorDescription.getProvider();

        if (!alreadyProcessed.containsKey(errorProvider.getId())) {    
        	agent.logInfo("Error provider was not parsed at "
        		+ "the moment parseErrors was called");
            return;
        }
        
        ComputationNode errorNode =
        		alreadyProcessed.get(errorProvider.getId());
        StandardBuffer<ErrorEdge> buffer =
        		new StandardBuffer<>(errorNode,child);
        errorNode.addBufferToOutput(
        		errorDescription.getOutputType(), buffer);
        child.addInput(errorDescription.getOutputType(),buffer);
        buffer.block();
    }

    /**
     * This is the root of all parsing
     * @param comDescription computation Description ontology
     * @param batchID Id of the batch
     * @param userID Id of the user
     */
    public void parseRoots(ComputationDescription comDescription,
    		int batchID, int userID) {
    	
        agent.logInfo("Ontology Parser - ComputationDescription");

        List<FileDataSaver> elements = comDescription.getRootElements();
        for (FileDataSaver fileSaverI : elements) {
        	this.parseRoot(fileSaverI, batchID, userID);
        }
    }

    /**
     * Processes a node that is in the beginning of computation - reads file
     * 
     * @param file File provider
     * @param child Child in the computation workflow
     * @param connectionName Name of the buffer
     */
    private void parseFileDataProvider(FileDataProvider file,
    		ComputationNode child, String connectionName, int userID) {

        agent.logInfo("Ontology Parser - FileDataProvider");
        
              
        
        String fileURI = file.getFileURI();
        
        String internalName
        		= DataManagerService.translateExternalFilename(agent, userID, fileURI);
        
        
        String PREFIX = "openml:";
        if ("error".equals(internalName) && fileURI.startsWith(PREFIX)) {
        	
        	String datasetStringID = fileURI.substring(PREFIX.length());
        	
        	try{
        		int dID = Integer.parseInt(datasetStringID);
        	
        		String dst = CoreConfiguration.getOpenmlTempFilesPath() + fileURI;
        		String downloadedPath = OpenMLAgentService.importDataset(agent, dID, dst);
        		
        		int dataSetID = 
        				DataManagerService
        				.sendRequestSaveDataSet(agent, downloadedPath, userID, "ID = "+dID+" OpenML import");

        		MetadataService.requestMetadataForDataset(agent, dataSetID, userID);
        		
        	}catch(NumberFormatException nfe){
        		agent.logSevere("Invalid number format for OpenML Dataset ID : "+datasetStringID);
        	}
        }
        
        
        
        if (!alreadyProcessed.containsKey(file.getId())) {
            alreadyProcessed.put(file.getId(), null);

        }
        DataSourceEdge fileEdge;
        fileEdge = new DataSourceEdge();
        fileEdge.setFile(true);
        fileEdge.setDataSourceId(file.getFileURI());

        if (child != null) {
            ComputationOutputBuffer<EdgeValue> buffer =
                    new NeverEndingBuffer<EdgeValue>(fileEdge);
            buffer.setData(true);
            buffer.setTarget(child);
            child.addInput(connectionName, buffer);
        }
    }

    /**
     * Parse CA
     * @param computingAgent Computing agent
     * @param batchID Id of the batch
     * @param userID Id of the owner
     * @param addOptions If options buffer should be added
     * @return Computation node of the error description
     */
    private ModelComputationNode parseComputing(IComputingAgent computingAgent,
    		int batchID, int userID, Boolean addOptions) {
    	
        agent.logInfo("Ontology Parser - Computing Agent Simple");

        if (!alreadyProcessed.containsKey(computingAgent.getId())) {
            Experiment experiment = new Experiment();
            experiment.setBatchID(batchID);
            experiment.setStatus(JPAExperimentStatus.COMPUTING.name());
            
            int experimentID =
            		DataManagerService.saveExperiment(agent, experiment);
            
            ModelComputationNode node =
            		new ModelComputationNode(
            				computationGraph, agent, experimentID);
            CAStartComputationStrategy strategy =
            		new CAStartComputationStrategy(
            				agent, batchID, experimentID, userID, node);
            node.setStartBehavior(strategy);
            alreadyProcessed.put(computingAgent.getId(),node);

        }
        
        ModelComputationNode computingNode = (ModelComputationNode)
        		alreadyProcessed.get(computingAgent.getId());
        computationGraph.addNode(computingNode);
        		
        ComputingAgent computingAgentO = (ComputingAgent) computingAgent;
        String agentType = computingAgentO.getAgentType();
        Integer model = computingAgentO.getModel();

        if (agentType != null) {
            NeverEndingBuffer<AgentTypeEdge> typeBuffer =
            		new NeverEndingBuffer<>(new AgentTypeEdge(agentType, model));
            typeBuffer.setTarget(computingNode);
            computingNode.addInput("agenttype",typeBuffer);

        }
        computingNode.setEvaluationMethod(computingAgentO.getEvaluationMethod());
        computingNode.setExpectedDuration(computingAgentO.getDuration());
        computingNode.setPriority(0);

        if (addOptions) {
            addOptionsToInputs(computingNode, computingAgentO.getOptions());
        }
        fillDataSources(computingAgentO, batchID, userID, computingNode);
        
        return computingNode;
    }

    /**
     * Parse CA, search and recommender
     * @param complex CA+Search+Recommender
     * @param batchID Id of the batch
     * @param userID Id of the owner
     * @return Computation node of CA+Search+Recommender
     */
    private ComputationNode parseComplex(CARecSearchComplex complex,
    		int batchID, int userID) {
        agent.logInfo("Ontology Parser - CARecSearchComplex");

        ComputationNode computingNode;
        List<NewOption> childOptions;
        IComputingAgent iComputingAgent = complex.getComputingAgent();
        Recommend recommenderO = complex.getRecommender();
        
        if (iComputingAgent instanceof CARecSearchComplex) {
            CARecSearchComplex complexChild = (CARecSearchComplex)iComputingAgent;
            childOptions=complexChild.getOptions();
            computingNode=parseComplex(complexChild, batchID, userID);
        
        } else {
        	
            ComputingAgent ca = (ComputingAgent)iComputingAgent;
            childOptions = ca.getOptions();
            computingNode = parseComputing(iComputingAgent,
            		batchID, userID, recommenderO==null);
        }
        
        if (recommenderO != null) {
            parseRecommender(recommenderO, computingNode, userID);
        
        } else {
            addOptionsToInputs(computingNode,complex.getOptions());
        }
        if ( iComputingAgent instanceof ComputingAgent) {
        	ComputingAgent computingAgent = (ComputingAgent) iComputingAgent;
            fillDataSources(computingAgent, batchID, userID, computingNode);
        }

        Search searchAgentO = complex.getSearch();
        if (searchAgentO != null) {
            return parseSearch(searchAgentO, computingNode,
            		complex.getErrors(), childOptions, batchID);
        }
        return computingNode;
    }

    /**
     * Parse search
     * @param search Search
     * @param child Child in the computation workflow
     * @param errors List of error source providers
     * @param childOptions Options of child
     * @param batchID Id of the batch
     * @return Computation node
     */
    private SearchComputationNode parseSearch(Search search,
    		ComputationNode child, List<ErrorSourceDescription> errors,
    		List<NewOption> childOptions, int batchID) {
    	
        agent.logInfo("Ontology Parser - Search");

        if (!alreadyProcessed.containsKey(search.getId())) {
        	
        	SearchComputationNode searchNode =
        			new SearchComputationNode(computationGraph);
            alreadyProcessed.put(search.getId(), searchNode);

        }
        SearchComputationNode searchNode =
        		(SearchComputationNode) alreadyProcessed.get(search.getId());
        searchNode.setModelClass(search.getAgentType());
        
        OptionEdge option = new OptionEdge();
        option.setOptions(childOptions);
        
        OneShotBuffer optionBuffer = new OneShotBuffer(option);
        searchNode.addInput("childoptions", optionBuffer);
        
        SearchStartComputationStrategy strategy =
        		new SearchStartComputationStrategy(agent, batchID, searchNode);
        
        searchNode.setStartBehavior(strategy);
        StandardBuffer searchBuffer = new StandardBuffer(searchNode,child);
        
        searchNode.addBufferToOutput("searchedoptions",searchBuffer);
        child.addInput("searchedoptions", searchBuffer);
        computationGraph.addNode(searchNode);
        alreadyProcessed.put(search.getId(), searchNode);
        addOptionsToInputs(searchNode, search.getOptions());
        for (ErrorSourceDescription error:errors) {
            parseErrors(error,searchNode);
        }

        return searchNode;
    }

    /**
     * Parse recommender
     * @param recommender Recommender
     * @param child Child in the computation workflow
     * @param userID Id of the user
     */
    private void parseRecommender(Recommend recommender, ComputationNode child,
                                 int userID) {
        agent.logInfo("Ontology Parser - Recommender");

        RecommenderComputationNode recNode =
        		new RecommenderComputationNode(computationGraph);
        RecommenderStartComputationStrategy recStrategy =
        		new RecommenderStartComputationStrategy(agent, userID, recNode);
        recNode.setStartBehavior(recStrategy);
        
        StandardBuffer recBuffer = new StandardBuffer(recNode,child);
        recNode.addBufferToOutput("agenttype",recBuffer);
        child.addInput("agenttype",recBuffer);

        StandardBuffer<OptionEdge> optionsBuffer =
        		new StandardBuffer<>(recNode, child);
        recNode.addBufferToOutput("options",optionsBuffer);
        child.addInput("options",optionsBuffer);
        computationGraph.addNode(recNode);
        alreadyProcessed.put(recommender.getId(), recNode);

        ComputationOutputBuffer buffer = child.getInputs().get(
        		CoreConstant.SlotContent.TRAINING_DATA.getSlotName());
        
        DataSourceEdge ds = (DataSourceEdge) buffer.getNext();
        DataSourceEdge copy = new DataSourceEdge();
        copy.setDataSourceId(ds.getDataSourceId());
        copy.setFile(ds.isFile());
        
        NeverEndingBuffer<DataSourceEdge> training = new NeverEndingBuffer<>(copy);
        training.setTarget(recNode);
        
        String trainingSlot =
        		CoreConstant.SlotContent.TRAINING_DATA.getSlotName();
        recNode.addInput(trainingSlot, training);

        for (ErrorSourceDescription error : recommender.getErrors()) {
            parseErrors(error, recNode);
        }

        List<NewOption> options = recommender.getOptions();
        addOptionsToInputs(recNode, options);
        recNode.setRecommenderClass(recommender.getAgentType());
    }

    /**
     * Parse data pre/post processing
     * @param dataProcessing Data processing
     * @param child Child in the computation workflow
     * @param batchID Id of the batch
     * @param userID Id of the user
     * @param connectionName Name of the buffer
     * @param connectionOutName Name of the output connection
     * @return Computation node
     */
    private ComputationNode parseDataProcessing(DataProcessing dataProcessing,
    		ComputationNode child, int batchID, int userID,
    		String connectionName, String connectionOutName) {
    	
        List<DataSourceDescription> dataSources =
        		dataProcessing.getDataSources();
        
        ComputationNode parent;
        if (alreadyProcessed.containsKey(dataProcessing.getId())) {
        	
             parent = alreadyProcessed.get(dataProcessing.getId());
        } else {
        	
            Experiment experiment = new Experiment();
            experiment.setBatchID(batchID);
            experiment.setStatus(JPAExperimentStatus.COMPUTING.name());
            
            int experimentID =
            		DataManagerService.saveExperiment(agent, experiment);
            
            DataProcessingComputationNode dpNode =
            		new DataProcessingComputationNode(computationGraph,
            				agent, experimentID);
            parent = dpNode;
            String agentType = dataProcessing.getAgentType();
            
            if (agentType != null) {
            	AgentTypeEdge agentTypeEdge =
            			new AgentTypeEdge(agentType, 0);
                NeverEndingBuffer<AgentTypeEdge> typeBuffer =
                		new NeverEndingBuffer<>(agentTypeEdge);
                		
                typeBuffer.setTarget(parent);
                parent.addInput("agenttype",typeBuffer);
            }

            addOptionsToInputs(parent,dataProcessing.getOptions());
            
            DataProcessingStrategy strategy = new DataProcessingStrategy(
            		agent, batchID, experimentID, userID, dpNode);
            
            parent.setStartBehavior(strategy);
            if (child != null) {
                NeverEndingBuffer<DataSourceEdge> buffer =
                        new NeverEndingBuffer<>();
                buffer.setTarget(child);
                buffer.setSource(parent);
                buffer.setTargetInput(connectionOutName);
                parent.addBufferToOutput(connectionName, buffer);
                child.addInput(connectionName, buffer);
            }

            computationGraph.addNode(parent);
            alreadyProcessed.put(dataProcessing.getId(), parent);
            
            for (DataSourceDescription datasourceI : dataSources){            	
            	parseDataSourceDescription(datasourceI, batchID, userID,
            			parent, datasourceI.getInputType(),
            			datasourceI.getOutputType());
            }
        }

        if (child != null) {
            NeverEndingBuffer<DataSourceEdge> buffer = new NeverEndingBuffer<>();
            buffer.setTarget(child);
            buffer.setSource(parent);
            buffer.setTargetInput(connectionOutName);
            parent.addBufferToOutput(connectionName, buffer);
            child.addInput(connectionName, buffer);
        }

        return parent;
    }

    /**
     * Fill all data sources
     * @param compAgent CA
     * @param batchID Id of the batch
     * @param userID Id of the user
     * @param node Computation node
     */
    private void fillDataSources(ComputingAgent compAgent,
    		int batchID, int userID, ComputationNode node) {
    	
        DataSourceDescription trainingData = compAgent.getTrainingData();
        DataSourceDescription testingData = compAgent.getTestingData();
        DataSourceDescription validationData = compAgent.getValidationData();

        if (trainingData != null) {
            parseDataSourceDescription(trainingData, batchID, userID, node,
            		trainingData.getInputType(), trainingData.getOutputType());
        }
        if (testingData != null) {
            parseDataSourceDescription(testingData, batchID, userID, node,
            		testingData.getInputType(), testingData.getOutputType());
        }
        if (validationData != null) {
            parseDataSourceDescription(validationData, batchID, userID, node,
            		validationData.getInputType(),
            		validationData.getOutputType());
        }
    }

    /**
     * Gets parsed graph
     * @return Parsed computation graph
     */
    public ComputationGraph getComputationGraph() {
        return computationGraph;
    }

    /**
     * Adds options to input buffer list
     * @param node Computation node
     * @param options Options to add
     */
    private void addOptionsToInputs(ComputationNode node,
    		List<NewOption> options) {
    	
        OptionEdge option = new OptionEdge();
        option.setOptions(options);
        OneShotBuffer optionBuffer = new OneShotBuffer(option);
        node.addInput("options", optionBuffer);
    }    
}
