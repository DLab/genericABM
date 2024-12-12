package cl.dlab.abm.core.sql.rad;

import cl.dlab.abm.core.sql.BaseSQL;
import java.sql.Connection;

public class GraphTopology extends BaseSQL {

	public GraphTopology() throws Exception {
		super();
	}

	public GraphTopology(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}