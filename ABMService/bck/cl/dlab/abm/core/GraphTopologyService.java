package cl.dlab.abm.core;

import cl.dlab.abm.core.BaseService;
import java.sql.Connection;
import cl.dlab.abm.service.vo.RespuestaVO;
import cl.dlab.abm.service.vo.GraphTopologyOutputVO;
import cl.dlab.abm.service.vo.InputVO;
import java.util.HashMap;

public class GraphTopologyService extends BaseService {

	public GraphTopologyService() {
		super();
	}

	public GraphTopologyService(Connection con) {
		super(con);
	}

	public RespuestaVO<GraphTopologyOutputVO> consultar(InputVO input)
			throws Exception {
		return new cl.dlab.abm.core.sql.rad.GraphTopology(con, con == null)
				.consultar(input);
	}

	public HashMap<String, Object> consultar(
			java.util.HashMap<String, Object> input) throws Exception {
		return new cl.dlab.abm.core.sql.rad.GraphTopology(con, true)
				.consultar(input);
	}

	public void eliminar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.GraphTopology(con, true).eliminar(input);
	}

	public void guardar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.GraphTopology(con, true).guardar(input);
	}
}