package cl.dlab.abm.core.model;

public class Action
{
	private String name;
	private String description;
	private Rule rule;
	
	public void execute(Model model, Agent agent1, Agent agent2) throws Exception
	{
	}
	public void execute(Model model, Agent agent1) throws Exception
	{
	}
	public void execute(Model model) throws Exception
	{
	}
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
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	/**
	 * @return the rule
	 */
	public Rule getRule()
	{
		return rule;
	}
	/**
	 * @param rule the rule to set
	 */
	public void setRule(Rule rule)
	{
		this.rule = rule;
	}
}
