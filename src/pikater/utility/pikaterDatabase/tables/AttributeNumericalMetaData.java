package pikater.utility.pikaterDatabase.tables;

import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

public class AttributeNumericalMetaData {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private boolean isReal;
	private float min;
	private float max;
	private float modus;
	private float median;
	private float classEntropy;
	private float variance;
	private float average;
	
	@Transient
	private static final String PERSISTENCE_UNIT_NAME = "attributenumericalmetadata";
	@Transient
	private static EntityManagerFactory factory;
	
	public void setReal(boolean isReal){
		this.isReal=isReal;
	}
	
	public boolean isReal(){
		return isReal;
	}
	
	public void setMin(float min){
		this.min=min;
	}
	
	public float getMin(){
		return this.min;
	}
	
	public void setMax(float max){
		this.max=max;
	}
	
	public float getMax(){
		return this.max;
	}
	
	public void setModus(float modus){
		this.modus=modus;
	}
	
	public float getModus(){
		return this.modus;
	}
	
	public void setMedian(float median){
		this.median=median;
	}
	
	public float getMedian(){
		return this.median;
	}
	
	public void setClassEntropy(float classEntropy){
		this.classEntropy=classEntropy;
	}
	
	public float getClassEntropy(){
		return this.classEntropy;
	}
	
	public void setVariance(float variance){
		this.variance=variance;
	}
	
	public float getVariance(){
		return this.variance;
	}
	
	public void setAverage(float average){
		this.average=average;
	}
	
	public float getAverage(){
		return this.average;
	}
	
}
