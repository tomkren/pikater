package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="GlobalMetaData")
@NamedQueries({
	@NamedQuery(name="GlobalMetaData.getAll",query="select gmd from JPAGlobalMetaData gmd"),
	@NamedQuery(name="GlobalMetaData.getById",query="select gmd from JPAGlobalMetaData gmd where gmd.id=:id")
})
public class JPAGlobalMetaData extends JPAAbstractEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	public int getId() {
        return id;
    }
	@ManyToOne
	private JPATaskType defaultTaskType;
	private int numberofInstances;
	private String attributeType;
	private int linearRegressionDuration;
	
	public JPAGlobalMetaData(){
		super();
	}
	
	public JPATaskType getDefaultTaskType() {
		return defaultTaskType;
	}
	public void setDefaultTaskType(JPATaskType defaultTaskType) {
		this.defaultTaskType = defaultTaskType;
	}
	public int getNumberofInstances() {
		return numberofInstances;
	}
	public void setNumberofInstances(int numberofInstances) {
		this.numberofInstances = numberofInstances;
	}
	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}
	public int getLinearRegressionDuration() {
		return linearRegressionDuration;
	}

	public void setLinearRegressionDuration(int linearRegressionDuration) {
		this.linearRegressionDuration = linearRegressionDuration;
	}
	
	@Transient
	public static final String EntityName = "GlobalMetaData";

	@Override
	public void updateValues(JPAAbstractEntity newValues) throws Exception {
		JPAGlobalMetaData updatedValues=(JPAGlobalMetaData)newValues;
		this.defaultTaskType=updatedValues.getDefaultTaskType();
		this.numberofInstances=updatedValues.getNumberofInstances();
		this.linearRegressionDuration=updatedValues.getLinearRegressionDuration();
		this.attributeType=updatedValues.getAttributeType();
	}
	
	
}
