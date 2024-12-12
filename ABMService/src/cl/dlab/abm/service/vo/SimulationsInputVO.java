package cl.dlab.abm.service.vo;

public class SimulationsInputVO extends InputVO {

	@Override
	public Class<SimulationsOutputVO> getOutputClass() {
		return SimulationsOutputVO.class;
	}
}