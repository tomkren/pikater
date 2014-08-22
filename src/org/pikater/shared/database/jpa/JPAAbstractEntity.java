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

@Entity
@Table(name="Root")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
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
	
	public abstract void updateValues(JPAAbstractEntity newValues) throws Exception;
	
}
