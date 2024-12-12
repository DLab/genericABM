package cl.dlab.abm.service.vo;

public class ModelActivateInputVO extends QueryVO {

	@Override
	public String getQueryName() {
		return "activate";
	}

	@Override
	public Class<ModelOutputVO> getOutputClass() {
		return ModelOutputVO.class;
	}

	public void setActivate(java.lang.Boolean activate) {
		set("activate", activate);
	}

	public Boolean getActivate() {
		return get("activate");
	}
}