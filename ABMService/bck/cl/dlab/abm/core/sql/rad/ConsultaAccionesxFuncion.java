package cl.dlab.abm.core.sql.rad;

import cl.dlab.abm.core.sql.BaseSQL;
import java.sql.Connection;

public class ConsultaAccionesxFuncion extends BaseSQL {

	public ConsultaAccionesxFuncion() throws Exception {
		super();
	}

	public ConsultaAccionesxFuncion(Connection con,
			java.lang.Boolean commitAndClose) throws Exception {
		super(con, commitAndClose);
	}
}