import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class GeneraDefinición
{
	protected static Connection newConnection() throws Exception
	{
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection("jdbc:postgresql://192.168.2.109/abm3", "postgres", "postgres");
		con.setAutoCommit(false);
		return con;
	}

	public static void main(String[] args) throws Exception
	{
		Connection con = newConnection();
		JSONObject json = new JSONObject();
		String name = "SIR";
		json.put("name", name);
		
		try(PreparedStatement stmt = con.prepareStatement("select name, variable_type_id, general_analysis_type_id from modelvariables where model_name = ?"))
		{
			stmt.setString(1, name);
			JSONArray prop = new JSONArray();
			json.put("properties", prop);
			try(ResultSet rset = stmt.executeQuery())
			{
				while(rset.next())
				{
					JSONObject p = new JSONObject();
					p.put("name", rset.getString(1));
					p.put("type", rset.getInt(2));
					Object o = rset.getObject(3);
					p.put("analysisType", o == null ? " " : o);
					prop.put(p);
					
				}
			}
		}
		JSONArray agents = new JSONArray();
		json.put("agents", agents);
		try(PreparedStatement stmt = con.prepareStatement("select name from agent where model_name = ?"))
		{
			stmt.setString(1, name);
			try(ResultSet rset = stmt.executeQuery())
			{
				while(rset.next())
				{
					JSONObject agent = new JSONObject();
					String agentName = rset.getString(1);
					agent.put("name", agentName);
					agents.put(agent);					
					try(PreparedStatement stmt2 = con.prepareStatement("select name, variable_type_id, general_analysis_type_id from agentsites where model_name = ? and agent_name = ?"))
					{
						stmt2.setString(1, name);
						stmt2.setString(2, agentName);
						JSONArray prop = new JSONArray();
						agent.put("properties", prop);
						try(ResultSet rset2 = stmt2.executeQuery())
						{
							while(rset2.next())
							{
								JSONObject p = new JSONObject();
								p.put("name", rset2.getString(1));
								p.put("type", rset2.getInt(2));
								Object o = rset2.getObject(3);
								p.put("analysisType", o == null ? " " : o);
								prop.put(p);
								
							}
						}
					}
				}
			}
		}
		con.close();
		System.out.println(json.toString(2));
	}
}
