package org.pikater.core.agents.experiment.recommend;

import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.metadata.GetAllMetadata;
import org.pikater.core.ontology.subtrees.metadata.Metadata;
import org.pikater.core.ontology.subtrees.metadata.Metadatas;
import org.pikater.core.ontology.subtrees.newoption.NewOptions;
import org.pikater.core.options.recommend.BasicRecommend_Box;

/**
 * Implementation of a simple recommendation strategy. The best-performing computational agent
 * on the closest dataset is recommended together with its parameters. 
 * 
 */
public class Agent_Basic extends Agent_Recommender {

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

	/**
	 * Returns the best agent selected based on the metadata. 
	 * 
	 * The closest dataset (according to metadata) is selected and then all the results on this
	 * dataset are investigated. The agent which provided the best result in this dataset is 
	 * recommended for the new data together with its parameters.
	 * 
	 * In case no results are found in the database (cold start) default agent is recommended
	 * (RBF Network)
	 * 
	 * @param data The new data, for which the agent is recommended
	 * @return The best agent and its settings for the data
	 */
	@Override
	protected org.pikater.core.ontology.subtrees.management.Agent
		chooseBestAgent(Datas data) {
		
		// in data there are already metadata filled in 
		// return agent with (partially/not at all) filled options
		
		logSevere(distanceMatrix());

		Metadata metadata = data.getMetadata();
		
		GetAllMetadata gm = new GetAllMetadata();
		gm.setResultsRequired(true);
		
		// 1. choose the nearest training data
		Metadatas allMetadata = DataManagerService.getAllMetadata(this, gm);
		
		// set the min, max instances and attributes first
		for(Metadata nextMd : allMetadata.getMetadatas()) {

			int na = nextMd.getNumberOfAttributes();
			if (na < minAttributes) {
				minAttributes = na;
			}
			if (na > maxAttributes) {
				maxAttributes = na;
			}

			int ni = nextMd.getNumberOfInstances();
			if (ni < minInstances) {
				minInstances = ni;
			}
			if (ni > maxInstances) {
				maxInstances = ni;
			}
		}
		
        StringBuilder sb = new StringBuilder("Files: ");
		logSevere("*********** files from the table: ");

		double dBest = Integer.MAX_VALUE;
		Metadata mBest = null;

		double dNew;

		for (Metadata nextMd : allMetadata.getMetadatas()) {
			dNew = distance(metadata, nextMd);
			if (!nextMd.getInternalName().equals(metadata.getInternalName()) && (dNew < dBest)) {
				dBest = dNew;
				mBest = nextMd;
			}
			sb.append("    " + nextMd.getExternalName() + " distance: " + dNew + "\n");
		}
        logInfo(sb.toString());
		
        String extNameOfBest;
        if (mBest != null) {
        	extNameOfBest = mBest.getExternalName();
        } else {
        	extNameOfBest = "only original file has results";
        }
        
		logWarning("Nearest file: " + extNameOfBest);
		
		
		org.pikater.core.ontology.subtrees.management.Agent agent = null;
		
		if (mBest != null) {
			String nearestInternalName = mBest.getInternalName();
			// 2. find the agent with the lowest error_rate
			agent = DataManagerService.getTheBestAgent(this, nearestInternalName);
		}
		
		if (agent != null){
			String wekaOptionString = NewOptions.exportToWeka(agent.getOptions()); 
			logWarning("Best agent type: "+ agent.getType() +
					", options: " + wekaOptionString);
			
		} else {
			agent = new org.pikater.core.ontology.subtrees.management.Agent();
			agent.setType(Agent_Recommender.DEFAULT_AGENT.getName());
			agent.setName(Agent_Recommender.DEFAULT_AGENT.getName());
			
			String externalName;
			if (mBest != null) {
				externalName = mBest.getExternalName();
			} else {
				externalName = "no file";
			}
			
			logInfo("No results in database for file " + externalName +
					" ... Using default agent: " + agent.getType());
			
		}
		
		return agent;

	}	
	
	/**
	 * Computes the distance matrix between all pairs of metadata. Used only for logging.
	 * 
	 * @return Formated String with the metadata distance matrix.
	 */
	private String distanceMatrix() {
		String matrix = "";
		
		GetAllMetadata getMetadata = new GetAllMetadata();
		getMetadata.setResultsRequired(false);
	
		Metadatas allMetadata =
				DataManagerService.getAllMetadata(this, getMetadata);
	
		for(Metadata nextCollI : allMetadata.getMetadatas()) {
			
			int na = nextCollI.getNumberOfAttributes();
			if (na < minAttributes) {
				minAttributes = na;
			}
			if (na > maxAttributes) {
				maxAttributes = na;
			}
	
			int ni = nextCollI.getNumberOfInstances();
			if (ni < minInstances) {
				minInstances = ni;
			}
			if (ni > maxInstances) {
				maxInstances = ni;
			}
		}
	
		double distance;
		for(Metadata nextCollI : allMetadata.getMetadatas()) {

			matrix += nextCollI.getExternalName() + ";";

			for (Metadata nextRowJ : allMetadata.getMetadatas()) {
				
				distance = distance(nextCollI, nextRowJ);
				matrix += String.format("%.10f", distance);
				matrix += ";";				
			}
			matrix +="\n";
		}
		
		return matrix;
		
	}

	/**
	 * Computes the distance between two datasets as a weighted sum of differences between their 
	 * metadata.
	 * 
	 * @param m1 Metadata for the first dataset
	 * @param m2 Metadata for the second dataset
	 * @return Distance between the two datasets
	 */
	private double distance(Metadata m1, Metadata m2) {

		double wAttributeType = 1;
		double wDefaultTask = 1;
		double wMissingValues = 1;
		double wNumberOfAttributes = 1;
		double wNumberOfInstances = 1;

		// can be null
		double dAttributeType = dCategory(m1.getAttributeType(), m2
				.getAttributeType());
		double dDefault_task = dCategory(m1.getDefaultTask(), m2
				.getDefaultTask());
		// default false - always set
		double dMissingValues = dBoolean(m1.getMissingValues(), m2
				.getMissingValues());
		// mandatory attributes - always set
		double dNumberOfAttributes = d(m1.getNumberOfAttributes(), m2
				.getNumberOfAttributes(), minAttributes, maxAttributes);
		double dNumberOfInstances = d(m1.getNumberOfInstances(), m2
				.getNumberOfInstances(), minInstances, maxInstances);

		// return distance
		return wAttributeType * dAttributeType + wDefaultTask
				* dDefault_task + wMissingValues * dMissingValues
				+ wNumberOfAttributes * dNumberOfAttributes
				+ wNumberOfInstances * dNumberOfInstances;
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
