package cl.dlab.abm.core.parser;

public class FracNode extends OperatorNode
{
	public FracNode(String name, Parser parser) throws Exception
	{
		super(name, parser);
	}
	@Override
	protected Node getLeftNode() throws Exception
	{
		return parserNode();
	}
	public String toJava(boolean condition, boolean getterAndSetter)
	{
		// TODO Auto-generated method stub
		return "(" + left.toJava(condition, getterAndSetter) + ") / (" + right.toJava(condition, getterAndSetter) + ")";
	}
	
}
