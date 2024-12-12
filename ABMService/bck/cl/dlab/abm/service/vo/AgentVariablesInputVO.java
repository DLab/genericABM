package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class AgentVariablesInputVO extends InputVO {

	@Override
	public Class<AgentVariablesOutputVO> getOutputClass() {
		return AgentVariablesOutputVO.class;
	}

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
}