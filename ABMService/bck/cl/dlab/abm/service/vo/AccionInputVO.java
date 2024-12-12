package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class AccionInputVO extends InputVO {

	@Override
	public Class<AccionOutputVO> getOutputClass() {
		return AccionOutputVO.class;
	}
}