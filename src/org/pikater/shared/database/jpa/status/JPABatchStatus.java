package org.pikater.shared.database.jpa.status;

/**
 * Enum used to define several statuses of an executed batch.
 */
public enum JPABatchStatus {
	/*
	 * IMPORTANT: keep chronological order so that the {@link ordinal()} method can be used.
	 */
	CREATED, WAITING, STARTED, COMPUTING, FAILED, FINISHED,
}