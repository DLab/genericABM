package cl.dlab.abm.core.model;

import java.util.HashMap;

public class Data 
{
	private int numPasos;
	public Data(int numPasos)
	{
		this.numPasos = numPasos;
	}
	public void addValues(int paso, Model model)
	{
		
	}
	/**
	 * @return the numPasos
	 */
	public int getNumPasos()
	{
		return numPasos;
	}
	/**
	 * @param numPasos the numPasos to set
	 */
	public void setNumPasos(int numPasos)
	{
		this.numPasos = numPasos;
	}
	public HashMap<String, double[]> getProperties()
	{
		return new HashMap<String, double[]>();
	}
	public HashMap<String, double[]> getLastProperties()
	{
		return new HashMap<String, double[]>();
	}
	
}
