package cl.dlab.abm.core.simulation;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import cl.dlab.abm.util.BytesUtil;

public class ExtractData
{
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception
	{
		Class.forName("org.postgresql.Driver");
//		return DriverManager.getConnection("jdbc:mysql://localhost/afoit?useUnicode=true&amp;characterEncoding=UTF-8", "root", "JHTi2990");
		try(Connection con = DriverManager.getConnection("jdbc:postgresql://192.168.2.109/abm2", "postgres", "postgres"))
		{
			try(PreparedStatement stmt = con.prepareStatement("select description, additional_data from simulations where id='2faf9c9f-c498-4b89-a629-b1524dc1481b'"))
			{
				try(ResultSet rset = stmt.executeQuery())
				{
					rset.next();
					byte[] bytes = (byte[])rset.getObject(2);
					try(BufferedWriter wr = new BufferedWriter(new FileWriter("/Users/manolocabello/Downloads/datos_" + rset.getString(1) + ".csv")))
					{
						wr.write("initReci;initRepu;initConf;umbralCoop;pozo");
						wr.newLine();
						try(ObjectInputStream oi = new ObjectInputStream(new ByteArrayInputStream(bytes)))
				        {
							try(ObjectInputStream oi2 = new ObjectInputStream(new ByteArrayInputStream((byte[])oi.readObject())))
							{
								ArrayList<byte[]> lastData = (ArrayList<byte[]>)oi2.readObject();
								ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
								for (byte[] b : lastData)
								{
									result.addAll((ArrayList<HashMap<String,Object>>)BytesUtil.getObjectByZipBytes(b));
								}
								System.out.println("size:" + result.size());
								for (HashMap<String, Object> hs : result)
								{
									
									System.out.println(hs.keySet() + "**" + hs);
									/*double[] pozo = (double[])hs.get("pozo");
									HashMap<String, Object> model = (HashMap<String, Object>)hs.get("model");
									double initReci = (double)model.get("initReci");
									double initRepu = (double)model.get("initRepu");
									double initConf = (double)model.get("initConf");
									HashMap<String, Object> umbralCoop = (HashMap<String, Object>)model.get("umbralCoop");
									double[] params = (double[])umbralCoop.get("params");
									double[] p = (double[])hs.get("umbralCoop");
									System.out.println(model.keySet() + "**" + hs.keySet() + "**" + umbralCoop + "**" + p.length);
									for (double val : pozo)
									{
										wr.write(initReci + ";" + initRepu + ";" + initConf + ";" + params[0] + ";" + val);
										wr.newLine();
									}
									wr.flush();*/
								}
							}
				        }
					}
				}
			}
		}

	}
}
