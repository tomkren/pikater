package org.pikater.core.agents.experiment.computing.communicator;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.pikater.core.CoreConstant;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.experiment.computing.Agent_ComputingAgent;
import org.pikater.core.agents.system.data.AgentDataSource;
import org.pikater.core.agents.system.data.AgentDataSourceCommunicator;
import org.pikater.core.ontology.subtrees.batchdescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.datainstance.DataInstances;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.task.Eval;
import org.pikater.core.ontology.subtrees.task.Evaluation;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task.InOutType;
import org.pikater.core.ontology.subtrees.task.TaskOutput;

import weka.core.Instances;

/**
 * 
 * Behavior which process training a testing part of Machine Learning 
 *
 */
public class ComputingAction extends FSMBehaviour {

	private static final long serialVersionUID = 7417933314402310322L;

	private static final String INIT_STATE = "Init";
	private static final String GETTRAINDATA_STATE = "GetTrainingData";
	private static final String GETTESTDATA_STATE = "GetTestData";
	private static final String GETLABELDATA_STATE = "GetLabelData";
	private static final String TRAINTEST_STATE = "TrainTest";
	private static final String SENDRESULTS_STATE = "SendResults";
	private static final int NEXT_JMP = 0;
	private static final int LAST_JMP = 1;
	ACLMessage incomingRequest;
	ACLMessage resultMsg;
	ExecuteTask executeAction;
	boolean success;
	Evaluation eval = new Evaluation();
	String trainFn;
	String testFn;
	String labelFn;
	String output;
	String mode;

	private Agent_ComputingAgent agent;

