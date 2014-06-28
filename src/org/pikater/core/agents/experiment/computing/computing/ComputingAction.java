package org.pikater.core.agents.experiment.computing.computing;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

import java.io.IOException;
import java.util.Date;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.experiment.computing.Agent_ComputingAgent;
import org.pikater.core.agents.system.data.AgentDataSource;
import org.pikater.core.agents.system.data.AgentDataSourceCommunicator;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.dataInstance.DataInstances;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.task.Eval;
import org.pikater.core.ontology.subtrees.task.Evaluation;
import org.pikater.core.ontology.subtrees.task.EvaluationMethod;
import org.pikater.core.ontology.subtrees.task.ExecuteTaksOnCPUCore;

public class ComputingAction extends FSMBehaviour {
	/**
	 * 
     */
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
	ExecuteTaksOnCPUCore executeAction;
	boolean success;
	Evaluation eval = new Evaluation();
	String trainFn;
	String testFn;
	String labelFn;
	String output;
	String mode;

	private Agent_ComputingAgent agent;

	/* Resulting message: FAILURE */

	void failureMsg(String desc) {
		java.util.List<Eval> evaluations = new java.util.ArrayList<Eval>();

		Eval er = new Eval();
		er.setName("error_rate");
		er.setValue(Float.MAX_VALUE);
		evaluations.add(er);

		// set duration to max_float
		Eval du = new Eval();
		du.setName("duration");
		du.setValue(Integer.MAX_VALUE);
		evaluations.add(du);

		// set start to now
		Eval st = new Eval();
		st.setName("start");
		st.setValue(System.currentTimeMillis());
		evaluations.add(st);

		eval.setEvaluations(evaluations);

		eval.setStatus(desc);
	}

	/* Get a message from the FIFO of tasks */
	boolean getRequest() {
		if (agent.taskFIFO.size() > 0) {
			incomingRequest = agent.taskFIFO.removeFirst();
			try {
				ContentElement content = agent.getContentManager()
						.extractContent(incomingRequest);
				executeAction = (ExecuteTaksOnCPUCore) ((Action) content)
						.getAction();
				return true;
			} catch (CodecException ce) {
				ce.printStackTrace();
			} catch (OntologyException oe) {
				oe.printStackTrace();
			}
		} else {
			block();
		}

		return false;
	}

