package cl.dlab.abm.core.parser;

public class SpecialNode extends Node
{

	public SpecialNode(String name, Parser parser)
	{
		super(name, parser);
	}
	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return "\\" + super.toString();
	}
	@Override
	public String toJava(boolean condition, boolean getterAndSetter)
	{
		String code = super.toJava(condition, getterAndSetter);
		if (code.equals("pi"))
		{
			return "Math.PI";
		}
		else if (code.equals("varnothing"))
		{
			return "null";
		}
		return code;
	}
}
