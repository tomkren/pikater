package org.pikater.core.ontology.subtrees.messages;

import java.util.List;

import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.core.ontology.subtrees.search.searchItems.SearchItem;

import jade.content.AgentAction;

public class GetParameters implements AgentAction{

	private static final long serialVersionUID = -4554163588726699351L;
	
	private List<SearchItem> schema; // List of Options
	private List<Option> search_options;
	
	public List<SearchItem> getSchema() {
		return schema;
	}
	public void setSchema(List<SearchItem> schema) {
		this.schema = schema;
	}
	public List<Option> getSearch_options() {
		return search_options;
	}
	public void setSearch_options(List<Option> search_options) {
		this.search_options = search_options;
	}

}
