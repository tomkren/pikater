/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.agents.experiment.recommend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.management.Agents;
import org.pikater.core.ontology.subtrees.metadata.GetAllMetadata;
import org.pikater.core.ontology.subtrees.metadata.Metadata;
import org.pikater.core.ontology.subtrees.metadata.Metadatas;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.base.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.core.options.recommend.NMTopRecommender_Box;

/**
 *
 * @author Martin Pilat
 */
public class Agent_NMTopRecommender extends Agent_Recommender {

	private static final long serialVersionUID = 5466662784862244724L;

	private int minAttributes;
    private int maxAttributes;
    private int minInstances;
    private int maxInstances;
    private int N = 100;
    private int M = 5;
    
	private boolean done = false;

    
    @Override
    protected String getAgentType() {
        return "NMTopRecommender";
    }

	@Override
	protected AgentInfo getAgentInfo() {

		return NMTopRecommender_Box.get();
	}
	
    class MetadataDistancePair implements Comparable<MetadataDistancePair> {

        Metadata m;

        public Metadata getMetadata() {
            return m;
        }

        public void setMetadata(Metadata m) {
            this.m = m;
        }

        public double getDistance() {
            return d;
        }

        public void setDistance(double d) {
            this.d = d;
        }
        double d;

        public MetadataDistancePair(Metadata m, double d) {
            this.m = m;
            this.d = d;
        }
        
        private Agent_NMTopRecommender getOuterType()
		{
			return Agent_NMTopRecommender.this;
		}

        @Override
        public int compareTo(MetadataDistancePair o) {
            if (o.getDistance() == this.getDistance()) {
                return 0;
            }
            return o.getDistance() < this.getDistance() ? 1 : -1;
        }

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			long temp;
			temp = Double.doubleToLongBits(d);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result + ((m == null) ? 0 : m.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MetadataDistancePair other = (MetadataDistancePair) obj;
			return compareTo(other) == 0;
		}
    }

