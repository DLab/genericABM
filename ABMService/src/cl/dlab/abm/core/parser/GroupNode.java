package cl.dlab.abm.core.parser;

public class GroupNode extends Node
{
	private boolean findRGroup;
	public GroupNode(String name, Parser parser) throws Exception
	{
		super(name, parser);
		findRGroup = false;
		while(!findRGroup)
		{
			Node node = parserNode();
			if (node != null)
			{
				this.node = node;
			}
			else
			{
				pos--;
			}
		}
		
		parser.pos = pos + 1;
	}
	
	protected boolean findCharacter(char c)
	{
		return findRGroup = (c == endCharacter());
	}
	protected char endCharacter()
	{
		return R_GROUP;
	}
	@Override
	public String toString()
	{
		return node.toString();
	}
	@Override
	public String toJava(boolean condition, boolean getterAndSetter)
	{
		return node.toJava(condition, getterAndSetter);
	}
	@Override
	public Node getOperationNode()
	{
		return node.getOperationNode();
	}
	
}
