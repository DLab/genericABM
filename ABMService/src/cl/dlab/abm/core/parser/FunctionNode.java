package cl.dlab.abm.core.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class FunctionNode extends Node
{
	private String index;
	private ArrayList<Node> parameters;
	public FunctionNode(String name, Parser parser) throws Exception
	{
		super(name, parser);
		char c = parser.exp.charAt(parser.pos);
		if (c == '_')
		{
			parser.pos++;
			index = parser.getWord();
			pos = parser.pos;
		}
		else
		{
			index = "";
		}
		
		int index = parser.exp.indexOf("(", pos);
		int index0 = index; 
		int n = 1;
		int j = -1, z = -1;
		this.parameters = new ArrayList<Node>();
		while(n != 0)
		{
			j = parser.exp.indexOf("(", index + 1);
			z = parser.exp.indexOf(")", index + 1);
			if (j >= 0 && (z == -1 || j < z))
			{
				n = n + 1;
				index = j;
			}
			else if (z >= 0)
			{
				n--;
				index = z;
				if (n == 0)
				{
					String[] params = parser.exp.substring(index0 + 1, z).split("[,]");
					LatextParser latextParser = getLatextParser(this.parser);
					StringBuilder exp = latextParser.exp;
					Node node = latextParser.node;
					pos = latextParser.pos;
					for (String p : params)
					{
						if (p.trim().length() > 0)
						{
							latextParser.exp = new StringBuilder(p);
							latextParser.pos = 0;
							this.parameters.add(latextParser.getNode());
						}
					}
					latextParser.exp = exp;
					latextParser.pos = pos = z + 1;
					latextParser.node = node;
					break;
				}
				
			}
		}
		
		//node = parserNode();
		parser.pos = pos;
	}
	private LatextParser getLatextParser(Parser parser)
	{
		if (parser instanceof LatextParser)
		{
			return (LatextParser)parser;
		}
		else if (parser instanceof Node)
		{
			return getLatextParser(((Node)parser).parser);
		}
		return null;
	}
	
	@Override
	public String toString()
	{
		return getName() + "[" + node.toString() + "]";
	}
	private String removeParenthesis(String s)
	{
		
		s = s.trim();
		if (s.charAt(0) == '('  && s.charAt(s.length() -1) == ')')
		{
			return s.substring(1, s.length() -1);
		}
		return s;
	}
	@SuppressWarnings("unchecked")
	private String getFunctionCode(boolean condition, boolean getterAndSetter)
	{
		String functionName = super.toJava(condition, getterAndSetter);
		StringBuilder parameters = new StringBuilder(); 
		if (functionName.equals("sum"))
		{
			StringBuilder s = new StringBuilder("for(int ");
			OperatorNode operationNode = (OperatorNode)this.parameters.get(0);
			String variable = removeParenthesis(operationNode.left.toJava(condition, getterAndSetter));
			String list = this.parameters.get(1).toJava(condition, getterAndSetter);
			s.append(operationNode.toJava(condition, getterAndSetter)).append(";")
			         .append(variable).append("<")
			         .append(list)
			         .append(".size();").append(variable).append("++){")
			         .append("cl.dlab.abm.core.model.Agent agent").append(variable).append("=").append(list).append(".get(").append(variable).append(");")
			         .append(this.parameters.get(2).toJava(condition, getterAndSetter)).append(";}");
			return s.toString();
		}
		else
		{
			parameters.append("(");
			String comma = "";
			for(Node node: this.parameters)
			{
				parameters.append(comma);
				parameters.append(node.toJava(condition, getterAndSetter));
				comma = ", ";
			}
			parameters.append(")");
			if (parser.alias.containsKey(functionName) || functionName.equals("normalizer") || functionName.equals("logistic") || functionName.equals("size"))
			{
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
				
				if (hs.get("variableTypeId").equals(5))
				{
					if (getterAndSetter)
					{
						return objName + "." + Util.getGetterName((String)hs.get("name")) + ".getValue" + parameters;
					}
					else
					{
						return objName + "." + hs.get("name") + ".getValue" + parameters;
					}
					
				}
				if (functionName.equals("size"))
				{
					return objName + ".getAgents(" + parameters + ").size()";
				}
				return objName + "." + hs.get("name") + parameters;
			}
			else 
			{
				return "Math." + functionName + parameters;
			}
		}
	}
	public String toJava(boolean condition, boolean getterAndSetter)
	{
		return getFunctionCode(condition, getterAndSetter);// + node.toJava(condition, getterAndSetter);
	}
	
}
