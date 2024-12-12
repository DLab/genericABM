package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class SimulationStateInputVO extends InputVO {

	@Override
	public Class<SimulationStateOutputVO> getOutputClass() {
		return SimulationStateOutputVO.class;
	}
}