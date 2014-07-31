package org.pikater.core.agents.system.computationDescriptionParser;

import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.*;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies.CAStartComputationStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies.FileSavingStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies.RecommenderStartComputationStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies.SearchStartComputationStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.edges.*;
import org.pikater.core.agents.system.manager.ManagerCommunicator;
import org.pikater.core.ontology.subtrees.account.User;
import org.pikater.core.ontology.subtrees.batchDescription.*;
import org.pikater.core.ontology.subtrees.experiment.Experiment;
import org.pikater.core.ontology.subtrees.experiment.experimentStatuses.ExperimentStatuses;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;

import java.util.HashMap;
import java.util.List;

public class Parser {
    private ComputationGraph computationGraph=new ComputationGraph();
    private HashMap<Integer,ComputationNode> alreadyProcessed=new HashMap<>();
    private Agent_Manager agent = null;
    private int priority;

    public Parser(Agent_Manager agent_) {
        this.agent = agent_;
    }

    public void parseRoot(IDataSaver dataSaver, int batchID) {
        agent.log("Ontology Parser - IDataSaver");
        //Ontology root is Leaf in Computation
        parseSaver(dataSaver, batchID);
    }

    private void parseSaver(IDataSaver dataSaver, int batchID) {
        if (dataSaver instanceof FileDataSaver) {
            agent.log("Ontology Matched - FileDataSaver");

            FileDataSaver fileDataSaver = (FileDataSaver) dataSaver;
            DataSourceDescription dataSource = fileDataSaver.getDataSource();
            FileSaverNode saverNode=new FileSaverNode();
            saverNode.setStartBehavior(new FileSavingStrategy(agent,saverNode.getId(),1,saverNode));
            computationGraph.addNode(saverNode);
            alreadyProcessed.put(dataSaver.getId(),saverNode);
            parseDataSourceDescription(dataSource, batchID, saverNode, "file");
        } else {
            agent.logError("Ontology Parser - Error unknown IDataSaver");
        }
    }

    private void parseDataSourceDescription(DataSourceDescription dataSource,
    		int batchID, ComputationNode child, String connectionName) {
        agent.log("Ontology Parser - DataSourceDescription");

        IDataProvider dataProvider = dataSource.getDataProvider();
        this.parseDataProvider(dataProvider, batchID, child, connectionName);
    }
        
    public void parseDataProvider(IDataProvider dataProvider,
    		int batchID, ComputationNode child, String connectionName) {
        agent.log("Ontology Parser - IDataProvider");
        ComputationNode parent;
        if (dataProvider instanceof FileDataProvider) {
            agent.log("Ontology Matched - FileDataProvider");

            FileDataProvider fileData = (FileDataProvider) dataProvider;
            this.parseFileDataProvider(fileData, child, connectionName);
            return;
        }  else if (dataProvider instanceof ComputingAgent) {
            agent.log("Ontology Matched - ComputingAgent");

            ComputingAgent computingAgent = (ComputingAgent) dataProvider;
            parent=parseComputing(computingAgent, batchID,true);
        }
        else if (dataProvider instanceof CARecSearchComplex)
        {
            agent.log("Ontology Matched - CARecSearchComplex");

            CARecSearchComplex complex = (CARecSearchComplex) dataProvider;
            parent=parseComplex(complex, batchID);
        }
        else if (dataProvider instanceof DataProcessing) {
            agent.log("Ontology Matched - DataProcessing");

            //TODO:  parseSaver(postprocessing) DataProcessing postprocessing = (DataProcessing) dataProvider;;
            return;
        } else {
            agent.log("Ontology Matched - Error unknown IDataProvider");
            return;
        }
        //handle parent - set him as file receiver
        ComputationOutputBuffer<EdgeValue> fileBuffer=new StandardBuffer<>(parent,child);
        parent.addBufferToOutput(connectionName,fileBuffer);
        child.addInput(connectionName,fileBuffer);
    }

    public void parseErrors(ErrorDescription errorDescription, ComputationNode child) {
        if (errorDescription==null) return;
        agent.log("Ontology Parser - IErrorProvider");
        ComputingAgent errorProvider=(ComputingAgent)errorDescription.getProvider();

        if (!alreadyProcessed.containsKey(errorProvider.getId()))
        {    agent.log("Error provider was not parsed at the moment parseErrors was called");
            return;
        }
        ComputationNode errorNode=alreadyProcessed.get(errorProvider.getId());
        StandardBuffer<ErrorEdge> buffer=new StandardBuffer<>(errorNode,child);
        errorNode.addBufferToOutput(errorDescription.getType(), buffer);
        child.addInput(errorDescription.getType(),buffer);
        buffer.block();
    }

