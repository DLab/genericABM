package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.VOBase;

public class AgentOutputVO extends VOBase {

	public void setModelName(java.lang.String modelName) {
		set("modelName", modelName);
	}

	public String getModelName() {
		return get("modelName");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public String getName() {
		return get("name");
	}

	public void setBase(java.lang.Boolean base) {
		set("base", base);
	}

	public Boolean getBase() {
		return get("base");
	}

	public void setDescription(java.lang.String description) {
		set("description", description);
	}

	public String getDescription() {
		return get("description");
	}

	public void setGraphName(java.lang.String graphName) {
		set("graphName", graphName);
	}

	public String getGraphName() {
		return get("graphName");
	}

	public void setExtendsFrom(java.lang.String extendsFrom) {
		set("extendsFrom", extendsFrom);
	}

	public String getExtendsFrom() {
		return get("extendsFrom");
	}

	public void setAdditionalCode(java.lang.String additionalCode) {
		set("additionalCode", additionalCode);
	}

	public String getAdditionalCode() {
		return get("additionalCode");
	}
}