package org.pikater.core.ontology.actions;

import org.pikater.core.ontology.messages.Agent;
import org.pikater.core.ontology.messages.Attribute;
import org.pikater.core.ontology.messages.Data;
import org.pikater.core.ontology.messages.DataInstances;
import org.pikater.core.ontology.messages.DeleteTempFiles;
import org.pikater.core.ontology.messages.Duration;
import org.pikater.core.ontology.messages.Eval;
import org.pikater.core.ontology.messages.Evaluation;
import org.pikater.core.ontology.messages.EvaluationMethod;
import org.pikater.core.ontology.messages.Execute;
import org.pikater.core.ontology.messages.ExecuteParameters;
import org.pikater.core.ontology.messages.Fitness;
import org.pikater.core.ontology.messages.GetAgents;
import org.pikater.core.ontology.messages.GetData;
import org.pikater.core.ontology.messages.GetDuration;
import org.pikater.core.ontology.messages.GetFileInfo;
import org.pikater.core.ontology.messages.GetFiles;
import org.pikater.core.ontology.messages.GetOptions;
import org.pikater.core.ontology.messages.GetParameters;
import org.pikater.core.ontology.messages.GetTheBestAgent;
import org.pikater.core.ontology.messages.Id;
import org.pikater.core.ontology.messages.ImportFile;
import org.pikater.core.ontology.messages.Instance;
import org.pikater.core.ontology.messages.LoadResults;
import org.pikater.core.ontology.messages.Method;
import org.pikater.core.ontology.messages.PartialResults;
import org.pikater.core.ontology.messages.Problem;
import org.pikater.core.ontology.messages.Recommend;
import org.pikater.core.ontology.messages.Results;
import org.pikater.core.ontology.messages.SaveResults;
import org.pikater.core.ontology.messages.SavedResult;
import org.pikater.core.ontology.messages.ShutdownDatabase;
import org.pikater.core.ontology.messages.Solve;
import org.pikater.core.ontology.messages.Task;
import org.pikater.core.ontology.messages.option.Interval;
import org.pikater.core.ontology.messages.option.Option;
import org.pikater.core.ontology.messages.option.Options;

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