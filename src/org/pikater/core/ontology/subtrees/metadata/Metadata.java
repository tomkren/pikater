package org.pikater.core.ontology.subtrees.metadata;

import jade.content.Concept;
import java.util.List;
import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.metadata.attributes.*;

public class Metadata implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4618372245480479979L;

	private String internalName;
	private String externalName;
	private int numberOfInstances = -1;
	private int numberOfAttributes = -1;
	private boolean missingValues;
	private String defaultTask; // Classification, Regression, Clustering
	private String attributeType; // Categorical, Numerical, Mixed
	private int linearRegressionDuration; // ms
	private List<AttributeMetadata> attributeMetadataList = new ArrayList<AttributeMetadata>();

	public String getTargetClassType() {

		for (int i = getAttributeMetadataList().size() - 1; i >= 0; i--) {
			AttributeMetadata attributeI = getAttributeMetadataList().get(i);
			if (attributeI.isIsTarget()) {
				return attributeI.getType();
			}
		}
		return "No target class";
	}

	public double getCategoricalRatio() {
		double n = getNumberOfAttributes() - 1;
		return getNumberOfCategorical() / n;
	}

	public double getIntegerRatio() {
		double n = getNumberOfAttributes() - 1;
		return getNumberOfInteger() / n;
	}

	public double getRealRatio() {
		double n = getNumberOfAttributes() - 1;
		return getNumberOfReal() / n;
	}

	public int getNumberOfCategorical() {
		int count = 0;
		for (int i = getAttributeMetadataList().size() - 1; i >= 0; i--) {
			AttributeMetadata att = (AttributeMetadata) getAttributeMetadataList()
					.get(i);
			if (!att.isIsTarget()) {
				if (att instanceof CategoricalAttributeMetadata)
					count++;
			}
		}
		return count;
	}

	public int getNumberOfInteger() {
		int count = 0;
		for (int i = getAttributeMetadataList().size() - 1; i >= 0; i--) {
			AttributeMetadata att = (AttributeMetadata) getAttributeMetadataList()
					.get(i);
			if (!att.isIsTarget()) {
				if (att instanceof IntegerAttributeMetadata)
					count++;
			}
		}
		return count;
	}

	public int getNumberOfReal() {
		int count = 0;
		for (AttributeMetadata att : getAttributeMetadataList()) {
			if (!att.isIsTarget()) {
				if (att instanceof RealAttributeMetadata)
					count++;
			}
		}
		return count;
	}

	public List<AttributeMetadata> getAttributeMetadataList() {
		return attributeMetadataList;
	}
	public void setAttributeMetadataList(
			List<AttributeMetadata> attributeMetadataList) {
		this.attributeMetadataList = attributeMetadataList;
	}

	public int getNumberOfInstances() {
		return numberOfInstances;
	}
	public void setNumberOfInstances(int numberOfInstances) {
		this.numberOfInstances = numberOfInstances;
	}

	public int getNumberOfAttributes() {
		return numberOfAttributes;
	}
	public void setNumberOfAttributes(int numberOfAttributes) {
		this.numberOfAttributes = numberOfAttributes;
	}

	public boolean getMissingValues() {
		return missingValues;
	}
	public void setMissingValues(boolean missingValues) {
		this.missingValues = missingValues;
	}

	public String getDefaultTask() {
		return defaultTask;
	}
	public void setDefaultTask(String defaultTask) {
		this.defaultTask = defaultTask;
	}

	// Deprecated
	public String getAttributeType() {
		return attributeType;
	}
	// Deprecated
	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	// Deprecated
	public String getInternalName() {
		return internalName;
	}
	// Deprecated
	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

	// Deprecated
	public String getExternalName() {
		return externalName;
	}
	// Deprecated
	public void setExternalName(String externalName) {
		this.externalName = externalName;
	}

	public int getLinearRegressionDuration() {
		return linearRegressionDuration;
	}
	public void setLinearRegressionDuration(int linearRegressionDuration) {
		this.linearRegressionDuration = linearRegressionDuration;
	}

}