package cl.dlab.abm.core.model;

public class Graph
{
	private String name;
	private GraphType graphType;
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the graphType
	 */
	public GraphType getGraphType()
	{
		return graphType;
	}

	/**
	 * @param graphType the graphType to set
	 */
	public void setGraphType(GraphType graphType)
	{
		this.graphType = graphType;
	}
}
