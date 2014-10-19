package org.pikater.core.ontology.subtrees.attribute;

import jade.content.Concept;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

import java.util.Date;

import weka.core.FastVector;
import weka.core.Utils;

public class Attribute implements Concept {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4335994730042008739L;
	public final static String NUMERIC_TYPE = "NUMERIC";
	public final static String NOMINAL_TYPE = "NOMINAL";
	public final static String STRING_TYPE = "STRING";
	public final static String DATE_TYPE = "DATE";
	public final static String RELATIONAL_TYPE = "RELATIONAL";
	private String name;
	// nominal/numeric/string
	private String type;
	private List values;
	private String dateFormat;

	/**
	 * @return the dateFormat
	 */
	public String getDate_format() {
		return dateFormat;
	}

	/**
	 * the dateFormat to set
	 */
	public void setDate_format(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the values
	 */
	public List getValues() {
		return values;
	}

	/**
	 * the values to set
	 */
	public void setValues(List values) {
		this.values = values;
	}

	// =======================================
	public weka.core.Attribute toWekaAttribute() {

		if (values != null && !values.isEmpty()) {
			FastVector myNominalValues = new FastVector();
			Iterator itr = values.iterator();
			while (itr.hasNext()) {
				String val = (String) itr.next();
				myNominalValues.addElement(val);
			}

			return new weka.core.Attribute(name, myNominalValues);
		} else {
			if (type.equals(DATE_TYPE)) {
				return new weka.core.Attribute(name, dateFormat);
			} else if (type.equals(RELATIONAL_TYPE)) {

				weka.core.Instances winst = null;
				return new weka.core.Attribute(name, winst);
			} else {
				return new weka.core.Attribute(name);
			}
		}
	}

	public void fillWekaAttribute(weka.core.Attribute wattr) {
		setName(wattr.name());
		switch (wattr.type()) {
		case weka.core.Attribute.NUMERIC:
			setType(NUMERIC_TYPE);
			break;
		case weka.core.Attribute.NOMINAL:
			setType(NOMINAL_TYPE);
			break;
		case weka.core.Attribute.STRING:
			setType(STRING_TYPE);
			break;
		case weka.core.Attribute.DATE:
			setType(DATE_TYPE);
			setDate_format(wattr.getDateFormat());
			break;
		case weka.core.Attribute.RELATIONAL:
			setType(RELATIONAL_TYPE);

			break;
		default:
			// error
		}
		List attrValues = new ArrayList();
		for (int j = 0; j < wattr.numValues(); j++) {
			attrValues.add(wattr.value(j));
		}
		setValues(attrValues);
	}

	String stringValue(double _dval) {
		if (values != null && !values.isEmpty()) {
			return Utils.quote((String) values.get((int) _dval));
		} else if (type.equals(NUMERIC_TYPE)) {
			return Utils.doubleToString(_dval, 6);
		} else if (type.equals(DATE_TYPE)) {
			Date d = new Date((long) _dval);
			return Utils.quote(d.toString());
		}
		return null;
	}
}
