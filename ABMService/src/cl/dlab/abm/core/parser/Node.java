package cl.dlab.abm.core.parser;

import java.util.HashMap;

public class Node extends Parser
{
	protected String name;
	protected Parser parser;
	public Node(String name, Parser parser)
	{
		super(parser.exp, parser.alias);
		this.parser = parser;
		this.pos = parser.pos;
		this.name = name;
	}
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	@Override
	public String toString()
	{
		return name;
	}
	public String toJava(boolean condition, boolean getterAndSetter)
	{
		if (name.equals("e"))
		{
			return "Math.E";
		}
		else if (name.equals("E"))
		{
			return "10";
		}
		return name;
	}
	public Node getOperationNode()
	{
		return this;
	}
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getAlias(boolean condition, boolean getterAndSetter)
	{
		return (HashMap<String, Object>)parser.alias.get(name);
	}
}
