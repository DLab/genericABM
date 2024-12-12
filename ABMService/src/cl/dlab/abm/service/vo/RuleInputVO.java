package cl.dlab.abm.service.vo;

public class RuleInputVO extends InputVO {

	@Override
	public Class<RuleOutputVO> getOutputClass() {
		return RuleOutputVO.class;
	}

	public void setModelName(java.lang.String modelName) {
		set("modelName", modelName);
	}

	public String getModelName() {
		return get("modelName");
	}
}