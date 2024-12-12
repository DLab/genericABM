package cl.dlab.abm.service.vo;

public class FunctionTypeInputVO extends InputVO {

	@Override
	public Class<FunctionTypeOutputVO> getOutputClass() {
		return FunctionTypeOutputVO.class;
	}
}