package cl.dlab.abm.core.parser;

import java.util.HashMap;

public class Parser
{
    protected static final char ESCAPE = '\\';
    protected static final char COMMENT = '#';

    // grouping characters (for parsing)
    protected static final char L_GROUP = '{';
    protected static final char R_GROUP = '}';
    protected static final char L_PARENTHESIS = '(';
    protected static final char R_PARENTHESIS = ')';
    protected static final char L_BRACK = '[';
    protected static final char R_BRACK = ']';
    protected static final char SPACE = ' ';
    protected static final char DOLLAR = '$';
    protected static final char DQUOTE = '\"';
    protected static final char QUOTE = '\'';

    // Percent char for comments
    protected static final char PERCENT = '%';

    // script characters (for parsing)
    protected static final char SUB_SCRIPT = '_';
    protected static final char SUPER_SCRIPT = '^';
    
    protected static final char EQ_OPERATOR = '=';
    protected static final char NEG_OPERATOR = '!';
    protected static final char GT_OPERATOR = '>';
    protected static final char LT_OPERATOR = '<';
    protected static final char MULTIP_OPERATOR = '*';
    protected static final char PLUS_OPERATOR = '+';
    protected static final char MINUS_OPERATOR = '-';
    protected static final char COMMA_OPERATOR = ',';
    

	protected int pos = 0;
	protected StringBuilder exp;
	protected Node node;
	protected HashMap<String, Object> alias;
	public Parser(String exp, HashMap<String, Object> alias)
	{
		this(new StringBuilder(exp), alias);
	}
	public Parser(StringBuilder exp, HashMap<String, Object> alias)
	{
		this.exp = exp;
		this.alias = alias;
	}
	protected int getLength()
	{
		return exp.length();
	}
	private boolean isFunctionNode(String name)
	{
		return name.equals("sin") || name.equals("cos") || name.equals("tan") || name.equals("sqrt") || name.equals("tanh") || name.equals("sign")  
				|| name.equals("normalizer") || name.equals("logistic") || name.equals("sum") || alias.containsKey(name);
	}
	@SuppressWarnings("unchecked")
	private Node getSpecialNode(String name) throws Exception
	{
		if (name.equals("frac"))
		{
			return new FracNode(name, this);
		}
		if (name.equals("*") || isLogicalOperator(name))
		{
			return new OperatorNode(name, this);
		}
		if (isFunctionNode(name))
		{
			//char c = exp.charAt(pos);
			HashMap<String, Object> p;
			if ((p = (HashMap<String, Object>)alias.get(name)) != null && !p.get("variableTypeId").equals(5) && !p.get("variableTypeId").equals(-1))
			{
				return new PropertyNode(name, this);
			}
			return new FunctionNode(name, this);
		}
		return new SpecialNode(name, this);
	}
    
