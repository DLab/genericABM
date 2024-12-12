package cl.dlab.abm.core;

import java.sql.Connection;
import java.util.HashMap;

import cl.dlab.abm.service.vo.AgentOutputVO;
import cl.dlab.abm.service.vo.InputVO;
import cl.dlab.abm.service.vo.RespuestaVO;

public class AgentService extends BaseService {

	public AgentService() {
		super();
	}

	public AgentService(Connection con) {
		super(con);
	}

	public RespuestaVO<AgentOutputVO> consultar(InputVO input) throws Exception {
		return new cl.dlab.abm.core.sql.rad.Agent(con, con == null)
				.consultar(input);
	}

	public HashMap<String, Object> consultar(
			java.util.HashMap<String, Object> input) throws Exception {
		return new cl.dlab.abm.core.sql.rad.Agent(con, true).consultar(input);
	}

	public void eliminar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.Agent(con, true).eliminar(input);
	}

	public void guardar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.Agent(con, true).guardar(input);
	}
}