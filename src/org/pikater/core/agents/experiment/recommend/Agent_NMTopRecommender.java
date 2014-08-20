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

    /**
	 * 
	 */
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

        @Override
        public int compareTo(MetadataDistancePair o) {
            if (o.getDistance() == this.getDistance()) {
                return 0;
            }
            return o.getDistance() < this.getDistance() ? 1 : -1;
        }
    }

    @Override
    protected Agent chooseBestAgent(Datas data) {
    	
        Metadata metadata = data.getMetadata();

        GetAllMetadata gm = new GetAllMetadata();
        gm.setResults_required(true);

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

        ArrayList<MetadataDistancePair> distances = new ArrayList<MetadataDistancePair>();

        for (Metadata metadataI : allMetadata.getMetadatas()) {

            double dNew = distance(metadata, metadataI);
            distances.add(new MetadataDistancePair(metadataI, dNew));
        }

        Collections.sort(distances);
        
        if(distances.size()<M){
        	//we do not have enough agents to choose from
        	//using default one
        	Agent agent=new Agent();
			agent.setType(Agent_Recommender.DEFAULT_AGENT.getName());
			agent.setName(Agent_Recommender.DEFAULT_AGENT.getName());
			logError("Not having enough agents to choose from. Using default agent: "+agent.getType());
			return agent;
        }

        List<Agent> agents = new LinkedList<Agent>();
        for (int i = 0; i < M; i++) {
            log(distances.get(i).m.getExternalName() + ": " + distances.get(i).d);
            Agents agentsOnt = DataManagerService.getNBestAgents(this, distances.get(i).m.getInternalName(), N);
            
            if((agents!=null)&&(agentsOnt!=null)) {
            
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
            log(s + ": " + counts.get(s));
            if (counts.get(s) > maxCount) {
                maxCount = counts.get(s);
                bestAgentType = s;
            }
        }

        log("Best agent: " + bestAgentType);

        ArrayList<Agent> bestAgents = new ArrayList<Agent>();

        for (Agent a : agents) {

            if (a.getType().equals(bestAgentType)) {
                bestAgents.add(a);
            }
        }

        List<NewOption> optionSamples = getAgentOptions(bestAgentType);
        List<NewOption> options = new java.util.ArrayList<NewOption>();
        
        for (NewOption optionI : optionSamples) {

        	String computedDatatype = optionI.computeDataType();
        	NewOption newOpt = optionI.clone();        	
            
            
        	//ignore boolean and set options for now, set their value to the one of the best agent on closest file
        	if (computedDatatype.equals(IntegerValue.class.getSimpleName())  || 
        			computedDatatype.equals(FloatValue.class.getSimpleName())) {
        		
	            double sum = 0;
	            int count = 0;
	            String optionName = optionI.getName();
	            for (Agent agentI : bestAgents) {
	            	if (agentI.getOptionByName(optionName) != null){
	            		
	            		NewOption optionOfAgentI = agentI.getOptionByName(optionName);
	            		IValueData valueI = optionOfAgentI.toSingleValue().getCurrentValue();
	            		 		
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
	            		
	            		NewOption optionOfAgentI = agent.getOptionByName(optionName);
	            		IValueData valueI = optionOfAgentI.toSingleValue().getCurrentValue();
	            		
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
	            	
	            	TypeRestriction typeRestriction = optionI.getValueRestrictions().fetchByIndex(0);
	            	ValueType type = typeRestriction.getTypes().get(0);
	            	
	            	RangeRestriction rangeRestriction = type.getRangeRestriction();
	            	IValueData minValue = rangeRestriction.getMinValue();
	            	IValueData maxValue = rangeRestriction.getMaxValue();
	            	
	            	float finalMin = 0;
	            	float finalMax = 0;
	            	
	            	if (minValue instanceof IntegerValue) {
	            		IntegerValue integeMin = (IntegerValue) minValue;
	            		IntegerValue integerMax = (IntegerValue) maxValue;
	            		
	            		finalMin = (float) Math.max(avg - 2*stdDev, integeMin.getValue());
	            		finalMax = (float) Math.min(avg - 2*stdDev, integerMax.getValue());
	            	}
	            	if (minValue instanceof FloatValue) {
	            		FloatValue floatMin = (FloatValue) minValue;
	            		FloatValue floatMax = (FloatValue) maxValue;
	            		
	            		finalMin = (float) Math.max(avg - 2*stdDev, floatMin.getValue());
	            		finalMax = (float) Math.min(avg - 2*stdDev, floatMax.getValue());
	            	}	            	
	            	
	            	QuestionMarkRange questionMark = new QuestionMarkRange(
	            			new FloatValue(finalMin),
	            			new FloatValue(finalMax),
	            			0
	            	);
	            	
	                newOpt.getValuesWrapper().addValue(new Value(questionMark));
	            }
	            else {
	            	Value valueI = optionI.toSingleValue();
            		IValueData iValueI = valueI.getCurrentValue();
            		
	                if (iValueI instanceof FloatValue) {
	                	float valueFloat = (float) avg;
	                    newOpt.getValuesWrapper().addValue(new Value(new FloatValue(valueFloat)));
	                }
	                if (iValueI instanceof IntegerValue) {
	                	int valueInteger = (int) avg;
	                	newOpt.getValuesWrapper().addValue(new Value(new IntegerValue(valueInteger)));
	                }
	            }
        	} else {
        		
        		Agent bestAgent = bestAgents.get(0);
                if (bestAgent.getOptionByName(optionI.getName()) == null){
                	continue;
                }
                NewOption option0 = bestAgent.getOptionByName(optionI.getName());
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

        double wAttribute_type = 1;
        double wDefault_task = 1;
        double wMissing_values = 1;
        double wNumber_of_attributes = 1;
        double wNumber_of_instances = 1;

        // can be null
        double dAttribute_type = dCategory(m1.getAttributeType(), m2.getAttributeType());
        double dDefault_task = dCategory(m1.getDefaultTask(), m2.getDefaultTask());
        // default false - always set
        double dMissing_values = dBoolean(m1.getMissingValues(), m2.getMissingValues());
        // mandatory attributes - always set
        double dNumber_of_attributes = d(m1.getNumberOfAttributes(), m2.getNumberOfAttributes(), minAttributes, maxAttributes);
        double dNumber_of_instances = d(m1.getNumberOfInstances(), m2.getNumberOfInstances(), minInstances, maxInstances);

        double distance = wAttribute_type * dAttribute_type
                + wDefault_task * dDefault_task
                + wMissing_values * dMissing_values
                + wNumber_of_attributes * dNumber_of_attributes
                + wNumber_of_instances * dNumber_of_instances;

        return distance;
    }

    private double d(double v1, double v2, double min, double max) {
        // map the value to the 0,1 interval; 0 - the same, 1 - the most
        // different

        return Math.abs(v1 - v2) / (max - min);
    }

    private int dCategory(String v1, String v2) {
        // null considered another value
        if (v1 == null) {
            v1 = "null";
        }
        if (v2 == null) {
            v2 = "null";
        }

        if (v1.equals(v2)) {
            return 0;
        }
        return 1;
    }

    private int dBoolean(Boolean v1, Boolean v2) {
        if (v1 == v2) {
            return 0;
        }
        return 1;
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
