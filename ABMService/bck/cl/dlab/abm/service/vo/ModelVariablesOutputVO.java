package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.VOBase;

public class ModelVariablesOutputVO extends VOBase {

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

	public void setAliasName(java.lang.String aliasName) {
		set("aliasName", aliasName);
	}

	public String getAliasName() {
		return get("aliasName");
	}

	public void setInitialValue(java.lang.String initialValue) {
		set("initialValue", initialValue);
	}

	public String getInitialValue() {
		return get("initialValue");
	}

	public void setRangeValues(java.lang.Boolean rangeValues) {
		set("rangeValues", rangeValues);
	}

	public Boolean getRangeValues() {
		return get("rangeValues");
	}

	public void setGeneralAnalysisId(java.lang.String generalAnalysisId) {
		set("generalAnalysisId", generalAnalysisId);
	}

	public String getGeneralAnalysisId() {
		return get("generalAnalysisId");
	}

	public void setDetailedAnalysis(java.lang.Boolean detailedAnalysis) {
		set("detailedAnalysis", detailedAnalysis);
	}

	public Boolean getDetailedAnalysis() {
		return get("detailedAnalysis");
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