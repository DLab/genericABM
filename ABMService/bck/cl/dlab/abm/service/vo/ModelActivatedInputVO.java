package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.QueryVO;

public class ModelActivatedInputVO extends QueryVO {

	@Override
	public String getQueryName() {
		return "activated";
	}

	@Override
	public Class<ModelOutputVO> getOutputClass() {
		return ModelOutputVO.class;
	}

	public void setActivated(java.lang.Boolean activated) {
		set("activated", activated);
	}

	public Boolean getActivated() {
		return get("activated");
	}
}