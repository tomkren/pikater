package org.pikater.core.ontology.subtrees.openml;

import jade.content.AgentAction;

public class ExportDataset implements AgentAction {

	private static final long serialVersionUID = 8475049934695874698L;

	private String path;
	
	private String name;
	private String description;
	private String type;
	
	public String getPath() {
		return path;
	}
	/**
	 * Sets the path to the file in the local file system, that should be uploaded to OpenMl.org .
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	/**
	 * Sets the name of the dataset, that will appear in the repository of OpenML.org
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
