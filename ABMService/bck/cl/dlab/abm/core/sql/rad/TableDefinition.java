package cl.dlab.abm.core.sql.rad;

import cl.dlab.abm.core.sql.BaseSQL;
import java.sql.Connection;

public class TableDefinition extends BaseSQL {

	public TableDefinition() throws Exception {
		super();
	}

	public TableDefinition(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}