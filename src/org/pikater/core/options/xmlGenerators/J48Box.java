package org.pikater.core.options.xmlGenerators;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.options.LogicalUnit;
import org.pikater.shared.experiment.parameters.EnumeratedValueParameter;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.experiment.parameters.ValueParameter;
import org.pikater.shared.util.Interval;

public class J48Box extends LogicalUnit {
	protected J48Box(){
		this.setDisplayName("J48");
		this.setDescription("...");
		this.setOntology(null);
		this.setPicture("picture3.jpg");
		
		
		
		ValueParameter<Boolean> parU=new ValueParameter<Boolean>(false);
		EnumeratedValueParameter<Float> parC=new EnumeratedValueParameter<Float>(
				0.25f,
				new ArrayList<Float>(Arrays.asList(new Float[] {null,null,null,0.0001f,0.1f,0.2f,0.25f,0.3f,0.4f}))
				);
		RangedValueParameter<Integer> parM=new RangedValueParameter<Integer>(2, new Interval<Integer>(1, 10), true);
		ValueParameter<Boolean> parR=new ValueParameter<Boolean>(false);
		EnumeratedValueParameter<Integer> parN=new EnumeratedValueParameter<Integer>(
				3,
				new ArrayList<Integer>(Arrays.asList(new Integer[] {null,1,2,3,4,5}))
				);
		ValueParameter<Boolean> parB=new ValueParameter<Boolean>(false);
		ValueParameter<Boolean> parS=new ValueParameter<Boolean>(false);
		ValueParameter<Boolean> parA=new ValueParameter<Boolean>(false);
		RangedValueParameter<Integer> parQ=new RangedValueParameter<Integer>(1, new Interval<Integer>(1, Integer.MAX_VALUE), true);
		
		this.addParameter(parU);
		this.addParameter(parC);
		this.addParameter(parM);
		this.addParameter(parR);
		this.addParameter(parN);
		this.addParameter(parB);
		this.addParameter(parS);
		this.addParameter(parA);
		this.addParameter(parQ);
	}
}
