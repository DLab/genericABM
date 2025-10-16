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

public class ReadContent
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
		String id = "c3dfb237-db54-4026-8dc2-812a8d518113";
		File file = new File("/Users/manolocabello/servers/apache-tomcat-9.0.63/webapps/generic_abm/sim/sim_ss_" + id + ".csv");
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
				HashMap<String, Object> item = result.get(0);
				System.out.println(((HashMap<String, Object>)item.get("mean")).keySet());
				int n  = ((double[])((HashMap<String, Object>)item.get("mean")).get("rho")).length - 1;
				bw.write("conf_ini;rep_ini;rcp_ini;rh0_0;rh0_" + n + ";pozo_0;pozo_1;pozo_" + (n-1) + ";pozo_" + n);
				bw.newLine();
				for (HashMap<String, Object> hs : result)
				{
					HashMap<String, Object> model = (HashMap<String, Object>)hs.get("model");
					HashMap<String, Object> mean = (HashMap<String, Object>)hs.get("mean");
					double[] rh0 = (double[])mean.get("rho");
					double[] pozo = (double[])mean.get("pozo");
					
					bw.write(model.get("initConf") 
					 + ";" + model.get("initRepu")
					 + ";" + model.get("initReci")
					 + ";" + rh0[0]
					 + ";" + rh0[n]
					 + ";" + pozo[0]
					 + ";" + pozo[1]
					 + ";" + pozo[n - 1]
					 + ";" + pozo[n]
					 );
					bw.newLine();
					bw.flush();
					
				}
				
			}
			
		}
		bw.close();
		con.close();
	}
}
