package cl.dlab.abm.core;

import java.sql.Connection;

public class BaseService
{
	protected Connection con;
	public BaseService()
	{
	}
	public BaseService(Connection con)
	{
		this.con = con;
	}

}
