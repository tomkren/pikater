package org.pikater.core.ontology.subtrees.datainstance;

import jade.content.Concept;
import weka.core.FastVector;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.attribute.Attribute;
import org.pikater.core.ontology.subtrees.attribute.Instance;
import weka.core.Instances;

public class DataInstances implements Concept {

	private static final long serialVersionUID = 4166896666680482675L;
	
	private List<Attribute> attributes;
	private List<Attribute> privateAttributes;
	private List<Instance> instances;
	private String name;
	private int classIndex;

	public DataInstances createEmptyCopy() {
		DataInstances res = new DataInstances();
		res.attributes = attributes;
		res.privateAttributes = privateAttributes;
		res.name = name;
		res.classIndex = classIndex;
		return res;
	}

	public void add(Instance inst) {
		instances.add(inst);
	}

	public List<Double> extractColumn(int i) {
		List<Double> values = new ArrayList<Double>();
		for (Instance ins : instances) {
			values.add(ins.getValue(i));
		}
		return values;
	}

	public List<Double> extractPrivateColumn(int i) {
		List<Double> values = new ArrayList<Double>();
		for (Instance ins : instances) {
			values.add(ins.getPrivateValue(i));
		}
		return values;
	}

	public List<Double> extractColumn(String name) {

		if (name.startsWith("__")) {
			for (int i = 0; i < privateAttributes.size(); i++) {
				if (privateAttributes.get(i).getName().equals(name))
					return extractPrivateColumn(i);
			}
		}
		else {
			for (int i = 0; i < attributes.size(); i++) {
				if (attributes.get(i).getName().equals(name))
					return extractColumn(i);
			}
		}

		return null;
	}

	public void mergePublicData(Instances winst) {
		DataInstances tmp = new DataInstances();
		tmp.fillWekaInstances(winst);

		assert this.instances.size() == tmp.instances.size();

		attributes = tmp.attributes;
		for (int i = 0; i < instances.size(); i++) {
			instances.get(i).setValues(tmp.instances.get(i).getValues());
		}
	}

	public boolean hasColumn(String name) {

		if (name.startsWith("__")) {
			for (Attribute a : privateAttributes) {
				if (a.getName().equals(name))
					return true;
			}
		}
		else {
			for (Attribute a : attributes) {
				if (a.getName().equals(name))
					return true;
			}
		}

		return false;

	}

	public List<Double> extractClassColumn() {
		return extractColumn(classIndex);
	}

	public void insertClassColumn(List<Double> predictions) {
		for (int i = 0; i < numInstances(); i++) {
			instances.get(i).setPrediction(predictions.get(i));
		}
	}

	public List<Attribute> getPrivateAttributes() {
		return privateAttributes;
	}

	public void setPrivateAttributes(List<Attribute> privateAttributes) {
		this.privateAttributes = privateAttributes;
	}

	public int numInstances() {
		return instances.size();
	}

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
		return toWekaInstances(true);
	}

	public weka.core.Instances toWekaInstances(boolean ignorePrivate) {
		// attributes
		FastVector wattrs = new FastVector();

		if (!ignorePrivate) {
			for (Attribute attr : privateAttributes) {
				wattrs.addElement(attr.toWekaAttribute());
			}
		}

		for (Attribute attr : attributes) {
			wattrs.addElement(attr.toWekaAttribute());
		}


		// data instances
		weka.core.Instances winsts = new weka.core.Instances(name, wattrs, instances.size());

		for (Instance instanceI : instances) {

			ArrayList<Double> vals = new ArrayList<>();

			if (!ignorePrivate) {
				for (int i = 0; i < privateAttributes.size(); i++) {
					double val = instanceI.getPrivateValues().get(i);
					vals.add(val);
				}
			}

			for (int i = 0; i < attributes.size(); i++) {
				double val = instanceI.getValues().get(i);
				if ( instanceI.getMissing().get(i) ) {
					vals.add(weka.core.Instance.missingValue());
				} else {
					vals.add(val);
				}
			}
			assert wattrs.size() == vals.size();
			weka.core.Instance winst = new weka.core.Instance(1, toDoubleArray(vals));
			winst.setDataset(winsts);
			winsts.add(winst);
		}
		winsts.setClassIndex(this.classIndex);
		return winsts;
	}

	private double[] toDoubleArray(ArrayList<Double> list) {
		double[] vals = new double[list.size()];
		for (int i = 0; i < vals.length; i++) {
			vals[i] = list.get(i);
		}
		return vals;
	}

	public void fillWekaInstances(weka.core.Instances winsts) {
		// set name
		setName(winsts.relationName());
		// set attributes
		List<Attribute> ontoAttrs = new ArrayList<Attribute>();
		List<Attribute> privateAttrs = new ArrayList<>();
		for (int i = 0; i < winsts.numAttributes(); i++) {
			Attribute a = new Attribute();
			a.fillWekaAttribute(winsts.attribute(i));
			if (a.getName().startsWith("__"))
				privateAttrs.add(a);
			else
				ontoAttrs.add(a);
		}
		setAttributes(ontoAttrs);
		privateAttributes=privateAttrs;

		boolean computeID = false;

		if (!hasColumn("__id")) {
			Attribute a = new Attribute();
			a.setName("__id");
			a.setType(Attribute.NUMERIC_TYPE);
			privateAttributes.add(a);
			computeID = true;
		}

		// set instances
		List<Instance> ontoInsts = new ArrayList<Instance>();
		for (int i = 0; i < winsts.numInstances(); i++) {
			Instance inst = new Instance();

			weka.core.Instance winst = winsts.instance(i);

			// add id

			List<Double> instvalues = new ArrayList<Double>();
			List<Boolean> instmis = new ArrayList<Boolean>();
			List<Double> priValues = new ArrayList<>();
			for (int j = 0; j < winst.numValues(); j++) {
				if (winsts.attribute(j).name().startsWith("__")) {
					priValues.add(new Double(winst.value(j)));
				}
				else {
					if (winst.isMissing(j)) {
						instvalues.add(new Double(0.0));
						instmis.add(Boolean.valueOf(true));
					} else {
						instvalues.add(new Double(winst.value(j)));
						instmis.add(Boolean.valueOf(false));
					}
				}
			}

			inst.setValues(instvalues);
			inst.setMissing(instmis);
			inst.setPrivateValues(priValues);
			ontoInsts.add(inst);

			if (computeID) {
				priValues.add(i*1.0);
			}
		}
		setInstances(ontoInsts);
		setClassIndex(winsts.classIndex());

		int IDIndex = 0;
		for (int i = 0; i < privateAttributes.size(); i++) {
			if (privateAttributes.get(i).getName().equals("__id")) {
				IDIndex = i;
				break;
			}
		}

		for (Instance inst : instances) {
			inst.setIDIdx(IDIndex);
			inst.setId(inst.getPrivateValue(IDIndex).intValue());
		}

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

	public void sortById() {
		Collections.sort(instances, new Comparator<Instance>() {
			@Override
			public int compare(Instance o1, Instance o2) {
				return Integer.compare(o1.getId(), o2.getId());
			}
		});
	}
}
