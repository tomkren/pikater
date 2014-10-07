package org.pikater.shared.util;

/**
 * Immutable number range implementation. Values are nullable.
 * 
 * @author SkyCrawl
 *
 * @param <N> Integer, Float or Double.
 */
public class Interval<N extends Number>
{
	private final N min;
	private final N max;
	
	public Interval(N min, N max)
	{
		this.min = min;
		this.max = max;
		if((min != null) && (max != null) && (min.doubleValue() > max.doubleValue()))
		{
			throw new IllegalArgumentException("Minimum exceeds maximum");
		}
	}

	public N getMin()
	{
		return min;
	}

	public N getMax()
	{
		return max;
	}
}