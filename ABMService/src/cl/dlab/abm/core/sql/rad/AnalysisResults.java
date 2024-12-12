package cl.dlab.abm.core.sql.rad;

import java.sql.Connection;

import cl.dlab.abm.core.sql.BaseSQL;

public class AnalysisResults extends BaseSQL {

	public AnalysisResults() throws Exception {
		super();
	}

	public AnalysisResults(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}