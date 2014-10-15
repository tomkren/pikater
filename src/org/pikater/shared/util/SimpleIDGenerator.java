package org.pikater.shared.util;

import java.io.Serializable;

/**
 * A simple integer-backed incremental ID generator.
 * 
 * @author SkyCrawl
 */
public class SimpleIDGenerator implements Serializable {
	private static final long serialVersionUID = -8774263381709855657L;

	private Integer currentID;

	public SimpleIDGenerator() {
		reset();
	}

	public Integer getAndIncrement() {
		currentID++;
		return currentID - 1;
	}

	public void reset() {
		currentID = getFirstID();
	}

	public static Integer getFirstID() {
		return 0; // this is required by some of the referencing code
	}
}
