package cl.dlab.abm.core.sql.rad;

import cl.dlab.abm.core.sql.BaseSQL;
import java.sql.Connection;

public class AnalysisMethods extends BaseSQL {

	public AnalysisMethods() throws Exception {
		super();
	}

	public AnalysisMethods(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}