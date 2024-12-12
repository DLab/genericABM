package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class VariableTypeInputVO extends InputVO {

	@Override
	public Class<VariableTypeOutputVO> getOutputClass() {
		return VariableTypeOutputVO.class;
	}
}