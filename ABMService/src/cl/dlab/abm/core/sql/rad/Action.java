package cl.dlab.abm.core.sql.rad;

import java.sql.Connection;

import cl.dlab.abm.core.sql.BaseSQL;

public class Action extends BaseSQL {

	public Action() throws Exception {
		super();
	}

	public Action(Connection con, java.lang.Boolean commitAndClose)
			throws Exception {
		super(con, commitAndClose);
	}
}