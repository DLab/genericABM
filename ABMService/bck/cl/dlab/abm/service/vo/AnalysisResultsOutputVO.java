package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.VOBase;

public class AnalysisResultsOutputVO extends VOBase {

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

	public void setDescription(java.lang.String description) {
		set("description", description);
	}

	public String getDescription() {
		return get("description");
	}

	public void setIdState(java.lang.Integer idState) {
		set("idState", idState);
	}

	public Integer getIdState() {
		return get("idState");
	}
}