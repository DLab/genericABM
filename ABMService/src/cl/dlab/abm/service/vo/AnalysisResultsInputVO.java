package cl.dlab.abm.service.vo;

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

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public Integer getId() {
		return get("id");
	}
}