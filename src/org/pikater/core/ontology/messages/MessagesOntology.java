package org.pikater.core.ontology.messages;

import org.pikater.core.ontology.batch.Batch;
import org.pikater.core.ontology.batch.SaveBatch;
import org.pikater.core.ontology.messages.metadata.AttributeMetadata;
import org.pikater.core.ontology.messages.metadata.CategoricalAttributeMetadata;
import org.pikater.core.ontology.messages.metadata.IntegerAttributeMetadata;
import org.pikater.core.ontology.messages.metadata.NumericalAttributeMetadata;
import org.pikater.core.ontology.messages.metadata.RealAttributeMetadata;
import org.pikater.core.ontology.messages.option.Interval;
import org.pikater.core.ontology.messages.option.Option;
import org.pikater.core.ontology.messages.option.Options;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

/**
 * Created by Stepan Balcar on 29.4.14.
 */
public class MessagesOntology extends BeanOntology {

	private static final long serialVersionUID = 4471377586541937606L;

	private MessagesOntology() {
        super("MessagesOntology");

        try {

        	add(SaveBatch.class);
        	add(Batch.class);
        	
            add(SendEmail.class);

        	///////////////OLD PIKATER////////////////////////
        	// messages
            add(Agent.class);
            add(Attribute.class);
            add(BoolSItem.class);
            add(CreateAgent.class);
            add(Data.class);
            add(DataInstances.class);
            add(DeleteTempFiles.class);
            add(Duration.class);
            add(Eval.class);
            add(Evaluation.class);
            add(EvaluationMethod.class);
            add(Execute.class);
            add(ExecuteExperiment.class);
            add(ExecuteParameters.class);
            add(Fitness.class);
            add(FloatSItem.class);
            add(GetAgents.class);
            add(GetAllMetadata.class);
            add(GetData.class);
            add(GetDuration.class);
            add(GetFileInfo.class);
            add(GetFiles.class);
            add(GetMetadata.class);
            add(GetOptions.class);
            add(GetParameters.class);
            add(GetSavedAgents.class);
            add(GetTheBestAgent.class);
            add(Id.class);
            add(ImportFile.class);
            add(Instance.class);
            add(Interval.class);
            add(IntSItem.class);
            add(LoadAgent.class);
            add(LoadResults.class);
            add(Metadata.class);
            add(Method.class);
            add(Option.class);
            add(Options.class);
            add(PartialResults.class);
            add(Problem.class);
            add(Recommend.class);
            add(Results.class);
            add(SaveAgent.class);
            add(SavedResult.class);
            add(SaveMetadata.class);
            add(SaveResults.class);
            add(SearchItem.class);
            add(SearchSolution.class);
            add(SetSItem.class);
            add(ShutdownDatabase.class);
            add(Solve.class);
            add(Task.class);
            add(TranslateFilename.class);
            add(UpdateMetadata.class);
            
            // metadata
            add(AttributeMetadata.class);
            add(CategoricalAttributeMetadata.class);
            add(IntegerAttributeMetadata.class);
            add(NumericalAttributeMetadata.class);
            add(RealAttributeMetadata.class);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static MessagesOntology theInstance = new MessagesOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}