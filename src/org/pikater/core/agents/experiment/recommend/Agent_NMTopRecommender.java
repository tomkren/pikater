/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.agents.experiment.recommend;

import jade.util.leap.Iterator;
import jade.util.leap.LinkedList;
import jade.util.leap.List;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.metadata.GetAllMetadata;
import org.pikater.core.ontology.subtrees.metadata.Metadata;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.Value;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.type.Types;
import org.pikater.core.ontology.subtrees.newOption.typedValue.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.QuestionMarkRange;
import org.pikater.core.options.NMTopRecommender_RecommendBox;

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
    protected Agent chooseBestAgent(Data data) {

        Metadata metadata = data.getMetadata();

        GetAllMetadata gm = new GetAllMetadata();
        gm.setResults_required(true);

        // 1. choose the nearest training data
        List allMetadata = DataManagerService.getAllMetadata(this, gm);

        // set the min, max instances and attributes first
        Iterator itr = allMetadata.iterator();
        while (itr.hasNext()) {
            Metadata next_md = (Metadata) itr.next();

            int na = next_md.getNumberOfAttributes();
            minAttributes = Math.min(minAttributes, na);
            maxAttributes = Math.max(maxAttributes, na);

            int ni = next_md.getNumberOfInstances();
            minInstances = Math.min(ni, minInstances);
            maxInstances = Math.max(ni, maxInstances);
        }

        ArrayList<MetadataDistancePair> distances = new ArrayList<MetadataDistancePair>();

        itr = allMetadata.iterator();
        while (itr.hasNext()) {
            Metadata next_md = (Metadata) itr.next();
            double dNew = distance(metadata, next_md);

            distances.add(new MetadataDistancePair(next_md, dNew));
        }

        Collections.sort(distances);

        List agents = new LinkedList();
        for (int i = 0; i < M; i++) {
            log(distances.get(i).m.getExternalName() + ": " + distances.get(i).d);
            List ag = DataManagerService.getTheBestAgents(this, distances.get(i).m.getInternalName(), N);
            Iterator it = ag.iterator();
            while (it.hasNext()) {
                agents.add(it.next());
            }
        }

        HashMap<String, Integer> counts = new HashMap<String, Integer>();

        Iterator it = agents.iterator();
        while (it.hasNext()) {
            Agent a = (Agent) it.next();

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

        it = agents.iterator();
        while (it.hasNext()) {
            Agent a = (Agent) it.next();

            if (a.getType().equals(bestAgentType)) {
                bestAgents.add(a);
            }
        }

        java.util.List<NewOption> optionSamples = getAgentOptions(bestAgentType);
        java.util.List<NewOption> options = new java.util.ArrayList<NewOption>();
        
        for (NewOption optionI : optionSamples) {

        	String computedDatatype = optionI.computeDataType();
        	NewOption newOpt = optionI.cloneOption();        	
            
            
        	//ignore boolean and set options for now, set their value to the one of the best agent on closest file
        	if (computedDatatype.equals(IntegerValue.class.getSimpleName())  || 
        			computedDatatype.equals(FloatValue.class.getSimpleName())) {
        		
	            double sum = 0;
	            int count = 0;
	            String optionName = optionI.getName();
	            for (Agent agentI : bestAgents) {
	            	if (agentI.getOptionByName(optionName) != null){
	            		
	            		NewOption optionOfAgentI = agentI.getOptionByName(optionName);
	            		ITypedValue valueI = optionOfAgentI.convertToSingleValue().getTypedValue();
	            		 		
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
	            		ITypedValue valueI = optionOfAgentI.convertToSingleValue().getTypedValue();
	            		
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
	            	
	            	java.util.List<Types> typesList = optionI.getPossibleTypesRestriction().getPossibleTypes();
	            	Types types = typesList.get(0);
	            	Type type = types.getTypes().get(0);
	            	
	            	RangeRestriction rangeRestriction = type.getRangeRestriction();
	            	ITypedValue minValue = rangeRestriction.getMinValeu();
	            	ITypedValue maxValue = rangeRestriction.getMaxValeu();
	            	
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
	            			new FloatValue(finalMax));
	            	
	                newOpt.addValue(new Value(questionMark));
	                newOpt.setIsMutable(true);
	            }
	            else {
	            	Value valueI = optionI.convertToSingleValue();
            		ITypedValue iValueI = valueI.getTypedValue();
            		
	                if (iValueI instanceof FloatValue) {
	                	float valueFloat = (float) avg;
	                    newOpt.addValue(new Value(new FloatValue(valueFloat)));
	                }
	                if (iValueI instanceof IntegerValue) {
	                	int valueInteger = (int) avg;
	                	newOpt.addValue(new Value(new IntegerValue(valueInteger)));
	                }
	            }
        	} else {
        		
        		Agent bestAgent = bestAgents.get(0);
                if (bestAgent.getOptionByName(optionI.getName()) == null){
                	continue;
                }
                NewOption option0 = bestAgent.getOptionByName(optionI.getName());
                newOpt.setValues(option0.getValues().cloneValues());   
        	}

            options.add(newOpt);
        }

        Agent agent = new Agent();
        agent.setName(null);
        agent.setType(bestAgentType);
        agent.setOptions(options);

        return agent;
    }

    @Override
    protected String getAgentType() {
        return "NMTopRecommender";
    }

	@Override
	protected AgentInfo getAgentInfo() {

		return NMTopRecommender_RecommendBox.get();
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


}