    //This is the root of all parsing
    public void parseRoots(ComputationDescription comDescription, int batchID) {
        agent.log("Ontology Parser - ComputationDescription");

        ManagerCommunicator communicator = new ManagerCommunicator();
        User user = communicator.loadUser(agent, 5856); //TODO:

        this.priority = 10 * user.getPriorityMax() +
        		comDescription.getPriority();        

        List<FileDataSaver> elements = comDescription.getRootElements();
        for (FileDataSaver fileSaverI : elements) {
        	this.parseRoot(fileSaverI, batchID);
        }
    }

    //Processes a node that is in the beginning of computation - reads file
    public void parseFileDataProvider(FileDataProvider file, ComputationNode child, String connectionName) {
        agent.log("Ontology Parser - FileDataProvider");
        if (!alreadyProcessed.containsKey(file.getId()))
        {
            alreadyProcessed.put(file.getId(), null);
            DataSourceEdge fileEdge=new DataSourceEdge();
            fileEdge.setFile(true);
            fileEdge.setDataSourceId(file.getFileURI());
            ComputationOutputBuffer<EdgeValue> buffer=new NeverEndingBuffer<EdgeValue>(fileEdge);
            buffer.setTarget(child);
            child.addInput(connectionName,buffer);
        }
    }

    public ModelComputationNode parseComputing(IComputingAgent computingAgent,
    		int batchID, Boolean addOptions)
    {
        agent.log("Ontology Parser - Computing Agent Simple");

        if (!alreadyProcessed.containsKey(computingAgent.getId()))
        {
            ModelComputationNode node = new ModelComputationNode();
            CAStartComputationStrategy strategy =
            		new CAStartComputationStrategy(agent, 1, node);
            node.setStartBehavior(strategy);
            alreadyProcessed.put(computingAgent.getId(),node);

        }
        ModelComputationNode computingNode = (ModelComputationNode) alreadyProcessed.get(computingAgent.getId());
        computationGraph.addNode(computingNode);
        		
        ComputingAgent computingAgentO = (ComputingAgent) computingAgent;
        String agentType=computingAgentO.getAgentType();

        if (agentType!=null)
        {
            NeverEndingBuffer typeBuffer=new NeverEndingBuffer(new AgentTypeEdge(agentType));
            typeBuffer.setTarget(computingNode);
            computingNode.addInput("agenttype",typeBuffer);

        }
        computingNode.setEvaluationMethod(computingAgentO.getEvaluationMethod());
        computingNode.setExpectedDuration(computingAgentO.getDuration());
        computingNode.setPriority(priority);

        if (addOptions) {
            addOptionsToInputs(computingNode, computingAgentO.getOptions());
        }
        fillDataSources(computingAgentO, batchID, computingNode);
        
        // save Experiment
        Experiment experiment = new Experiment();
        experiment.setStatus(ExperimentStatuses.COMPUTING);
        experiment.setBatchID(batchID);
        
        ManagerCommunicator communicator = new ManagerCommunicator();
        int experimentID = communicator.saveExperiment(agent, experiment);
        
        computingNode.setExperimentID(experimentID);
        
        return computingNode;
    }

    public ComputationNode parseComplex(CARecSearchComplex complex, int batchID) {
        agent.log("Ontology Parser - CARecSearchComplex");

        ComputationNode computingNode;
        List<NewOption> childOptions;
        IComputingAgent iComputingAgent = complex.getComputingAgent();
        Recommend recommenderO = complex.getRecommender();
        if (iComputingAgent instanceof CARecSearchComplex)
        {
            CARecSearchComplex complexChild=(CARecSearchComplex)iComputingAgent;
            childOptions=complexChild.getOptions();
            computingNode=parseComplex(complexChild, batchID);
        }
        else
        {
            ComputingAgent ca=(ComputingAgent)iComputingAgent;
            childOptions=ca.getOptions();
            computingNode=parseComputing(iComputingAgent, batchID,recommenderO==null);
        }
        if (recommenderO!=null)
        {
            parseRecommender(recommenderO, computingNode);
        }
        else
        {
            addOptionsToInputs(computingNode,complex.getOptions());
        }
        if ( iComputingAgent instanceof ComputingAgent) {
        	ComputingAgent computingAgent = (ComputingAgent) iComputingAgent;
            fillDataSources(computingAgent, batchID, computingNode);
        }

        Search searchAgentO = complex.getSearch();
        if (searchAgentO!=null)
        {
            return parseSearch(searchAgentO, computingNode, complex.getErrors(),childOptions);
        }
        return computingNode;
    }

