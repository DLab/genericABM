package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class RolInputVO extends InputVO {

	@Override
	public Class<RolOutputVO> getOutputClass() {
		return RolOutputVO.class;
	}
}