	/* Extract data from INFORM message (ARFF reader) */
	DataInstances processGetData(ACLMessage inform) {
		ContentElement content;
		try {
			content = agent.getContentManager().extractContent(inform);
			if (content instanceof Result) {
				Result result = (Result) content;

				if (result.getValue() instanceof DataInstances) {
					return (DataInstances) result.getValue();
				} else if (result.getValue() instanceof Boolean) {

					Object dataInstance = agent.getO2AObject();
					if (dataInstance == null)
						throw new IllegalStateException(
								"received GetData response without o2a object in queue");
					else
						return (DataInstances) dataInstance;
				} else {
					throw new IllegalStateException(
							"received unexpected Inform");
				}
			}
		} catch (UngroundedException e) {
			e.printStackTrace();
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ComputingAction(final Agent_ComputingAgent agent) {
		super(agent);
		this.agent = agent;

		/* FSM: register states */
		registerFirstState(new Behaviour(agent) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -4607390644948524477L;

			boolean cont;

			@Override
			public void action() {
				resultMsg = null;
				executeAction = null;
				if (!getRequest()) {
					// no task to execute
					cont = true;
					// block();
					return;
				}
				cont = false;
				if (!agent.resurrected) {
					agent.state = Agent_ComputingAgent.states.NEW;
				}
				// Set options
				agent.setOptions(executeAction.getTask());

				// set agent name in Task
				Agent agent_ = agent.currentTask.getAgent();
				agent_.setName(agent.getLocalName());
				agent.currentTask.setAgent(agent_);

				eval = new Evaluation();
				success = true;
				Data data = executeAction.getTask().getData();
				output = data.getOutput();
				mode = data.getMode();

				trainFn = data.getTrainFileName();
				AchieveREInitiator get_train_behaviour = (AchieveREInitiator) ((ComputingAction) parent).getState(GETTRAINDATA_STATE);

				if (!trainFn.equals(agent.trainFileName)) {
					
					ComputingComminicator communicator = new ComputingComminicator();
					ACLMessage dataMsg = communicator.sendGetDataReq(agent, trainFn);
					
					get_train_behaviour.reset(dataMsg);
				} else {
					// We have already the right data
					get_train_behaviour.reset(null);
				}

				if (!mode.equals("train_only")) {
					testFn = data.getTestFileName();
					AchieveREInitiator get_test_behaviour = (AchieveREInitiator) ((ComputingAction) parent)
							.getState(GETTESTDATA_STATE);
					if (!testFn.equals(agent.testFileName)) {
						
						ComputingComminicator communicator = new ComputingComminicator();
						ACLMessage dataMsg = communicator.sendGetDataReq(agent, testFn);

						get_test_behaviour.reset(dataMsg);
					} else {
						// We have already the right data
						get_test_behaviour.reset(null);
					}
				}

				if (data.getLabelFileName() != null) {
					labelFn = data.getLabelFileName();
					AchieveREInitiator get_label_behaviour = (AchieveREInitiator) ((ComputingAction) parent)
							.getState(GETLABELDATA_STATE);
					if (!labelFn.equals(agent.labelFileName)) {
						
						ComputingComminicator communicator = new ComputingComminicator();
						ACLMessage dataMsg = communicator.sendGetDataReq(agent, labelFn);
						
						get_label_behaviour.reset(dataMsg);
					} else {
						// We have already the right data
						get_label_behaviour.reset(null);
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
				DataInstances _train = processGetData(inform);
				if (_train != null) {
					agent.trainFileName = trainFn;
					agent.ontoTrain = _train;
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
				DataInstances _test = processGetData(inform);
				if (_test != null) {
					agent.testFileName = testFn;
					agent.ontoTest = _test;
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
				DataInstances _label = processGetData(inform);
				if (_label != null) {
					agent.labelFileName = labelFn;
					agent.ontoLabel = _label;
					agent.label = agent.ontoLabel.toWekaInstances();
					agent.label.setClassIndex(agent.label.numAttributes() - 1);
					next = NEXT_JMP;
					return;
				} else {
					next = LAST_JMP;
					failureMsg("No label data received from the reader agent: Wrong content.");
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

			/**
			 * 
			 */
			private static final long serialVersionUID = 1479579948554502568L;

			@Override
			public void action() {
				try {

					java.util.List<DataInstances> labeledData = new java.util.ArrayList<DataInstances>();

					eval = new Evaluation();

					eval.setEvaluations(new java.util.ArrayList<Eval>());

					Date start = null;
					if (agent.state != Agent_ComputingAgent.states.TRAINED) {
						start = agent.train(eval);
					} else if (!agent.resurrected) {
						if (!mode.equals("test_only")) {
							start = agent.train(eval);
						}
					}
					eval.setStart(start);

					if (agent.state == Agent_ComputingAgent.states.TRAINED) {
						EvaluationMethod evaluation_method = executeAction
								.getTask().getEvaluation_method();

						if (!mode.equals("train_only")) {
							agent.evaluateCA(evaluation_method, eval);

							if (output.equals("predictions")) {
								DataInstances di = new DataInstances();
								di.fillWekaInstances(agent.test);
								DataInstances labeledTest = agent
										.getPredictions(agent.test, di);
								labeledData.add(labeledTest);
								// Save datasource and inform datasource
								// manager about this particular datasource
								AgentDataSource.SerializeFile(labeledTest,
										resultMsg.getConversationId()
												+ ".labeledtest");
								AgentDataSourceCommunicator dsCom = new AgentDataSourceCommunicator(
										(PikaterAgent) myAgent, true);
								dsCom.registerDataSources(
										resultMsg.getConversationId(),
										new String[] { "labeledtest" });
								if (!agent.labelFileName.equals("")) {
									di = new DataInstances();
									di.fillWekaInstances(agent.label);
									DataInstances labeledPredictions = agent
											.getPredictions(agent.label, di);
									// Save datasource and inform datasource
									// manager about this particular
									// datasource
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
						}
					}

				} catch (Exception e) {
					success = false;
					agent.working = false;
					failureMsg(e.getMessage());
					System.err.println(agent.getLocalName() + ": Error: "
							+ e.getMessage() + ".");

					// e.printStackTrace();
				}
			}

			@Override
			public boolean done() {
				return (agent.state == Agent_ComputingAgent.states.TRAINED)
						|| !success;
			}
		}, TRAINTEST_STATE);

		// send results state
		registerState(new OneShotBehaviour(agent) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7838676822707371053L;

			@Override
			public void action() {

				if (success && (resultMsg == null)) {

					if ((agent.currentTask.getSaveMode() != null && agent.currentTask
							.getSaveMode().equals("file"))
							&& !agent.resurrected) {
						try {
							ComputingComminicator communicator = new ComputingComminicator();
							String objectFilename = communicator.saveAgentToFile(agent);
							eval.setObjectFilename(objectFilename);

						} catch (CodecException e) {
							agent.logError("", e);
						} catch (OntologyException e) {
							agent.logError("", e);
						} catch (IOException e) {
							agent.logError("", e);
						} catch (FIPAException e) {
							agent.logError("", e);
						}
					}

					if (agent.currentTask.getSaveMode() != null
							&& agent.currentTask.getSaveMode().equals(
									"message")) {
						try {
							eval.setObject(agent.getAgentObject());
						} catch (IOException e1) {
							agent.logError("", e1);
						}
					}
				}

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
					;
					agent.currentTask.setResult(eval);

					List results = new ArrayList();
					results.add(agent.currentTask);
					Result result = new Result((Action) content, results);
					agent.getContentManager().fillContent(resultMsg, result);

				} catch (UngroundedException e) {
					agent.logError("", e);
				} catch (CodecException e) {
					agent.logError("", e);
				} catch (OntologyException e) {
					agent.logError("", e);
				}

				if (agent.currentTask.getGetResults() != null
						&& agent.currentTask.getGetResults().equals(
								"after_each_task")) {
					resultMsg.addReceiver(new AID(agent.currentTask
							.getGuiAgent(), false));
				}

				agent.currentTask.setFinish(agent.getDateTime());

				agent.send(resultMsg);

				if (agent.taskFIFO.size() > 0) {
					agent.executionBehaviour.restart();
				} else {
					agent.log("CA terminating");
					agent.terminate();
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
}
