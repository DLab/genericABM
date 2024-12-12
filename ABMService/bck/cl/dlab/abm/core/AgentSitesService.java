package cl.dlab.abm.core;

import cl.dlab.abm.core.BaseService;
import java.sql.Connection;
import cl.dlab.abm.service.vo.RespuestaVO;
import cl.dlab.abm.service.vo.AgentSitesOutputVO;
import cl.dlab.abm.service.vo.InputVO;
import java.util.HashMap;

public class AgentSitesService extends BaseService {

	public AgentSitesService() {
		super();
	}

	public AgentSitesService(Connection con) {
		super(con);
	}

	public RespuestaVO<AgentSitesOutputVO> consultar(InputVO input)
			throws Exception {
		return new cl.dlab.abm.core.sql.rad.AgentSites(con, con == null)
				.consultar(input);
	}

	public HashMap<String, Object> consultar(
			java.util.HashMap<String, Object> input) throws Exception {
		return new cl.dlab.abm.core.sql.rad.AgentSites(con, true)
				.consultar(input);
	}

	public void eliminar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.AgentSites(con, true).eliminar(input);
	}

	public void guardar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.AgentSites(con, true).guardar(input);
	}
}