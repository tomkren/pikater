/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.agents.experiment.search;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.Value;
import org.pikater.core.ontology.subtrees.newOption.value.IntegerValue;
import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.search.searchItems.BoolSItem;
import org.pikater.core.ontology.subtrees.search.searchItems.FloatSItem;
import org.pikater.core.ontology.subtrees.search.searchItems.IntSItem;
import org.pikater.core.ontology.subtrees.search.searchItems.SearchItem;
import org.pikater.core.ontology.subtrees.search.searchItems.SetSItem;
import org.pikater.core.options.GridSearch_SearchBox;

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
    ArrayList<String> values = null;

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
            List<String> v = new ArrayList<String>();
            for (String s: values.get(i).split(",")) {
                v.add(s);
            }
            ss.setValues(v);
            ret.add(ss);
        }
        
        System.err.println("ret: " + ret.size());
        
        return ret;
    }

    private ArrayList<String> generateValues() {
        
        ArrayList<String> vals = new ArrayList<String>();
        
        ArrayList<ArrayList<String>> valsForOpts = new ArrayList<ArrayList<String>>();

        for (SearchItem si : schema ) {
            Integer tries = si.getNumber_of_values_to_try();
            if (tries == 0) {
                tries = defaultTries;
            }
            ArrayList<String> valsForItem = new ArrayList<String>();
            if (si instanceof IntSItem) {
                IntSItem isi = (IntSItem)si;
                if (isi.getMax() - isi.getMin() < tries) {
                    for (int i = isi.getMin(); i <= isi.getMax(); i++) {
                        valsForItem.add(Integer.toString(i));
                    }
                } else {
                    if (linearSteps) {
                        double stepSize = 1.0 * (isi.getMax() - isi.getMin()) / (tries - 1);
                        for (int i = 0; i < tries; i++) {
                            String add = Integer.toString((int) Math.round(isi.getMin() + i * stepSize));
                            if (!valsForItem.contains(add)) {
                                valsForItem.add(add);
                            }
                        }
                    }
                    if (logSteps) {
                        double normalization = isi.getMin() < logZero ? isi.getMin() - logZero : 0.0;
                        double start = Math.log(isi.getMin() - normalization);
                        double range = Math.log(isi.getMax() - normalization) - Math.log(isi.getMin() - normalization);
                        double stepSize = range / (tries - 1);
                        for (int i = 0; i < tries; i++) {
                            String add = Integer.toString((int) Math.round(Math.exp(start + i * stepSize) + normalization));
                            if (!valsForItem.contains(add)) {
                                valsForItem.add(add);
                            }
                        }
                    }
                }
            }

            if (si instanceof FloatSItem) {
                FloatSItem isi = (FloatSItem)si;
                if (linearSteps) {
                    double stepSize = 1.0 * (isi.getMax() - isi.getMin()) / (tries - 1);
                    for (int i = 0; i < tries; i++) {
                        String add = Double.toString(isi.getMin() + i * stepSize);
                        if (!valsForItem.contains(add)) {
                            valsForItem.add(add);
                        }
                    }
                }
                if (logSteps) {
                    double normalization = isi.getMin() < logZero ? isi.getMin() - logZero : 0.0;
                    double start = Math.log(isi.getMin() - normalization);
                    double range = Math.log(isi.getMax() - normalization) - Math.log(isi.getMin() - normalization);
                    double stepSize = range / (tries - 1);
                    for (int i = 0; i < tries; i++) {
                        String add = Double.toString(Math.exp(start + i * stepSize) + normalization);
                        if (!valsForItem.contains(add)) {
                            valsForItem.add(add);
                        }
                    }
                }
            }

            if (si instanceof BoolSItem) {
                valsForItem.add("True");
                valsForItem.add("False");
            }

            if (si instanceof SetSItem) {
                SetSItem ssi = (SetSItem) si;
                for (int i = 0; i < tries && i < ssi.getSet().size(); i++) {
                    valsForItem.add(ssi.getSet().get(i).toString());
                }
            }
            valsForOpts.add(valsForItem);
        }
        
        System.err.println("valsForOpts.size(): " + valsForOpts.size());
        
        vals.addAll(valsForOpts.get(0));
        
        for (int i = 1; i < valsForOpts.size(); i++) {
            ArrayList<String> newVals = new ArrayList<String>();
            for (int j = 0; j < vals.size(); j++) {
                for (int k = 0; k < valsForOpts.get(i).size(); k++) {
                    newVals.add(vals.get(j) + "," + valsForOpts.get(i).get(k));
                    System.err.println("VALUES: " + vals.get(j) + "," + valsForOpts.get(i).get(k));
                }
            }
            vals = newVals;
        }
        
        for (String v : vals) {
            System.err.println("VALUES: " + v);
        }
        
        return vals;
    }

    @Override
    protected void updateFinished(float[][] evaluations) {}

    @Override
    protected void loadSearchOptions() { 
        List<NewOption> search_options = getSearch_options();
        
        for (NewOption next : search_options) {

			Value valueI = next.convertToSingleValue();
			
            if (next.getName().equals("N")) {
            	IntegerValue value = (IntegerValue) valueI.getValue();
                defaultTries = value.getValue();
            }
            if (next.getName().equals("B")) {
            	IntegerValue value = (IntegerValue) valueI.getValue();
                query_block_size = value.getValue();
            }
            //if (next.getName().equals("L")) {
            //   linearSteps = Boolean.parseBoolean(next.getValue());
            //}
            //if (next.getName().equals("G")) {
            //    logSteps = Boolean.parseBoolean(next.getValue());
            //}
            if (next.getName().equals("Z")) {
               	IntegerValue value = (IntegerValue) valueI.getValue();
                logZero = value.getValue();
            }
        }
        schema = getSchema();
    }

}
