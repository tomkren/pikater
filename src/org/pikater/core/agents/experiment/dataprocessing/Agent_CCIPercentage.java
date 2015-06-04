package org.pikater.core.agents.experiment.dataprocessing;

import jade.content.Concept;
import jade.content.ContentException;
import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import org.pikater.core.CoreConstant;
import org.pikater.core.agents.experiment.errorcomputing.Agent_Error;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.ExperimentOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentinfo.Slot;
import org.pikater.core.ontology.subtrees.batchdescription.DataProcessing;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.datainstance.DataInstances;
import org.pikater.core.ontology.subtrees.task.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Klara on 4.6.2015.
 */
public class Agent_CCIPercentage extends Agent_Error{

    private static final long serialVersionUID = 4679962419249103511L;

    @Override
    public List<Ontology> getOntologies() {
        List<Ontology> ontologies = new ArrayList<Ontology>();
        ontologies.add(TaskOntology.getInstance());
        ontologies.add(AgentInfoOntology.getInstance());
        ontologies.add(ExperimentOntology.getInstance());
        ontologies.add(DataOntology.getInstance());
        return ontologies;
    }

    @Override
    protected AgentInfo getAgentInfo() {
        AgentInfo agentInfo = new AgentInfo();
        agentInfo.importAgentClass(this.getClass());
        agentInfo.importOntologyClass(DataProcessing.class);

        agentInfo.setName("CCIPercentage");
        agentInfo.setDescription("Computes percent of correctly classified instances.");

        Slot labeled = new Slot("Labeled",
                CoreConstant.SlotCategory.DATA_GENERAL, "Labeled data.");
        Slot original = new Slot("Original",
                CoreConstant.SlotCategory.DATA_GENERAL, "Original data.");

        agentInfo.setInputSlots(Arrays.asList(labeled, original));

        Slot CCIpercent = new Slot("Error",
                CoreConstant.SlotCategory.DATA_GENERAL, "Percent of correctly classified instances.");

        agentInfo.setOutputSlots(Arrays.asList(CCIpercent));

        return agentInfo;
    }

    @Override
    protected void setup() {
        super.setup();

        Ontology ontology = TaskOntology.getInstance();
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchOntology(ontology.getName()),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

        addBehaviour(new AchieveREResponder(this, template) {
            private static final long serialVersionUID = 746138569142900592L;

            @Override
            protected ACLMessage prepareResultNotification(ACLMessage request,
                                                           ACLMessage response) throws FailureException {
                try {
                    Concept action = ((Action) (getContentManager()
                            .extractContent(request))).getAction();
                    if (action instanceof ExecuteTask) {
                        return respondToExecute(request);
                    } else {
                        throw new RefuseException("Invalid action requested");
                    }
                } catch (Codec.CodecException e) {
                    logException("Unknown codec: ", e);
                    throw new FailureException("Unknown codec: "
                            + e.getMessage());
                } catch (OntologyException e) {
                    logException("Unknown ontology: ", e);
                    throw new FailureException("Unknown ontology: "
                            + e.getMessage());
                } catch (ContentException e) {
                    logException("Content problem: ", e);
                    throw new FailureException("Content problem: "
                            + e.getMessage());
                } catch (FIPAException e) {
                    logException("FIPA exception when performing task", e);
                    throw new FailureException(
                            "Failed because of another agent " + e.getMessage());
                }
            }
        });

    }

    private ACLMessage respondToExecute(ACLMessage request)
            throws ContentException, FIPAException {
        logInfo("got execute");
        final ACLMessage reply = request.createReply();
        final ExecuteTask content;
        Task performed;
        try {
            content = (ExecuteTask) ((Action) getContentManager()
                    .extractContent(request)).getAction();
            performed = performTask(content.getTask());
        } catch (Codec.CodecException e) {
            logException("Failed to extract task", e);
            throw e;
        } catch (OntologyException e) {
            logException("Failed to extract task", e);
            throw e;
        }

        Result result = new Result(content, performed);
        getContentManager().fillContent(reply, result);
        reply.setPerformative(ACLMessage.INFORM);

        logInfo("responding with finished task");
        return reply;
    }

    private List<DataInstances> getDataForTask(Task t) throws FIPAException {
        List<DataInstances> res = new ArrayList<DataInstances>();
        for (Data dataI : t.getDatas().getDatas()) {
            String fname = dataI.getInternalFileName();
            ACLMessage request = makeGetDataRequest(fname);
            logInfo("sending getData for " + fname);
            ACLMessage reply = FIPAService.doFipaRequestClient(this, request);
            res.add(processGetDataResponse(reply));
            logInfo("got data for " + fname);
        }
        return res;
    }

    private Task performTask(Task t) throws FIPAException {
        logInfo("getting data");

        List<DataInstances> data = getDataForTask(t);
        logInfo("processing data");
        List<TaskOutput> outputs = processData(data);
        logInfo("returning outputs");
        t.setOutput(outputs);
        return t;
    }

    private List<TaskOutput> processData(List<DataInstances> data) {
        List<TaskOutput> res = new ArrayList<TaskOutput>();

        // compare class attribute of the two datasets
        DataInstances d1 = data.get(0);
        DataInstances d2 = data.get(1);
        int classIndex = d1.getClassIndex(); // same for both datasets

        int correct = 0;
        for (int i=0; i < Math.max(d1.getInstances().size(), d2.getInstances().size()); i++){
            org.pikater.core.ontology.subtrees.attribute.Instance inst = d1.getInstances().get(i);
            org.pikater.core.ontology.subtrees.attribute.Instance correspondingInstance = d2.getInstanceById(inst.getId());

            if (correspondingInstance != null){
                if (correspondingInstance.getValues().get(classIndex) ==
                        inst.getValues().get(classIndex) ){

                    correct ++;
                }
            }
        }

        int size = Math.min(d1.getInstances().size(), d2.getInstances().size());

        // count percentage
        double error = correct / (size/100);
        res.add(makeOutput(error, "Output"));

        return res;
    }

    private TaskOutput makeOutput(double error, String dataType) {
        Eval ev = new Eval();
        ev.setName("CCIPercentage");
        ev.setValue((float)error);
        List evs = new ArrayList();
        evs.add(ev);

        Evaluation eval = new Evaluation();
        eval.setEvaluations(evs);

        TaskOutput res = new TaskOutput();
        res.setName("Error");
        res.setValue(eval);
        res.setType(Task.InOutType.ERRORS);
        res.setDataType(dataType);
        return res;
    }

}
