package pikater.data.jpa;

import java.util.ArrayList;

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
	private ArrayList<JPAAttributeNumericalMetadata> attributes;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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

	public ArrayList<JPAAttributeNumericalMetadata> getAttributes() {
		return attributes;
	}
	public void setAttributes(ArrayList<JPAAttributeNumericalMetadata> attributes) {
		this.attributes = attributes;
	}
	public void addAttribute(JPAAttributeNumericalMetadata attribute) {
		this.attributes.add(attribute);
	}	
}
