package pikater.utility.pikaterDatabase;

import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

public class AttributeMetadata {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private float ratioOfMissingValues;
	private boolean isTarget;
	
	@Transient
	private static final String PERSISTENCE_UNIT_NAME = "attributemetadata";
	@Transient
	private static EntityManagerFactory factory;
	
	
	public void setRatioOfMissingValues(float ratioOfMissingValues){
		this.ratioOfMissingValues=ratioOfMissingValues;
	}
	
	public float getRatioOfMissingValues(){
		return this.ratioOfMissingValues;
	}
	
	public void setTarget(boolean isTarget){
		this.isTarget=isTarget;
	}
	
	public boolean isTarget(){
		return this.isTarget;
	}
	
}
