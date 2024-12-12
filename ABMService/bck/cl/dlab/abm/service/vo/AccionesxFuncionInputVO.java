package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class AccionesxFuncionInputVO extends InputVO {

	@Override
	public Class<AccionesxFuncionOutputVO> getOutputClass() {
		return AccionesxFuncionOutputVO.class;
	}

	public void setIdFuncion(java.lang.Integer idFuncion) {
		set("idFuncion", idFuncion);
	}

	public Integer getIdFuncion() {
		return get("idFuncion");
	}
}