	/**
	 * Constructor
	 * 
	 * @param agent - computing agent
	 */
	public ComputingAction(final Agent_ComputingAgent agent) {
		super(agent);
		this.agent = agent;

		/* FSM: register states */
		registerFirstState(new Behaviour(agent) {

			private static final long serialVersionUID = -4607390644948524477L;

			boolean cont;

			@Override
			public void action() {
			
				resultMsg = null;
				executeAction = null;
				if (!getRequest()) {
					// no task to execute
					cont = true;
					return;
				}
				cont = false;
				if (!agent.resurrected) {
					agent.state = Agent_ComputingAgent.States.NEW;
				}
				// Set options
				agent.setOptions(executeAction.getTask());

				// set agent name in Task
				Agent agentOnt = agent.currentTask.getAgent();
				agentOnt.setName(agent.getLocalName());
				agent.currentTask.setAgent(agentOnt);

				eval = new Evaluation();
				success = true;
				Datas data = executeAction.getTask().getDatas();
				output = data.getOutput();
				mode = data.getMode();

				trainFn = data.exportInternalTrainFileName();
				AchieveREInitiator getTrainBehaviour = (AchieveREInitiator)
						((ComputingAction) parent).getState(GETTRAINDATA_STATE);

				if (!trainFn.equals(agent.trainFileName)) {
					
					ACLMessage dataMsg = agent.makeGetDataRequest(trainFn);
					
					getTrainBehaviour.reset(dataMsg);
				} else {
					// We have already the right data
					getTrainBehaviour.reset(null);
				}

				if (!mode.equals(CoreConstant.Mode.TRAIN_ONLY.name())) {
					testFn = data.exportInternalTestFileName();
					AchieveREInitiator getTestBehaviour = (AchieveREInitiator)
							((ComputingAction) parent).getState(GETTESTDATA_STATE);
					
					if (!testFn.equals(agent.testFileName)) {
						
						ACLMessage dataMsg = agent.makeGetDataRequest(testFn);

						getTestBehaviour.reset(dataMsg);
					} else {
						// We have already the right data
						getTestBehaviour.reset(null);
					}
				}

				if (data.exportData(CoreConstant.DataType.LABEL_DATA.getType()) != null) {
					labelFn = data.exportInternalLabelFileName();
					AchieveREInitiator getLabelBehaviour = (AchieveREInitiator)
							((ComputingAction) parent).getState(GETLABELDATA_STATE);
					
					if (!labelFn.equals(agent.labelFileName)) {
						
						ACLMessage dataMsg = agent.makeGetDataRequest(labelFn);
						
						getLabelBehaviour.reset(dataMsg);
					} else {
						// We have already the right data
						getLabelBehaviour.reset(null);
					}
				}
			}

			@Override
			public boolean done() {
				return !cont;
			}
		}, INIT_STATE);

		// get train data state
		registerState(new AchieveREInitiator(agent, null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -133823979702541803L;

			public int next = NEXT_JMP;

			@Override
			protected void handleInform(ACLMessage inform) {
				DataInstances trainDataInstances = agent.processGetDataResponse(inform);
				if (trainDataInstances != null) {
					agent.trainFileName = trainFn;
					agent.ontoTrain = trainDataInstances;
					agent.train = agent.ontoTrain.toWekaInstances();
					agent.train.setClassIndex(agent.train.numAttributes() - 1);
					next = NEXT_JMP;
					return;
				} else {
					next = LAST_JMP;
					failureMsg("No train data received from the reader agent: Wrong content.");
					return;
				}
			}

			@Override
			protected void handleFailure(ACLMessage failure) {
				failureMsg("No train data received from the reader agent: Reader Failed.");
				next = LAST_JMP;
			}

			@Override
			public int onEnd() {
				next = NEXT_JMP;
				return next;
			}
		}, GETTRAINDATA_STATE);

		// get test data state
		registerState(new AchieveREInitiator(agent, null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2398937572588954013L;

			public int next = NEXT_JMP;

			@Override
			protected void handleInform(ACLMessage inform) {
				DataInstances testDataInstances = agent.processGetDataResponse(inform);
				if (testDataInstances != null) {
					agent.testFileName = testFn;
					agent.ontoTest = testDataInstances;
					agent.test = agent.ontoTest.toWekaInstances();
					agent.test.setClassIndex(agent.test.numAttributes() - 1);

					next = NEXT_JMP;
					return;
				} else {
					next = LAST_JMP;
					failureMsg("No test data received from the reader agent: Wrong content.");
					return;
				}

			}

			@Override
			protected void handleFailure(ACLMessage failure) {
				failureMsg("No test data received from the reader agent: Reader Failed.");
				next = LAST_JMP;
			}

			@Override
			public int onEnd() {
				next = NEXT_JMP;
				return next;
			}
		}, GETTESTDATA_STATE);

		// get label data state
		registerState(new AchieveREInitiator(agent, null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -968264895781473739L;

			public int next = NEXT_JMP;

			@Override
			protected void handleInform(ACLMessage inform) {
					DataInstances labelDataInstances =
						agent.processGetDataResponse(inform);

				if (labelDataInstances != null) {
					agent.labelFileName = labelFn;
					agent.ontoLabel = labelDataInstances;
					agent.label = agent.ontoLabel.toWekaInstances();
					agent.label.setClassIndex(agent.label.numAttributes() - 1);
					next = NEXT_JMP;
					return;
				} else {
					next = LAST_JMP;
					failureMsg("No label data received from the reader"
							+ "agent: Wrong content.");
					return;
				}
			}

			@Override
			protected void handleFailure(ACLMessage failure) {
				failureMsg("No label data received from the reader agent: Reader Failed.");
				next = LAST_JMP;
			}

			@Override
			public int onEnd() {
				next = NEXT_JMP;
				return next;
			}
		}, GETLABELDATA_STATE);

		// Train&test&label state
		registerState(new Behaviour(agent) {

			private static final long serialVersionUID = 1479579948554502568L;

			@Override
			public void action() {
				try {

					List<DataInstances> labeledData =
							new ArrayList<DataInstances>();

					eval = new Evaluation();
					eval.setEvaluations(new ArrayList<Eval>());

					Date startDate = null;
					if (agent.state != Agent_ComputingAgent.States.TRAINED) {
						startDate = agent.train(eval);
					
					} else if (!agent.resurrected && !mode.equals("test_only")) {
						startDate = agent.train(eval);
					}
					eval.setStart(startDate);

					if (agent.state == Agent_ComputingAgent.States.TRAINED) {
						EvaluationMethod evaluationMethod =
								executeAction.getTask().getEvaluationMethod();

						if (! mode.equals(
								CoreConstant.Mode.TRAIN_ONLY.name())) {
							agent.evaluateCA(evaluationMethod, eval);

							if ( (output.equals(CoreConstant.Output.EVALUATION_LABEL.name()))
								|| (output.equals(CoreConstant.Output.LABEL_ONLY.name()))) {
								saveDataSource(labeledData);
							}
						}
					}

				} catch (Exception e) {
					success = false;
					agent.working = false;
					agent.logException("Unexpected error occured:", e);
				}
			}

			/**
			 * Save DataSources
			 * 
			 * @throws Exception
			 */
			private void saveDataSource (List<DataInstances> labeledData) throws Exception {
				
				DataInstances dataInstance = new DataInstances();
				dataInstance.fillWekaInstances(agent.label);
				
				DataInstances labeledTest =
						agent.getPredictions(agent.label, dataInstance);
				labeledData.add(labeledTest);
				
				// Save datasource and inform datasource
				// manager about this particular datasource
				AgentDataSource.SerializeFile(labeledTest,
						resultMsg.getConversationId() + ".labeledtest");
				
				PikaterAgent pikaterAgent = (PikaterAgent) myAgent;
				AgentDataSourceCommunicator dsCom =
						new AgentDataSourceCommunicator(pikaterAgent);
				
				dsCom.registerDataSources(
						resultMsg.getConversationId(),
						new String[] { "labeledtest" });
				
				if (!agent.labelFileName.equals("")) {
					dataInstance = new DataInstances();
					dataInstance.fillWekaInstances(agent.label);
					
					DataInstances labeledPredictions =
							agent.getPredictions(agent.label, dataInstance);
					
					// Save dataSource and inform dataSource manager about
					// this particular DataSource
					
					AgentDataSource.SerializeFile(
							labeledPredictions,
							resultMsg.getConversationId()
									+ ".labeledpredictions");
					
					dsCom.registerDataSources(
							resultMsg.getConversationId(),
							new String[] { "labeledpredictions" });
					labeledData.add(labeledPredictions);
				}
				
				eval.setLabeledData(labeledData);
			}
			
			@Override
			public boolean done() {
				return (agent.state == Agent_ComputingAgent.States.TRAINED)
						|| !success;
			}
		}, TRAINTEST_STATE);

		// send results state
		registerState(new OneShotBehaviour(agent) {

			private static final long serialVersionUID = -7838676822707371053L;

			@Override
			public void action() {

				if (success && (resultMsg == null)) {

					if ((agent.currentTask.getSaveMode() != null &&
							"file".equals(agent.currentTask .getSaveMode())) && 
							!agent.resurrected) {
						
						try {
							String objectFilename =
									ComputingCommunicator.saveAgentToFile(agent);
							eval.setObjectFilename(objectFilename);

						} catch (CodecException e) {
							agent.logException(e.getMessage(), e);
						} catch (OntologyException e) {
							agent.logException(e.getMessage(), e);
						} catch (IOException e) {
							agent.logException(e.getMessage(), e);
						} catch (FIPAException e) {
							agent.logException(e.getMessage(), e);
						}
					}

					if (agent.currentTask.getSaveMode() != null
							&& "message".equals(agent.currentTask.getSaveMode()
									)) {
						try {
							eval.setObject(agent.getAgentObject());
						} catch (IOException e1) {
							agent.logException(e1.getMessage(), e1);
						}
					}
				}
				
				addTaskOutput(InOutType.TEST, agent.test);
				addTaskOutput(InOutType.TRAIN, agent.train);
				addTaskOutput(InOutType.LABELED, agent.label);

				// TODO add error

				resultMsg = incomingRequest.createReply();
				resultMsg.setPerformative(ACLMessage.INFORM);

				ContentElement content;
				try {
					content = agent.getContentManager().extractContent(
							incomingRequest);

					// Prepare the content: Result with current task &
					// filled in evaluaton
					if (agent.resurrected) {
						eval.setObject(null);
					}
					agent.currentTask.setResult(eval);

					Result result = new Result((Action) content, agent.currentTask);
					agent.getContentManager().fillContent(resultMsg, result);

				} catch (UngroundedException e) {
					agent.logException(e.getMessage(), e);
				} catch (CodecException e) {
					agent.logException(e.getMessage(), e);
				} catch (OntologyException e) {
					agent.logException(e.getMessage(), e);
				}

				agent.currentTask.setFinish(agent.getDateTime());

				agent.send(resultMsg);
				
				if (!agent.taskFIFO.isEmpty()) {
					agent.executionBehaviour.restart();
				} else {
					agent.logFinishedTask();
				}

			}
		}, SENDRESULTS_STATE);

		/* FSM: register transitions */
		// init state transition
		registerDefaultTransition(INIT_STATE, GETTRAINDATA_STATE);

		// get train data transitions
		registerTransition(GETTRAINDATA_STATE, GETTESTDATA_STATE, NEXT_JMP);
		registerTransition(GETTRAINDATA_STATE, SENDRESULTS_STATE, LAST_JMP);

		// get test data transitions
		registerTransition(GETTESTDATA_STATE, GETLABELDATA_STATE, NEXT_JMP);
		registerTransition(GETTESTDATA_STATE, SENDRESULTS_STATE, LAST_JMP);

		// get label data transition
		registerTransition(GETLABELDATA_STATE, TRAINTEST_STATE, NEXT_JMP);
		registerTransition(GETLABELDATA_STATE, SENDRESULTS_STATE, LAST_JMP);

		// train&test state transition
		registerDefaultTransition(TRAINTEST_STATE, SENDRESULTS_STATE);

		// backward transition: reset all states
		registerDefaultTransition(SENDRESULTS_STATE, INIT_STATE, new String[] {
				INIT_STATE, GETTRAINDATA_STATE, GETTESTDATA_STATE,
				GETLABELDATA_STATE, TRAINTEST_STATE, SENDRESULTS_STATE });
	}

