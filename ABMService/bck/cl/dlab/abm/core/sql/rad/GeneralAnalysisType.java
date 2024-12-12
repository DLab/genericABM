package cl.dlab.abm.core.sql.rad;

import cl.dlab.abm.core.sql.BaseSQL;
import java.sql.Connection;

public class GeneralAnalysisType extends BaseSQL {

	public GeneralAnalysisType() throws Exception {
		super();
	}

	public GeneralAnalysisType(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}