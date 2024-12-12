package cl.dlab.abm.core.model;

public class Rule
{
	private String name;
	private String description;
	protected Action trueAction;
	protected Action falseAction;
	protected Action beforeAction;
	protected Action afterAction;
	protected boolean initial;
	protected String iterate;
	protected String meet;

	public boolean isOk(Model model, Agent agent1, Agent agent2)
	{
		return true;
	}
	public boolean isOk(Model model, Agent agent1)
	{
		return true;
	}
	public boolean isOk(Model model)
	{
		return true;
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
	 * @return the trueAction
	 */
	public Action getTrueAction()
	{
		return trueAction;
	}

	/**
	 * @param trueAction the trueAction to set
	 */
	public void setTrueAction(Action trueAction)
	{
		this.trueAction = trueAction;
	}

	/**
	 * @return the falseAction
	 */
	public Action getFalseAction()
	{
		return falseAction;
	}

	/**
	 * @param falseAction the falseAction to set
	 */
	public void setFalseAction(Action falseAction)
	{
		this.falseAction = falseAction;
	}

	/**
	 * @return the iterate
	 */
	public String getIterate()
	{
		return iterate;
	}

	/**
	 * @return the meet
	 */
	public String getMeet()
	{
		return meet;
	}
	/**
	 * @return the beforeAction
	 */
	public Action getBeforeAction()
	{
		return beforeAction;
	}
	/**
	 * @param beforeAction the beforeAction to set
	 */
	public void setBeforeAction(Action beforeAction)
	{
		this.beforeAction = beforeAction;
	}
	/**
	 * @return the afterAction
	 */
	public Action getAfterAction()
	{
		return afterAction;
	}
	/**
	 * @param afterAction the afterAction to set
	 */
	public void setAfterAction(Action afterAction)
	{
		this.afterAction = afterAction;
	}


}
