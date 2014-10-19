package org.pikater.core.ontology.subtrees.search;

import java.util.List;

import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.core.ontology.subtrees.search.searchitems.SearchItem;

import jade.content.AgentAction;

/**
 * 
 * Ontology represents a request for parameters
 * 
 */
public class GetParameters implements AgentAction {

	private static final long serialVersionUID = -4554163588726699351L;

	// List of Options
	private List<SearchItem> schema;
	private List<NewOption> searchOptions;

	/**
	 * Get schema
	 */
	public List<SearchItem> getSchema() {
		return schema;
	}

	/**
	 * Set schema
	 */
	public void setSchema(List<SearchItem> schema) {
		this.schema = schema;
	}

	/**
	 * Get options which are searched
	 */
	public List<NewOption> getSearchOptions() {
		return searchOptions;
	}

	/**
	 * Set searched options
	 */
	public void setSearchOptions(List<NewOption> searchOptions) {
		this.searchOptions = searchOptions;
	}

}
