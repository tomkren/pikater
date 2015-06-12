package org.pikater.core.agents.experiment.dataprocessing.errorcomputing;

import org.pikater.core.CoreConstant;
import org.pikater.core.agents.experiment.dataprocessing.Agent_AbstractDataProcessing;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentinfo.Slot;
import org.pikater.core.ontology.subtrees.batchdescription.DataProcessing;
import org.pikater.core.ontology.subtrees.datainstance.DataInstances;
import org.pikater.core.ontology.subtrees.task.Eval;
import org.pikater.core.ontology.subtrees.task.Evaluation;
import org.pikater.core.ontology.subtrees.task.Task;
import org.pikater.core.ontology.subtrees.task.TaskOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Klara on 4.6.2015.
 */
public class Agent_Accuracy extends Agent_AbstractDataProcessing {

    private static final long serialVersionUID = 4679962419249103511L;

    @Override
    protected AgentInfo getAgentInfo() {
        AgentInfo agentInfo = new AgentInfo();
        agentInfo.importAgentClass(this.getClass());
        agentInfo.importOntologyClass(DataProcessing.class);

        agentInfo.setName("CCIPercentage");
        agentInfo.setDescription("Computes percent of correctly classified instances.");

        Slot labeled = new Slot(CoreConstant.SlotContent.DATA_TO_LABEL);
        Slot original = new Slot(CoreConstant.SlotContent.TRAINING_DATA);

        agentInfo.setInputSlots(Arrays.asList(labeled, original));

        Slot CCIpercent = new Slot("Error",
                CoreConstant.SlotCategory.ERROR, "Percent of correctly classified instances.");

        agentInfo.setOutputSlots(Arrays.asList(CCIpercent));

        return agentInfo;
    }

    protected List<TaskOutput> processData(List<DataInstances> data) {
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
        System.out.println("ERROR: " + error);
        res.add(makeOutput(error, "Output"));

        return res;
    }

    private TaskOutput makeOutput(double error, String dataType) {
        Eval ev = new Eval();
        ev.setName("Accuracy");
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
