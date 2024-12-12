package cl.dlab.abm.core.sql.rad;

import cl.dlab.abm.core.sql.BaseSQL;
import java.sql.Connection;

public class AnalysisResultsContent extends BaseSQL {

	public AnalysisResultsContent() throws Exception {
		super();
	}

	public AnalysisResultsContent(Connection con,
			java.lang.Boolean commitAndClose) throws Exception {
		super(con, commitAndClose);
	}
}