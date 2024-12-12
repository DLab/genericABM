package cl.dlab.abm.core.sql.rad;

import cl.dlab.abm.core.sql.BaseSQL;
import java.sql.Connection;

public class SimulationsContent extends BaseSQL {

	public SimulationsContent() throws Exception {
		super();
	}

	public SimulationsContent(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}