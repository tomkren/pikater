package org.pikater.core.ontology;

import org.pikater.core.ontology.subtrees.attribute.Attribute;
import org.pikater.core.ontology.subtrees.attribute.Instance;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.data.GetData;
import org.pikater.core.ontology.subtrees.dataInstance.DataInstances;
import org.pikater.core.ontology.subtrees.database.ShutdownDatabase;
import org.pikater.core.ontology.subtrees.duration.Duration;
import org.pikater.core.ontology.subtrees.duration.GetDuration;
import org.pikater.core.ontology.subtrees.experiment.Solve;
import org.pikater.core.ontology.subtrees.file.DeleteTempFiles;
import org.pikater.core.ontology.subtrees.file.GetFileInfo;
import org.pikater.core.ontology.subtrees.file.GetFiles;
import org.pikater.core.ontology.subtrees.file.ImportFile;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.management.GetAgents;
import org.pikater.core.ontology.subtrees.management.GetTheBestAgent;
import org.pikater.core.ontology.subtrees.oldPikaterMessages.Method;
import org.pikater.core.ontology.subtrees.oldPikaterMessages.Problem;
import org.pikater.core.ontology.subtrees.option.GetOptions;
import org.pikater.core.ontology.subtrees.option.Interval;
import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.core.ontology.subtrees.option.Options;
import org.pikater.core.ontology.subtrees.recomend.Recommend;
import org.pikater.core.ontology.subtrees.result.LoadResults;
import org.pikater.core.ontology.subtrees.result.PartialResults;
import org.pikater.core.ontology.subtrees.result.Results;
import org.pikater.core.ontology.subtrees.result.SaveResults;
import org.pikater.core.ontology.subtrees.result.SavedResult;
import org.pikater.core.ontology.subtrees.search.ExecuteParameters;
import org.pikater.core.ontology.subtrees.search.GetParameters;
import org.pikater.core.ontology.subtrees.task.Eval;
import org.pikater.core.ontology.subtrees.task.Evaluation;
import org.pikater.core.ontology.subtrees.task.EvaluationMethod;
import org.pikater.core.ontology.subtrees.task.Execute;
import org.pikater.core.ontology.subtrees.task.Id;
import org.pikater.core.ontology.subtrees.task.Task;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

/**
 * Created by Stepan Balcar on 29.4.14.
 */
public class MessagesOntology extends BeanOntology {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1829196889268218770L;

	private MessagesOntology() {
        super("MessagesOntology");

        try {

        	///////////////OLD PIKATER////////////////////////
        	// messages
            add(Agent.class);
            add(Attribute.class);
            add(Data.class);
            add(DataInstances.class);
            add(DeleteTempFiles.class);
            add(Duration.class);
            add(Eval.class);
            add(Evaluation.class);
            add(EvaluationMethod.class);
            add(Execute.class);
            add(ExecuteParameters.class);
            add(GetAgents.class);
            add(GetData.class);
            add(GetDuration.class);
            add(GetFileInfo.class);
            add(GetFiles.class);
            add(GetOptions.class);
            add(GetParameters.class);
            add(GetTheBestAgent.class);
            add(Id.class);
            add(ImportFile.class);
            add(Instance.class);
            add(Interval.class);
            add(LoadResults.class);
            add(Method.class);
            add(Option.class);
            add(Options.class);
            add(PartialResults.class);
            add(Problem.class);
            add(Recommend.class);
            add(Results.class);
            add(SavedResult.class);
            add(SaveResults.class);
            add(ShutdownDatabase.class);
            add(Solve.class);
            add(Task.class);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static MessagesOntology theInstance = new MessagesOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}