/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.agents.experiment.search;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.*;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.search.searchItems.IntervalSearchItem;
import org.pikater.core.ontology.subtrees.search.searchItems.SearchItem;
import org.pikater.core.options.search.GridSearch_Box;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Martin Pilat
 */
public class Agent_GridSearch extends Agent_Search {

	private static final long serialVersionUID = -5728853644752654450L;

	int defaultTries = 10;
	List<SearchItem> schema;
    boolean linearSteps = true;
    boolean logSteps = true;
    double logZero = 1.0E-8;
    List<IValueData> values = null;

    @Override
    protected String getAgentType() {
        return "GridSearch";
    }

	@Override
	protected AgentInfo getAgentInfo() {
		
		return GridSearch_Box.get();
	}

    @Override
    protected boolean isFinished() {
        return values != null;
    }

    @Override
    protected List<SearchSolution> generateNewSolutions(
    		List<SearchSolution> solutions, float[][] evaluations) {

    	logInfo("is generating...");

        if (values == null) {
            values = generateValues();
        }
        
        logInfo("generated values: " + values.size());
        
        List<SearchSolution> ret = new LinkedList<SearchSolution>();
        for (int i = 0; i < values.size(); i++) {
            SearchSolution ss = new SearchSolution();
            List<IValueData> v = new ArrayList<IValueData>();
            for (IValueData s: values) {
            	v.add(s);
            }
            ss.setValues(v);
            ret.add(ss);
        }
        logInfo("result values: " + ret.size());
        return ret;
    }

    private List<IValueData> generateValues() {

        List<IValueData> vals = new ArrayList<IValueData>();
        
        List<List<IValueData>> valsForOpts =
        		new ArrayList<List<IValueData>>();

        for (SearchItem searchItemI : schema ) {

            List<IValueData> valsForItem =
            		generateValue(searchItemI);
            valsForOpts.add(valsForItem);
        }
        
        logInfo("valsForOpts.size(): " + valsForOpts.size());
        
        vals.addAll(valsForOpts.get(0));
        
        for (int i = 1; i < valsForOpts.size(); i++) {
            List<IValueData> newVals = new ArrayList<IValueData>();
            for (int j = 0; j < vals.size(); j++) {
            	for (int k = 0; k < valsForOpts.get(i).size(); k++) {
            		
            		String value = vals.get(j) + "," +
            				valsForOpts.get(i).get(k);
                    newVals.add(new StringValue(value));
                }
            }
            vals = newVals;
        }
        
        return vals;
    }

    private List<IValueData> generateValue(SearchItem searchInterval) {
    	
        Integer tries = searchInterval.getNumberOfValuesToTry();
        if (tries == 0) {
            tries = defaultTries;
        }
        ArrayList<IValueData> valsForItem = new ArrayList<IValueData>();
        IntervalSearchItem searchItem = (IntervalSearchItem) searchInterval;
        if (searchItem.getMin() instanceof BooleanValue) {
        	valsForItem.addAll(searchItem.possibleValues());
        }
        
        if (searchInterval instanceof IntervalSearchItem) {
        	IntervalSearchItem isi = (IntervalSearchItem)searchInterval;
        	if (isi.getMin() instanceof IntegerValue) {
            	int max = ((IntegerValue)isi.getMax()).getValue();
            	int min = ((IntegerValue)isi.getMin()).getValue();
            	if ( max - min < tries) {
                    for (int i = min; i <= max; i++) {
                        valsForItem.add(new IntegerValue(i));
                    }
                } else {
                    if (linearSteps) {
                        double stepSize = 1.0 * (max - min) / (tries - 1);
                        for (int i = 0; i < tries; i++) {
                        	int addValue = (int)Math.round(min + i * stepSize);
                            IValueData add = new IntegerValue(addValue);
                            if (!valsForItem.contains(add)) {
                                valsForItem.add(add);
                            }
                        }
                    }
                    if (logSteps) {
                        double normalization;
                        if (min < logZero) {
                        	normalization = min - logZero;
                        } else {
                        	normalization = 0.0;
                        }
                        
                        double start = Math.log(min - normalization);
                        double range = Math.log(max - normalization)
                        		-Math.log(min - normalization);
                        
                        double stepSize = range / (tries - 1);
                        for (int i = 0; i < tries; i++) {
                        	
                        	int value = (int) Math.round(
                        			Math.exp(start + i * stepSize) +
                        			normalization);
                            IValueData add = new IntegerValue(value);
                            if (!valsForItem.contains(add)) {
                                valsForItem.add(add);
                            }
                        }
                    }
                }	            	
        	}
        	
        	if (isi.getMin() instanceof FloatValue) {

                float max = ((FloatValue)isi.getMax()).getValue();
            	int min = ((IntegerValue)isi.getMin()).getValue();
                if (linearSteps) {
                    double stepSize = 1.0 * (max - min) / (tries - 1);
                    for (int i = 0; i < tries; i++) {
                        IValueData add = new DoubleValue(min + i * stepSize);
                        if (!valsForItem.contains(add)) {
                            valsForItem.add(add);
                        }
                    }
                }
                if (logSteps) {
                    double normalization;
                    if (min < logZero) {
                    	normalization = min - logZero;
                    } else {
                    	normalization = 0.0;
                    }
                    
                    double start = Math.log(min - normalization);
                    double range = Math.log(max - normalization)
                    		-Math.log(min - normalization);
                    
                    double stepSize = range / (tries - 1);
                    for (int i = 0; i < tries; i++) {
                    	double value = Math.exp(start + i * stepSize) +
                    			normalization;
                        DoubleValue add = new DoubleValue(value);
                        if (!valsForItem.contains(add)) {
                            valsForItem.add(add);
                        }
                    }
                }
        	}
        }
        
        return valsForItem;
    }
    
    @Override
    protected float updateFinished(float[][] evaluations) {
    	return 1;
    }

    @Override
    protected void loadSearchOptions() { 
        
        NewOptions options = getSearchOptions();
        
        if (options.containsOptionWithName("N")) {
        	NewOption optionN = options.fetchOptionByName("N");
        	IntegerValue valueN =
        			(IntegerValue) optionN.toSingleValue().getCurrentValue();
        	defaultTries = valueN.getValue();
        }
        if (options.containsOptionWithName("B")) {
        	NewOption optionB = options.fetchOptionByName("B");
        	IntegerValue valueB =
        			(IntegerValue) optionB.toSingleValue().getCurrentValue();
        	queryBlockSize = valueB.getValue();
        }
        if (options.containsOptionWithName("Z")) {
        	NewOption optionZ = options.fetchOptionByName("Z");
        	IntegerValue valueZ =
        			(IntegerValue) optionZ.toSingleValue().getCurrentValue();
        	logZero = valueZ.getValue();
        }
        
        schema = getSchema();
    }

}
