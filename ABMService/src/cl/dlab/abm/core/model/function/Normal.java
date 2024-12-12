package cl.dlab.abm.core.model.function;

import java.util.Random;

public class Normal implements Function, Cloneable
{
	private double stddev;
	private double mean;
	private double lastValue;
	private Random random = new Random();

	public Normal(double... params)
	{
		this(params[0], params[1]);
	}

	
	public Normal(double mean, double stddev)
	{
		this.stddev = stddev;
		this.mean = mean;
		this.lastValue = -1;
		this.random = new Random();
	}

	@Override
	public void setValues(double... params)
	{
		mean = params[0];
		if (params.length > 1)
		{
			stddev = params[1];
		}
	}


	private double getNextValue(double[] params)
	{
		double value;
		do
		{
			value = stddev * random.nextGaussian() + (mean == -1 ? params[0] : mean);
		}
		while (value < 0 || value > 1);
		return value;
	}
	@Override
	public double getValue(double... params)
	{
		if (lastValue != -1)
		{
			return lastValue;
		}
		
		return lastValue = getNextValue(params);
	}

	@Override
	public void clear()
	{
		this.lastValue = -1;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException 
	{
		return super.clone();
	}
	@Override
	public String toString()
	{
		return lastValue + "**" + stddev + "**" + mean;
	}
	public String getName()
	{
		return "Normal";
	}
	public double[] getParams()
	{
		return new double[] {mean, stddev};
	}
}
