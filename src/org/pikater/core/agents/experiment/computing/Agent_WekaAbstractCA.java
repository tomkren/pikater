package org.pikater.core.agents.experiment.computing;

import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

import org.pikater.core.CoreConstant;
import org.pikater.core.ontology.subtrees.attribute.Instance;
import org.pikater.core.ontology.subtrees.batchDescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.batchDescription.evaluationMethod.CrossValidation;
import org.pikater.core.ontology.subtrees.dataInstance.DataInstances;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.task.Eval;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.RBFNetwork;
import weka.core.Instances;

import java.util.Date;
import java.util.Random;

public abstract class Agent_WekaAbstractCA extends Agent_ComputingAgent {

	private static final long serialVersionUID = -3594051562022044000L;

	protected Classifier classifier;

	@Override
	public String getAgentType()
	{
		//TODO: get method and sets the classifier???
		return RBFNetwork.class.getName();
	}
	
	protected abstract Classifier createClassifier();

	@Override
	public Date train(org.pikater.core.ontology.subtrees.task.Evaluation evaluation) throws Exception {
		working = true;
		
		this.logStartTask();
		
		logInfo("Training...");
		logInfo("Options: " + getOptions());
        classifier=createClassifier();
		if(classifier == null)
		{
			throw new IllegalStateException(getLocalName() + ": Weka classifier class hasn't been created (Wrong type?).");
		}
		if (options.length > 0)
		{
            //this is destructive, the options array will be emptied
			classifier.setOptions(options);
		}
		
		long start = System.currentTimeMillis();
		classifier.buildClassifier(train);
		long end = System.currentTimeMillis();
		long duration = end - start;
		if (duration < 1) { duration = 1; } 
		
		List evals = new ArrayList();
		
		Eval s = new Eval();
		s.setName("start");
		s.setValue(start);
		evals.add(s);
		
		Eval d = new Eval();
		d.setName("duration");
		d.setValue(duration);
		evals.add(d);

		this.lastStartDate=new Date(start);
		this.lastDuration=duration;
		logInfo("start: " + new Date(start) + " : duration: " + duration);
		
		state = states.TRAINED; // change agent state
		options = classifier.getOptions();

		// write out net parameters
		logOptions();

		working = false;
		
		// add evals to Evaluation
		java.util.List<Eval> evaluations_new = evaluation.getEvaluations();
		
		Iterator itr = evals.iterator();
		while (itr.hasNext()) {
			Eval eval = (Eval) itr.next();
			evaluations_new.add(eval);
		}
		
		evaluation.setEvaluations(evaluations_new);
		
		return new Date(start);
	}


	private Evaluation test(EvaluationMethod evaluation_method) throws Exception{
		working = true;
		logInfo("Testing...");

		// evaluate classifier and print some statistics
		Evaluation eval = new Evaluation(train);
        logInfo("Evaluation method: \t");
		
		if (evaluation_method.getAgentType().equals(CrossValidation.class.getName()) ){
			
			int folds = 5; // TODO read default value from file (if necessary)
			if (evaluation_method.getOptions() != null) {
				
				NewOptions options = new NewOptions(evaluation_method.getOptions());
				NewOption optionF = options.fetchOptionByName("F");
				if (optionF != null){
					IntegerValue valueF = (IntegerValue) optionF.toSingleValue().getCurrentValue();
					folds = valueF.getValue();
				}				
			}
			
			logInfo(folds + "-fold cross validation.");
			eval.crossValidateModel(
					classifier,
					test,
					folds, new Random(1));
		}
		else { // name = Standard
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
	public void evaluateCA(EvaluationMethod evaluation_method,
			org.pikater.core.ontology.subtrees.task.Evaluation evaluation) throws Exception{
		
		float default_value = Float.MAX_VALUE;
		Evaluation eval = test(evaluation_method);
		
		java.util.List<Eval> evaluations = evaluation.getEvaluations();
		//List evaluations = new ArrayList();
		Eval ev = new Eval();
		ev.setName(CoreConstant.Error.ERROR_RATE.name());
		ev.setValue((float) eval.errorRate());
		evaluations.add(ev);
		
		ev = new Eval();
		ev.setName(CoreConstant.Error.KAPPA_STATISTIC.name());
		try {
			ev.setValue((float) eval.kappa());
		} catch (Exception e) {
			ev.setValue(default_value);
		}
		evaluations.add(ev);

		ev = new Eval();
		ev.setName(CoreConstant.Error.MEAN_ABSOLUTE.name());
		try {
			ev.setValue((float) eval.meanAbsoluteError());
		} catch (Exception e) {
			ev.setValue(default_value);
		}
		evaluations.add(ev);

		ev = new Eval();
		ev.setName(CoreConstant.Error.RELATIVE_ABSOLUTE.name());
		try {
			ev.setValue((float) eval.relativeAbsoluteError());
		} catch (Exception e) {
			ev.setValue(default_value);
		}
		evaluations.add(ev);
		
		ev = new Eval();
		ev.setName(CoreConstant.Error.ROOT_MEAN_SQUARED.name());
		try {
			ev.setValue((float) eval.rootMeanSquaredError());
		} catch (Exception e) {
			ev.setValue(default_value);
		}
		evaluations.add(ev);

		ev = new Eval();
		ev.setName(CoreConstant.Error.ROOT_RELATIVE_SQUARED.name());
		try {
			ev.setValue((float) eval.rootRelativeSquaredError());
		} catch (Exception e) {
			ev.setValue(default_value);
		}
		evaluations.add(ev);
		
		evaluation.setEvaluations(evaluations);		
	}

	@Override
	public DataInstances getPredictions(Instances test,
			DataInstances onto_test) {

		//Evaluation eval = test();
		double[] pre = new double[test.numInstances()];
		for (int i = 0; i < test.numInstances(); i++) {
			try {
				pre[i] = classifier.classifyInstance(test.instance(i));
			} catch (Exception e) {
				pre[i] = Integer.MAX_VALUE;
			}
		}

		// copy results to the DataInstancs
		int i = 0;
		for (Instance next_instance : onto_test.getInstances()) {
			next_instance.setPrediction(pre[i]);
			i++;
		}

		return onto_test;
	}
}