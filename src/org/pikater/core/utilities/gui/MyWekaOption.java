package org.pikater.core.utilities.gui;

import java.io.Serializable;

public class MyWekaOption implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8437447557561297247L;

	public enum dataType {
		INT, FLOAT, BOOLEAN, MIXED
	}

	public boolean mutable = false;
	public float lower;
	public float upper;
	public float numArgsMin;
	public float numArgsMax;
	public dataType type;

	public String description;
	public String name;
	public int numArguments;
	public String synopsis;
	public String defaultValue;

	public String[] set;
	public boolean isASet = false;

	public MyWekaOption(String arg0, String arg1, int arg2, String arg3,
			dataType type, int numArgsMin, int numArgsMax, String range,
			String defaultValue, String rest) {

		this.description = arg0;
		this.name = arg1;
		this.numArguments = arg2;
		this.synopsis = arg3;

		this.numArgsMin = numArgsMin;
		this.numArgsMax = numArgsMax;

		this.type = type;

		this.defaultValue = defaultValue;

		String delims = "[, ]+";
		String[] params = rest.split(delims);

		if (range.equals("r")) {
			float maxValue;
			if (params[1].equals("MAXINT")) {
				maxValue = new Float(Integer.MAX_VALUE);
			} else {
				maxValue = new Float(params[1]);
			}

			lower = new Float(params[0]);
			upper = maxValue;
		}
		if (range.equals("s")) {
			isASet = true;
			set = new String[params.length];
			System.arraycopy(params, 0, set, 0, params.length);
		}

	}

}
