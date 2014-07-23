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
import org.pikater.core.ontology.subtrees.search.searchItems.SetSItem;
import org.pikater.core.options.GridSearch_SearchBox;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Martin Pilat
 */
public class Agent_GridSearch extends Agent_Search {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5728853644752654450L;

	int defaultTries = 10;
	List<SearchItem> schema;
    boolean linearSteps = true;
    boolean logSteps = true;
    double logZero = 1.0E-8;
    ArrayList<IValueData> values = null;

    @Override
    protected String getAgentType() {
        return "GridSearch";
    }

	@Override
	protected AgentInfo getAgentInfo() {
		
		return GridSearch_SearchBox.get();
	}

    @Override
    protected boolean finished() {
        return values != null;
    }

    @Override
    protected List<SearchSolution> generateNewSolutions(List<SearchSolution> solutions, float[][] evaluations) {

        System.err.println("GENERATING");

        if (values == null) {
            values = generateValues();
        }
        
        System.err.println("VALUES: " + values.size());
        
        List<SearchSolution> ret = new LinkedList<SearchSolution>();
        
        for (int i = 0; i < values.size(); i++) {
            SearchSolution ss = new SearchSolution();
            List<IValueData> v = new ArrayList<IValueData>();
            for (IValueData s: values) { // values.get(i).split(",")
            	v.add(s);
            }
            ss.setValues(v);
            ret.add(ss);
        }
        
        System.err.println("ret: " + ret.size());
        
        return ret;
    }

    private ArrayList<IValueData> generateValues() {
        
        ArrayList<IValueData> vals = new ArrayList<>();
        
        ArrayList<ArrayList<IValueData>> valsForOpts = new ArrayList<>();

        for (SearchItem si : schema ) {
            Integer tries = si.getNumber_of_values_to_try();
            if (tries == 0) {
                tries = defaultTries;
            }
            ArrayList<IValueData> valsForItem = new ArrayList<>();
            IntervalSearchItem searchItem= ((IntervalSearchItem) si);
            if (searchItem.getMin() instanceof BooleanValue)
            {
                    valsForItem.addAll(searchItem.possibleValues());
            }
            if (si instanceof IntervalSearchItem) {
            	IntervalSearchItem isi = (IntervalSearchItem)si;
            	if (isi.getMin() instanceof IntegerValue)
            	{            		            	
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
                                IValueData add = new IntegerValue((int) Math.round(min + i * stepSize));
	                            if (!valsForItem.contains(add)) {
	                                valsForItem.add(add);
	                            }
	                        }
	                    }
	                    if (logSteps) {
	                        double normalization = min < logZero ? min - logZero : 0.0;
	                        double start = Math.log(min - normalization);
	                        double range = Math.log(max - normalization) - Math.log(min - normalization);
	                        double stepSize = range / (tries - 1);
	                        for (int i = 0; i < tries; i++) {
                                IValueData add = new IntegerValue((int) Math.round(Math.exp(start + i * stepSize) + normalization));
	                            if (!valsForItem.contains(add)) {
	                                valsForItem.add(add);
	                            }
	                        }
	                    }
	                }	            	
            	}
            	if (isi.getMin() instanceof FloatValue)
            	{            		            	

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
	                    double normalization = min < logZero ? min - logZero : 0.0;
	                    double start = Math.log(min - normalization);
	                    double range = Math.log(max - normalization) - Math.log(min - normalization);
	                    double stepSize = range / (tries - 1);
	                    for (int i = 0; i < tries; i++) {
	                        DoubleValue add = new DoubleValue(Math.exp(start + i * stepSize) + normalization);
	                        if (!valsForItem.contains(add)) {
	                            valsForItem.add(add);
	                        }
	                    }
	                }
            	}
            }
            if (si instanceof SetSItem) {
                SetSItem ssi = (SetSItem) si;
                for (int i = 0; i < tries && i < ssi.getSet().size(); i++) {
                    valsForItem.add(ssi.getSet().get(i));
                }
            }
            valsForOpts.add(valsForItem);
        }
        
        System.err.println("valsForOpts.size(): " + valsForOpts.size());
        
        vals.addAll(valsForOpts.get(0));
        
        for (int i = 1; i < valsForOpts.size(); i++) {
            ArrayList<IValueData> newVals = new ArrayList<>();
            for (int j = 0; j < vals.size(); j++) {
            	for (int k = 0; k < valsForOpts.get(i).size(); k++) {
                    newVals.add(new StringValue(vals.get(j) + "," + valsForOpts.get(i).get(k)));
                	// newVals.add(vals.get(j) + "," + valsForOpts.get(i).get(k));
                    // System.err.println("VALUES: " + vals.get(j) + "," + valsForOpts.get(i).get(k));
                }
            }
            vals = newVals;
        }
        
        /* for (String v : vals) {
            System.err.println("VALUES: " + v);
        }
        */
        
        return vals;
    }

    @Override
    protected void updateFinished(float[][] evaluations) {}

    @Override
    protected void loadSearchOptions() { 
        
        NewOptions options = new NewOptions(getSearchOptions());
        
        if (options.containsOptionWithName("N")) {
        	NewOption optionN = options.getOptionByName("N");
        	IntegerValue valueN = (IntegerValue) optionN.toSingleValue().getCurrentValue();
        	defaultTries = valueN.getValue();
        }
        if (options.containsOptionWithName("B")) {
        	NewOption optionB = options.getOptionByName("B");
        	IntegerValue valueB = (IntegerValue) optionB.toSingleValue().getCurrentValue();
        	query_block_size = valueB.getValue();
        }
        if (options.containsOptionWithName("Z")) {
        	NewOption optionZ = options.getOptionByName("Z");
        	IntegerValue valueZ = (IntegerValue) optionZ.toSingleValue().getCurrentValue();
        	logZero = valueZ.getValue();
        }
        //if (next.getName().equals("L")) {
        //   linearSteps = Boolean.parseBoolean(next.getValue());
        //}
        //if (next.getName().equals("G")) {
        //    logSteps = Boolean.parseBoolean(next.getValue());
        //}
        
        schema = getSchema();
    }

}
