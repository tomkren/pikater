package pikater.data.jpa;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JPAAttributeMetaData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	private float ratioOfMissingValues;
	private boolean isTarget;
	private List<JPAAttributeNumericalMetaData> numericalMetaData;
	private List<JPAAttributeCategoricalMetaData> categoricalMetaData;
	
	public JPAAttributeMetaData(){
		this.numericalMetaData=new LinkedList<JPAAttributeNumericalMetaData>();
		this.categoricalMetaData=new LinkedList<JPAAttributeCategoricalMetaData>();
	}
	
	public List<JPAAttributeNumericalMetaData> getNumericalMetaData() {
		return numericalMetaData;
	}

	public List<JPAAttributeCategoricalMetaData> getCategoricalMetaData() {
		return categoricalMetaData;
	}

	public int getId() {
		return id;
	}

	public float getRatioOfMissingValues() {
		return ratioOfMissingValues;
	}
	public void setRatioOfMissingValues(float ratioOfMissingValues) {
		this.ratioOfMissingValues = ratioOfMissingValues;
	}

	public boolean isTarget() {
		return isTarget;
	}
	public void setTarget(boolean isTarget) {
		this.isTarget = isTarget;
	}
}
