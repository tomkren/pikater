package org.pikater.core.ontology.subtrees.dataSource;

import jade.content.Concept;

/**
 * User: Kuba
 * Date: 2.5.2014
 * Time: 13:21
 */
public class DataSourcePath implements Concept {
    /**
	 * 
	 */
	private static final long serialVersionUID = -651945976173141864L;

	private String path;
    
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
}