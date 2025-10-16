package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.VOBase;
import java.util.Date;

public class ModelOutputVO extends VOBase {

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public String getName() {
		return get("name");
	}

	public void setDescription(java.lang.String description) {
		set("description", description);
	}

	public String getDescription() {
		return get("description");
	}

	public void setActivate(java.lang.Boolean activate) {
		set("activate", activate);
	}

	public Boolean getActivate() {
		return get("activate");
	}

	public void setPrivate() {
	}

	public Boolean getPrivate() {
		return get("private");
	}

	public void setSingleAgentsList(java.lang.Boolean singleAgentsList) {
		set("singleAgentsList", singleAgentsList);
	}

	public Boolean getSingleAgentsList() {
		return get("singleAgentsList");
	}

	public void setSynchronized() {
	}

	public Boolean getSynchronized() {
		return get("synchronized");
	}

	public void setKqmlIntegration(java.lang.Boolean kqmlIntegration) {
		set("kqmlIntegration", kqmlIntegration);
	}

	public Boolean getKqmlIntegration() {
		return get("kqmlIntegration");
	}

	public void setAdditionalCode(java.lang.String additionalCode) {
		set("additionalCode", additionalCode);
	}

	public String getAdditionalCode() {
		return get("additionalCode");
	}

	public void setTimestamp(Date timestamp) {
		set("timestamp", timestamp);
	}

	public Date getTimestamp() {
		return get("timestamp");
	}

	public void setRunWith(java.lang.String runWith) {
		set("runWith", runWith);
	}

	public String getRunWith() {
		return get("runWith");
	}

	public void setUserId(java.lang.String userId) {
		set("userId", userId);
	}

	public String getUserId() {
		return get("userId");
	}

	public void setMeanSimulationTime(java.lang.Double meanSimulationTime) {
		set("meanSimulationTime", meanSimulationTime);
	}

	public Double getMeanSimulationTime() {
		return get("meanSimulationTime");
	}
}