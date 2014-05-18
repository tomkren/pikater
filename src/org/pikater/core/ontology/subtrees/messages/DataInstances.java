package org.pikater.core.ontology.subtrees.messages;

import jade.content.Concept;
import weka.core.FastVector;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.attribute.Attribute;
import org.pikater.core.ontology.subtrees.attribute.Instance;

public class DataInstances implements Concept {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4166896666680482675L;
	private List<Attribute> attributes;
	private List<Instance> instances;
	private String name;
	private int class_index;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
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
	 * @param attributes
	 *            the attributes to set
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
	 * @param instances
	 *            the instaces to set
	 */
	public void setInstances(List<Instance> instances) {
		this.instances = instances;
	}

	// =============================

	public int getClass_index() {
		return class_index == -1 ? getAttributes().size() - 1 : class_index;
	}

	public void setClass_index(int classIndex) {
		class_index = classIndex;
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

		for (Instance inst : instances) {

			double[] vals = new double[wattrs.size()];
			for (int i = 0; i < wattrs.size(); i++) {
				double val = inst.getValues().get(i);
				if ( inst.getMissing().get(i) ) {
					vals[i] = weka.core.Instance.missingValue();
				} else {
					vals[i] = val;
				}
			}
			weka.core.Instance winst = new weka.core.Instance(1, vals);
			winst.setDataset(winsts);
			winsts.add(winst);
		}
		winsts.setClassIndex(this.class_index);
		return winsts;
	}

	public void fillWekaInstances(weka.core.Instances winsts) {
		// set name
		setName(winsts.relationName());
		// set attributes
		List<Attribute> onto_attrs = new ArrayList<Attribute>();
		for (int i = 0; i < winsts.numAttributes(); i++) {
			Attribute a = new Attribute();
			a.fillWekaAttribute(winsts.attribute(i));
			onto_attrs.add(a);
		}
		setAttributes(onto_attrs);

		// set instances
		List<Instance> onto_insts = new ArrayList<Instance>();
		for (int i = 0; i < winsts.numInstances(); i++) {
			Instance inst = new Instance();
			weka.core.Instance winst = winsts.instance(i);

			List<Double> instvalues = new ArrayList<Double>();
			List<Boolean> instmis = new ArrayList<Boolean>();
			for (int j = 0; j < winst.numValues(); j++) {
				if (winst.isMissing(j)) {
					instvalues.add(new Double(0.0));
					instmis.add(new Boolean(true));
				} else {
					instvalues.add(new Double(winst.value(j)));
					instmis.add(new Boolean(false));
				}
			}

			inst.setValues(instvalues);
			inst.setMissing(instmis);
			onto_insts.add(inst);
		}
		setInstances(onto_insts);
		setClass_index(winsts.classIndex());

	}

	/*
	 * returns all instances as a multi-line string
	 */
	@Override
	public String toString() {
		if (instances == null) {
			return "";
		}

		StringBuffer text = new StringBuffer();
		for (Instance inst : instances ) {
			text.append(inst.toString(this));
			text.append('\n');
		}
		return text.toString();
	}

	/* returns a value in the table on the row and index */
	public String toString(int row, int index) {
		if (instances == null) {
			return "";
		}
		Instance inst = (Instance) instances.get(row);
		return inst.toString(this, index);
	}
}