package org.pikater.core.ontology.subtrees.search;

import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.search.searchItems.SearchItem;

import jade.content.AgentAction;

/**
 * 
 * Ontology represents a request for parameters
 *
 */
public class GetParameters implements AgentAction {

	private static final long serialVersionUID = -4554163588726699351L;
	
	private List<SearchItem> schema; // List of Options
	private List<NewOption> searchOptions;
	
	/**
	 * Get schema
	 * @return
	 */
	public List<SearchItem> getSchema() {
		return schema;
	}
	
	/**
	 * Set schema
	 * @param schema
	 */
	public void setSchema(List<SearchItem> schema) {
		this.schema = schema;
	}
	
	/**
	 * Get options which are searched 
	 * @return
	 */
	public List<NewOption> getSearchOptions() {
		return searchOptions;
	}
	
	/**
	 * Set searched options
	 * @param searchOptions
	 */
	public void setSearchOptions(List<NewOption> searchOptions) {
		this.searchOptions = searchOptions;
	}

}
