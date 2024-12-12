package cl.dlab.abm.service.vo;

public class TableDefinitionInputVO extends InputVO {

	@Override
	public Class<TableDefinitionOutputVO> getOutputClass() {
		return TableDefinitionOutputVO.class;
	}
}