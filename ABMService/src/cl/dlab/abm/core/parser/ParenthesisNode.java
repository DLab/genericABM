package cl.dlab.abm.core.parser;

public class ParenthesisNode extends GroupNode
{

	public ParenthesisNode(String name, Parser parser) throws Exception
	{
		super(name, parser);
	}
	protected char endCharacter()
	{
		return R_PARENTHESIS;
	}
	@Override
	public String toString()
	{
		return "(" + node.toString() + ")";
	}
	@Override
	public String toJava(boolean condition, boolean getterAndSetter)
	{
		return "(" + (node != null ? node.toJava(condition, getterAndSetter) : "") + ")";
	}
	@Override
	public Node getOperationNode()
	{
		return node.getOperationNode();
	}
	
}
