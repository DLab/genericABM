package cl.dlab.abm.core.sql.rad;

import cl.dlab.abm.core.sql.BaseSQL;
import java.sql.Connection;

public class SimulationDetails extends BaseSQL {

	public SimulationDetails() throws Exception {
		super();
	}

	public SimulationDetails(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}