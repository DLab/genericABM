package cl.dlab.abm.core.sql.rad;

import cl.dlab.abm.core.sql.BaseSQL;
import java.sql.Connection;

public class AgentVariables extends BaseSQL {

	public AgentVariables() throws Exception {
		super();
	}

	public AgentVariables(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}