    @Override
    protected Agent chooseBestAgent(Datas data) {
    	
        Metadata metadata = data.getMetadata();

        GetAllMetadata gm = new GetAllMetadata();
        gm.setResultsRequired(true);

        // 1. choose the nearest training data
        Metadatas allMetadata = DataManagerService.getAllMetadata(this, gm);

        // set the min, max instances and attributes first
        for (Metadata metadataI : allMetadata.getMetadatas()) {
        	
            int na = metadataI.getNumberOfAttributes();
            minAttributes = Math.min(minAttributes, na);
            maxAttributes = Math.max(maxAttributes, na);

            int ni = metadataI.getNumberOfInstances();
            minInstances = Math.min(ni, minInstances);
            maxInstances = Math.max(ni, maxInstances);
        }

        ArrayList<MetadataDistancePair> distances =
        		new ArrayList<MetadataDistancePair>();

        for (Metadata metadataI : allMetadata.getMetadatas()) {

            double dNew = distance(metadata, metadataI);
            distances.add(new MetadataDistancePair(metadataI, dNew));
        }

        Collections.sort(distances);
        
        if(distances.size() < M){
        	//we do not have enough agents to choose from
        	//using default one
        	Agent agent=new Agent();
			agent.setType(Agent_Recommender.DEFAULT_AGENT.getName());
			agent.setName(Agent_Recommender.DEFAULT_AGENT.getName());
			logSevere("Not having enough agents to choose from. "
					+ "Using default agent: "+agent.getType());
			return agent;
        }

        List<Agent> agents = new LinkedList<Agent>();
        
        for (int i = 0; i < M; i++) {
        	String externalName =
        			distances.get(i).m.getExternalName();
        	double d = distances.get(i).d;
            logInfo(externalName + ": " + d);
            
            String internalName =
            		distances.get(i).m.getInternalName();
            Agents agentsOnt =
            		DataManagerService.getNBestAgents(this, internalName, N);
            
            if ((agents != null) && (agentsOnt != null)) {
            
	            for (Agent agentI : agentsOnt.getAgents()) {
	                agents.add(agentI);
	            }
            }
        }

        HashMap<String, Integer> counts = new HashMap<String, Integer>();

        for (Agent a : agents) {

            if (counts.containsKey(a.getType())) {
                counts.put(a.getType(), counts.get(a.getType()) + 1);
            } else {
                counts.put(a.getType(), 1);
            }
        }

        int maxCount = 0;
        String bestAgentType = null;
        for (String s : counts.keySet()) {
            logInfo(s + ": " + counts.get(s));
            if (counts.get(s) > maxCount) {
                maxCount = counts.get(s);
                bestAgentType = s;
            }
        }

        logInfo("Best agent: " + bestAgentType);

        ArrayList<Agent> bestAgents = new ArrayList<Agent>();

        for (Agent a : agents) {

            if (a.getType().equals(bestAgentType)) {
                bestAgents.add(a);
            }
        }

        List<NewOption> optionSamples = getAgentOptions(bestAgentType);
        List<NewOption> options = new ArrayList<NewOption>();
        
        for (NewOption optionI : optionSamples) {

        	String computedDatatype = optionI.computeDataType();
        	NewOption newOpt = optionI.clone();        	
            
            
        	// ignore boolean and set options for now, set their value
        	// to the one of the best agent on closest file
        	if (computedDatatype.equals(IntegerValue.class.getSimpleName())  ||
        			computedDatatype.equals(FloatValue.class.getSimpleName())) {
        		
	            double sum = 0;
	            int count = 0;
	            String optionName = optionI.getName();
	            for (Agent agentI : bestAgents) {
	            	if (agentI.getOptionByName(optionName) != null){
	            		
	            		NewOption optionOfAgentI =
	            				agentI.getOptionByName(optionName);
	            		IValueData valueI =
	            				optionOfAgentI.toSingleValue().getCurrentValue();
	            		 		
	            		if (valueI instanceof IntegerValue) {
	            			IntegerValue integerValue = (IntegerValue) valueI;
	            			sum += integerValue.getValue();
	            		}
	            		if (valueI instanceof FloatValue) {
	            			FloatValue floatValue = (FloatValue) valueI;
	            			sum += floatValue.getValue();
	            		}
	            		
	            	}
	                count++;
	            }
	            double avg = sum/count;
	            
	            double stdDev = 0;
	            for (Agent agent : bestAgents) {
	            	if (agent.getOptionByName(optionName) != null) {
	            		
	            		NewOption optionOfAgentI =
	            				agent.getOptionByName(optionName);
	            		IValueData valueI =
	            				optionOfAgentI.toSingleValue().getCurrentValue();
	            		
	            		if (valueI instanceof IntegerValue) {
	            			IntegerValue integerValue = (IntegerValue) valueI;
	            			stdDev += Math.pow(integerValue.getValue() - avg, 2);
	            		}
	            		if (valueI instanceof FloatValue) {
	            			FloatValue floatValue = (FloatValue) valueI;
	            			stdDev += Math.pow(floatValue.getValue() - avg, 2);
	            		}
	            	}
	            }
	            
	            stdDev = Math.sqrt(stdDev/count);
	
	            if (stdDev > 0) {
	            	
	            	TypeRestriction typeRestriction =
	            			optionI.getValueRestrictions().fetchByIndex(0);
	            	ValueType type = typeRestriction.getTypes().get(0);
	            	
	            	RangeRestriction rangeRestriction = type.getRangeRestriction();
	            	IValueData minValue = rangeRestriction.getMinValue();
	            	IValueData maxValue = rangeRestriction.getMaxValue();
	            	
	            	float finalMin = 0;
	            	float finalMax = 0;
	            	
	            	if (minValue instanceof IntegerValue) {
	            		IntegerValue integeMin = (IntegerValue) minValue;
	            		IntegerValue integerMax = (IntegerValue) maxValue;
	            		
	            		finalMin = (float)
	            				Math.max(avg - 2*stdDev, integeMin.getValue());
	            		finalMax = (float)
	            				Math.min(avg - 2*stdDev, integerMax.getValue());
	            	}
	            	if (minValue instanceof FloatValue) {
	            		FloatValue floatMin = (FloatValue) minValue;
	            		FloatValue floatMax = (FloatValue) maxValue;
	            		
	            		finalMin = (float)
	            				Math.max(avg - 2*stdDev, floatMin.getValue());
	            		finalMax = (float)
	            				Math.min(avg - 2*stdDev, floatMax.getValue());
	            	}	            	
	            	
	            	QuestionMarkRange questionMark = new QuestionMarkRange(
	            			new FloatValue(finalMin),
	            			new FloatValue(finalMax),
	            			0
	            	);
	            	
	            	Value value = new Value(questionMark);
	                newOpt.getValuesWrapper().addValue(value);
	            }
	            else {
	            	Value valueI = optionI.toSingleValue();
            		IValueData iValueI = valueI.getCurrentValue();
            		
	                if (iValueI instanceof FloatValue) {
	                	float valueFloat = (float) avg;
	                	Value value = new Value(new FloatValue(valueFloat));
	                    newOpt.getValuesWrapper().addValue(value);
	                }
	                if (iValueI instanceof IntegerValue) {
	                	int valueInteger = (int) avg;
	                	Value value =
	                			new Value(new IntegerValue(valueInteger));
	                	newOpt.getValuesWrapper().addValue(value);
	                }
	            }
        	} else {
        		
        		Agent bestAgent = bestAgents.get(0);
                if (bestAgent.getOptionByName(optionI.getName()) == null) {
                	continue;
                }
                NewOption option0 =
                		bestAgent.getOptionByName(optionI.getName());
                newOpt.setValuesWrapper(option0.getValuesWrapper().clone());
        	}

            options.add(newOpt);
        }

        Agent agent = new Agent();
        agent.setName(null);
        agent.setType(bestAgentType);
        agent.setOptions(options);


        return agent;
    }


    private double distance(Metadata m1, Metadata m2) {

        double wAttributeType = 1;
        double wDefaultTask = 1;
        double wMissing_values = 1;
        double wNumberOfAttributes = 1;
        double wNumberOfInstances = 1;

        // can be null
        double dAttributeType = dCategory(
        		m1.getAttributeType(),
        		m2.getAttributeType());
        double dDefaultTask = dCategory(
        		m1.getDefaultTask(),
        		m2.getDefaultTask());
        // default false - always set
        double dMissingValues = dBoolean(
        		m1.getMissingValues(),
        		m2.getMissingValues());
        // mandatory attributes - always set
        double dNumberOfAttributes = d(
        		m1.getNumberOfAttributes(),
        		m2.getNumberOfAttributes(),
        		minAttributes, maxAttributes);
        double dNumberOfInstances = d(
        		m1.getNumberOfInstances(),
        		m2.getNumberOfInstances(),
        		minInstances, maxInstances);

        // return distance
        return wAttributeType * dAttributeType
                + wDefaultTask * dDefaultTask
                + wMissing_values * dMissingValues
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
