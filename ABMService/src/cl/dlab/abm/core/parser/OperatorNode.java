package cl.dlab.abm.core.parser;

import java.util.HashMap;

public class OperatorNode extends Node
{
	protected Node left;
	protected Node right;
	public OperatorNode(String name, Parser parser)  throws Exception
	{
		super(name, parser);
		this.left = getLeftNode();
		this.right = parserRightNode();
		parser.pos = super.pos;
	}
	protected Node parserRightNode() throws Exception
	{
		return parserNode();
	}
	protected Node getLeftNode()throws Exception
	{
		return parser.node;
	}
	/**
	 * @return the left
	 */
	public Node getLeft() 
	{
		return left;
	}
	/**
	 * @return the right
	 */
	public Node getRight()
	{
		return right;
	}
	@Override
	public String toString()
	{
		return "[" + left  + "] " + getName() + " [" + right + "]";
	}
	@Override
	public String toJava(boolean condition, boolean getterAndSetter)
	{
		String operator = getName();
		if (operator.charAt(0) == SUPER_SCRIPT)
		{
			return "Math.pow(" + left.toJava(condition, getterAndSetter) + ", " + right.toJava(condition, getterAndSetter) + ")";
		}
		if (operator.equals("<>"))
		{
			operator = "!=";
		}
		else if (operator.equals("="))
		{
			if (condition)
			{
				operator = "==";
			}
			else if (left != null)
			{
				if (left instanceof ParenthesisNode)
				{
					left = ((ParenthesisNode)left).node;
				}
				if (right instanceof ParenthesisNode)
				{
					right = ((ParenthesisNode)right).node;
				}
				if (getterAndSetter) 
				{
					return ((PropertyNode)left).toJava(condition, getterAndSetter, false) + "(" 
							+ (right instanceof PropertyNode ? ((PropertyNode)right).toJava(condition, getterAndSetter, true) 
									: right.toJava(condition, getterAndSetter)) + ")";
				}
				else
				{
					return left.toJava(condition, getterAndSetter) + " " + operator + " " + right.toJava(condition, getterAndSetter);
				}
			}
		}
		else if (operator.equals("wedge") || operator.equals("land"))
		{
			operator = " && ";
		}
		else if (operator.equals("lor") || operator.equals("vee"))
		{
			operator =  " || ";
		}
		if (condition && left != null && (operator.equals("==") || operator.equals("!=")))
		{
			HashMap<String, Object> hs = left.getAlias(condition, getterAndSetter);
			if (hs.get("variableTypeId").equals(4))
			{
				return (operator.equals("!=") ? "!" : "") + left.toJava(condition, getterAndSetter) + ".equals(" + right.toJava(condition, getterAndSetter) + ")";
			}
		}
		
		return (left != null ? left.toJava(condition, getterAndSetter) : "") + " " + operator + " " + right.toJava(condition, getterAndSetter);
	}
}
