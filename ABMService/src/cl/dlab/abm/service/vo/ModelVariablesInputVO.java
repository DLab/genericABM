package cl.dlab.abm.service.vo;

public class ModelVariablesInputVO extends InputVO {

	@Override
	public Class<ModelVariablesOutputVO> getOutputClass() {
		return ModelVariablesOutputVO.class;
	}

	public void setModelName(java.lang.String modelName) {
		set("modelName", modelName);
	}

	public String getModelName() {
		return get("modelName");
	}
}