package cl.dlab.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil
{
	public static String BASE = "";
	public static String CONFIG_NAME = "abm.config";

	private static Properties CONFIG_PPROPERTIES = new Properties();
	private static long CONFIG_PPROPERTIES_LENGTH;
	private static Properties SQL_COMPATIBILITY;

	public static Integer getId(String key) throws IOException
	{
		return Integer.parseInt(getProperty("ID-" + key));
	}
	public static int getIntValue(String key) throws IOException
	{
		return Integer.parseInt(getProperty(key));
	}
	public static int getIntValueSinErrores(String key) 
	{
		try
		{
			return Integer.parseInt(getProperty(key));
		}
		catch(Exception e)
		{
			LogUtil.error(PropertyUtil.class, e, "Error al recuperar variable");
			return -1;
		}
	}
	public static double getDoubleValue(String key) throws IOException
	{
		String value = getProperty(key);
		return value == null ? -1 : Double.parseDouble(value);
	}

	public static int[] getIntArray(String key) throws IOException
	{
		String property = getProperty(key);
		String[] t = property.split("[,]");
		int[] results = new int[t.length];
		for (int i = 0; i < t.length; i++)
		{
			results[i] = Integer.parseInt(t[i].trim());
		}
		return results;
		
	}
	public static Integer[] getIntegerArray(String key) throws IOException
	{
		String property = getProperty(key);
		String[] t = property.split("[,]");
		Integer[] results = new Integer[t.length];
		for (int i = 0; i < t.length; i++)
		{
			results[i] = Integer.parseInt(t[i].trim());
		}
		return results;
		
	}
	public static double[] getDoubleArray(String key) throws IOException
	{
		String property = getProperty(key);
		if (property == null)
		{
			return null;
		}
		String[] t = property.split("[,]");
		double[] results = new double[t.length];
		for (int i = 0; i < t.length; i++)
		{
			results[i] = Double.parseDouble(t[i].trim());
		}
		return results;
		
	}
	public static Double[] getDoubleObjectArray(String key) throws IOException
	{
		String property = getProperty(key);
		String[] t = property.split("[,]");
		Double[] results = new Double[t.length];
		for (int i = 0; i < t.length; i++)
		{
			results[i] = Double.parseDouble(t[i].trim());
		}
		return results;
		
	}
	public static Boolean[] getBooleanArray(String key) throws IOException
	{
		String property = getProperty(key);
		String[] t = property.split("[,]");
		Boolean[] results = new Boolean[t.length];
		for (int i = 0; i < t.length; i++)
		{
			results[i] = Boolean.parseBoolean(t[i].trim());
		}
		return results;
		
	}
	public static int getIdSinErrores(String key)
	{
		try
		{
			return Integer.parseInt(getProperty("ID-" + key));
		}
		catch (Exception e)
		{
			LogUtil.error(PropertyUtil.class, e, "Error al cargar constante:", key);
			return -1;
		}
	}
	public static String getPropertySinErrores(String key)
	{
		try
		{
			return getProperty(key);
		}
		catch (Exception e)
		{
			LogUtil.error(PropertyUtil.class, e, "Error al cargar constante:", key);
			return null;
		}
	}

	public static String getProperty(String key) throws IOException
	{
		synchronized (CONFIG_PPROPERTIES)
		{
			File f = new File(BASE + CONFIG_NAME);
			if (f.length() != CONFIG_PPROPERTIES_LENGTH)
			{
				CONFIG_PPROPERTIES.load(new FileInputStream(f));
			}
		}
		return CONFIG_PPROPERTIES.getProperty(key);
	}
	public static void loadSqlCompatibility(String databaseType) throws Exception
	{
		loadSqlCompatibility("", databaseType);
	}
	public static void loadSqlCompatibility(String path, String databaseType) throws Exception
	{
		SQL_COMPATIBILITY = new Properties();
		SQL_COMPATIBILITY.load(new FileInputStream(path + "sql-compatibilities-" + databaseType + ".config"));
		
	}
	public static String getSqlProperty(String key)
	{
		return SQL_COMPATIBILITY.getProperty(key);
	}
}
