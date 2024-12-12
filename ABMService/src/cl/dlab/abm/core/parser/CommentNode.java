package cl.dlab.abm.core.parser;

public class CommentNode extends OperatorNode
{
	public CommentNode(String name, Parser parser) throws Exception
	{
		super(name, parser);
		this.name = exp.substring(pos);
		pos = exp.length();
		parser.pos = pos;
	}
	@Override
	protected Node parserRightNode() throws Exception
	{
		return this.node;
	}
	@Override
	public String toJava(boolean condition, boolean getterAndSetter)
	{
		return left.toJava(condition, getterAndSetter) + (condition ? "" : ";") + " /*" + name + "*/";
	}
}
