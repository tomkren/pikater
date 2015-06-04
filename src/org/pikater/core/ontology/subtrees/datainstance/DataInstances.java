package org.pikater.core.ontology.subtrees.datainstance;

import jade.content.Concept;
import weka.core.FastVector;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.attribute.Attribute;
import org.pikater.core.ontology.subtrees.attribute.Instance;

public class DataInstances implements Concept {

	private static final long serialVersionUID = 4166896666680482675L;
	
	private List<Attribute> attributes;
	private List<Instance> instances;
	private String name;
	private int classIndex;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the attributes
	 */
	public List<Attribute> getAttributes() {
		return attributes;
	}

	/**
	 * The attributes to set
	 */
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return the instaces
	 */
	public List<Instance> getInstances() {
		return instances;
	}

	/**
	 * The instaces to set
	 */
	public void setInstances(List<Instance> instances) {
		this.instances = instances;
	}

	// =============================

	/**
	 * Get the index of Class
	 */
	public int getClassIndex() {
		int index;
		if (classIndex == -1) {
			index = getAttributes().size() - 1;
		} else {
			index = classIndex;
		}
		
		return index;
	}

	/**
	 * Set Class-Index
	 */
	public void setClassIndex(int classIndex) {
		this.classIndex = classIndex;
	}

	public weka.core.Instances toWekaInstances() {
		// attributes
		FastVector wattrs = new FastVector();
		for (Attribute attr : attributes) {
			wattrs.addElement(attr.toWekaAttribute());
		}
		// data instances
		weka.core.Instances winsts = new weka.core.Instances(name, wattrs,
				instances.size());

		for (Instance instanceI : instances) {

			double[] vals = new double[wattrs.size()];
			for (int i = 0; i < wattrs.size(); i++) {
				double val = instanceI.getValues().get(i);
				if ( instanceI.getMissing().get(i) ) {
					vals[i] = weka.core.Instance.missingValue();
				} else {
					vals[i] = val;
				}
			}
			weka.core.Instance winst = new weka.core.Instance(1, vals);
			winst.setDataset(winsts);
			winsts.add(winst);
		}
		winsts.setClassIndex(this.classIndex);
		return winsts;
	}

	public void fillWekaInstances(weka.core.Instances winsts) {
		// set name
		setName(winsts.relationName());
		// set attributes
		List<Attribute> ontoAttrs = new ArrayList<Attribute>();
		for (int i = 0; i < winsts.numAttributes(); i++) {
			Attribute a = new Attribute();
			a.fillWekaAttribute(winsts.attribute(i));
			ontoAttrs.add(a);
		}
		setAttributes(ontoAttrs);


		// set instances
		List<Instance> ontoInsts = new ArrayList<Instance>();
		for (int i = 0; i < winsts.numInstances(); i++) {
			Instance inst = new Instance();

			// add id
			inst.setId(i);

			weka.core.Instance winst = winsts.instance(i);

			List<Double> instvalues = new ArrayList<Double>();
			List<Boolean> instmis = new ArrayList<Boolean>();
			for (int j = 0; j < winst.numValues(); j++) {
				if (winst.isMissing(j)) {
					instvalues.add(new Double(0.0));
					instmis.add(Boolean.valueOf(true));
				} else {
					instvalues.add(new Double(winst.value(j)));
					instmis.add(Boolean.valueOf(false));
				}
			}

			inst.setValues(instvalues);
			inst.setMissing(instmis);
			ontoInsts.add(inst);
		}
		setInstances(ontoInsts);
		setClassIndex(winsts.classIndex());

	}

	/**
	 * Returns all instances as a multi-line string
	 */
	@Override
	public String toString() {
		if (instances == null) {
			return "";
		}

		StringBuilder text = new StringBuilder();
		for (Instance inst : instances ) {
			text.append(inst.toString(this));
			text.append('\n');
		}
		return text.toString();
	}

	/**
	 * Returns a value in the table on the row and index
	 */
	public String toString(int row, int index) {
		if (instances == null) {
			return "";
		}
		Instance inst = (Instance) instances.get(row);
		return inst.toString(this, index);
	}

	public Instance getInstanceById(int id){
		for (Instance inst: instances){
			if (inst.getId() == id){
				return inst;
			}
		}
		return null;
	}
}
