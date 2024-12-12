package cl.dlab.abm.core.model.gen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cl.dlab.abm.core.sql.rad.Jars;
import cl.dlab.abm.servlet.InitializeServlet;
import cl.dlab.abm.util.BytesUtil;
import cl.dlab.util.LogUtil;
import cl.dlab.util.PropertyUtil;

public class JarService
{
	private static void writeJar(File jar, byte[] zipBytes, long lastModification) throws Exception
	{
		byte[] bytes = (byte[])BytesUtil.getObjectByZipBytes((byte[])zipBytes);
		FileOutputStream fo = new FileOutputStream(jar);
		fo.write(bytes);
		fo.close();
		jar.setLastModified(lastModification);
		System.out.println("Actualizando archivo " + jar.getPath() + " con date:" + new Date(lastModification));
	}
	private static byte[] getZipBytes(File jar) throws IOException
	{
		try(FileInputStream fi = new FileInputStream(jar))
		{
			byte[] b = new byte[fi.available()];
			fi.read(b);
			return BytesUtil.getGZipBytes(b);
		}											
	}
	private static void updateJarsDB(Jars service, SimpleDateFormat fmt, File jar, String jarName) throws Exception
	{
		System.out.println("Actualizando bd Jars " + jarName + " con date:" + new Date(jar.lastModified()));
		try(PreparedStatement upd = service.getConnection().prepareStatement("update jars set content = ? , timestamp = ? where jar_name = ?"))
		{
			upd.setObject(1, getZipBytes(jar));
			upd.setString(2, fmt.format(new Date(jar.lastModified())));
			upd.setString(3, jarName);
			upd.executeUpdate();
			service.getConnection().commit();
		}		
	}
	public synchronized static void reloadJar(String jarName) throws Exception
	{
		Jars service = new Jars(null, false);
		try
		{
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSS");
			String path = InitializeServlet.REAL_PATH + "/jars/";
			File jar = new File(path + jarName + ".jar");
			try(PreparedStatement stmt = service.getConnection().prepareStatement("select timestamp, content from jars where jar_name = ?"))
			{
				stmt.setString(1, jarName);
				try(ResultSet rset = stmt.executeQuery())
				{
					if (rset.next())
					{
						validateJar(service, fmt, jar, jarName, fmt.parse(rset.getString(1)), (byte[])rset.getObject(2));
					}
				}
			}
		}
		finally
		{
			service.connectionClose();
		}
	}
	private static void externalReloadJar(String jarName) throws Exception
	{
		String[] nodos = PropertyUtil.getProperty("CLUSTER-NODES").split(";");
		for (String nodo : nodos)
		{
			if (nodo.trim().length() > 0)
			{
				LogUtil.info(JarService.class, "reaload Jar, nodo:", nodo, " jar:", jarName);
				URL url = new URL(nodo + "service/restServices/reloadJar?jarName=" + jarName);
				URLConnection connection = url.openConnection();
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				br.readLine();				
			}
		}
		
	}
	private static void validateJar(Jars service, SimpleDateFormat fmt, File jar, String jarName, Date dateTimeDbJar, byte[] bytes) throws Exception
	{
		long timeDbJar = dateTimeDbJar.getTime();
		if (jar != null && jar.exists())
		{
			if (timeDbJar < jar.lastModified())
			{
				updateJarsDB(service, fmt, jar, jarName);
				externalReloadJar(jarName);
			}
			else if (timeDbJar > jar.lastModified())
			{
				writeJar(jar, bytes, timeDbJar);
			}
			
		}
		else
		{
			if (jar == null)
			{
				jar = new File(InitializeServlet.REAL_PATH + "/jars/" + jarName + ".jar");
			}
			writeJar(jar, bytes, timeDbJar);
		}
		
	}
	public static void initializeJars() throws Exception
	{
		System.out.println("Initialize jars");
		HashMap<String, File> hsJars = new HashMap<String, File>();
		File path[] = new File(InitializeServlet.REAL_PATH + "/jars/").listFiles();
		for (File f : path)
		{
			if (f.getName().endsWith(".jar") && !f.getName().endsWith("-src.jar") )
			{
				String[] t = f.getName().split("[.]");
				hsJars.put(t[0], f);				
			}
		}
		
		Jars service = new Jars(null, false);
		try
		{
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSS");
			//File jar = new File(path + jarName + ".jar");
			try(PreparedStatement stmt = service.getConnection().prepareStatement("select jar_name, timestamp, content from jars"))
			{
				try(ResultSet rset = stmt.executeQuery())
				{
					while(rset.next())
					{
						String jarName = rset.getString(1);
						File jar = hsJars.remove(jarName);
						validateJar(service, fmt, jar, jarName, fmt.parse(rset.getString(2)), (byte[])rset.getObject(3));
					}
				}
			}
			System.out.println("preparando a insertar:" + hsJars.keySet());
			try(PreparedStatement stmt = service.getConnection().prepareStatement("insert into jars (jar_name, timestamp, content) values (?, ?, ?)"))
			{
				for (String jarName : hsJars.keySet())
				{
					File f = hsJars.get(jarName);
					stmt.setString(1, jarName);
					stmt.setString(2, fmt.format(new Date(f.lastModified())));
					stmt.setObject(3, getZipBytes(f));
					stmt.executeUpdate();
					System.out.println("Agregando bd Jars " + jarName + " con date:" + new Date(f.lastModified()));
					service.connectionCommit();
					externalReloadJar(jarName);
				}
			}
		}
		finally
		{
			service.connectionClose();
		}
		System.out.println("EndInitialize jars");
	}
	public static void main(String[] args) throws Exception
	{
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSS");
		System.out.println(fmt.format(System.currentTimeMillis()));
		
	}
}
