package org.pikater.core.ontology.subtrees.visualisation;

import jade.content.AgentAction;

public class GenerateScatterPlot implements AgentAction {
	
	private static final long serialVersionUID = -8316578088164706670L;

	private String dataSetPath="";
	private String plotPath="";
	
	public String getDataSetPath() {
		return dataSetPath;
	}
	public void setDataSetPath(String dataSetPath) {
		this.dataSetPath = dataSetPath;
	}
	public String getPlotPath() {
		return plotPath;
	}
	public void setPlotPath(String plotPath) {
		this.plotPath = plotPath;
	}
}
