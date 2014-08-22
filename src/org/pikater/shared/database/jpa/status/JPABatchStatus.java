package org.pikater.shared.database.jpa.status;

public enum JPABatchStatus
{
	/*
	 * IMPORTANT: keep chronological order so that the {@link ordinal()} method can be used.
	 */
	CREATED,
	WAITING,
	STARTED,
	COMPUTING,
	FAILED,
	FINISHED,
}