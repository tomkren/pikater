package org.pikater.core.agents.experiment.computing;

import org.pikater.core.CoreConstant;
import org.pikater.core.ontology.subtrees.attribute.Instance;
import org.pikater.core.ontology.subtrees.batchdescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.batchdescription.evaluationmethod.CrossValidation;
import org.pikater.core.ontology.subtrees.datainstance.DataInstances;
import org.pikater.core.ontology.subtrees.newoption.NewOptions;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.core.ontology.subtrees.newoption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.task.Eval;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.RBFNetwork;
import weka.core.Instances;

import java.util.*;

/**
 * 
 * Abstract class for implementation Computing Weka Agent
 *
 */
public abstract class Agent_WekaAbstractCA extends Agent_ComputingAgent {

	private static final long serialVersionUID = -3594051562022044000L;

	// Weka classifier
	protected Classifier classifier;

	/**
	 * Get the agent type - classifier
	 */
	@Override
	public String getAgentType() {
		// get default classifier
		return RBFNetwork.class.getName();
	}
	
	/**
	 * Get Weka classifier
	 */
	protected abstract Classifier createClassifier();

	/**
	 * Training process of the Machine Learning
	 * 
	 */
	@Override
	public Date train(org.pikater.core.ontology.subtrees.task.Evaluation evaluation) throws Exception {
		working = true;
		
		this.logStartTask();
		
		logInfo("Training...");
		logInfo("Options: " + getOptions());
		
        classifier = createClassifier();
		if (classifier == null) {
			throw new IllegalStateException(getLocalName() +
					": Weka classifier class hasn't been created.");
		}
		if (options.length > 0) {
            //this is destructive, the options array will be emptied
			classifier.setOptions(options);
		}
		
		long start = System.currentTimeMillis();
		classifier.buildClassifier(train);
		long end = System.currentTimeMillis();
		long duration = end - start;
		
		if (duration < 1) {
			duration = 1;
		} 
		
		List<Eval> evals = new ArrayList<Eval>();
		
		Eval startEval = new Eval();
		startEval.setName("start");
		startEval.setValue(start);
		evals.add(startEval);
		
		Eval durationEval = new Eval();
		durationEval.setName("duration");
		durationEval.setValue(duration);
		evals.add(durationEval);

		this.lastStartDate = new Date(start);
		this.lastDuration = duration;
		logInfo("start: " + new Date(start) + " : duration: " + duration);
		
		// change agent state
		state = States.TRAINED;
		options = classifier.getOptions();

		// write out net parameters
		logOptions();

		working = false;
		
		// add eval to Evaluation
		List<Eval> evaluationsNew = evaluation.getEvaluations();
		
		for (Eval evalI : evals) {
			evaluationsNew.add(evalI);
		}
		
		evaluation.setEvaluations(evaluationsNew);
		
		return new Date(start);
	}


	/**
	 * Testing process of the Machine Learning
	 * 
	 * @throws Exception
	 */
	private Evaluation test(EvaluationMethod evaluationMethod
			) throws Exception {
		
		working = true;
		logInfo("Testing...");

		// evaluate classifier and print some statistics
		Evaluation eval = new Evaluation(train);
        logInfo("Evaluation method: \t");
		
        String agentType = evaluationMethod.getAgentType();
		if (agentType.equals(CrossValidation.class.getName()) ) {
			
			// TODO read default value from file (if necessary)
			int folds = 5;
			if (evaluationMethod.getOptions() != null) {
				
				NewOptions options =
						new NewOptions(evaluationMethod.getOptions());
				NewOption optionF = options.fetchOptionByName("F");
				if (optionF != null) {
					IntegerValue valueF = (IntegerValue)
							optionF.toSingleValue().getCurrentValue();
					folds = valueF.getValue();
				}				
			}
			
			logInfo(folds + "-fold cross validation.");
			eval.crossValidateModel(
					classifier,
					test,
					folds, new Random(1));
		} else {
			logInfo("Standard weka evaluation.");
			eval.evaluateModel(classifier, test);
		}
				
		logInfo("Error rate: " + eval.errorRate()+" ");
		logInfo(eval.toSummaryString(getLocalName() + " agent: "
				+ "\nResults\n=======\n", false));

		working = false;
		return eval;
	}

	@Override
	public void evaluateCA(EvaluationMethod evaluationMethod,
			org.pikater.core.ontology.subtrees.task.Evaluation evaluation
			) throws Exception{
		
		float defaultValue = Float.MAX_VALUE;
		Evaluation eval = test(evaluationMethod);
		
		List<Eval> evaluations = evaluation.getEvaluations();
		
		Eval errorRateEval = new Eval();
		errorRateEval.setName(CoreConstant.Error.ERROR_RATE.name());
		errorRateEval.setValue((float) eval.errorRate());
		evaluations.add(errorRateEval);
		
		Eval kappaStatisticEval = new Eval();
		kappaStatisticEval.setName(CoreConstant.Error.KAPPA_STATISTIC.name());
		try {
			kappaStatisticEval.setValue((float) eval.kappa());
		} catch (Exception e) {
			kappaStatisticEval.setValue(defaultValue);
		}
		evaluations.add(kappaStatisticEval);

		Eval meanAbsoluteError = new Eval();
		meanAbsoluteError.setName(CoreConstant.Error.MEAN_ABSOLUTE.name());
		try {
			meanAbsoluteError.setValue((float) eval.meanAbsoluteError());
		} catch (Exception e) {
			meanAbsoluteError.setValue(defaultValue);
		}
		evaluations.add(meanAbsoluteError);

		Eval relativeAbsoluteError = new Eval();
		relativeAbsoluteError.setName(
				CoreConstant.Error.RELATIVE_ABSOLUTE.name());
		try {
			relativeAbsoluteError.setValue(
					(float) eval.relativeAbsoluteError());
		} catch (Exception e) {
			relativeAbsoluteError.setValue(defaultValue);
		}
		evaluations.add(relativeAbsoluteError);
		
		Eval meanSquaredEval = new Eval();
		meanSquaredEval.setName(CoreConstant.Error.ROOT_MEAN_SQUARED.name());
		try {
			meanSquaredEval.setValue((float) eval.rootMeanSquaredError());
		} catch (Exception e) {
			meanSquaredEval.setValue(defaultValue);
		}
		evaluations.add(meanSquaredEval);

		Eval relativeSquaredEval = new Eval();
		relativeSquaredEval.setName(
				CoreConstant.Error.ROOT_RELATIVE_SQUARED.name());
		try {
			relativeSquaredEval.setValue(
					(float) eval.rootRelativeSquaredError());
		} catch (Exception e) {
			relativeSquaredEval.setValue(defaultValue);
		}
		evaluations.add(relativeSquaredEval);
		
		evaluation.setEvaluations(evaluations);		
	}

	@Override
	public DataInstances getPredictions(Instances test,
			DataInstances ontoTest) {

		Double[] pre = new Double[test.numInstances()];
		for (int i = 0; i < test.numInstances(); i++) {
			try {
				pre[i] = classifier.classifyInstance(test.instance(i));
			} catch (Exception e) {
				pre[i] = Double.NaN;
			}
		}

		ontoTest.insertClassColumn(Arrays.asList(pre));

		return ontoTest;
	}
}
