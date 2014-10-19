package org.pikater.shared.database.jpa.status;

/**
 * Execution status of an experiment. Because experiments are created during the batch execution
 * smaller amount of available statuses is enough than for for batches.
 *
 */
public enum JPAExperimentStatus {
	FINISHED, COMPUTING, FAILED
}
