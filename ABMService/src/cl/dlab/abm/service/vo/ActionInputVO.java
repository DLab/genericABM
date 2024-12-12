package cl.dlab.abm.service.vo;

public class ActionInputVO extends InputVO {

	@Override
	public Class<ActionOutputVO> getOutputClass() {
		return ActionOutputVO.class;
	}

	public void setModelName(java.lang.String modelName) {
		set("modelName", modelName);
	}

	public String getModelName() {
		return get("modelName");
	}
}