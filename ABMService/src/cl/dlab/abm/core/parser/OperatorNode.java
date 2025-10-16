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
				String leftOpenParenthesis = "";
				String leftCloseParenthesis = "";
				String rightOpenParenthesis = "";
				String rightCloseParenthesis = "";
				if (left instanceof ParenthesisNode)
				{
					leftOpenParenthesis = "(";
					leftCloseParenthesis = ")";
					left = ((ParenthesisNode)left).node;
				}
				if (right instanceof ParenthesisNode)
				{
					rightOpenParenthesis = "(";
					rightCloseParenthesis = ")";
					right = ((ParenthesisNode)right).node;
				}
				if (getterAndSetter) 
				{
					return leftOpenParenthesis + ((PropertyNode)left).toJava(condition, getterAndSetter, false) + leftCloseParenthesis + "(" 
							+ rightOpenParenthesis + (right instanceof PropertyNode ? ((PropertyNode)right).toJava(condition, getterAndSetter, true)
									: right.toJava(condition, getterAndSetter)) + rightCloseParenthesis + ")";
				}
				else
				{
					return leftOpenParenthesis + left.toJava(condition, getterAndSetter) + leftCloseParenthesis 
							+ " " + operator + " " 
							+ rightOpenParenthesis + right.toJava(condition, getterAndSetter) + rightCloseParenthesis;
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
