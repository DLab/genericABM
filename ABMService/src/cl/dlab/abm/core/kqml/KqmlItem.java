package cl.dlab.abm.core.kqml;

import cl.dlab.abm.core.model.Model;

public class KqmlItem
{
	private Model model;
	private String agent1;
	private String agent2;
	public KqmlItem(Model model, String agent1, String agent2)
	{
		this.model = model;
		this.agent1 = agent1;
		this.agent2 = agent2;
	}
	/**
	 * @return the model
	 */
	public Model getModel()
	{
		return model;
	}
	/**
	 * @return the agent1
	 */
	public String getAgent1()
	{
		return agent1;
	}
	/**
	 * @return the agent2
	 */
	public String getAgent2()
	{
		return agent2;
	}
}
