package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Table(name="Root")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class JPAAbstractEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	public int getId() {
        return id;
    }

	public String getEntityName(){
		return "AbstractEntity";
	}
	
	public abstract void updateValues(JPAAbstractEntity newValues) throws Exception;
	
}
