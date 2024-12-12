package cl.dlab.abm.core;

import cl.dlab.abm.core.BaseService;
import java.sql.Connection;
import cl.dlab.abm.service.vo.RespuestaVO;
import cl.dlab.abm.service.vo.ClusteringMethodsOutputVO;
import cl.dlab.abm.service.vo.InputVO;
import java.util.HashMap;

public class ClusteringMethodsService extends BaseService {

	public ClusteringMethodsService() {
		super();
	}

	public ClusteringMethodsService(Connection con) {
		super(con);
	}

	public RespuestaVO<ClusteringMethodsOutputVO> consultar(InputVO input)
			throws Exception {
		return new cl.dlab.abm.core.sql.rad.ClusteringMethods(con, con == null)
				.consultar(input);
	}

	public HashMap<String, Object> consultar(
			java.util.HashMap<String, Object> input) throws Exception {
		return new cl.dlab.abm.core.sql.rad.ClusteringMethods(con, true)
				.consultar(input);
	}

	public void eliminar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.ClusteringMethods(con, true)
				.eliminar(input);
	}

	public void guardar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.ClusteringMethods(con, true)
				.guardar(input);
	}
}