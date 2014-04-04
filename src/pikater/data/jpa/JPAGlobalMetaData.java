package pikater.data.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class JPAGlobalMetaData {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	private JPATaskType defaultTaskType;
	private int numberofInstances;
	
	public JPAGlobalMetaData(){
		super();
	}
	
	public JPAGlobalMetaData(int numberOfInstances,JPATaskType defaultTaskType){
		this.numberofInstances=numberOfInstances;
		this.defaultTaskType=defaultTaskType;
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
	public int getId() {
		return id;
	}
	
	
}
