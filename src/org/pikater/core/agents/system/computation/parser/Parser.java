package org.pikater.core.agents.system.computation.parser;

import org.pikater.core.CoreConstant;
import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computation.graph.*;
import org.pikater.core.agents.system.computation.graph.edges.*;
import org.pikater.core.agents.system.computation.graph.strategies.*;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.subtrees.batchDescription.*;
import org.pikater.core.ontology.subtrees.experiment.Experiment;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.shared.database.jpa.status.JPAExperimentStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private ComputationGraph computationGraph =
    		new ComputationGraph();
    private Map<Integer,ComputationNode> alreadyProcessed =
    		new HashMap<Integer,ComputationNode>();
    private Agent_Manager agent = null;
    private int priority;

    public Parser(Agent_Manager agent) {
        this.agent = agent;
    }

    public void parseRoot(IDataSaver dataSaver, int batchID, int userID) {
        agent.logInfo("Ontology Parser - IDataSaver");
        //Ontology root is Leaf in Computation
        parseSaver(dataSaver, batchID, userID);
    }

    private void parseSaver(IDataSaver dataSaver, int batchID, int userID) {
        if (dataSaver instanceof FileDataSaver) {
            agent.logInfo("Ontology Matched - FileDataSaver");

            FileDataSaver fileDataSaver = (FileDataSaver) dataSaver;
            DataSourceDescription dataSource = fileDataSaver.getDataSource();
            FileSaverNode saverNode=new FileSaverNode(computationGraph);
            //saverNode.setStartBehavior(new FileSavingStrategy(agent,saverNode.getId(),batchID,saverNode));
            //computationGraph.addNode(saverNode);
            //alreadyProcessed.put(dataSaver.getId(),saverNode);
            parseDataSourceDescription(dataSource, batchID, userID, saverNode, "file");
        } else {
            agent.logSevere("Ontology Parser - Error unknown IDataSaver");
        }
    }

    private void parseDataSourceDescription(DataSourceDescription dataSource,
    		int batchID, int userID, ComputationNode child, String connectionName, 
    		String connectionOutName) {
        agent.logInfo("Ontology Parser - DataSourceDescription");

        IDataProvider dataProvider = dataSource.getDataProvider();
        this.parseDataProvider(dataProvider, batchID, userID, child, 
        		connectionName, connectionOutName);
    }
    
    private void parseDataSourceDescription(DataSourceDescription dataSource,
    		int batchID, int userID, ComputationNode child, String connectionName){
    	parseDataSourceDescription(dataSource, batchID, userID, child, 
    			connectionName, null);
    }
    
    public void parseDataProvider(IDataProvider dataProvider, int batchID,
    		int userID, ComputationNode child, String connectionName,
    		String connectionOutName) {
        agent.logInfo("Ontology Parser - IDataProvider");
        ComputationNode parent;
        if (dataProvider instanceof FileDataProvider) {
            agent.logInfo("Ontology Matched - FileDataProvider");

            FileDataProvider fileData = (FileDataProvider) dataProvider;
            this.parseFileDataProvider(fileData, child, connectionName);
            return;
        }  else if (dataProvider instanceof ComputingAgent) {
            agent.logInfo("Ontology Matched - ComputingAgent");

            ComputingAgent computingAgent = (ComputingAgent) dataProvider;
            parent=parseComputing(computingAgent, batchID, userID, true);
        }
        else if (dataProvider instanceof CARecSearchComplex)
        {
            agent.logInfo("Ontology Matched - CARecSearchComplex");

            CARecSearchComplex complex = (CARecSearchComplex) dataProvider;
            parent=parseComplex(complex, batchID, userID);
        }
        else if (dataProvider instanceof DataProcessing) {
            agent.logInfo("Ontology Matched - DataProcessing");
            
            parseDataProcessing((DataProcessing) dataProvider, child, batchID, userID, connectionName, connectionOutName);
            return;
        } else {
            agent.logInfo("Ontology Matched - Error unknown IDataProvider");
            return;
        }
        //handle parent - set him as file receiver
        ComputationOutputBuffer<EdgeValue> fileBuffer=new StandardBuffer<>(parent,child);
        fileBuffer.setData(true);        
        parent.addBufferToOutput(connectionName,fileBuffer);
        child.addInput(connectionName,fileBuffer);
    }

    
    public void parseErrors(ErrorSourceDescription errorDescription, ComputationNode child) {
        if (errorDescription==null)
        {
        	return;
        }
        agent.logInfo("Ontology Parser - IErrorProvider");
        ComputingAgent errorProvider=(ComputingAgent)errorDescription.getProvider();

        if (!alreadyProcessed.containsKey(errorProvider.getId()))
        {    agent.logInfo("Error provider was not parsed at the moment parseErrors was called");
            return;
        }
        ComputationNode errorNode=alreadyProcessed.get(errorProvider.getId());
        StandardBuffer<ErrorEdge> buffer=new StandardBuffer<>(errorNode,child);
        errorNode.addBufferToOutput(errorDescription.getOutputType(), buffer);
        child.addInput(errorDescription.getOutputType(),buffer);
        buffer.block();
    }

    //This is the root of all parsing
    public void parseRoots(ComputationDescription comDescription, int batchID, int userID) {
        agent.logInfo("Ontology Parser - ComputationDescription");

        List<FileDataSaver> elements = comDescription.getRootElements();
        for (FileDataSaver fileSaverI : elements) {
        	this.parseRoot(fileSaverI, batchID, userID);
        }
    }

    //Processes a node that is in the beginning of computation - reads file
    public void parseFileDataProvider(FileDataProvider file, ComputationNode child, String connectionName) {
        agent.logInfo("Ontology Parser - FileDataProvider");
        if (!alreadyProcessed.containsKey(file.getId()))
        {
            alreadyProcessed.put(file.getId(), null);

        }
        DataSourceEdge fileEdge;
        fileEdge=new DataSourceEdge();
        fileEdge.setFile(true);
        fileEdge.setDataSourceId(file.getFileURI());
        ComputationOutputBuffer<EdgeValue> buffer=new NeverEndingBuffer<EdgeValue>(fileEdge);
        buffer.setData(true);
        buffer.setTarget(child);
        child.addInput(connectionName,buffer);
    }

    public ModelComputationNode parseComputing(IComputingAgent computingAgent,
    		int batchID, int userID, Boolean addOptions)
    {
        agent.logInfo("Ontology Parser - Computing Agent Simple");

        if (!alreadyProcessed.containsKey(computingAgent.getId()))
        {
            Experiment experiment = new Experiment();
            experiment.setBatchID(batchID);
            experiment.setStatus(JPAExperimentStatus.COMPUTING.name());
            
            int experimentID = DataManagerService.saveExperiment(agent, experiment);
            
            ModelComputationNode node = new ModelComputationNode(computationGraph, agent, experimentID);
            CAStartComputationStrategy strategy =
            		new CAStartComputationStrategy(agent, batchID, experimentID, userID, node);
            node.setStartBehavior(strategy);
            alreadyProcessed.put(computingAgent.getId(),node);

        }
        ModelComputationNode computingNode = (ModelComputationNode) alreadyProcessed.get(computingAgent.getId());
        computationGraph.addNode(computingNode);
        		
        ComputingAgent computingAgentO = (ComputingAgent) computingAgent;
        String agentType = computingAgentO.getAgentType();
        Integer model = computingAgentO.getModel();

        if (agentType!=null)
        {
            NeverEndingBuffer<AgentTypeEdge> typeBuffer=new NeverEndingBuffer<AgentTypeEdge>(new AgentTypeEdge(agentType, model));
            typeBuffer.setTarget(computingNode);
            computingNode.addInput("agenttype",typeBuffer);

        }
        computingNode.setEvaluationMethod(computingAgentO.getEvaluationMethod());
        computingNode.setExpectedDuration(computingAgentO.getDuration());
        computingNode.setPriority(priority);

        if (addOptions) {
            addOptionsToInputs(computingNode, computingAgentO.getOptions());
        }
        fillDataSources(computingAgentO, batchID, userID, computingNode);
        
        return computingNode;
    }

    public ComputationNode parseComplex(CARecSearchComplex complex, int batchID, int userID) {
        agent.logInfo("Ontology Parser - CARecSearchComplex");

        ComputationNode computingNode;
        List<NewOption> childOptions;
        IComputingAgent iComputingAgent = complex.getComputingAgent();
        Recommend recommenderO = complex.getRecommender();
        if (iComputingAgent instanceof CARecSearchComplex)
        {
            CARecSearchComplex complexChild=(CARecSearchComplex)iComputingAgent;
            childOptions=complexChild.getOptions();
            computingNode=parseComplex(complexChild, batchID, userID);
        }
        else
        {
            ComputingAgent ca=(ComputingAgent)iComputingAgent;
            childOptions=ca.getOptions();
            computingNode=parseComputing(iComputingAgent, batchID, userID, recommenderO==null);
        }
        if (recommenderO!=null)
        {
            parseRecommender(recommenderO, computingNode, batchID, userID);
        }
        else
        {
            addOptionsToInputs(computingNode,complex.getOptions());
        }
        if ( iComputingAgent instanceof ComputingAgent) {
        	ComputingAgent computingAgent = (ComputingAgent) iComputingAgent;
            fillDataSources(computingAgent, batchID, userID, computingNode);
        }

        Search searchAgentO = complex.getSearch();
        if (searchAgentO!=null)
        {
            return parseSearch(searchAgentO, computingNode, complex.getErrors(),childOptions,batchID);
        }
        return computingNode;
    }

    public SearchComputationNode parseSearch(Search search, ComputationNode child,
    		List<ErrorSourceDescription> errors, List<NewOption> childOptions,
    		int batchID) {
        agent.logInfo("Ontology Parser - Search");

        if (!alreadyProcessed.containsKey(search.getId()))
        {
        	SearchComputationNode searchNode =
        			new SearchComputationNode(computationGraph);
            alreadyProcessed.put(search.getId(), searchNode);

        }
        SearchComputationNode searchNode = (SearchComputationNode) alreadyProcessed.get(search.getId());
        searchNode.setModelClass(search.getAgentType());
        
        OptionEdge option = new OptionEdge();
        option.setOptions(childOptions);
        OneShotBuffer optionBuffer=new OneShotBuffer(option);
        searchNode.addInput("childoptions",optionBuffer);
        
        SearchStartComputationStrategy strategy =
        		new SearchStartComputationStrategy(agent, batchID, searchNode);
        
        searchNode.setStartBehavior(strategy);
        StandardBuffer searchBuffer=new StandardBuffer(searchNode,child);
        searchNode.addBufferToOutput("searchedoptions",searchBuffer);
        child.addInput("searchedoptions",searchBuffer);
        computationGraph.addNode(searchNode);
        alreadyProcessed.put(search.getId(),searchNode);
        addOptionsToInputs(searchNode, search.getOptions());
        for (ErrorSourceDescription error:errors)
        {
            parseErrors(error,searchNode);
        }

        return searchNode;
    }

    public void parseRecommender(Recommend recommender, ComputationNode child,
    		int batchID, int userID) {
        agent.logInfo("Ontology Parser - Recommender");

        RecommenderComputationNode recNode = new RecommenderComputationNode(computationGraph);
        RecommenderStartComputationStrategy recStrategy =
        		new RecommenderStartComputationStrategy(agent, batchID, userID, recNode);
        recNode.setStartBehavior(recStrategy);
        StandardBuffer recBuffer=new StandardBuffer(recNode,child);
        recNode.addBufferToOutput("agenttype",recBuffer);
        child.addInput("agenttype",recBuffer);

        StandardBuffer<OptionEdge> optionsBuffer=new StandardBuffer<>(recNode,child);
        recNode.addBufferToOutput("options",optionsBuffer);
        child.addInput("options",optionsBuffer);
        computationGraph.addNode(recNode);
        alreadyProcessed.put(recommender.getId(),recNode);

        DataSourceEdge ds=(DataSourceEdge) child.getInputs().get(CoreConstant.SlotContent.TRAINING_DATA.getSlotName()).getNext();
        DataSourceEdge copy=new DataSourceEdge();
        copy.setDataSourceId(ds.getDataSourceId());
        copy.setFile(ds.isFile());
        NeverEndingBuffer<DataSourceEdge> training=new NeverEndingBuffer<>(copy);
        training.setTarget(recNode);
        recNode.addInput(CoreConstant.SlotContent.TRAINING_DATA.getSlotName(), training);

        for (ErrorSourceDescription error:recommender.getErrors()) {
            parseErrors(error, recNode);
        }

        List<NewOption> options = recommender.getOptions();
        addOptionsToInputs(recNode, options);
        recNode.setRecommenderClass(recommender.getAgentType());
    }

    private ComputationNode parseDataProcessing(DataProcessing dataProcessing,
    		ComputationNode child, int batchID, int userID, String connectionName,
    		String connectionOutName) {
    	
        List<DataSourceDescription> dataSources= dataProcessing.getDataSources();
        
        ComputationNode parent;
        if (alreadyProcessed.containsKey(dataProcessing.getId()))
        {
             parent = alreadyProcessed.get(dataProcessing.getId());
        }
        else {
            Experiment experiment = new Experiment();
            experiment.setBatchID(batchID);
            experiment.setStatus(JPAExperimentStatus.COMPUTING.name());
            
            int experimentID = DataManagerService.saveExperiment(agent, experiment);
            
            DataProcessingComputationNode dpNode = new DataProcessingComputationNode(
            		computationGraph, agent, experimentID);
            dpNode.setNumberOfInputs(dataSources.size());
            parent=dpNode;
            String agentType = dataProcessing.getAgentType();
            if (agentType!=null)
            {
                NeverEndingBuffer<AgentTypeEdge> typeBuffer=new NeverEndingBuffer<AgentTypeEdge>(new AgentTypeEdge(agentType, 0));
                typeBuffer.setTarget(parent);
                parent.addInput("agenttype",typeBuffer);
            }

            addOptionsToInputs(parent,dataProcessing.getOptions());
            
            DataProcessingStrategy strategy = new DataProcessingStrategy(
            		agent, batchID, experimentID, userID, dpNode);
            
            parent.setStartBehavior(strategy);
            NeverEndingBuffer<DataSourceEdge> buffer=new NeverEndingBuffer<>();
            buffer.setTarget(child);
            buffer.setSource(parent);
            buffer.setTargetInput(connectionOutName);
            parent.addBufferToOutput(connectionName,buffer);
            child.addInput(connectionName,buffer);

            computationGraph.addNode(parent);
            alreadyProcessed.put(dataProcessing.getId(),parent);
            /* for (int i=0;i<dataSources.size();i++) {
                DataSourceDescription ds = dataProcessing.getDataSources().get(i);
                parseDataSourceDescription(ds, batchID, userID, parent, "data"+i);
            }
            */            
            
            for (DataSourceDescription ds : dataSources){            	
            	parseDataSourceDescription(ds, batchID, userID, parent, ds.getInputType(), ds.getOutputType());
            }
        }
        
        NeverEndingBuffer<DataSourceEdge> buffer=new NeverEndingBuffer<>();
        buffer.setTarget(child);
        buffer.setSource(parent);
        buffer.setTargetInput(connectionOutName);
        parent.addBufferToOutput(connectionName,buffer);
        child.addInput(connectionName,buffer);

        return parent;
    }
    
    private void fillDataSources(ComputingAgent compAgent,
    		int batchID, int userID, ComputationNode node) {
        DataSourceDescription trainingData = compAgent.getTrainingData();
        DataSourceDescription testingData = compAgent.getTestingData();
        DataSourceDescription validationData = compAgent.getValidationData();

        if (trainingData!=null) {
            parseDataSourceDescription(trainingData, batchID, userID, node, trainingData.getInputType(), trainingData.getOutputType());
        }
        if (testingData!=null) {
            parseDataSourceDescription(testingData, batchID, userID, node, testingData.getInputType(), testingData.getOutputType());
        }
        if (validationData!=null) {
            parseDataSourceDescription(validationData, batchID, userID, node, validationData.getInputType(), validationData.getOutputType());
        }
    }
    
    public ComputationGraph getComputationGraph() {
        return computationGraph;
    }

    private void addOptionsToInputs(ComputationNode node,List<NewOption> options)
    {
        OptionEdge option = new OptionEdge();
        option.setOptions(options);
        OneShotBuffer optionBuffer=new OneShotBuffer(option);
        node.addInput("options",optionBuffer);
    }    
}
