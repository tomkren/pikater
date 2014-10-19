package org.pikater.shared.database.util;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

public class ItemCounter<T> {

	HashMap<T, Integer> hh;

	public ItemCounter() {
		hh = new HashMap<T, Integer>();
	}

	/**
	 * Adds an object to the set of values
	 * @param item The item's object
	 */
	public void addItem(T item) {
		Integer valcount = hh.get(item);
		if (valcount == null) {
			hh.put(item, new Integer(1));
		} else {
			hh.put(item, new Integer(valcount + 1));
		}
	}

	/**
	 * Returns the count of appearances in the set of values for the given object.
	 * @param item The item's object for which we want to get the count of appearance
	 * @return The count of appearance of the object
	 */
	public int getItemCount(T item) {
		Integer valcount = hh.get(item);
		if (valcount == null) {
			return -1;
		} else {
			return valcount.intValue();
		}
	}

	/**
	 * Computes the mode of the stored values
	 * @return The mode of values
	 */
	public T getMode() {
		T currModus = null;
		int modusCount = -1;

		Set<Entry<T, Integer>> values = hh.entrySet();
		Integer val = null;
		for (Entry<T, Integer> entry : values) {
			val = entry.getValue();
			if (val > modusCount) {
				modusCount = val.intValue();
				currModus = entry.getKey();
			}
		}

		return currModus;
	}

	/**
	 * Returns the String representation of the object in the following format:
	 * Value = apple   Count = 2
	 * Value = pear   Count = 3
	 * @return The String representation
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		Set<Entry<T, Integer>> values = hh.entrySet();

		for (Entry<T, Integer> entry : values) {
			sb.append("Value = " + entry.getKey() + "   Count = " + entry.getValue() + "\n");
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		ItemCounter<String> applesvsoranges = new ItemCounter<String>();
		applesvsoranges.addItem("Apple");
		applesvsoranges.addItem("Apple");
		applesvsoranges.addItem("Orange");
		applesvsoranges.addItem("Apple");
		applesvsoranges.addItem("Apple");
		applesvsoranges.addItem("Apple");
		applesvsoranges.addItem("Orange");
		applesvsoranges.addItem("Orange");
		applesvsoranges.addItem("Orange");
		applesvsoranges.addItem("Pear");
		applesvsoranges.addItem("Orange");
		applesvsoranges.addItem("Orange");
		applesvsoranges.addItem("Orange");
		applesvsoranges.addItem("Pear");
		applesvsoranges.addItem("Orange");
		applesvsoranges.addItem("Pear");
		applesvsoranges.addItem("Pear");
		applesvsoranges.addItem("Pear");

		System.out.println("Modus: " + applesvsoranges.getMode());
		System.out.println(applesvsoranges.toString());

		ItemCounter<Double> counter = new ItemCounter<Double>();
		counter.addItem(0.111);
		counter.addItem(0.1110);
		counter.addItem(0.222);
		counter.addItem(0.11123);
		counter.addItem(0.12345);
		counter.addItem(0.12222);
		counter.addItem(0.12222);
		counter.addItem(0.12345);
		counter.addItem(0.12345);
		counter.addItem(0.222);
		counter.addItem(0.1222);
		counter.addItem(0.1222);
		counter.addItem(0.1222);
		counter.addItem(0.1222);
		counter.addItem(0.1333);
		counter.addItem(0.333);
		counter.addItem(0.333);
		counter.addItem(0.333);
		counter.addItem(0.333);

		System.out.println("Modus: " + counter.getMode());
		System.out.println(counter.toString());

	}

}
