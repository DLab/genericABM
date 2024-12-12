package cl.dlab.abm.core.parser;

import java.util.HashMap;

public class LatextParser extends Parser
{	
	private static String clean(String exp)
	{
		return breakDown(clean(clean(clean(clean(clean(clean(clean(clean(clean(clean(clean(clean(clean(cleanText(convertFunction(exp))
					, "\\left", ""), "\\ ", " ")
				    , "\\right", ""), "\\cdot", " *"), "\\neg", " !"), "\\ne", " <> "), "\\wedge", "\\land ")
					, "\\ge", ">="), "\\le", "<="), "\\text{And}", "\\land "), "\\text{Or}", "\\lor ")
				    , "\\times", " *"), "\\in", " instanceof ").trim());
						
	}
	private static String cleanText(String s)
	{
		StringBuilder exp = new StringBuilder(s);
		int index = 0;
		
		while((index = exp.indexOf("\\text{")) != -1)
		{
			int i1 = exp.indexOf("}", index);
			exp.replace(index, i1 + 1, exp.substring(index + 6, i1));
		}
		return exp.toString();
	}
	private static String putParenthesis(String exp)
	{
		StringBuilder buff = new StringBuilder();
		int len = exp.length();
		buff.append("(");
		int find = 0;
		for (int i = 0; i < len; i++)
		{
			char c = exp.charAt(i);
			if (find == 0 && (c == '=' || c == '>' || c == '<'))
			{
				buff.append(")");
				find = 1;
			}
			else if (find == 1 && !(c == '=' || c == '>' || c == '<'))
			{
				buff.append("(");
				find = 2;
			}
			buff.append(c);
		}
		buff.append(")");
		return buff.toString();
	}
	@SuppressWarnings("unused")
	private static String removeParenthesis(String exp)
	{
		//System.out.println(exp);
		int len = exp.length();
		boolean has = false;
		boolean equals = false;
		for (int i = 1; i < len; i++)
		{
			char c = exp.charAt(i);
			if (c == '=' || c == '>' || c == '<' || (c == '!' && exp.charAt(i + 1) == '='))
			{
				equals = true;
				if ((exp.charAt(i - 1) == ' ' && exp.charAt(i - 3) == ')')  
						|| exp.charAt(i - 2) == ')') 
				{
					has = true;
					break;
				}
			}
		}
		if (!equals && exp.charAt(0) == '(' && exp.charAt(len -1) == ')')
		{
			return exp.substring(1, len - 1);
		}
		if (!has)
		{
			return exp;
		}
		StringBuilder buff = new StringBuilder();
		int find = 0;
		if (exp.charAt(len - 1) == ')')
		{
			len = len - 1;
		}
		for (int i = 1; i < len; i++)
		{
			char c = exp.charAt(i);
			if (find == 0 && (c == '=' || c == '>' || c == '<' || (c == '!' && exp.charAt(i + 1) == '=')))
			{
				if (exp.charAt(i - 1) == ' ' && buff.charAt(i - 3) == ')')
				{
					buff.deleteCharAt(i - 3);
					find = 1;
				}
				else if (buff.charAt(i - 2) == ')')
				{
					buff.deleteCharAt(i - 2);
					find = 1;
				}
			}
			else if (find == 1 && !(c == '=' || c == '>' || c == '<' || c == '!'))
			{
				find = 2;
			}
			if (find == 2 && c == '(')
			{
				find = 3;
				continue;
			}
			buff.append(c);
		}
		return buff.toString();
	}
	private static int findRParenthesis(StringBuilder exp, int index)
	{
		int n = 0;
		while(true)
		{
			int j = exp.indexOf(")", index);
			index = exp.indexOf("(", index);
			if (index != -1 && j != -1)
			{
				if (index < j)
				{
					n++;
				}
				else if (n == 0)
				{
					return j;
				}
				else
				{
					n--;
				}
			}
			else if (index == -1)
			{
				if (n == 0)
				{
					return j;
				}
				n--;
				index = j;
			}
			else
			{
				return -1;
			}
			index = index +1;
		}
	}
	private static String breakDown(String s)
	{
		StringBuilder exp = new StringBuilder(s);
		int index = 0;
		int i;
		if ((i = exp.indexOf("(", index)) != -1)
		{
			int j = exp.indexOf("(", i + 1);
			int z = exp.indexOf(")", i + 1);
			if (j >= 0 && j < z)
			{
				int k = findRParenthesis(exp, j + 1);
				exp.replace(j + 1, k, breakDown(exp.substring(j + 1, k)));
				index = exp.length() - 1;
			}
			else
			{
				//System.out.println(exp);
				String[] t = exp.substring(i + 1, z).split("[,]");
				StringBuilder buff = new StringBuilder();
				String comma = "";
				for (String _s : t)
				{
					if (_s.trim().length() > 0)
					{
						buff.append(comma);
						buff.append(putParenthesis(_s.trim()));
						comma = ", ";
					}
				}
				exp.replace(i + 1, z, buff.toString());
				index = i + 1;
			}
		}
		return exp.toString();
	}
	private static String clean(String exp, String text, String replace)
	{
		int index = 0;
		int len = text.length();
		StringBuilder buff = new StringBuilder(exp);
		while((index = buff.indexOf(text, index)) != -1)
		{
			buff.replace(index, index + len, replace);
			index += replace.length();
		}
		return buff.toString();
	}
	public LatextParser(String exp, HashMap<String, Object> alias) throws Exception
	{
		super(clean(exp), alias);
		System.out.println("antes:" + super.exp);
		getNode();
		System.out.println(super.exp);
	}
	protected Node getNode() throws Exception
	{
		pos = 0;
		while(pos < getLength()){
			super.node = parserNode();			
		}
		return super.node;
	}
	public String toJava(boolean condition, boolean getterAndSetter)
	{
		return toJava(condition, condition, getterAndSetter);
	}
	public String toJava(boolean remove, boolean condition, boolean getterAndSetter)
	{
		if (node == null) 
		{
			return ""; 
		}
		String exp = node.toJava(condition, getterAndSetter).replaceAll("[*] instanceof [*]", " instanceof ");
		return exp;//remove ? removeParenthesis(exp) : exp;
	}
	private static String convertFunction(String s)
	{
		StringBuilder exp = new StringBuilder(s);
		int index = 0;
		while((index = exp.indexOf("\\sum_", index)) != -1)
		{
			int i0 = exp.indexOf("{", index);
			int i1 = exp.indexOf("}", i0);
			int i2 = exp.indexOf("{", i1);
			int i3 = exp.indexOf("}", i2);
			int i4 = exp.indexOf("\\left", i3);
			//System.out.println(i4 + "**" + i3);
			if (i4 == -1 || i4 > i3 + 2)
			{
				i4 = i3 + 1;
				exp.replace(index, exp.length(), "\\sum(" + exp.substring(i0 + 1, i1) + ", " + exp.substring(i2 + 1, i3) + ", " + exp.substring(i4, exp.length()) + ")");
			}
			else 
			{
				i4 = exp.indexOf("(", i4);
				int i5 = exp.indexOf(")", i4);
				exp.replace(index, exp.length(), "\\sum(" + exp.substring(i0 + 1, i1) + ", " + exp.substring(i2 + 1, i3) + ", " + exp.substring(i4 + 1, i5 - 7) + ")");			}
			index = index + 5;
		}
		return exp.toString();
	}
	public static void main(String[] args) throws Exception
	{
		String exp = "agent_j\\ne\\varnothing\\wedge\\left(masaD_i\\ge umbralHunted\\left(\\right)\\cdot masaInicial_i\\right)";
		//String exp = "agent_j\\ne \\varnothing \\wedge \\left(masaD_i\\ge \\left(1-\\frac{1}{size\\left(\"\\text{Presas}\"\\right)}\\right)\\cdot masaInicial_i\\right)";
		//String exp = "masaD_j\\ge\\frac{20\\cdot masaInicial_j}{size\\left(\"\\text{Presas}\"\\right)}";
		//System.out.println(clean(exp, "\\cdot", " *"));
		//String exp = "masaD_j\\ge normalizer\\left(\\frac{20}{size\\left(\"\\text{Presa}\"\\right)}\\right)\\cdot masaInicial_j";
		//String exp = "state_j=0\\land  random<infectionRate #comentario xxx yaa";
		//String exp = "isFactual=newsGenerator()>0.5 #La noticia es verdadera si es > 0.5";
		//String exp = "true";
		HashMap<String, Object> alias = new HashMap<String, Object>();
		alias.put("isFactual", Util.getAlias("isFactual", new ObjectName("this"), 1));
		alias.put("state", Util.getAlias("state", new ObjectName("agent"), 1));
		alias.put("repu", Util.getAlias("repu", new ObjectName("agent"), 1));
		alias.put("masaInicial", Util.getAlias("masaInicial", new ObjectName("agent"), 1));
		alias.put("masaD", Util.getAlias("masa", new ObjectName("((Depredador)agent)"), 1));
		alias.put("random", Util.getAlias("random()", new ObjectName("this"), -1));
		alias.put("newGenerator", Util.getAlias("newGenerator", new ObjectName("model"), -1));

		alias.put("x", Util.getAlias("x", new ObjectName("this"), 1));
		alias.put("y", Util.getAlias("y", new ObjectName("this"), 1));
		alias.put("total", Util.getAlias("total", new ObjectName("this"), 1));
		
		alias.put("newAgents", Util.getAlias("newAgents", new ObjectName("this"), 1));
		alias.put("umbralHunting", Util.getAlias("umbralHunting", new ObjectName("this"), -1));
		alias.put("normalizer", Util.getAlias("normalizer", new ObjectName("this"), -1));
		//alias.put("sum", Util.getAlias("sum", new ObjectName("this"), 1));
		alias.put("cooperates", Util.getAlias("cooperates", new ObjectName("agent"), 1));
		alias.put("coop", Util.getAlias("coop", new ObjectName("this"), -1));
		alias.put("size", Util.getAlias("size", new ObjectName("model"), -1));
		alias.put("agent", Util.getAlias("agent", new ObjectName("(Depredador)agent"), -2));
		alias.put("create", Util.getAlias("create", new ObjectName("agent"), -1));
		System.out.println(exp);
		System.out.println("final:" + new LatextParser(exp, alias).toJava(true, false, false));
	}
}
