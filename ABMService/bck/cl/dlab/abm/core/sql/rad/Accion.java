package cl.dlab.abm.core.sql.rad;

import cl.dlab.abm.core.sql.BaseSQL;
import java.sql.Connection;

public class Accion extends BaseSQL {

	public Accion() throws Exception {
		super();
	}

	public Accion(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}