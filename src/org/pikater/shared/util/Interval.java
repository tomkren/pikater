package org.pikater.shared.util;

public class Interval<T extends Comparable<T>>
{
	public final T min;
	public final T max;
	
	public Interval(T min, T max)
	{
		this.min = min;
		this.max = max;
		if(min.compareTo(max) > 0) // min > max
		{
			throw new IllegalArgumentException();
		}
	}
}
