package org.pikater.core.agents.experiment.recommend;

import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.metadata.GetAllMetadata;
import org.pikater.core.ontology.subtrees.metadata.Metadata;
import org.pikater.core.ontology.subtrees.metadata.Metadatas;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.options.recommend.BasicRecommend_Box;


public class Agent_Basic extends Agent_Recommender {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1175580440950655620L;

	private double minAttributes = Integer.MAX_VALUE;
	private double maxAttributes = Integer.MIN_VALUE;
	private double minInstances = Integer.MAX_VALUE;
	private double maxInstances = Integer.MIN_VALUE;
	
	private boolean done = false;
	
	@Override
	protected String getAgentType(){
		return "BasicRecommender";
	}
	
	@Override
	protected AgentInfo getAgentInfo() {

		return BasicRecommend_Box.get();
	}

	@Override
	protected org.pikater.core.ontology.subtrees.management.Agent chooseBestAgent(Datas data){		
		// in data there are already metadata filled in 
		// return agent with (partially/not at all) filled options
		
		logSevere(distanceMatrix());

		Metadata metadata = data.getMetadata();
		
		GetAllMetadata gm = new GetAllMetadata();
		gm.setResultsRequired(true);
		
		// 1. choose the nearest training data
		Metadatas allMetadata = DataManagerService.getAllMetadata(this, gm);
		
		// set the min, max instances and attributes first
		for(Metadata next_md : allMetadata.getMetadatas()) {

			int na = next_md.getNumberOfAttributes();
			if (na < minAttributes) {
				minAttributes = na;
			}
			if (na > maxAttributes) {
				maxAttributes = na;
			}

			int ni = next_md.getNumberOfInstances();
			if (ni < minInstances) {
				minInstances = ni;
			}
			if (ni > maxInstances) {
				maxInstances = ni;
			}
		}
		
        StringBuilder sb = new StringBuilder("Files: ");
		logSevere("*********** files from the table: ");

		double d_best = Integer.MAX_VALUE;
		Metadata m_best = null;

		double d_new;

		for (Metadata next_md : allMetadata.getMetadatas()) {
			d_new = distance(metadata, next_md);
			if (!next_md.getInternalName().equals(metadata.getInternalName()) && (d_new < d_best))
			{
				d_best = d_new;
				m_best = next_md;
			}
			sb.append("    " + next_md.getExternalName() + " distance: " + d_new + "\n");
		}
        logInfo(sb.toString());
		
		logWarning("Nearest file: " + ((m_best!=null)?m_best.getExternalName():"only original file has results"));
		
		
		org.pikater.core.ontology.subtrees.management.Agent agent = null;
		
		if(m_best!=null){
			String nearestInternalName = m_best.getInternalName();
			// 2. find the agent with the lowest error_rate
			agent = DataManagerService.getTheBestAgent(this, nearestInternalName);
		}
		
		if (agent != null){
			String wekaOptionString = NewOptions.exportToWeka(agent.getOptions()); 
			logWarning("Best agent type: "+ agent.getType() +
					", options: " + wekaOptionString);
		}
		else{
			agent=new org.pikater.core.ontology.subtrees.management.Agent();
			agent.setType(Agent_Recommender.DEFAULT_AGENT.getName());
			agent.setName(Agent_Recommender.DEFAULT_AGENT.getName());
			logInfo("No results in database for file " + ((m_best!=null)?m_best.getExternalName():"no file")+" ... Using default agent: "+agent.getType());
			
		}
		
		return agent;

	}	         
	
	private String distanceMatrix() {
		String matrix = "";
		
		GetAllMetadata gm = new GetAllMetadata();
		gm.setResultsRequired(false);
	
		Metadatas allMetadata = DataManagerService.getAllMetadata(this, gm);
	
		for(Metadata next_coll : allMetadata.getMetadatas()) {
			
			int na = next_coll.getNumberOfAttributes();
			if (na < minAttributes) {
				minAttributes = na;
			}
			if (na > maxAttributes) {
				maxAttributes = na;
			}
	
			int ni = next_coll.getNumberOfInstances();
			if (ni < minInstances) {
				minInstances = ni;
			}
			if (ni > maxInstances) {
				maxInstances = ni;
			}
		}
	
		double d;
		for(Metadata next_coll : allMetadata.getMetadatas()) {

			matrix +=next_coll.getExternalName() + ";";

			for(Metadata next_row : allMetadata.getMetadatas()) {
				
				d = distance(next_coll, next_row);
				matrix += String.format("%.10f", d);
				matrix += ";";				
			}
			matrix +="\n";
		}
		
		return matrix;
		
	} // end distanceMatrix

	/*
	 * Compute distance between two datasets (use metadata)
	 */
	private double distance(Metadata m1, Metadata m2) {

		double wAttribute_type = 1;
		double wDefault_task = 1;
		double wMissing_values = 1;
		double wNumber_of_attributes = 1;
		double wNumber_of_instances = 1;

		// can be null
		double dAttribute_type = dCategory(m1.getAttributeType(), m2
				.getAttributeType());
		double dDefault_task = dCategory(m1.getDefaultTask(), m2
				.getDefaultTask());
		// default false - always set
		double dMissing_values = dBoolean(m1.getMissingValues(), m2
				.getMissingValues());
		// mandatory attributes - always set
		double dNumber_of_attributes = d(m1.getNumberOfAttributes(), m2
				.getNumberOfAttributes(), minAttributes, maxAttributes);
		double dNumber_of_instances = d(m1.getNumberOfInstances(), m2
				.getNumberOfInstances(), minInstances, maxInstances);

		// return distance
		return wAttribute_type * dAttribute_type + wDefault_task
				* dDefault_task + wMissing_values * dMissing_values
				+ wNumber_of_attributes * dNumber_of_attributes
				+ wNumber_of_instances * dNumber_of_instances;
	}

	@Override
	protected boolean finished() {
		return done;
	}

	@Override
	protected void updateFinished() {
		done = true;
	}
	
}
