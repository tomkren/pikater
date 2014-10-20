package org.pikater.shared.database.jpa.status;

/**
 * Enum used to define the source of a dataset stored in the database. This status is needed
 * for the reason that datasets can be uploaded by a user or created by an experiment. In several
 * cases it's useful to distinguish between the sources.
 *
 */
public enum JPADatasetSource {
	EXPERIMENT, USERUPLOAD
}
