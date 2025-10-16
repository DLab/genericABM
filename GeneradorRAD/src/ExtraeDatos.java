import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import cl.dlab.abm.util.BytesUtil;

public class ExtraeDatos
{
	protected static Connection newConnection() throws Exception
	{
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection("jdbc:postgresql://192.168.2.109/abm3", "postgres", "postgres");
		con.setAutoCommit(false);
		return con;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception
	{
		Connection con = newConnection();
		String id = "f6f8a942-8769-4b3d-908f-5b00b220d62a";
		File file = new File("data_" + id + ".csv");
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		
		try(PreparedStatement stmt = con.prepareStatement("select content from simulation_details where id_simulation = ?"))
		{
			stmt.setString(1, id);
			try(ResultSet rset = stmt.executeQuery())
			{
				ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
				while(rset.next())
				{
					byte[] bytes = (byte[])rset.getObject(1);
					try(ObjectInputStream oi = new ObjectInputStream(new ByteArrayInputStream(bytes)))
			        {
						result.add((HashMap<String,Object>)BytesUtil.getObjectByZipBytes((byte[])oi.readObject()));
			        }
					
				}
				bw.write("conf_ini;rep_ini;rcp_ini;paso;rh0;pozo");
				bw.newLine();
				bw.flush();
				for (HashMap<String, Object> hs : result)
				{
					HashMap<String, Object> model = (HashMap<String, Object>)hs.get("model");
					HashMap<String, Object> mean = (HashMap<String, Object>)hs.get("mean");
					double[] rh0 = (double[])mean.get("rho");
					double[] pozo = (double[])mean.get("pozo");
					
					double initConf = (Double)model.get("initConf");
					double initRepu = (Double)model.get("initRepu");
					double initReci = (Double)model.get("initReci");
					System.out.println(initConf + "**" + initReci + "**" + initRepu);
					if (initRepu == -0.5 && initConf == 0.7 && initReci == -0.1)
					{
						for (int i = 0; i < pozo.length; i++)
						{
							bw.write(model.get("initConf") 
							 + ";" + model.get("initRepu")
							 + ";" + model.get("initReci")
							 + ";" + i
							 + ";" + rh0[i]
							 + ";" + pozo[i]
							 );
							bw.newLine();
							bw.flush();
						}
					}
				}
				
			}
		}
		bw.close();
		con.close();
	}
}
