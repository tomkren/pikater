package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;

public class DataSourceDescriptions implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3364960401290114272L;
	
	private List<DataSourceDescription> descriptions;

	public DataSourceDescriptions() {
		this.descriptions = new ArrayList<DataSourceDescription>();
	}
	
	public DataSourceDescriptions(List<DataSourceDescription> descriptions) {
		super();
		this.descriptions = descriptions;
	}
	
	public List<DataSourceDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<DataSourceDescription> descriptions) {
		if (descriptions == null) {
			throw new IllegalArgumentException("Argument descriptions can't be null");
		}
		this.descriptions = descriptions;
	}
	
	public DataSourceDescription getDataSourceDescriptionIBynputType(
			String inputType) {
		
		for (DataSourceDescription descriptionI : this.descriptions) {
			
			if (descriptionI.getInputType() == null) {
				int dsf =5;
				dsf++;
			}
			if (descriptionI.getInputType().equals(inputType)) {
				return descriptionI;
			}
		}
		return null;
	}
	
}
