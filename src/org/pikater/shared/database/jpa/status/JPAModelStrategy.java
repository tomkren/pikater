package org.pikater.shared.database.jpa.status;

import org.pikater.shared.database.jpa.JPAExperiment;

/**
 * <p>
 * Enum used to mark whether an experiment should use an existing trained model or create a new one.
 * </p>
 * <p>
 * The two cases are used in the following way: 
 * <ul>
 * <li>If strategy is set to {@link JPAModelStrategy.CREATION}, than the experiment should create a new model.</li>
 * <li>If strategy is set to {@link JPAModelStrategy.EXISTING}, than the experiment should use an existing model
 * defined in {@link JPAExperiment#setUsedModel(org.pikater.shared.database.jpa.JPAModel)} function.</li>
 * </ul>
 * </p>
 */
public enum JPAModelStrategy {
	CREATION,
	EXISTING
}
