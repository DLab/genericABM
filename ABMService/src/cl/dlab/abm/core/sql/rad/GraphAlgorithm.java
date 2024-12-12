package cl.dlab.abm.core.sql.rad;

import java.sql.Connection;

import cl.dlab.abm.core.sql.BaseSQL;

public class GraphAlgorithm extends BaseSQL {

	public GraphAlgorithm() throws Exception {
		super();
	}

	public GraphAlgorithm(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}