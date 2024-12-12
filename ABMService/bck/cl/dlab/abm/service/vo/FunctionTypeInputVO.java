package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class FunctionTypeInputVO extends InputVO {

	@Override
	public Class<FunctionTypeOutputVO> getOutputClass() {
		return FunctionTypeOutputVO.class;
	}
}