package cl.dlab.abm.core.model.function;

import java.util.Random;

public class LogNormal implements Function, Cloneable
{

	private double mean;
	private double stddev;
	@SuppressWarnings("unused")
	private double lastValue;
	private Random randGen;

	public LogNormal(double... values)
	{
		this(values[0], values[1]);
	}

	public LogNormal(double mean, double stddev)
	{
		this.mean = mean;
		this.stddev = stddev;
		this.lastValue = -1;
		this.randGen = new Random();
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

	
	private double logNormal()
	{
		if (lastValue != -1)
		{
			return lastValue;
		}
		if (mean == 0 && stddev == 0)
		{
			return 0D;
		}
		double varx = Math.pow(stddev, 2);
		double ess = Math.log(1.0 + (varx / Math.pow(mean, 2)));
		double mu = Math.log(mean) - (0.5 * Math.pow(ess, 2));
		return lastValue = (Math.pow(2.71828, (mu + (ess * randGen.nextGaussian()))));

	}

	@Override
	public double getValue(double... params)
	{
		return logNormal();
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
	public String getName()
	{
		return "LogNormal";
	}
	public double[] getParams()
	{
		return new double[] {mean, stddev};
	}

	
}
