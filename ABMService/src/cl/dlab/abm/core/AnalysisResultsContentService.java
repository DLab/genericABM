package cl.dlab.abm.core;

import java.sql.Connection;
import java.util.HashMap;

import cl.dlab.abm.service.vo.AnalysisResultsContentOutputVO;
import cl.dlab.abm.service.vo.InputVO;
import cl.dlab.abm.service.vo.RespuestaVO;

public class AnalysisResultsContentService extends BaseService {

	public AnalysisResultsContentService() {
		super();
	}

	public AnalysisResultsContentService(Connection con) {
		super(con);
	}

	public RespuestaVO<AnalysisResultsContentOutputVO> consultar(InputVO input)
			throws Exception {
		return new cl.dlab.abm.core.sql.rad.AnalysisResultsContent(con,
				con == null).consultar(input);
	}

	public HashMap<String, Object> consultar(
			java.util.HashMap<String, Object> input) throws Exception {
		return new cl.dlab.abm.core.sql.rad.AnalysisResultsContent(con, true)
				.consultar(input);
	}

	public void eliminar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.AnalysisResultsContent(con, true)
				.eliminar(input);
	}

	public void guardar(java.util.HashMap<String, Object> input)
			throws Exception {
		new cl.dlab.abm.core.sql.rad.AnalysisResultsContent(con, true)
				.guardar(input);
	}
}