package org.pikater.core.ontology;

import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.management.GetAgents;
import org.pikater.core.ontology.subtrees.messages.Attribute;
import org.pikater.core.ontology.subtrees.messages.DataInstances;
import org.pikater.core.ontology.subtrees.messages.DeleteTempFiles;
import org.pikater.core.ontology.subtrees.messages.Duration;
import org.pikater.core.ontology.subtrees.messages.Execute;
import org.pikater.core.ontology.subtrees.messages.ExecuteParameters;
import org.pikater.core.ontology.subtrees.messages.Fitness;
import org.pikater.core.ontology.subtrees.messages.GetData;
import org.pikater.core.ontology.subtrees.messages.GetDuration;
import org.pikater.core.ontology.subtrees.messages.GetFileInfo;
import org.pikater.core.ontology.subtrees.messages.GetFiles;
import org.pikater.core.ontology.subtrees.messages.GetOptions;
import org.pikater.core.ontology.subtrees.messages.GetParameters;
import org.pikater.core.ontology.subtrees.messages.GetTheBestAgent;
import org.pikater.core.ontology.subtrees.messages.Id;
import org.pikater.core.ontology.subtrees.messages.ImportFile;
import org.pikater.core.ontology.subtrees.messages.Instance;
import org.pikater.core.ontology.subtrees.messages.LoadResults;
import org.pikater.core.ontology.subtrees.messages.Method;
import org.pikater.core.ontology.subtrees.messages.PartialResults;
import org.pikater.core.ontology.subtrees.messages.Problem;
import org.pikater.core.ontology.subtrees.messages.Results;
import org.pikater.core.ontology.subtrees.messages.SaveResults;
import org.pikater.core.ontology.subtrees.messages.SavedResult;
import org.pikater.core.ontology.subtrees.messages.ShutdownDatabase;
import org.pikater.core.ontology.subtrees.messages.Solve;
import org.pikater.core.ontology.subtrees.option.Interval;
import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.core.ontology.subtrees.option.Options;
import org.pikater.core.ontology.subtrees.recomend.Recommend;
import org.pikater.core.ontology.subtrees.task.Eval;
import org.pikater.core.ontology.subtrees.task.Evaluation;
import org.pikater.core.ontology.subtrees.task.EvaluationMethod;
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
            add(Fitness.class);
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