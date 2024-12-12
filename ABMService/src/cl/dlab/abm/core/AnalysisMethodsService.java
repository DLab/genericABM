package cl.dlab.abm.core;

import java.sql.Connection;
import java.util.HashMap;

import cl.dlab.abm.service.vo.AnalysisMethodsOutputVO;
import cl.dlab.abm.service.vo.InputVO;
import cl.dlab.abm.service.vo.RespuestaVO;

public class AnalysisMethodsService extends BaseService {

	public AnalysisMethodsService() {
		super();
	}

	public AnalysisMethodsService(Connection con) {
		super(con);
	}

	public RespuestaVO<AnalysisMethodsOutputVO> consultar(InputVO input)
			throws Exception {
		return new cl.dlab.abm.core.sql.rad.AnalysisMethods(con, con == null)
				.consultar(input);
	}

	public HashMap<String, Object> consultar(
			java.util.HashMap<String, Object> input) throws Exception {
		return new cl.dlab.abm.core.sql.rad.AnalysisMethods(con, true)
				.consultar(input);
	}

	public void eliminar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.AnalysisMethods(con, true).eliminar(input);
	}

	public void guardar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.AnalysisMethods(con, true).guardar(input);
	}
}