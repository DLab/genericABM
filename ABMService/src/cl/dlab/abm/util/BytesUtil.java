package cl.dlab.abm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class BytesUtil
{
	
	public static byte[] getBytes(Object obj) throws IOException
	{
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(obj);
        oo.close();
        return bo.toByteArray();
	}
	public static byte[] getGZipBytes(Object obj) throws IOException
	{
        ByteArrayOutputStream boz = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(boz);
        gzip.write(getBytes(obj));
        gzip.close();	        
        return boz.toByteArray();
		
	}
	public static Object getUnzipBytes(byte[] bytes) throws IOException, ClassNotFoundException
	{
		ObjectInputStream oi = new ObjectInputStream(new ByteArrayInputStream((byte[])bytes));
		Object value = oi.readObject();				
		oi = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream((byte[])value)));
		return oi.readObject();		
	}
	public static Object getObjectByZipBytes(byte[] bytes) throws IOException, ClassNotFoundException
	{
		ObjectInputStream oi = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes)));
		return oi.readObject();		
	}
	public static Object getUnzipBytes(File file) throws IOException, ClassNotFoundException
	{
		try(ObjectInputStream oi = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file))))
		{
			return oi.readObject();
		}
	}
	@SuppressWarnings("unchecked")
	public static final ArrayList<HashMap<String, Object>> getArrayListFromFile(String fileName, boolean delete) throws Exception
	{
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
		int n = 0;
		while(true)
		{
			File f = new File(fileName + "_" + (n++));
			if (f.exists())
			{
				System.out.println("buscando:" + f.getPath());
				ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>)BytesUtil.getUnzipBytes(f);
				result.addAll(list);				
			}
			else
			{
				break;
			}
			if (delete)
			{
				f.delete();
			}
		}
		return result;
	}
	
}
