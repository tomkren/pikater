package org.pikater.core.agents.system.computationDescriptionParser;

import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.*;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies.CAStartComputationStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies.FileSavingStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies.SearchStartComputationStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.edges.DataSourceEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.EdgeValue;
import org.pikater.core.agents.system.computationDescriptionParser.edges.ErrorEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.OptionEdge;
import org.pikater.core.ontology.subtrees.batchDescription.*;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser {
    private ComputationGraph computationGraph=new ComputationGraph();
    private HashMap<Integer,ComputationNode> alreadyProcessed=new HashMap<>();
    private Agent_Manager agent = null;

    public Parser(Agent_Manager agent_) {
        this.agent = agent_;
    }

    public void parseRoot(IDataSaver dataSaver) {
        agent.log("Ontology Parser - IDataSaver");
        //Ontology root is Leaf in Computation
        parseSaver(dataSaver);
    }

    private void parseSaver(IDataSaver dataSaver) {
        if (dataSaver instanceof FileDataSaver) {
            agent.log("Ontology Matched - FileDataSaver");

            FileDataSaver fileDataSaver = (FileDataSaver) dataSaver;
            DataSourceDescription dataSource = fileDataSaver.getDataSource();
            FileSaverNode saverNode=new FileSaverNode();
            saverNode.setStartBehavior(new FileSavingStrategy(agent,saverNode.getId(),1,saverNode));
            computationGraph.addNode(saverNode);
            alreadyProcessed.put(dataSaver.getId(),saverNode);
            parseDataSourceDescription(dataSource, saverNode, "file");
        } else {
            agent.logError("Ontology Parser - Error unknown IDataSaver");
        }
    }

    private void parseDataSourceDescription(DataSourceDescription dataSource, ComputationNode child, String connectionName) {
        agent.log("Ontology Parser - DataSourceDescription");

        IDataProvider dataProvider = dataSource.getDataProvider();
        this.parseDataProvider(dataProvider, child, connectionName);
    }
        
    public void parseDataProvider(IDataProvider dataProvider, ComputationNode child, String connectionName) {
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
            parent=parseComputing(computingAgent);
        }
        else if (dataProvider instanceof CARecSearchComplex)
        {
            agent.log("Ontology Matched - CARecSearchComplex");

            CARecSearchComplex complex = (CARecSearchComplex) dataProvider;
            parent=parseComplex(complex);
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
    public void parseRoots(ComputationDescription comDescription) {
        agent.log("Ontology Parser - ComputationDescription");

        List<FileDataSaver> elements = comDescription.getRootElements();
        for (FileDataSaver fileSaverI : elements) {
        	this.parseRoot(fileSaverI);
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

    public ModelComputationNode parseComputing(IComputingAgent computingAgent)
    {
        agent.log("Ontology Parser - Computing Agent Simple");

        if (!alreadyProcessed.containsKey(computingAgent.getId()))
        {
            ModelComputationNode node= new ModelComputationNode();
            CAStartComputationStrategy strategy=new CAStartComputationStrategy(agent,node.getId(),1,node);
            node.setStartBehavior(strategy);
            alreadyProcessed.put(computingAgent.getId(),node);

        }
        ModelComputationNode computingNode = (ModelComputationNode) alreadyProcessed.get(computingAgent.getId());
        computationGraph.addNode(computingNode);
        		
        ComputingAgent computingAgentO = (ComputingAgent) computingAgent;          
        computingNode.setModelClass(computingAgentO.getAgentType());       
        computingNode.setEvaluationMethod(computingAgentO.getEvaluationMethod());
        
        ArrayList<NewOption> options=new ArrayList<>();
        for (NewOption o:computingAgentO.getOptions())
        {
            options.add(o);
        }
        addOptionsToInputs(computingNode,options);
        fillDataSources(computingAgentO,computingNode);
                       
        return computingNode;
    }

    public ComputationNode parseComplex(CARecSearchComplex complex) {
        agent.log("Ontology Parser - CARecSearchComplex");

        ComputationNode computingNode;
        List<NewOption> childOptions;
        IComputingAgent iComputingAgent = complex.getComputingAgent();
        if (iComputingAgent instanceof CARecSearchComplex)
        {
            CARecSearchComplex complexChild=(CARecSearchComplex)iComputingAgent;
            childOptions=complexChild.getOptions();
            computingNode=parseComplex(complexChild);
        }
        else
        {
            ComputingAgent ca=(ComputingAgent)iComputingAgent;
            childOptions=ca.getOptions();
            computingNode=parseComputing(iComputingAgent);
        }
        addOptionsToInputs(computingNode,complex.getOptions());

        Recommend recommenderO = complex.getRecommender();
        if (recommenderO!=null)
        {
            parseRecommender(recommenderO, computingNode);
        }
        if ( iComputingAgent instanceof ComputingAgent) {
            fillDataSources((ComputingAgent) iComputingAgent, computingNode);
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
        searchNode.setModelClass(search.getSearchClass());
        
        OptionEdge option=new OptionEdge();
        option.setOptions(childOptions);
        OneShotBuffer optionBuffer=new OneShotBuffer(option);
        searchNode.addInput("childoptions",optionBuffer);

        searchNode.setStartBehavior(new SearchStartComputationStrategy(agent,searchNode.getId(),1,searchNode));
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
        StandardBuffer recBuffer=new StandardBuffer(recNode,child);
        recNode.addBufferToOutput("recommender",recBuffer);
        child.addInput("recommender",recBuffer);
        computationGraph.addNode(recNode);
        alreadyProcessed.put(recommender.getId(),recNode);
        List<NewOption> options = recommender.getOptions();

        addOptionsToInputs(recNode, options);
        recNode.setRecommenderClass(recommender.getRecommenderClass());
    }

    private void fillDataSources(ComputingAgent compAgent,ComputationNode node) {
        DataSourceDescription trainingData = compAgent.getTrainingData();
        DataSourceDescription testingData = compAgent.getTestingData();
        DataSourceDescription validationData = compAgent.getValidationData();

        if (trainingData!=null) {
            parseDataSourceDescription(trainingData, node, "training");
        }
        if (testingData!=null) {
            parseDataSourceDescription(testingData, node, "testing");
        }
        if (validationData!=null) {
            parseDataSourceDescription(validationData, node, "validation");
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
