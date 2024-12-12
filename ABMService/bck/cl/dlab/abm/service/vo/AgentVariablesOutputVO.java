package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.VOBase;

public class AgentVariablesOutputVO extends VOBase {

	public void setModelName(java.lang.String modelName) {
		set("modelName", modelName);
	}

	public String getModelName() {
		return get("modelName");
	}

	public void setAgentName(java.lang.String agentName) {
		set("agentName", agentName);
	}

	public String getAgentName() {
		return get("agentName");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public String getName() {
		return get("name");
	}

	public void setDefaultValue(java.lang.String defaultValue) {
		set("defaultValue", defaultValue);
	}

	public String getDefaultValue() {
		return get("defaultValue");
	}

	public void setTableId(java.lang.Integer tableId) {
		set("tableId", tableId);
	}

	public Integer getTableId() {
		return get("tableId");
	}

	public void setVariableTypeId(java.lang.Integer variableTypeId) {
		set("variableTypeId", variableTypeId);
	}

	public Integer getVariableTypeId() {
		return get("variableTypeId");
	}

	public void setMemoryTypeId(java.lang.Integer memoryTypeId) {
		set("memoryTypeId", memoryTypeId);
	}

	public Integer getMemoryTypeId() {
		return get("memoryTypeId");
	}

	public void setFunctionTypeId(java.lang.Integer functionTypeId) {
		set("functionTypeId", functionTypeId);
	}

	public Integer getFunctionTypeId() {
		return get("functionTypeId");
	}
}