package cl.dlab.abm.core.sql.rad;

import cl.dlab.abm.core.sql.BaseSQL;
import java.sql.Connection;

public class SimulationState extends BaseSQL {

	public SimulationState() throws Exception {
		super();
	}

	public SimulationState(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}