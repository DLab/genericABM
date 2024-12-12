package cl.dlab.abm.core;

import cl.dlab.abm.core.BaseService;
import java.sql.Connection;
import cl.dlab.abm.service.vo.RespuestaVO;
import cl.dlab.abm.service.vo.SimulationStateOutputVO;
import cl.dlab.abm.service.vo.InputVO;
import java.util.HashMap;

public class SimulationStateService extends BaseService {

	public SimulationStateService() {
		super();
	}

	public SimulationStateService(Connection con) {
		super(con);
	}

	public RespuestaVO<SimulationStateOutputVO> consultar(InputVO input)
			throws Exception {
		return new cl.dlab.abm.core.sql.rad.SimulationState(con, con == null)
				.consultar(input);
	}

	public HashMap<String, Object> consultar(
			java.util.HashMap<String, Object> input) throws Exception {
		return new cl.dlab.abm.core.sql.rad.SimulationState(con, true)
				.consultar(input);
	}

	public void eliminar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.SimulationState(con, true).eliminar(input);
	}

	public void guardar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.SimulationState(con, true).guardar(input);
	}
}