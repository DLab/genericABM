package cl.dlab.abm.service.vo;

public class GraphInputVO extends InputVO {

	@Override
	public Class<GraphOutputVO> getOutputClass() {
		return GraphOutputVO.class;
	}

	public void setModelName(java.lang.String modelName) {
		set("modelName", modelName);
	}

	public String getModelName() {
		return get("modelName");
	}
}