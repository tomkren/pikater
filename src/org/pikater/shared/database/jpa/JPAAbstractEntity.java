package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

/**
 * From {@link JPAAbstractEntity} class are all JPA entity classes 
 * inherited, that are used for database access.
 * <p>
 * Main reason for its existence is providing ID values for each instance. These
 * ID values are unique in the whole database, not just in the particular table of 
 * the entity. 
 */
@Entity
@Table(name = "Root")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public abstract class JPAAbstractEntity {

	@Transient
	public static final String EntityName = "AbstractEntity";
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected int id;

	public int getId() {
		return id;
	}

	/**
	 * Abstract function for entity updates. This function should be overridden
	 * with function, that updates entity's values or throws an exception if the entity
	 * is not updateable.
	 * @param newValues entity containing new values
	 * @throws Exception
	 */
	public abstract void updateValues(JPAAbstractEntity newValues) throws Exception;

}
