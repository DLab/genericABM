package cl.dlab.abm.core.sql.rad;

import cl.dlab.abm.core.sql.BaseSQL;
import java.sql.Connection;

public class GraphAlgorithm extends BaseSQL {

	public GraphAlgorithm() throws Exception {
		super();
	}

	public GraphAlgorithm(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}