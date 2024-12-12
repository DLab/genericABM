package cl.dlab.abm.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import cl.dlab.abm.core.sql.rad.TableDefinition;
import cl.dlab.abm.service.vo.InputVO;
import cl.dlab.abm.service.vo.RespuestaVO;
import cl.dlab.abm.service.vo.TableDefinitionOutputVO;

public class TableDefinitionService extends BaseService {

	public TableDefinitionService() {
		super();
	}

	public TableDefinitionService(Connection con) {
		super(con);
	}

	public RespuestaVO<TableDefinitionOutputVO> consultar(InputVO input) throws Exception {
		return new TableDefinition(con, con == null).consultar(input);
	}

	public HashMap<String, Object> consultar(java.util.HashMap<String, Object> input) throws Exception {
		return new TableDefinition(con, true).consultar(input);
	}

	public void eliminar(java.util.HashMap<String, Object> input) throws Exception {
		new TableDefinition(con, true).eliminar(input);
	}
	private int getMaxId(TableDefinition service) throws Exception
	{
		try(PreparedStatement stmt = service.getConnection().prepareStatement("select max(id) from tabledefinition"))
		{
			ResultSet rset = stmt.executeQuery();
			if (rset.next()) {
				Integer val = (Integer)rset.getObject(1);
				return val == null ? 0 : val;
			}
			return 0;
		}
	}
	public void guardar(java.util.HashMap<String, Object> input) throws Exception {
		boolean isNew = (Boolean)input.get("isNew");
		TableDefinition service = new TableDefinition(con, false);
		try
		{
			if (isNew)
			{
				input.put("id", getMaxId(service) + 1);
			}
			System.out.println(input);
			service.guardar(input);
			service.getConnection().commit();
		}
		finally
		{
			service.getConnection().close();
		}
	}
	public ArrayList<HashMap<String, Object>> allTablesQuery() throws Exception
	{
		return new TableDefinition(con, true).allTablesQuery();
	}
}