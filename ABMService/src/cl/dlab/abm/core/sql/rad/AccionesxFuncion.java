package cl.dlab.abm.core.sql.rad;

import java.sql.Connection;

import cl.dlab.abm.core.sql.BaseSQL;

public class AccionesxFuncion extends BaseSQL {

	public AccionesxFuncion() throws Exception {
		super();
	}

	public AccionesxFuncion(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}