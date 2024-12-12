package cl.dlab.abm.core.sql.rad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import cl.dlab.abm.core.sql.BaseSQL;
import cl.dlab.abm.util.BytesUtil;

public class AnalysisResultsContent extends BaseSQL {

	public AnalysisResultsContent() throws Exception {
		super();
	}

	public AnalysisResultsContent(Connection con,
			java.lang.Boolean commitAndClose) throws Exception {
		super(con, commitAndClose);
	}
	
	private int getNextId(String idSimulation) throws Exception
	{
		try(PreparedStatement stmt = con.prepareStatement("select max(id) from AnalysisResults where id_simulation = ?"))
		{
			stmt.setString(1, idSimulation);
			ResultSet rset = stmt.executeQuery();
			if (rset.next())
			{
				return rset.getInt(1) + 1;
			}
			return 1;
		}
		
	}
	public int create(String idSimulation, String description) throws Exception
	{
		try(PreparedStatement stmt = con.prepareStatement("insert into AnalysisResults (id_simulation, id, description, id_state) values (?, ?, ?, 1)"))
		{
			int id = getNextId(idSimulation);
			stmt.setString(1, idSimulation);
			stmt.setInt(2, id);
			stmt.setString(3, description);
			stmt.executeUpdate();
			return id;
		}
		
	}
	public int update(String idSimulation, int id, String data) throws Exception
	{
		try(PreparedStatement stmt = con.prepareStatement("update AnalysisResults  set data = ?, id_state = 2 where id_simulation = ? and id = ?"))
		{			
			stmt.setBytes(1, BytesUtil.getGZipBytes(data));
			stmt.setString(2, idSimulation);
			stmt.setInt(3, id);
			stmt.executeUpdate();
			return id;
		}
		
	}
	public int updateState(String idSimulation, int id, int idState) throws Exception
	{
		try(PreparedStatement stmt = con.prepareStatement("update AnalysisResults  set id_state=? where id_simulation = ? and id = ?"))
		{			
			stmt.setInt(1, idState);
			stmt.setString(2, idSimulation);
			stmt.setInt(3, id);
			stmt.executeUpdate();
			return id;
		}
		
	}
	public void delete(String idSimulation) throws Exception
	{
		try(PreparedStatement stmt = con.prepareStatement("delete from  AnalysisResults  where id_simulation = ?"))
		{			
			stmt.setString(1, idSimulation);
			stmt.executeUpdate();
		}
		
	}
	public void deleteItem(String idSimulation, int id) throws Exception
	{
		try(PreparedStatement stmt = con.prepareStatement("delete from  AnalysisResults  where id_simulation = ? and id = ?"))
		{			
			stmt.setString(1, idSimulation);
			stmt.setInt(2, id);
			stmt.executeUpdate();
		}
		
	}
	public String getContent(String idSimulation, int id) throws Exception
	{
		try(PreparedStatement stmt = con.prepareStatement("select data from AnalysisResults  where id_simulation = ? and id = ?"))
		{			
			stmt.setString(1, idSimulation);
			stmt.setInt(2, id);
			ResultSet rset = stmt.executeQuery();
			if (rset.next())
			{
				byte[] bytes = rset.getBytes(1);
				return (String)BytesUtil.getObjectByZipBytes(bytes);
			}
			return null;
		}
		finally
		{
			close();
		}
		
	}
	
}