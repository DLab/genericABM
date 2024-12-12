package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.QueryVO;

public class SimulationsByIdInputVO extends QueryVO {

	@Override
	public String getQueryName() {
		return "byId";
	}

	@Override
	public Class<SimulationsOutputVO> getOutputClass() {
		return SimulationsOutputVO.class;
	}

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public String getId() {
		return get("id");
	}
}