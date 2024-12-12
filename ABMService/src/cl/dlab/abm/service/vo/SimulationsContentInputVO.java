package cl.dlab.abm.service.vo;

public class SimulationsContentInputVO extends InputVO {

	@Override
	public Class<SimulationsContentOutputVO> getOutputClass() {
		return SimulationsContentOutputVO.class;
	}
}