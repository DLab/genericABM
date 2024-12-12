package cl.dlab.abm.service.vo;

public class GraphOutputVO extends VOBase {

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

	public void setGraphTypeId(java.lang.Integer graphTypeId) {
		set("graphTypeId", graphTypeId);
	}

	public Integer getGraphTypeId() {
		return get("graphTypeId");
	}

	public void setGraphTopologyId(java.lang.Integer graphTopologyId) {
		set("graphTopologyId", graphTopologyId);
	}

	public Integer getGraphTopologyId() {
		return get("graphTopologyId");
	}

	public void setGraphAlgorithmId(java.lang.Integer graphAlgorithmId) {
		set("graphAlgorithmId", graphAlgorithmId);
	}

	public Integer getGraphAlgorithmId() {
		return get("graphAlgorithmId");
	}
}