package cl.dlab.abm.core.sql.rad;

import java.sql.Connection;

import cl.dlab.abm.core.sql.BaseSQL;

public class AccionesxFuncionRol extends BaseSQL {

	public AccionesxFuncionRol() throws Exception {
		super();
	}

	public AccionesxFuncionRol(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}