package cl.dlab.abm.core.sql.rad;

import cl.dlab.abm.core.sql.BaseSQL;
import java.sql.Connection;

public class ClusteringMethods extends BaseSQL {

	public ClusteringMethods() throws Exception {
		super();
	}

	public ClusteringMethods(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}