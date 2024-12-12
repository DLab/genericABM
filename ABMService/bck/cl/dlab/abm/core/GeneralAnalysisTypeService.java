package cl.dlab.abm.core;

import cl.dlab.abm.core.BaseService;
import java.sql.Connection;
import cl.dlab.abm.service.vo.RespuestaVO;
import cl.dlab.abm.service.vo.GeneralAnalysisTypeOutputVO;
import cl.dlab.abm.service.vo.InputVO;
import java.util.HashMap;

public class GeneralAnalysisTypeService extends BaseService {

	public GeneralAnalysisTypeService() {
		super();
	}

	public GeneralAnalysisTypeService(Connection con) {
		super(con);
	}

	public RespuestaVO<GeneralAnalysisTypeOutputVO> consultar(InputVO input)
			throws Exception {
		return new cl.dlab.abm.core.sql.rad.GeneralAnalysisType(con,
				con == null).consultar(input);
	}

	public HashMap<String, Object> consultar(
			java.util.HashMap<String, Object> input) throws Exception {
		return new cl.dlab.abm.core.sql.rad.GeneralAnalysisType(con, true)
				.consultar(input);
	}

	public void eliminar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.GeneralAnalysisType(con, true)
				.eliminar(input);
	}

	public void guardar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.GeneralAnalysisType(con, true)
				.guardar(input);
	}
}