	protected String getWord()
	{
		StringBuilder buff = new StringBuilder();
		char c;
		int len = getLength();
		while(((c = exp.charAt(pos)) >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'))
		{
			buff.append(c);
			if (++pos == len)
			{
				return buff.toString();
			}
		}
		return buff.toString();
	}
	private String getNumber()
	{
		StringBuilder buff = new StringBuilder();
		char c;
		int len = getLength();
		while(((c = exp.charAt(pos)) >= '0' && c <= '9') || c == '.')
		{
			buff.append(c);
			if (++pos == len)
			{
				return buff.toString();
			}
		}
		return buff.toString();
	}
	private boolean isDigit(char c)
	{
		return c >= '0' && c <= '9';
	}
	private boolean isOperator(char c)
	{
		return c == EQ_OPERATOR 
				|| c == GT_OPERATOR || c == LT_OPERATOR || c == PLUS_OPERATOR 
				|| c == MINUS_OPERATOR || c == SUPER_SCRIPT || c == MULTIP_OPERATOR;
	}
	private char nextCharacter(int index) {
		char c;
		while((c = exp.charAt(index)) == ' ')
		{
			index++;
		}
		return c;
	}
	private boolean withoutOperation(int index) {
		char c = nextCharacter(index);
		return !isOperator(c) && c != R_GROUP && c != R_PARENTHESIS && c != ',';
	}
	private String getOperator(char c)
	{
		StringBuilder buff = new StringBuilder().append(c);
		int len = getLength();
		while(isOperator(c = exp.charAt(pos))) 
		{
			buff.append(c);
			if (++pos == len)
			{
				return buff.toString();
			}
			
		}
		return buff.toString();
	}
	protected boolean findCharacter(char c)
	{
		return false;
	}
	private boolean isLogicalOperator(String s)
	{
		return s.equals("land") || s.equals("lor") || s.equals("wedge") || s.equals("vee");
	}
	private boolean isNextWordLogicalOperator()
	{
		char c = exp.charAt(pos);
		if (c == '\\')
		{
			int oldPos = pos;
			pos++;
			String s = getWord();
			pos = oldPos;
			return isLogicalOperator(s);
		}
		
		return false;
		
	}
	protected Node parserNode() throws Exception
	{
		char c;
		char nextCharacter;
		String s;
		Node node;
		int len = getLength();
		while (pos < len && !findCharacter(c = exp.charAt(pos++)))
		{
			switch (c)
			{
			case SPACE:
				break;
			case ESCAPE:
				s = getWord();
				if (pos < len && !isNextWordLogicalOperator() && (exp.charAt(pos) == '\\' || (withoutOperation(pos) && !s.equals("frac") && !isFunctionNode(s) && !isLogicalOperator(s))))
				{
					exp.insert(pos, '*');
					//System.out.println(exp);
				}
				return getSpecialNode(s);
			case L_GROUP:
				node = new GroupNode("group", this);
				if (pos < exp.length())
				{
					nextCharacter = nextCharacter(pos);
					if (pos < len && withoutOperation(pos) && nextCharacter != L_GROUP)
					{
						exp.insert(pos, '*');
						//System.out.println(exp);
					}
				}
				return node;
			case L_PARENTHESIS:
				node = new ParenthesisNode("Parenthesis", this);
				if (pos < len && withoutOperation(pos) && !isNextWordLogicalOperator())
				{
					exp.insert(pos, '*');
					//System.out.println(exp);
				}				
				return node;
			case COMMENT:
				return new CommentNode(null, this);
			case DQUOTE:
			case QUOTE:
				return new TextNode(String.valueOf(c), this);
			case LT_OPERATOR:
			case EQ_OPERATOR:
			case GT_OPERATOR:	
			case PLUS_OPERATOR:	
			case MINUS_OPERATOR:
			case SUPER_SCRIPT:
			case MULTIP_OPERATOR:
			case NEG_OPERATOR:		
			case COMMA_OPERATOR:
				return new OperatorNode(getOperator(c), this);
			default:				
				pos--;
				s = isDigit(c) ? getNumber() : getWord();
				if (pos < len && withoutOperation(pos))
				{
					if (alias.containsKey(s))
					{
						return getSpecialNode(s);
					}
					else
					{
						c = exp.charAt(pos);
						if (c == '_')
						{
							throw new Exception("Property " + s + " not defined");
						}
						else if (c != ',' && c != '"' && exp.charAt(pos + 1) != COMMENT && !isNextWordLogicalOperator() && !isFunctionNode(s))
						{
							exp.insert(pos, '*');
						}
					}
				}
				if (alias.containsKey(s))
				{
					return new PropertyNode(s, this);
				}
				if (isFunctionNode(s))
				{
					node = getSpecialNode(s);
				}
				else
				{
					node = new Node(s, this);
				}
				if (pos < len)
				{
					c = exp.charAt(pos);
					nextCharacter = nextCharacter(pos);
					if (this instanceof Node && nextCharacter == '^' && 
							(((Node)this).getName().equals("*") || ((Node)this).getName().equals("+") || ((Node)this).getName().equals("-")))
					{
						((Node)this).node = node;
						node = parserNode();
					}
				}
				return node;
			}
		}
		return null;
	}
    
}
