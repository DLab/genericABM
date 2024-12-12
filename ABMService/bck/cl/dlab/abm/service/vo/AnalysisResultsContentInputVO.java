package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class AnalysisResultsContentInputVO extends InputVO {

	@Override
	public Class<AnalysisResultsContentOutputVO> getOutputClass() {
		return AnalysisResultsContentOutputVO.class;
	}

	public void setIdSimulation(java.lang.String idSimulation) {
		set("idSimulation", idSimulation);
	}

	public String getIdSimulation() {
		return get("idSimulation");
	}

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public Integer getId() {
		return get("id");
	}
}