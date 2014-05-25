package org.pikater.shared.database.jpa.status;

/**
 * If strategy is set to creation, than the experiment should create a new model.
 * If strategy is set to existing, than the experiment should use an existing model
 * defined in usedModel property and if this usedModel is null it should cause exception.
 */
public enum JPAModelStrategy {
	CREATION,
	EXISTING
}
