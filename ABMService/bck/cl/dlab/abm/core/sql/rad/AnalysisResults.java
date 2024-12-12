package cl.dlab.abm.core.sql.rad;

import cl.dlab.abm.core.sql.BaseSQL;
import java.sql.Connection;

public class AnalysisResults extends BaseSQL {

	public AnalysisResults() throws Exception {
		super();
	}

	public AnalysisResults(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}