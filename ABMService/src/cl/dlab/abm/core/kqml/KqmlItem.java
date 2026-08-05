package cl.dlab.abm.core.kqml;

import java.io.Serializable;

import cl.dlab.abm.core.model.Agent;
import cl.dlab.abm.core.model.Model;

@SuppressWarnings("serial")
public class KqmlItem implements Serializable
{
	private Model model;
	private Agent agent1;
	private Agent agent2;
	public KqmlItem(Model model, Agent agent1, Agent agent2)
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
	public Agent getAgent1()
	{
		return agent1;
	}
	/**
	 * @return the agent2
	 */
	public Agent getAgent2()
	{
		return agent2;
	}
}
