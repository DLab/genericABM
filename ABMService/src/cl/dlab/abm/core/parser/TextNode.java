package cl.dlab.abm.core.parser;

public class TextNode extends Node
{
	public TextNode(String name, Parser parser) throws Exception
	{
		super(name, parser);
		int index = exp.indexOf(name, pos + 1);
		this.name = exp.substring(pos, index);
		pos = index + 1;
		parser.pos = pos;
	}
	@Override
	public String toJava(boolean condition, boolean getterAndSetter)
	{
		return "\"" + name + "\"";
	}
}
