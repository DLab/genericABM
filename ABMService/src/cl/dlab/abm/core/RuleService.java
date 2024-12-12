package cl.dlab.abm.core;

import java.sql.Connection;
import java.util.HashMap;

import cl.dlab.abm.service.vo.InputVO;
import cl.dlab.abm.service.vo.RespuestaVO;
import cl.dlab.abm.service.vo.RuleOutputVO;

public class RuleService extends BaseService {

	public RuleService() {
		super();
	}

	public RuleService(Connection con) {
		super(con);
	}

	public RespuestaVO<RuleOutputVO> consultar(InputVO input) throws Exception {
		return new cl.dlab.abm.core.sql.rad.Rule(con, con == null)
				.consultar(input);
	}

	public HashMap<String, Object> consultar(
			java.util.HashMap<String, Object> input) throws Exception {
		return new cl.dlab.abm.core.sql.rad.Rule(con, true).consultar(input);
	}

	public void eliminar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.Rule(con, true).eliminar(input);
	}

	public void guardar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.Rule(con, true).guardar(input);
	}
}