package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class SimulationsContentInputVO extends InputVO {

	@Override
	public Class<SimulationsContentOutputVO> getOutputClass() {
		return SimulationsContentOutputVO.class;
	}

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public String getId() {
		return get("id");
	}
}