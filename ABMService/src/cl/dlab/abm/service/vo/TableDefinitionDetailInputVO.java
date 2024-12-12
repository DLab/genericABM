package cl.dlab.abm.service.vo;

public class TableDefinitionDetailInputVO extends QueryVO {

	@Override
	public String getQueryName() {
		return "detail";
	}

	@Override
	public Class<TableDefinitionOutputVO> getOutputClass() {
		return TableDefinitionOutputVO.class;
	}

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public Integer getId() {
		return get("id");
	}

	public void setParentId(java.lang.Integer parentId) {
		set("parentId", parentId);
	}

	public Integer getParentId() {
		return get("parentId");
	}
}