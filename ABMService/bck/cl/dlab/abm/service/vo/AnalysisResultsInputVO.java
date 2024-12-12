package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class AnalysisResultsInputVO extends InputVO {

	@Override
	public Class<AnalysisResultsOutputVO> getOutputClass() {
		return AnalysisResultsOutputVO.class;
	}

	public void setIdSimulation(java.lang.String idSimulation) {
		set("idSimulation", idSimulation);
	}

	public String getIdSimulation() {
		return get("idSimulation");
	}
}