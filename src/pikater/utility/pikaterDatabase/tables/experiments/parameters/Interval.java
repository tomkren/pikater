package pikater.utility.pikaterDatabase.tables.experiments.parameters;

public class Interval<T>
{
	public final T min;
	public final T max;
	
	public Interval(T min, T max)
	{
		this.min = min;
		this.max = max;
	}
}
