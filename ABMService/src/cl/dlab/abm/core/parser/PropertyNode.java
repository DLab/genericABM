package cl.dlab.abm.core.parser;

import java.util.HashMap;

public class PropertyNode extends Node
{
	private String index;
	public PropertyNode(String name, Parser parser)
	{
		super(name, parser);
		if (parser.pos < parser.exp.length())
		{
			char c = parser.exp.charAt(parser.pos);
			if (c == '_')
			{
				parser.pos++;
				index = parser.getWord();
			}
			else
			{
				index = "";
			}
		}
		else
		{
			index = "";
		}
	}
	@Override
	public String toJava(boolean condition, boolean getterAndSetter)
	{
		return toJava(condition, getterAndSetter, true);
	}
	@SuppressWarnings("unchecked")
	public String toJava(boolean condition, boolean getterAndSetter, boolean getter)
	{
		String functionName = super.toJava(condition, getterAndSetter);
		HashMap<String, Object> hs = (HashMap<String, Object>)parser.alias.get(functionName);
		StringBuilder objName = new StringBuilder(((ObjectName)hs.get("objName")).getName());
		if (index != "")
		{
			if (objName.charAt(objName.length() - 1) == ')')
			{
				objName.insert(objName.length() - 1, index);
			}
			else
			{
				objName.append(index);
			}
		}
		if (hs.get("variableTypeId").equals(-2))
		{
			return objName.toString();
		}
		if (getterAndSetter)
		{
			return objName + "." + (getter ? Util.getGetterName((String)hs.get("name")) : Util.getSetterName((String)hs.get("name")));
		}
		else
		{
			return objName + "." + hs.get("name");
		}
	}
}
