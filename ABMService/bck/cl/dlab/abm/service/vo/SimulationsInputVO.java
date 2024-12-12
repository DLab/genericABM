package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class SimulationsInputVO extends InputVO {

	@Override
	public Class<SimulationsOutputVO> getOutputClass() {
		return SimulationsOutputVO.class;
	}
}