	/**
	 * Resulting message: FAILURE
	 * 
	 */
	private void failureMsg(String desc) {
		List<Eval> evaluations = new ArrayList<Eval>();

		// set error_rate
		Eval errorRateEval = new Eval();
		errorRateEval.setName("error_rate");
		errorRateEval.setValue(Float.MAX_VALUE);
		evaluations.add(errorRateEval);

		// set duration to max_float
		Eval durationEval = new Eval();
		durationEval.setName("duration");
		durationEval.setValue(Integer.MAX_VALUE);
		evaluations.add(durationEval);

		// set start to now
		Eval startEval = new Eval();
		startEval.setName("start");
		startEval.setValue(System.currentTimeMillis());
		evaluations.add(startEval);

		eval.setEvaluations(evaluations);
		eval.setStatus(desc);
	}

	/**
	 * Get a message from the FIFO of tasks
	 * 
	 */
	private boolean getRequest() {
		if (!agent.taskFIFO.isEmpty()) {
			incomingRequest = agent.taskFIFO.removeFirst();
			try {
				ContentElement content = agent.getContentManager()
						.extractContent(incomingRequest);
				executeAction = (ExecuteTask) ((Action) content)
						.getAction();
				return true;
			} catch (CodecException ce) {
				agent.logException(ce.getMessage(), ce);
			} catch (OntologyException oe) {
				agent.logException(oe.getMessage(), oe);
			}
		} else {
			block();
		}

		return false;
	}
	
	private String addTaskOutput(InOutType type, Instances inst) {
		
		if (inst != null) {
			String md5 = agent.saveArff(inst);
			agent.logInfo("Saved "+type+" to " + md5);
			
			TaskOutput taskOutput = new TaskOutput();
			taskOutput.setType(type);
			taskOutput.setName(md5);
			
			if (agent.currentTask.getOutput() == null) {
				agent.currentTask.setOutput(new ArrayList<TaskOutput>());
			}
			agent.currentTask.getOutput().add(taskOutput);
			return md5;
		} else {
			return null;
		}
	}
}
