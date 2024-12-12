package cl.dlab.abm.core.sql.rad;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cl.dlab.abm.core.gen.FieldType;
import cl.dlab.abm.core.sql.BaseSQL;

public class TableDefinition extends BaseSQL {

	public TableDefinition() throws Exception {
		super();
	}
	protected String getSql(HashMap<String, Object> input, String sql)
	{
		if (input.get("all_tables") != null)
		{
			return sql.replaceAll("= [?]", "is not null and valid <> '0'");
		}
		if (input.get("parentId") == null)
		{
			return sql.replaceAll("= [?]", "is null");
		}
		return sql;
	}	
	protected List<FieldType> getConditionFields(HashMap<String, Object> input, List<FieldType> conditionFields)
	{
		if (input.get("parentId") == null)
		{
			return new ArrayList<FieldType>();
		}
		return conditionFields;
	}

	public TableDefinition(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, Object>> allTablesQuery() throws Exception
	{
		HashMap<String, Object> input = new HashMap<String, Object>();
		input.put("valid", true);
		input.put("all_tables", true);
		return (ArrayList<HashMap<String, Object>>)super.consultar(input).get("listData");
	}
	

}