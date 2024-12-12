package cl.dlab.abm.service.vo;

public class VariableTypeInputVO extends InputVO {

	@Override
	public Class<VariableTypeOutputVO> getOutputClass() {
		return VariableTypeOutputVO.class;
	}
}