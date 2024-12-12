package cl.dlab.abm.core.model.gen;

import java.util.HashMap;

import cl.dlab.abm.core.parser.LatextParser;

public class JavaLatextParser
{
	private HashMap<String, Object> alias;
	public JavaLatextParser(HashMap<String, Object> alias)
	{
		this.alias = alias;
	}
	public String parser(boolean condition, boolean getterAndSetter, String exp)throws Exception
	{
		return parser(condition, condition, getterAndSetter, exp);
	}
	public String parser(boolean remove, boolean condition, boolean getterAndSetter, String exp)throws Exception
	{
		System.out.println("latexoparser:" + exp);
		return new LatextParser(exp, alias).toJava(remove, condition, getterAndSetter);		
	}
}
