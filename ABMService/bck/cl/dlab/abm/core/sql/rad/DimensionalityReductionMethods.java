package cl.dlab.abm.core.sql.rad;

import cl.dlab.abm.core.sql.BaseSQL;
import java.sql.Connection;

public class DimensionalityReductionMethods extends BaseSQL {

	public DimensionalityReductionMethods() throws Exception {
		super();
	}

	public DimensionalityReductionMethods(Connection con,
			java.lang.Boolean commitAndClose) throws Exception {
		super(con, commitAndClose);
	}
}