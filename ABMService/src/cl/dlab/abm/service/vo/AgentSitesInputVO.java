package cl.dlab.abm.service.vo;

public class AgentSitesInputVO extends InputVO {

	@Override
	public Class<AgentSitesOutputVO> getOutputClass() {
		return AgentSitesOutputVO.class;
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