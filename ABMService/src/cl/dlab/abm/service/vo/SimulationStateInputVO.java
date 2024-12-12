package cl.dlab.abm.service.vo;

public class SimulationStateInputVO extends InputVO {

	@Override
	public Class<SimulationStateOutputVO> getOutputClass() {
		return SimulationStateOutputVO.class;
	}
}