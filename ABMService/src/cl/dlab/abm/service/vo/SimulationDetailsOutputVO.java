package cl.dlab.abm.service.vo;

public class SimulationDetailsOutputVO extends VOBase {

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

	public void setContent(java.lang.Object content) {
		set("content", content);
	}

	public Object getContent() {
		return get("content");
	}
	public void setAdditionalData(java.lang.Object additionalData) {
		set("additionalData", additionalData);
	}

	public Object getAdditionalData() {
		return get("additionalData");
	}
}