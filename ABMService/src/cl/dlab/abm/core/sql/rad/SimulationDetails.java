package cl.dlab.abm.core.sql.rad;

import java.sql.Connection;

import cl.dlab.abm.core.sql.BaseSQL;

public class SimulationDetails extends BaseSQL {

	public SimulationDetails() throws Exception {
		super();
	}

	public SimulationDetails(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}