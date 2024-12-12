package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class FuncionInputVO extends InputVO {

	@Override
	public Class<FuncionOutputVO> getOutputClass() {
		return FuncionOutputVO.class;
	}
}