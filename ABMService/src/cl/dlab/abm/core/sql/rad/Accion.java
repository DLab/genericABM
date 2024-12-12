package cl.dlab.abm.core.sql.rad;

import java.sql.Connection;

import cl.dlab.abm.core.sql.BaseSQL;

public class Accion extends BaseSQL {

	public Accion() throws Exception {
		super();
	}

	public Accion(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}