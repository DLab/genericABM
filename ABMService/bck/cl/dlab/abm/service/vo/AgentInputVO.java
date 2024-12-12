package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class AgentInputVO extends InputVO {

	@Override
	public Class<AgentOutputVO> getOutputClass() {
		return AgentOutputVO.class;
	}

	public void setModelName(java.lang.String modelName) {
		set("modelName", modelName);
	}

	public String getModelName() {
		return get("modelName");
	}
}