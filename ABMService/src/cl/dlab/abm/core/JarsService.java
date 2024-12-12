package cl.dlab.abm.core;

import java.sql.Connection;
import java.util.HashMap;

import cl.dlab.abm.service.vo.InputVO;
import cl.dlab.abm.service.vo.JarsOutputVO;
import cl.dlab.abm.service.vo.RespuestaVO;

public class JarsService extends BaseService {

	public JarsService() {
		super();
	}

	public JarsService(Connection con) {
		super(con);
	}

	public RespuestaVO<JarsOutputVO> consultar(InputVO input) throws Exception {
		return new cl.dlab.abm.core.sql.rad.Jars(con, con == null)
				.consultar(input);
	}

	public HashMap<String, Object> consultar(
			java.util.HashMap<String, Object> input) throws Exception {
		return new cl.dlab.abm.core.sql.rad.Jars(con, true).consultar(input);
	}

	public void eliminar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.Jars(con, true).eliminar(input);
	}

	public void guardar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.Jars(con, true).guardar(input);
	}
}