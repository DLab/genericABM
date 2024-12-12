package cl.dlab.abm.core.sql.rad;

import cl.dlab.abm.core.sql.BaseSQL;
import java.sql.Connection;

public class DataTypeMethods extends BaseSQL {

	public DataTypeMethods() throws Exception {
		super();
	}

	public DataTypeMethods(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}