package cl.dlab.abm.core.sql.rad;

import cl.dlab.abm.core.sql.BaseSQL;
import java.sql.Connection;

public class ModelVariables extends BaseSQL {

	public ModelVariables() throws Exception {
		super();
	}

	public ModelVariables(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}