    public SearchComputationNode parseSearch(Search search, ComputationNode child, List<ErrorDescription> errors,List<NewOption> childOptions) {
        agent.log("Ontology Parser - Search");

        if (!alreadyProcessed.containsKey(search.getId()))
        {
            alreadyProcessed.put(search.getId(), new SearchComputationNode());

        }
        SearchComputationNode searchNode= (SearchComputationNode) alreadyProcessed.get(search.getId());
        searchNode.setModelClass(search.getAgentType());
        
        OptionEdge option=new OptionEdge();
        option.setOptions(childOptions);
        OneShotBuffer optionBuffer=new OneShotBuffer(option);
        searchNode.addInput("childoptions",optionBuffer);

        searchNode.setStartBehavior(new SearchStartComputationStrategy(agent,1,searchNode));
        StandardBuffer searchBuffer=new StandardBuffer(searchNode,child);
        searchNode.addBufferToOutput("searchedoptions",searchBuffer);
        child.addInput("searchedoptions",searchBuffer);
        computationGraph.addNode(searchNode);
        alreadyProcessed.put(search.getId(),searchNode);
        addOptionsToInputs(searchNode, search.getOptions());
        for (ErrorDescription error:errors)
        {
            parseErrors(error,searchNode);
        }

        return searchNode;
    }

    public void parseRecommender(Recommend recommender, ComputationNode child) {
        agent.log("Ontology Parser - Recommender");

        RecommenderComputationNode recNode=new RecommenderComputationNode();
        RecommenderStartComputationStrategy recStrategy=new RecommenderStartComputationStrategy(agent,1,recNode);
        recNode.setStartBehavior(recStrategy);
        StandardBuffer recBuffer=new StandardBuffer(recNode,child);
        recNode.addBufferToOutput("agenttype",recBuffer);
        child.addInput("agenttype",recBuffer);

        StandardBuffer<OptionEdge> optionsBuffer=new StandardBuffer<>(recNode,child);
        recNode.addBufferToOutput("options",optionsBuffer);
        child.addInput("options",optionsBuffer);
        computationGraph.addNode(recNode);
        alreadyProcessed.put(recommender.getId(),recNode);

        DataSourceEdge ds=(DataSourceEdge) child.getInputs().get("training").getNext();
        DataSourceEdge copy=new DataSourceEdge();
        copy.setDataSourceId(ds.getDataSourceId());
        copy.setFile(ds.isFile());
        NeverEndingBuffer<DataSourceEdge> training=new NeverEndingBuffer<>(copy);
        training.setTarget(recNode);
        recNode.addInput("training",training);

        for (ErrorDescription error:recommender.getErrors()) {
            parseErrors(error, recNode);
        }

        List<NewOption> options = recommender.getOptions();
        addOptionsToInputs(recNode, options);
        recNode.setRecommenderClass(recommender.getAgentType());
    }

    private void fillDataSources(ComputingAgent compAgent,
    		int batchID, ComputationNode node) {
        DataSourceDescription trainingData = compAgent.getTrainingData();
        DataSourceDescription testingData = compAgent.getTestingData();
        DataSourceDescription validationData = compAgent.getValidationData();

        if (trainingData!=null) {
            parseDataSourceDescription(trainingData, batchID, node, "training");
        }
        if (testingData!=null) {
            parseDataSourceDescription(testingData, batchID, node, "testing");
        }
        if (validationData!=null) {
            parseDataSourceDescription(validationData, batchID, node, "validation");
        }
    }
    
    public ComputationGraph getComputationGraph() {
        return computationGraph;
    }

    private void addOptionsToInputs(ComputationNode node,List<NewOption> options)
    {
        OptionEdge option=new OptionEdge();
        option.setOptions(options);
        OneShotBuffer optionBuffer=new OneShotBuffer(option);
        node.addInput("options",optionBuffer);
    }
}
