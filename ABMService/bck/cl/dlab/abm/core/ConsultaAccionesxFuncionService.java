package cl.dlab.abm.core;

import cl.dlab.abm.core.BaseService;
import java.sql.Connection;
import cl.dlab.abm.service.vo.RespuestaVO;
import cl.dlab.abm.service.vo.ConsultaAccionesxFuncionOutputVO;
import cl.dlab.abm.service.vo.InputVO;

public class ConsultaAccionesxFuncionService extends BaseService {

	public ConsultaAccionesxFuncionService() {
		super();
	}

	public ConsultaAccionesxFuncionService(Connection con) {
		super(con);
	}

	public RespuestaVO<ConsultaAccionesxFuncionOutputVO> consultar(InputVO input)
			throws Exception {
		return new cl.dlab.abm.core.sql.rad.ConsultaAccionesxFuncion(con,
				con == null).consultar(input);
	}
}