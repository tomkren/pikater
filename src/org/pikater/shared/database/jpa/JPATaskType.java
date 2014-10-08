package org.pikater.shared.database.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Class {@link JPATaskType} represents record about the task types, that can
 * be performed on the datasets.
 */
@Entity
@Table(name="TaskType")
@NamedQueries({
	@NamedQuery(name="TaskType.getAll",query="select tt from JPATaskType tt"),
	@NamedQuery(name="TaskType.getByName",query="select tt from JPATaskType tt where tt.name=:name")
})
public class JPATaskType extends JPAAbstractEntity{
	
	@Column(unique=true)
	private String name;
	
	public JPATaskType(){
		super();
	}
	
	public JPATaskType(String name){
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Transient
	public static final String EntityName = "TaskType";

	@Override
	public void updateValues(JPAAbstractEntity newValues) throws Exception {
		JPATaskType updatedValues=(JPATaskType)newValues;
		this.name=updatedValues.getName();
	}
}
