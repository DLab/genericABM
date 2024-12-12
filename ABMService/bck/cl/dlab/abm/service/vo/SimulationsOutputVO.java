package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.VOBase;
import java.util.Date;

public class SimulationsOutputVO extends VOBase {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public String getId() {
		return get("id");
	}

	public void setDescription(java.lang.String description) {
		set("description", description);
	}

	public String getDescription() {
		return get("description");
	}

	public void setUserId(java.lang.String userId) {
		set("userId", userId);
	}

	public String getUserId() {
		return get("userId");
	}

	public void setTimestamp(Date timestamp) {
		set("timestamp", timestamp);
	}

	public Date getTimestamp() {
		return get("timestamp");
	}

	public void setIdSimulationState(java.lang.Integer idSimulationState) {
		set("idSimulationState", idSimulationState);
	}

	public Integer getIdSimulationState() {
		return get("idSimulationState");
	}

	public void setModelName(java.lang.String modelName) {
		set("modelName", modelName);
	}

	public String getModelName() {
		return get("modelName");
	}

	public Boolean getPrivate() {
		return get("private");
	}

	public String getSimulationState() {
		return get("simulationState");
	}
}