package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.VOBase;

public class AnalysisResultsContentOutputVO extends VOBase {

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

	public void setData(java.lang.Object data) {
		set("data", data);
	}

	public Object getData() {
		return get("data");
	}
}