package cl.dlab.abm.core.sql.rad;

import java.sql.Connection;

import cl.dlab.abm.core.sql.BaseSQL;

public class ModelVariables extends BaseSQL {

	public ModelVariables() throws Exception {
		super();
	}

	public ModelVariables(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}