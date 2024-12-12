package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class TableDefinitionInputVO extends InputVO {

	@Override
	public Class<TableDefinitionOutputVO> getOutputClass() {
		return TableDefinitionOutputVO.class;
	}

	public void setParentId(java.lang.Integer parentId) {
		set("parentId", parentId);
	}

	public Integer getParentId() {
		return get("parentId");
	}
}