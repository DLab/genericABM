package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class ConsultaAccionesxFuncionInputVO extends InputVO {

	@Override
	public Class<ConsultaAccionesxFuncionOutputVO> getOutputClass() {
		return ConsultaAccionesxFuncionOutputVO.class;
	}

	public void setIdFuncion(java.lang.Integer idFuncion) {
		set("idFuncion", idFuncion);
	}

	public Integer getIdFuncion() {
		return get("idFuncion");
	}
}