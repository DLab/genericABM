package cl.dlab.abm.core.parser;

import java.util.HashMap;

public class Util
{
	public static String getJavaName(String name)
	{
		StringBuilder buff = new StringBuilder();
		String[] t = name.split("[ ]+");
		for (String s : t)
		{
			buff.append(Character.toUpperCase(s.charAt(0)) + s.substring(1));
		}
		return buff.toString().replaceAll("-", "_").replaceAll("[(]", "_").replaceAll("[)]", "_");
	}
	public static String getPropertyName(String name)
	{
		String s = getJavaName(name);
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}
	public static String getGetterName(String name)
	{
		return "get" + getJavaName(name) + "()";
	}
	public static String getSetterName(String name)
	{
		return "set" + getJavaName(name);
	}

	public static HashMap<String, Object> getAlias(String name, ObjectName objName, int type)
	{
		HashMap<String, Object> hs = new HashMap<String, Object>();
		hs.put("name", name);
		hs.put("objName", objName);
		hs.put("variableTypeId", type);
		return hs;
	}

}
