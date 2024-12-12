package cl.dlab.abm.service.vo;

public class AccionesxFuncionActivosInputVO extends QueryVO {

	@Override
	public String getQueryName() {
		return "activos";
	}

	@Override
	public Class<AccionesxFuncionOutputVO> getOutputClass() {
		return AccionesxFuncionOutputVO.class;
	}

	public void setParentId(java.lang.Integer parentId) {
		set("parentId", parentId);
	}

	public Integer getParentId() {
		return get("parentId");
	}
}