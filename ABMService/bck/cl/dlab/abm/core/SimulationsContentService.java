package cl.dlab.abm.core;

import cl.dlab.abm.core.BaseService;
import java.sql.Connection;
import cl.dlab.abm.service.vo.RespuestaVO;
import cl.dlab.abm.service.vo.SimulationsContentOutputVO;
import cl.dlab.abm.service.vo.InputVO;
import java.util.HashMap;

public class SimulationsContentService extends BaseService {

	public SimulationsContentService() {
		super();
	}

	public SimulationsContentService(Connection con) {
		super(con);
	}

	public RespuestaVO<SimulationsContentOutputVO> consultar(InputVO input)
			throws Exception {
		return new cl.dlab.abm.core.sql.rad.SimulationsContent(con, con == null)
				.consultar(input);
	}

	public HashMap<String, Object> consultar(
			java.util.HashMap<String, Object> input) throws Exception {
		return new cl.dlab.abm.core.sql.rad.SimulationsContent(con, true)
				.consultar(input);
	}

	public void eliminar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.SimulationsContent(con, true)
				.eliminar(input);
	}

	public void guardar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.SimulationsContent(con, true)
				.guardar(input);
	}
}